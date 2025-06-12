    .data

input_addr:  .word 0x80
memory_size: .word 0x1000

    .text

_start:
    movea.l memory_size, A7  ; A7 = &memory_size
    movea.l (A7), A7         ; A7 = memory_size

    movea.l input_addr, A0   ; A0 = &input_addr
    movea.l (A0), A0         ; A0 = input_addr

    move.l (A0), D0          ; D0 = *input_addr

    jsr reverse_bits

    move.l D0, 4(A0)         ; *(input_addr + 4) = result
    halt

reverse_bits:
    link A6, -4              ; save fp, decrement sp
    move.l D0, -4(A6)        ; int orig = D0

    move.l 0, D0             ; result
    move.b 31, D1            ; counter
    move.l 1, D2             ; low_mask
    move.l 0x80000000, D3    ; high_mask
loop:
    move.l -4(A6), D4        ; temp
    and.l D2, D4             ; temp &= low_mask
    beq skip_bit_set         ; if ^ == 0 goto skip_bit_set
    or.l D3, D0              ; else result |= high_mask
skip_bit_set:
    lsl.l 1, D2              ; low_mask << 1
    lsr.l 1, D3              ; high_mask >> 1
    sub.b 1, D1              ; counter--
    bpl loop                 ; while i >= 0
done:
    unlk A6                  ; restore sp, pop fp
    rts