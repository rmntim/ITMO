#!/bin/bash

mkdir repo && cd repo

git init -b b0

touch 0
git add .
git commit -m "r0"

git checkout -b b1

touch 1
git add .
git commit -m "r1"

echo 'old' > 2
git add .
git commit -m "r2"

git checkout -b b2

echo new > 2
git add .
git commit -m "r3"

git checkout -b b3

touch 3
git add .
git commit -m "r4"

git checkout b2

git merge b3 -m "r5" --no-ff

git checkout b1

echo new > 3
git add .
git commit -m "r6"

git merge b2 -m "r7" --no-ff
