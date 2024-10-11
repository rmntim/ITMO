%include "../shared/lib.asm"

%macro pushn 2-*
%rep %0
  push %1
%rotate 1
%endrep
%endmacro

%macro popn 2-*
%rep %0
%rotate -1
  pop %1
%endrep
%endmacro

section .text

global _start
_start:
  mov rax, 0xDEADBEEF
  mov rbx, 0xCAFE
  mov rcx, 0x80085
; Сохраняем значения регистров rax, rbx, rcx
  pushn rax, rbx, rcx

  xor rax, rax
  xor rbx, rbx
  xor rcx, rcx

; Восстанавливаем значения регистров rax, rbx, rcx
  popn rax, rbx, rcx

  mov rdi, rax
  call print_hex

; Завершаем программу
  mov     rax, 60
  xor     rdi, rdi
  syscall 

