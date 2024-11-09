.PHONY: all
all: preprocess run

.PHONY: run
run: main.c
	$(CC) $(CFLAGS) $? -o main
	./main

.PHONY: preprocess
preprocess: main.c
	$(CC) -E $? -o main.cc

.PHONY: clean
clean:
	@rm main
	@rm main.cc
