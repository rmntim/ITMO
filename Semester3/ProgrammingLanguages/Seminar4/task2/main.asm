; hello_mmap.asm
%define O_RDONLY    0

%define PROT_READ   0x1
%define MAP_PRIVATE 0x2

%define SYS_WRITE  	1
%define SYS_OPEN   	2
%define SYS_CLOSE  	3
%define SYS_FSTAT   5
%define SYS_MMAP   	9
%define SYS_MUNMAP  11

%define FD_STDOUT   1
%define FD_STDERR   2

%define STAT_SIZE   144
%define ST_SIZE_OFF 48
%define OFF_T_SIZE  8

section .rodata

fname db 'hello.txt', 0

section .data
statbuf:   times STAT_SIZE db 0xCA
file_size: dq   0
file_fd:   dq   0
map_ptr:   dq   0

section .text

global _start

; use exit system call to shut down correctly
exit:
  mov     rax, 60
  syscall 

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

get_file_size:
    mov rax, SYS_FSTAT
    mov rsi, statbuf
    syscall

    cmp rax, -1
    je .error

    mov rax, [statbuf + ST_SIZE_OFF]
    ret

.error:
    mov rax, -1
    ret

_start:
  mov rdi, fname
  mov rsi, O_RDONLY
  mov rdx, 0
  call open
  mov [file_fd], rax

  mov rdi, rax
  call get_file_size
  mov [file_size], rax

  mov rdi, 0           ; addr
  mov rsi, rax         ; length
  mov rdx, PROT_READ   ; prot
  mov r10, MAP_PRIVATE ; flags
  mov r8,  [file_fd]   ; fd
  mov r9,  0           ; offset
  call mmap
  mov [map_ptr], rax

  mov rdi, [file_fd]
  call close

  mov rdi, [map_ptr]
  mov rsi, [file_size]
  call print_substring

  mov rdi, [map_ptr]
  mov rsi, [file_size]
  call munmap

  xor rdi, rdi
  call exit
