#!/bin/bash

set -ueo pipefail

PYTHON3=$(which python3)
WRENCH=$(which wrench)

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

for arch in */; do
  asm_source="$(realpath $arch/main.s)"
  config="$(realpath $arch/config.yaml)"

  echo "--- running $arch ---"

  ($WRENCH --isa ${arch::${#arch}-1} -c $config $asm_source && echo -e "\n$arch ${GREEN}SUCCESS${NC}") || echo -e "\n$arch ${RED}FAILED${NC}"

  echo "--- finished ---"
  echo
done

