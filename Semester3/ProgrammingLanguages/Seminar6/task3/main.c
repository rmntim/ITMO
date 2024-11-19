#include "vector.h"
#include <stdio.h>

int main() {
  struct vector *v = vector_create(5);
  if (!v) {
    fprintf(stderr, "Failed to create vector\n");
    return 1;
  }

  for (size_t i = 0; i <= 100; i++) {
    vector_push_back(v, i * i);
  }

  vector_print(v, stdout);
  vector_destroy(v);

  return 0;
}
