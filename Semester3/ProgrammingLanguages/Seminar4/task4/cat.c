#include <stdio.h>

void print_file(const char* fname);

int main(int argc, char** argv) {
    if (argc != 2) {
        puts("ti eblan");
        return 1;
    }

    print_file(argv[1]);
    return 0;
}
