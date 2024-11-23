#ifndef VECTOR_H
#define VECTOR_H

#include <stdbool.h>
#include <stddef.h>
#include <stdint.h>
#include <stdio.h>

struct vector;

struct maybe_int64 {
  bool valid;
  int64_t value;
};

struct vector *vector_create(size_t initial_capacity);
void vector_destroy(struct vector *v);

size_t vector_size(const struct vector *v);
size_t vector_capacity(const struct vector *v);

struct maybe_int64 vector_get(const struct vector *v, size_t index);
bool vector_set(struct vector *v, size_t index, int64_t value);

void vector_push_back(struct vector *v, int64_t value);
void vector_append_array(struct vector *v, const int64_t *array, size_t size);
void vector_resize(struct vector *v, size_t new_size);

void vector_print(const struct vector *v, FILE *out);

#endif /* VECTOR_H */
