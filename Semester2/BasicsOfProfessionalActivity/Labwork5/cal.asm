; IO-9 calculator program
;   DR => #1C
;   SR => #1D
;   MR => #1A
;
;   Numbers are equal to their value,
;     - => 0xA
;     + => 0xB
;     / => 0xC
;     * => 0xD
;     . => 0xE
;     = => 0xF
org 0x100

FIRST:    word 0     ; addr of first number
SECOND:   word 0     ; addr of second number
SIGN:     word 0
RESULT:   word 0
SIGNADDR: word $SIGN_IN
FINIADDR: word $FINI

start:
        cla
        call $READ_NUMBER
SIGN_IN:
        st SIGN
        ld $CURR
        st $FIRST
        cla
        st $CURR
        call $READ_NUMBER
       
FINI:
        ld $CURR
        st $SECOND
        cla
        call WORK
        hlt


WORK:
        ld $SIGN
        cmp #0xA
        beq MINUS
        cmp #0xB
        beq PLUS
        cmp #0xC
        beq DIVIDE
        cmp #0xD
        beq MULTIPLY
        ret
MINUS:
        ld $FIRST
        sub $SECOND
        st $RESULT
        ret
PLUS:
        ld $FIRST
        add $SECOND
        st $RESULT
        ret
DIVIDE:
        ld $SECOND
        push
        ld $FIRST
        push
        call $DIV
        pop
        st $RESULT
        pop
        ret
MULTIPLY:
        ld $SECOND
        push
        ld $FIRST
        push
        call $MUL
        pop
        st $RESULT
        pop
        ret

; read_number gets number from IO-9 and jumps to 1st stack arg
; if it gets a sign.
CURR:     word 0     ; current number
READ_NUMBER:
        in 0x1D
        and #0x40
        bzs READ_NUMBER
        in 0x1C
        cmp #0xA
        blt skip
        ret
skip:   push
        ld CURR
        call $SHIFT
        add &0
        st CURR
        pop
        cla
        jump READ_NUMBER

; shift multiplies number in AC by 10.
SHIFT:
        st tmp
        asl
        asl
        asl
        add tmp
        add tmp
        ret
tmp:    word 0

MUL:
      cla
iter: add &1
      loop &2 
      jump iter
      st &1
      ret

DIVRES:   word 0
DIV:
          cla
div_iter: ld &1
          bns div_fini
          sub &2
          st &1
          cmp (DIVRES)+
          jump div_iter
div_fini: ld DIVRES
          dec
          st &1
          ret
