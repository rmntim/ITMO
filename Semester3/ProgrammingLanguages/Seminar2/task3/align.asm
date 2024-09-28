section .data
codes:
    db      "0123456789ABCDEF"
n:
    db 10

section .text
global _start
_start:
    mov rdi, 0xaa 
    mov rsi, 0xbb
    mov rdx, 0xcc
    mov rcx, 0xff
    call put_stack
    jmp exit

print_n:
    mov rax, 1
    mov rdi, 1
    mov rsi, n
    mov rdx, 1
    syscall
    ret

exit:
    mov  rax, 60
    xor  rdi, rdi
    syscall

put_stack:
    xor r10, r10
    sub rsp, 32
    mov [rsp +  0], rdi
    mov [rsp +  8], rsi
    mov [rsp + 16], rdx
    mov [rsp + 24], rcx
.loop:
    mov rdi, [rsp + r10]
    sub rsp, 8
    call print_hex
    call print_n
    add rsp, 8
    add r10, 8
    cmp r10, 32
    jne .loop
    add rsp, 32
    ret


print_hex: 
    mov  rax, rdi
    mov  rdi, 1
    mov  rdx, 1
    mov  rcx, 64
.loop:
    push rax
    sub  rcx, 4
    sar  rax, cl
    and  rax, 0xf

    lea  rsi, [codes + rax]
    mov  rax, 1

    push rcx
    syscall
    pop  rcx

    pop rax
    test rcx, rcx
    jnz .loop
    ret
