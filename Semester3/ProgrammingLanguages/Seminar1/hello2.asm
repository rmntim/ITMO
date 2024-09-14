; hello.asm 
section .data
outmsg: db  "hello, stdout!", 10
errmsg: db  "hello, stderr!", 10

section .text
global _start

_start:
    mov     rax, 1           ; 'write' syscall number
    mov     rdi, 1           ; stdout descriptor
    mov     rsi, outmsg      ; string address
    mov     rdx, 15          ; string length in bytes
    syscall

    mov     rax, 1           ; 'write' syscall number
    mov     rdi, 2           ; stderr descriptor
    mov     rsi, errmsg      ; string address
    mov     rdx, 15          ; string length in bytes
    syscall

    mov     rax, 60          ; 'exit' syscall number
    xor     rdi, rdi
    syscall
