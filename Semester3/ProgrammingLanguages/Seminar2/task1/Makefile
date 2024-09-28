all: print_hex

print_hex: print_hex.asm
	nasm -felf64 print_hex.asm
	ld -o print_hex print_hex.o
	./print_hex

.PHONY: clean
clean:
	rm *.o print_hex
