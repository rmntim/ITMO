#!/bin/bash

git cherry-pick -X ours --allow-empty --empty=keep -m 1 $(seq 63 84 | xargs -L1 -I{} git log --grep "r{}" | grep commit | awk '{print $2}')
git cherry-pick -X ours --allow-empty --empty=keep -m 1 $(seq 62 41 | xargs -L1 -I{} git log --grep "r{}" | grep commit | awk '{print $2}')
git cherry-pick -X ours --allow-empty --empty=keep -m 1 $(seq 19 40 | xargs -L1 -I{} git log --grep "r{}" | grep commit | awk '{print $2}')
git cherry-pick -X ours --allow-empty --empty=keep -m 1 $(seq 19 0 | xargs -L1 -I{} git log --grep "r{}" | grep commit | awk '{print $2}')
