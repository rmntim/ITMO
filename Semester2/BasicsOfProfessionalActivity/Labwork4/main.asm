org 0x1ef

cla
st R
ld X
push
call $func
pop
inc
sub R
st R
ld Y
dec
push
call $func
pop
sub R
st R
ld Z
inc
push
call $func
pop
inc
add R
st R
hlt

Z: word 2004
Y: word 1
X: word 0
R: word 0

;; subroutine
org 0x6fe
func:
      ld &1
      bns calc
      cmp N
      beq dflt
      blt dflt
calc: asl
      sub M
      jump fini
dflt: ld N
fini: st &1
      ret

N: word 2003
M: word 43
