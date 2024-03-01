org 0x100

SMASK: word 0x0040
X:     word 0x0041
Y:     word 0x0030 ; 7 битные числа
R:     word 0

_start:
  cla

  ld  X
  and SMASK
  bzs x_pos
  neg
  or X
  st X

x_pos:
  ld Y
  and SMASK
  bzs y_pos
  neg
  or Y
  st Y

y_pos:
  ld X
  add Y
  st R

  hlt
