#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int check_password(FILE *f, const char *password) {
  char buffer[10];
  int okay = 0;

  if (fgets(buffer, sizeof(buffer), f) != NULL) {
    buffer[strcspn(buffer, "\n")] = '\0';

    if (strcmp(buffer, password) == 0)
      okay = 1;
  }

  return okay;
}

int main(int argc, char **argv) {
  if (check_password(stdin, "password"))
    puts("Access granted.");
  else
    puts("Wrong password.");
}
