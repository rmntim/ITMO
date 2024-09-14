#/bin/bash

set -uexo pipefail

FILENAME=test.txt

grep '^.$' $FILENAME
grep '\d' $FILENAME
grep -E '^0[xX][0-9a-fA-F]+$' $FILENAME
grep -P '[\s^]\w{3}[\s$]' $FILENAME
grep '^\s*$' $FILENAME
grep -v 'mmm' $FILENAME
