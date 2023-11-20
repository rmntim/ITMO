#!/bin/bash

set -uexo pipefail

if [ ! -d out ]; then
    mkdir out
fi

pdflatex -output-directory out/ src/Main.tex
