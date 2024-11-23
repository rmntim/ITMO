#include "vector.h"

#include <inttypes.h>
#include <stdlib.h>
#include <string.h>

struct vector {
  int64_t *data;
  size_t size;
  size_t capacity;
};

struct vector *vector_create(size_t initial_capacity) {
  struct vector *v = malloc(sizeof(struct vector));
  if (!v)
    return NULL;

  v->data = malloc(sizeof(int64_t) * initial_capacity);
  if (!v->data) {
    free(v);
    return NULL;
  }

  v->size = 0;
  v->capacity = initial_capacity;
  return v;
}

void vector_destroy(struct vector *v) {
  if (v) {
    free(v->data);
    free(v);
  }
}

size_t vector_size(const struct vector *v) { return v->size; }

size_t vector_capacity(const struct vector *v) { return v->capacity; }

struct maybe_int64 vector_get(const struct vector *v, size_t index) {
  if (index >= v->size) {
    return (struct maybe_int64){.valid = false};
  }
  return (struct maybe_int64){.valid = true, .value = v->data[index]};
}

void vector_set(struct vector *v, size_t index, int64_t value) {
  if (index >= v->size) {
    fprintf(stderr, "Index out of bounds\n");
    exit(EXIT_FAILURE);
  }
  v->data[index] = value;
}

void vector_push_back(struct vector *v, int64_t value) {
  if (v->size == v->capacity) {
    size_t new_capacity = v->capacity * 2;
    int64_t *new_data = realloc(v->data, sizeof(int64_t) * new_capacity);
    if (!new_data) {
      fprintf(stderr, "Failed to allocate memory\n");
      exit(EXIT_FAILURE);
    }
    v->data = new_data;
    v->capacity = new_capacity;
  }
  v->data[v->size++] = value;
}

void vector_append_array(struct vector *v, const int64_t *array, size_t size) {
  for (size_t i = 0; i < size; i++) {
    vector_push_back(v, array[i]);
  }
}

void vector_resize(struct vector *v, size_t new_size) {
  if (new_size > v->capacity) {
    int64_t *new_data = realloc(v->data, sizeof(int64_t) * new_size);
    if (!new_data) {
      fprintf(stderr, "Failed to allocate memory\n");
      exit(EXIT_FAILURE);
    }
    v->data = new_data;
    v->capacity = new_size;
  }
  v->size = new_size;
}

void vector_print(const struct vector *v, FILE *out) {
  for (size_t i = 0; i < v->size; i++) {
    fprintf(out, "%" PRId64 " ", v->data[i]);
    if ((i + 1) % 10 == 0) {
      fprintf(out, "\n");
    }
  }
  fprintf(out, "\n");
}
