all: main

main:
	@nasm -felf64 -o lib.o ../shared/lib.asm
	@nasm -felf64 -o main.o main.asm
	@ld -o main lib.o main.o
	@./main

.PHONY: clean
clean:
	@rm *.o main
