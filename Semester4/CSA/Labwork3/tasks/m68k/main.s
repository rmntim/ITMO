    .data

input_addr: .word 0x80

    .text

_start:
    movea.l input_addr, A0
    movea.l (A0), A0
    move.l (A0), D0
    move.l 0, D1
    move.b 31, D2
    move.l 1, D3
    move.l 0x80000000, D4
loop:
    move.l D0, D5
    and.l D3, D5
    beq skip_bit_set
    or.l D4, D1
skip_bit_set:
    lsl.l 1, D3
    lsr.l 1, D4
    sub.b 1, D2
    bpl loop
done:
    move.l D1, 4(A0)
    halt
