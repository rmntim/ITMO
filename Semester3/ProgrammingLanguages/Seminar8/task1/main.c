/* fork-example-1.c */

#include <stdio.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <sys/wait.h>
#include <unistd.h>

void *create_shared_memory(size_t size) {
  return mmap(NULL, size, PROT_READ | PROT_WRITE, MAP_SHARED | MAP_ANONYMOUS,
              -1, 0);
}

int main() {
  // Выделяем память для 10 чисел типа int
  int *shmem = (int *)create_shared_memory(10 * sizeof(int));

  // Заполняем массив числами от 1 до 10
  for (int i = 0; i < 10; i++) {
    shmem[i] = i + 1;
  }

  printf("Shared memory at: %p\n", shmem);

  int pid = fork();

  if (pid == 0) { // проверяем, чтобы блок кода выполнял только дочерний процесс
                  // у родительского процесса fork возвращает
    // Дочерний процесс
    int index, value;

    printf("Enter index (0-9): ");
    scanf("%d", &index);

    printf("Enter new value: ");
    scanf("%d", &value);

    if (index >= 0 && index < 10) {
      shmem[index] = value; // Обновляем значение в массиве
    } else {
      printf("Invalid index!\n");
    }

    exit(0); // Завершаем работу дочернего процесса
  } else {
    // Родительский процесс
    printf("Child's pid is: %d\n", pid);

    wait(NULL); // Ждём завершения дочернего процесса

    // Выводим содержимое массива
    printf("Updated array: ");
    for (int i = 0; i < 10; i++) {
      printf("%d ", shmem[i]);
    }
    printf("\n");
  }

  // Освобождаем общую память
  munmap(shmem, 10 * sizeof(int));

  return 0;
}
