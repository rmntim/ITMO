    .data

input_addr:      .word  0xA0
output_addr:     .word  0xA4

    .text
_start:
    lui t0, %hi(input_addr)
    addi t0, t0, %lo(input_addr)
    lw t1, 0(t0)
    lw a0, 0(t1)

    jal ra, little_to_big_endian

    lui t0, %hi(output_addr)
    addi t0, t0, %lo(output_addr)
    lw t1, 0(t0)
    sw a0, 0(t1)
    
    halt

little_to_big_endian:
    addi t5, zero, 0xFF

    mv t0, a0                   ; t0 = original value
    addi a0, zero, 0            ; Initialize result to 0
    addi t1, zero, 0            ; Initialize loop counter
    addi t2, zero, 4            ; Loop 4 times (for 4 bytes)

byte_swap_loop:
    beq t1, t2, byte_swap_done  ; Exit loop when counter reaches 4
    
    and t3, t0, t5              ; Extract current byte
    
    addi t6, zero, 3       
    sub t6, t6, t1              ; Calculate shift amount (3-i)*8
    addi t4, zero, 8
    mul t6, t6, t4              ; t6 = (3-i)*8
    
    sll t3, t3, t6              ; Shift byte to its new position
    or a0, a0, t3               ; Add byte to result
    
    addi t6, zero, 8
    srl t0, t0, t6              ; Shift original value right by 8 bits
    addi t1, t1, 1              ; Increment counter
    j byte_swap_loop

byte_swap_done:
    jr ra
