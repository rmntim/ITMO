"""Two exponential servers and one shared FIFO queue with seven waiting places.

The system is work-conserving: an arrival to an empty system chooses P1 with
probability p and P2 otherwise; when exactly one server is idle it takes the
arrival regardless of p. A queued job takes the first server to become free.
Thus p defines only a nominal offered-load split, not a persistent server
preference. There are nine total places (two service plus seven waiting).

Times are derived from PASTA and tagged arrivals, not from Little's law.
Queue size, system size and loss have no unique per-server allocation here.
"""

import numpy as np

from common import stationary


def build(lam=0.5, b=10.0, p=0.65):
    """Return state labels and the exact row-source CTMC generator."""
    mu = 1.0 / b
    states = ["E", "A", "B"] + [f"C{q}" for q in range(8)]
    Q = np.zeros((11, 11), dtype=float)
    Q[0, 1] = lam * p
    Q[0, 2] = lam * (1.0 - p)
    Q[1, 0] = mu
    Q[2, 0] = mu
    Q[1, 3] = lam
    Q[2, 3] = lam
    Q[3, 1] = mu
    Q[3, 2] = mu
    for q in range(7):
        Q[3 + q, 4 + q] = lam
    for q in range(1, 8):
        Q[3 + q, 2 + q] = 2.0 * mu
    # Rejected arrivals in C7 do not change state and are not generator edges.
    np.fill_diagonal(Q, -Q.sum(axis=1))
    return states, Q


def analyze(lam=0.5, b=10.0, p=0.65):
    """Return stationary metrics for positive lam and b, and 0 <= p <= 1.

    A tagged arrival seeing Cq, q < 7, waits for q+1 completions at rate
    2*mu. Its eventual server is uniform and independent of that wait.
    Per-server times condition on the server that actually serves the job;
    total times condition on acceptance. Service adds mean b to each wait.
    """
    states, Q = build(lam, b, p)
    pi = stationary(Q)
    both = pi[3:]
    busy = [float(pi[1] + both.sum()), float(pi[2] + both.sum())]

    # PASTA arrival masses, with rejected C7 arrivals excluded.
    queued_mass = float(both[:7].sum())
    assigned = [
        float(p * pi[0] + pi[2] + 0.5 * queued_mass),
        float((1.0 - p) * pi[0] + pi[1] + 0.5 * queued_mass),
    ]
    throughput = [float(lam * mass) for mass in assigned]
    weighted_wait = float(sum(both[q] * (q + 1) * b / 2.0 for q in range(7)))
    wait = [float(0.5 * weighted_wait / mass) for mass in assigned]
    sojourn = [float(value + b) for value in wait]
    accepted_mass = float(sum(assigned))
    total_wait = float(weighted_wait / accepted_mass)
    total_busy = float(sum(busy))
    total_queue = float(sum(q * both[q] for q in range(8)))

    metrics = {
        "busy": busy,
        "queue": None,
        "number": None,
        "wait": wait,
        "sojourn": sojourn,
        "loss": None,
        "throughput": throughput,
        "total_busy": total_busy,
        "total_queue": total_queue,
        "total_number": float(total_busy + total_queue),
        "total_wait": total_wait,
        "total_sojourn": float(total_wait + b),
        "total_loss": float(both[7]),
        "total_throughput": float(sum(throughput)),
        "offered_load": [float(lam * p * b), float(lam * (1.0 - p) * b)],
        "total_load": float(lam * b),
        "idle": float(pi[0]),
    }
    return {"states": states, "Q": Q, "pi": pi, "metrics": metrics}
