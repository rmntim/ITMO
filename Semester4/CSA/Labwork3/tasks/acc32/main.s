    .data

input_addr:      .word  0x80
output_addr:     .word  0x84
const_1:         .word  1
const_2:         .word  2
n:               .word  0

    .text

_start:
    load_ind     input_addr
    bgt          good

    load_imm     -1
    store_ind    output_addr
    halt

good:
    div          const_2
    store        n
    add          const_1
    mul          n
    bvs          overflow
    store_ind    output_addr
    halt

overflow:
    load_imm     0xCCCCCCCC
    store_ind    output_addr
    halt
