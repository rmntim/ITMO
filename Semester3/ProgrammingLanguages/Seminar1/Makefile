all: hello hello2

.PHONY: hello
hello:
	nasm -felf64 -g hello.asm -o hello.o
	ld -o hello hello.o

.PHONY: hello2
hello2:
	nasm -felf64 -g hello2.asm -o hello2.o
	ld -o hello2 hello2.o
	./hello2 1>out.txt 2>err.txt

.PHONY: clean
clean:
	rm hello.o hello
	rm hello2.o hello2
	rm out.txt err.txt
