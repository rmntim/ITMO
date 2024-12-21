.PHONY: all
all: bad_print main

main: main.c
	$(CC) $(CFLAGS) -o $@ $?

bad_print: bad_print.c
	$(CC) $(CFLAGS) -pthread -o $@ $?

.PHONY: clean
clean:
	rm main bad_print
