"""Stationary distribution of a finite continuous-time Markov chain."""
import numpy as np


def stationary(generator):
    """Solve pi Q = 0 with sum(pi) = 1; Q uses row-source convention."""
    q = np.asarray(generator, dtype=float)
    a = q.T.copy()
    a[-1, :] = 1.0
    rhs = np.zeros(len(q))
    rhs[-1] = 1.0
    return np.linalg.solve(a, rhs)
