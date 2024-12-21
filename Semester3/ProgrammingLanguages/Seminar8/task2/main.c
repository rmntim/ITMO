#include <stdio.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <sys/wait.h>
#include <unistd.h>

#define SIZE 10

void *create_shared_memory(size_t size) {
  return mmap(NULL, size, PROT_READ | PROT_WRITE, MAP_SHARED | MAP_ANONYMOUS,
              -1, 0);
}

int main() {
  int pipefd[2]; // Дескрипторы для конвейера
  if (pipe(pipefd) == -1) {
    perror("pipe");
    exit(EXIT_FAILURE);
  }

  // Выделяем память для 10 чисел типа int
  int *shmem = (int *)create_shared_memory(SIZE * sizeof(int));

  // Заполняем массив числами от 1 до 10
  for (int i = 0; i < SIZE; i++) {
    shmem[i] = i + 1;
  }

  printf("Shared memory at: %p\n", shmem);

  int pid = fork();

  if (pid == 0) {
    // Дочерний процесс
    close(pipefd[0]); // Закрываем неиспользуемый конец для чтения
    int index, value;

    while (1) {
      printf("Enter index (0-9, or negative to exit): ");
      scanf("%d", &index);

      if (index < 0) {
        // Сообщаем родителю о завершении
        write(pipefd[1], &index, sizeof(int));
        break;
      }

      printf("Enter new value: ");
      scanf("%d", &value);

      if (index >= 0 && index < SIZE) {
        shmem[index] = value; // Обновляем значение в массиве

        // Сообщаем родительскому процессу об изменении
        write(pipefd[1], &index, sizeof(int));
      } else {
        printf("Invalid index!\n");
      }
    }

    close(pipefd[1]);
    exit(0); // Завершаем работу дочернего процесса
  } else {
    // Родительский процесс
    close(pipefd[1]); // Закрываем неиспользуемый конец для записи

    int received_index;
    printf("Child's pid is: %d\n", pid);

    while (1) {
      // Ожидаем сообщения от дочернего процесса
      if (read(pipefd[0], &received_index, sizeof(int)) > 0) {
        if (received_index < 0) {
          printf("Child process has exited.\n");
          break;
        }

        // Выводим массив при получении сообщения
        printf("Updated array: ");
        for (int i = 0; i < SIZE; i++) {
          printf("%d ", shmem[i]);
        }
        printf("\n");
      }
    }

    close(pipefd[0]);
    wait(NULL); // Ждём завершения дочернего процесса
  }

  // Освобождаем общую память
  munmap(shmem, SIZE * sizeof(int));

  return 0;
}
