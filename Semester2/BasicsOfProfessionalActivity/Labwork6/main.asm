org 0x0
IO0: word $DEFAULT, 0x180
IO1: word $INT1,    0x180
IO2: word $INT2,    0x180

org 0x00B
X:   word ?

MAX_X:     word 0x0028
MIN_X:     word 0xFFD3

org 0x010
DEFAULT:   iret

START:
           di
           cla
           out 0x1     ; disable interrupts on unused IO
           out 0x7
           out 0xB
           out 0xD
           out 0x11
           out 0x15
           out 0x19
           out 0x1D

           ld #0x9
           out 0x3
           ld #0xA
           out 0x5

           ei
MAIN:      di
           ld $X
           add #3
           call CHECK
           st $X
           ei
           jump MAIN

CHECK:
           cmp MAX_X
           blt CHECK_MIN
           cmp MAX_X
           beq EXIT
           jump SET_MAX
CHECK_MIN: cmp MIN_X 
           bge EXIT
SET_MAX:   ld MAX_X
EXIT:      
           ret

INT1:
           di
           push
           ld $X
           nop
           asl
           add $X
           neg
           sub #6
           out 0x2
           nop
           pop
           ei
           iret

INT2:
           di
           push
           in 0x4
           nop
           push
           asl
           add &1
           sub $X
           st $X
           pop
           nop
           pop
           ei
           iret
