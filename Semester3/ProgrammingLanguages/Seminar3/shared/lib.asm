section .data

codes:
 db "0123456789ABCDEF"

section .text

global print_hex
print_hex:
  mov rax, rdi
  mov rdi, 1
  mov rdx, 1
  mov rcx, 64
.loop:
  push rax
  sub  rcx, 4
  sar rax, cl
  and rax, 0xf
  lea rsi, [codes + rax]
  mov rax, 1
  push    rcx
  syscall 
  pop     rcx
  pop rax
  test rcx, rcx
  jnz  .loop
  ret  

global exit
exit:
  mov     rax, 60
  syscall 
