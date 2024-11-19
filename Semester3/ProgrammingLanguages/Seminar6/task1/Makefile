CFLAGS = -Wall -std=c17 -pedantic

.PHONY: all
all: run

.PHONY: run
run: main.c
	$(CC) $(CFLAGS) $? -o main
	./main

.PHONY: clean
clean:
	@rm main
