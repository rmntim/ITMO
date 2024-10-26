CC=gcc
CFLAGS=-Wall -Werror -Wextra -pedantic -std=c99 -no-pie
AS=nasm
ASFLAGS=-felf64
TARGET=main

.PHONY: all
all: $(TARGET)

mmap.o: mmap.asm
	$(AS) $(ASFLAGS) -o $@ $?

cat.o: cat.c
	$(CC) -c $(CFLAGS) -o $@ $?

$(TARGET): mmap.o cat.o
	$(CC) $(CFLAGS) -o $@ $?

.PHONY: clean
clean:
	rm $(TARGET) *.o
