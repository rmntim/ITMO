"""System 2: independent M/M/1/4 and M/E3/1/2 queues.

The state ordering is P1-major, with P2 local states
0, (1,1), (1,2), (1,3), (2,1), (2,2), (2,3).
Rejected arrivals do not change state and are not generator transitions.
"""

import numpy as np

from common import stationary


_P2_STATES = ((0, 0), (1, 1), (1, 2), (1, 3),
              (2, 1), (2, 2), (2, 3))


def _local_matrices(lam, b, p):
    """Return row-source generators without changing either queue capacity."""
    lam1, lam2 = lam * p, lam * (1.0 - p)
    mu, nu = 1.0 / b, 3.0 / b
    q1 = np.zeros((5, 5), dtype=float)
    for n in range(5):
        if n < 4:
            q1[n, n + 1] = lam1
        if n > 0:
            q1[n, n - 1] = mu
    q1[np.diag_indices(5)] = -q1.sum(axis=1)

    q2 = np.zeros((7, 7), dtype=float)
    indices = {state: i for i, state in enumerate(_P2_STATES)}
    for i, (n, phase) in enumerate(_P2_STATES):
        if n == 0:
            q2[i, indices[(1, 1)]] = lam2
        else:
            if n < 2:
                q2[i, indices[(n + 1, phase)]] = lam2
            if phase < 3:
                q2[i, indices[(n, phase + 1)]] = nu
            else:
                destination = (0, 0) if n == 1 else (n - 1, 1)
                q2[i, indices[destination]] = nu
    q2[np.diag_indices(7)] = -q2.sum(axis=1)
    return q1, q2


def _product_model(q1, q2):
    states = [
        f"({n1};{n2},{phase})"
        for n1 in range(5)
        for n2, phase in _P2_STATES
    ]
    q = np.kron(q1, np.eye(7)) + np.kron(np.eye(5), q2)
    return states, q


def build(lam=0.5, b=10.0, p=0.65):
    """Return all 35 state labels and the exact product generator."""
    return _product_model(*_local_matrices(lam, b, p))


def analyze(lam=0.5, b=10.0, p=0.65):
    """Compute stationary rewards and tagged-arrival times, not Little times.

    Independence gives pi = pi1 tensor pi2. PASTA gives the same local
    probabilities to arrivals. Conditional on admission, a P1 arrival in
    state n waits n*b. A P2 arrival in busy state (n,j) waits
    (4-j)/nu + (n-1)*b; admission requires n < 2. Service then adds b.
    """
    q1, q2 = _local_matrices(lam, b, p)
    states, q = _product_model(q1, q2)
    pi1, pi2 = stationary(q1), stationary(q2)
    pi = np.kron(pi1, pi2)
    rates = (lam * p, lam * (1.0 - p))
    nu = 3.0 / b
    n1 = np.arange(5, dtype=float)
    n2 = np.array([n for n, _ in _P2_STATES], dtype=float)
    busy = [float(pi1[1:].sum()), float(pi2[1:].sum())]
    queue = [float(pi1 @ np.maximum(n1 - 1.0, 0.0)),
             float(pi2 @ np.maximum(n2 - 1.0, 0.0))]
    number = [float(pi1 @ n1), float(pi2 @ n2)]
    acceptance = [float(pi1[:4].sum()), float(pi2[:4].sum())]
    loss = [float(pi1[4]), float(pi2[4:].sum())]

    # These rewards are evaluated only on admissible pre-arrival states.
    wait1_numerator = float(pi1[:4] @ (n1[:4] * b))
    wait2_numerator = sum(
        float(pi2[i]) * ((4 - phase) / nu + (n - 1) * b)
        for i, (n, phase) in enumerate(_P2_STATES)
        if 0 < n < 2
    )
    wait = [wait1_numerator / acceptance[0],
            float(wait2_numerator / acceptance[1])]
    sojourn = [float(value + b) for value in wait]

    # Main throughputs are completion rewards, independent of arrival rewards.
    throughput = [float(busy[0] / b),
                  float(nu * (pi2[3] + pi2[6]))]
    accepted_throughput = [float(rate * probability)
                           for rate, probability in zip(rates, acceptance)]
    total_throughput = float(sum(throughput))
    admitted_rate = float(sum(accepted_throughput))
    total_wait = (float(sum(rate * value for rate, value
                            in zip(accepted_throughput, wait)) / admitted_rate)
                  if admitted_rate > 0.0 else 0.0)
    total_sojourn = (float(sum(rate * value for rate, value
                               in zip(accepted_throughput, sojourn)) / admitted_rate)
                     if admitted_rate > 0.0 else 0.0)
    metrics = {
        "busy": busy,
        "queue": queue,
        "number": number,
        "wait": wait,
        "sojourn": sojourn,
        "loss": loss,
        "throughput": throughput,
        "accepted_throughput": accepted_throughput,
        "total_busy": float(sum(busy)),
        "total_queue": float(sum(queue)),
        "total_number": float(sum(number)),
        "total_wait": total_wait,
        "total_sojourn": total_sojourn,
        "total_loss": float(p * loss[0] + (1.0 - p) * loss[1]),
        "total_throughput": total_throughput,
        "offered_load": [float(rate * b) for rate in rates],
        "total_load": float(lam * b),
        "idle": float(pi1[0] * pi2[0]),
    }
    return {
        "states": states,
        "Q": q,
        "pi": pi,
        "metrics": metrics,
        "local_Q": [q1, q2],
        "local_pi": [pi1, pi2],
    }
