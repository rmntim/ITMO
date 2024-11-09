#include <inttypes.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

void error(const char *s) {
  fprintf(stderr, "%s", s);
  abort();
}

#define _print(type, x) type##_print(x)

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
  };                                                                           \
                                                                               \
  void list_##type##_push(struct list_##type *list, type v) {                  \
    while (list->next != NULL)                                                 \
      list = list->next;                                                       \
    struct list_##type *node = malloc(sizeof(struct list_##type));             \
    node->value = v;                                                           \
    list->next = node;                                                         \
  }                                                                            \
                                                                               \
  void list_##type##_print(struct list_##type *list) {                         \
    while (list->next != NULL) {                                               \
      print(list->value);                                                      \
      list = list->next;                                                       \
    }                                                                          \
    print(list->value);                                                        \
  }

DEFINE_LIST(int64_t)
DEFINE_LIST(double)
DEFINE_LIST(float)

int main(void) {
  struct list_float lf = {0};
  struct list_double ld = {0};
  struct list_int64_t li = {0};

  list_float_push(&lf, 0.1234f);
  list_double_push(&ld, 0.1234);
  list_int64_t_push(&li, 1234);

  list_float_print(&lf);
  list_double_print(&ld);
  list_int64_t_print(&li);

  return 0;
}
