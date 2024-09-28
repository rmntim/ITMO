section .data
message:       db  'hello, world!', 10, 0
error_message: db  'hello, errors!', 10, 0

section .text
exit:
    mov  rax, 60
    xor  rdi, rdi          
    syscall

string_length:
    mov  rax, rdi
  .counter:
    cmp  byte [rdi], 0
    je   .end
    inc  rdi
    jmp  .counter
  .end:
    sub  rdi, rax
    mov  rax, rdi
    ret

print_string:
    push rdi
    call string_length
    mov  rdx, rax
    pop rsi
    mov  rax, 1
    mov  rdi, 1
    syscall
    ret
