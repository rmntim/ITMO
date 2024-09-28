all: align

align: align.asm
	nasm -felf64 align.asm
	ld -o align align.o
	./align

.PHONY: clean
clean:
	rm *.o align
