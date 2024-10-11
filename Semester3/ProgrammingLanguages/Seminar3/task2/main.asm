%include "../shared/lib.inc"

section .text

global _start
_start:
    mov  rdi, 0xDEADBEEF
    call print_hex

    xor rdi, rdi
    call exit
