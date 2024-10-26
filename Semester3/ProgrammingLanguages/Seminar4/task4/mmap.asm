section .note.GNU-stack noalloc noexec nowrite progbits
; hello_mmap.asm
%define O_RDONLY    0

%define PROT_READ   0x1
%define MAP_PRIVATE 0x2

%define SYS_WRITE  	1
%define SYS_OPEN   	2
%define SYS_CLOSE  	3
%define SYS_MMAP   	9
%define SYS_MUNMAP  11

%define FD_STDOUT   1
%define FD_STDERR   2

section .text

global print_file

; These functions are used to print a null terminated string
; rdi holds a string pointer
print_string:
  push    rdi
  call    string_length
  pop     rsi
  mov     rdx, rax
  mov     rax, SYS_WRITE
  mov     rdi, FD_STDOUT
  syscall 
  ret     

string_length:
  xor rax, rax
.loop:
  cmp byte [rdi+rax], 0
  je  .end
  inc rax
  jmp .loop
.end:
  ret 

; This function is used to print a substring with given length
; rdi holds a string pointer
; rsi holds a substring length
print_substring:
  mov     rdx, rsi
  mov     rsi, rdi
  mov     rax, SYS_WRITE
  mov     rdi, FD_STDOUT
  syscall 
  ret     

open:
  mov rax, SYS_OPEN
  syscall
  ret

close:
  mov rax, SYS_CLOSE
  syscall
  ret

mmap:
  mov rax, SYS_MMAP
  syscall
  ret

munmap:
  mov rax, SYS_MUNMAP
  syscall
  ret

print_file:
  mov rsi, O_RDONLY
  mov rdx, 0
  call open
  push rax

  mov rdi, 0           ; addr
  mov rsi, 4096        ; length
  mov rdx, PROT_READ   ; prot
  mov r10, MAP_PRIVATE ; flags
  mov r8,  rax 		   ; fd
  mov r9,  0           ; offset
  call mmap

  pop rdi
  push rax

  call close

  mov rdi, [rsp]
  call print_string

  pop rdi
  mov rsi, 4096
  call munmap
  ret
