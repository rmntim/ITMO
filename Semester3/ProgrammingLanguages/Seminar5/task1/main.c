#include <stdio.h>

#define print_var(x) printf(#x " is %d\n", x)
#define MFOO 2

int main(int argc, char *argv[]) {
  int foo = 0;
  const int cFoo = 1;

  print_var(foo);
  print_var(cFoo);
  print_var(MFOO);

  print_var(42);

  return 0;
}
