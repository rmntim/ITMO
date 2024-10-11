all: stackn

stackn:
	@nasm -felf64 -g -o stackn.o stackn.asm
	@ld -o stackn stackn.o
	@./stackn

.PHONY: clean
clean:
	@rm *.o
	@rm stackn
