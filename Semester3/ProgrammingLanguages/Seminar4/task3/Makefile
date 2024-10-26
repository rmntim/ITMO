CC=gcc
CFLAGS=-Wall -Werror -Wextra -pedantic -std=c99 -no-pie
AS=nasm
ASFLAGS=-felf64
TARGET=main

.PHONY: all
all: $(TARGET)

string.o: string.asm
	$(AS) $(ASFLAGS) -o $@ $?

hello.o: hello.c
	$(CC) -c $(CFLAGS) -o $@ $?

$(TARGET): string.o hello.o
	$(CC) $(CFLAGS) -o $@ $?

.PHONY: clean
clean:
	rm $(TARGET) *.o
