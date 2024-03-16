org 0x100

cla
ld Y
push
ld X
push
call $multiply
pop
st R
pop
hlt

X: word -100
Y: word 3

R: word 0

org 0x300
multiply:
      cla
iter: add &1
      loop &2 
      jump iter
      st &1
      ret

org 0x500
RES: word 0
divide:
          cla
          ld &1
          bpl pos_iter

neg_iter: ld &1
          bpl neg_fini
          add &2
          st &1
          cmp -(RES)
          jump neg_iter
neg_fini: ld RES
          inc
          st &1
          ret

pos_iter: ld &1
          bns pos_fini
          sub &2
          st &1
          cmp (RES)+
          jump pos_iter
pos_fini: ld RES
          dec
          st &1
          ret

