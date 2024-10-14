AS=nasm
ASFLAGS=-felf64
LD=ld

all: main

%.o: %.asm
	@$(AS) $(ASFLAGS) -o $@ $<

main: lib.o main.o
	@$(LD) -o $@ $?
	@./main

.PHONY: clean
clean:
	@rm *.o main
