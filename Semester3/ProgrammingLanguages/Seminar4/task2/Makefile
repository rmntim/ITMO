AS=nasm
ASFLAGS=-felf64
LD=ld
LDFLAGS=
TARGET=main
ASMS=$(wildcard *.asm)
OBJS=$(ASMS:%.asm=%.o)

.PHONY: all
all: $(TARGET)

%.o: %.asm
	$(AS) $(ASFLAGS) -o $@ $?

$(TARGET): $(OBJS)
	$(LD) $(LDFLAGS) -o $@ $?

.PHONY: clean
clean:
	rm $(TARGET) $(OBJS)
