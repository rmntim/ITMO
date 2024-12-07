#include <stdio.h>
#include <stdlib.h>

struct user {
  const char *name, *password;
} const users[] = {{"Cat", "Meowmeow"}, {"Skeletor", "Nyarr"}};

void print_users() {
  printf("Users:\n");
  for (size_t i = 0; i < sizeof(users) / sizeof(struct user); i++) {
    printf("%s: %s\n", users[i].name, users[i].password);
  }
}

void fill(FILE *f, char *where, size_t size) {
  size_t read_total = 0;
  while (read_total < size - 1) {
    size_t read = fread(where + read_total, 1, 1, f);
    if (!read)
      break;
    read_total += read;
  }
  where[read_total] = '\0';
}

void vulnerable(FILE *f) {
  char buffer[8];
  fill(f, buffer, sizeof(buffer));
}

int main(int argc, char **argv) {
  vulnerable(stdin);
  puts("nothing happened");
  return 0;
}
