#include <inttypes.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

void error(const char *s) {
  fprintf(stderr, "%s", s);
  abort();
}

#define print(x)                                                               \
  _Generic((x),                                                                \
      int64_t: int64_t_print(x),                                               \
      double: double_print(x),                                                 \
      float: float_print(x),                                                   \
      default: error("Unsupported operation"))

void int64_t_print(int64_t i) { printf("%" PRId64 "\n", i); }
void double_print(double d) { printf("%lf\n", d); }
void float_print(float f) { printf("%f\n", f); }
void string_print(const char *s) { printf("%s\n", s); }

#define DEFINE_LIST(type)                                                      \
  struct list_##type {                                                         \
    type value;                                                                \
    struct list_##type *next;                                                  \
  };

DEFINE_LIST(int64_t)
DEFINE_LIST(double)
DEFINE_LIST(float)

#define list_push(l, v)                                                        \
  _Generic((l),                                                                \
      struct list_int64_t *: int64_t_push,                                     \
      struct list_double *: double_push,                                       \
      struct list_float *: float_push,                                         \
      default: error("Unsupported operation"))(l, v)

#define list_print(l)                                                          \
  _Generic((l),                                                                \
      struct list_int64_t *: int64_t_list_print,                               \
      struct list_double *: double_list_print,                                 \
      struct list_float *: float_list_print,                                   \
      default: error("Unsupported operation"))(l)

void int64_t_push(struct list_int64_t *list, int64_t v) {
  while (list->next != NULL)
    list = list->next;
  struct list_int64_t *node = malloc(sizeof(struct list_int64_t));
  node->value = v;
  list->next = node;
}

void double_push(struct list_double *list, double v) {
  while (list->next != NULL)
    list = list->next;
  struct list_double *node = malloc(sizeof(struct list_double));
  node->value = v;
  list->next = node;
}

void float_push(struct list_float *list, float v) {
  while (list->next != NULL)
    list = list->next;
  struct list_float *node = malloc(sizeof(struct list_float));
  node->value = v;
  list->next = node;
}

void int64_t_list_print(struct list_int64_t *list) {
  while (list->next != NULL) {
    print(list->value);
    list = list->next;
  }
  print(list->value);
}

void double_list_print(struct list_double *list) {
  while (list->next != NULL) {
    print(list->value);
    list = list->next;
  }
  print(list->value);
}

void float_list_print(struct list_float *list) {
  while (list->next != NULL) {
    print(list->value);
    list = list->next;
  }
  print(list->value);
}

int main(void) {
  struct list_int64_t li = {0};
  struct list_double ld = {0};
  struct list_float lf = {0};

  list_push(&li, 1234);
  list_push(&lf, 1234.0f);
  list_push(&ld, 1234.0);

  list_print(&li);
  list_print(&lf);
  list_print(&ld);

  return 0;
}
