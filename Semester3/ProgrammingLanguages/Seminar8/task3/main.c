#include <semaphore.h>
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
  // Создаем разделяемую память для массива и семафоров
  int *shmem = (int *)create_shared_memory(SIZE * sizeof(int));
  sem_t *sem_parent =
      (sem_t *)create_shared_memory(sizeof(sem_t)); // Семафор для родителя
  sem_t *sem_child = (sem_t *)create_shared_memory(
      sizeof(sem_t)); // Семафор для дочернего процесса

  // Инициализация семафоров
  sem_init(sem_parent, 1, 0); // Родитель ждет
  sem_init(sem_child, 1, 1);  // Дочерний процесс может начать

  // Инициализация массива
  for (int i = 0; i < SIZE; i++) {
    shmem[i] = i + 1;
  }

  printf("Shared memory at: %p\n", shmem);

  int pid = fork();

  if (pid == 0) {
    // Дочерний процесс
    int index, value;

    while (1) {
      sem_wait(sem_child); // Ждем разрешения от родителя
      // Изначальное состояние - 1, после wait становится 0

      printf("Enter index (0-9, or negative to exit): ");
      scanf("%d", &index);

      if (index < 0) {
        // Сообщаем родителю, что программа завершилась
        shmem[0] = -1; // Маркер завершения
        sem_post(sem_parent);
        break;
      }

      printf("Enter new value: ");
      scanf("%d", &value);

      if (index >= 0 && index < SIZE) {
        shmem[index] = value; // Обновляем массив
      } else {
        printf("Invalid index!\n");
      }

      sem_post(sem_parent); // Сообщаем родителю об обновлении
                            // sem_parent становится 1
    }

    exit(0);
  } else {
    // Родительский процесс
    printf("Child's pid is: %d\n", pid);

    while (1) {
      sem_wait(sem_parent); // Ожидаем сигнал от дочернего процесса

      // Проверяем завершение программы по маркеру
      if (shmem[0] == -1) {
        printf("Child process has exited.\n");
        break;
      }

      // Выводим массив
      printf("Updated array: ");
      for (int i = 0; i < SIZE; i++) {
        printf("%d ", shmem[i]);
      }
      printf("\n");

      sem_post(sem_child); //  Разрешаем дочернему процессу продолжить
    }

    wait(NULL); // Дожидаемся полного завершения дочернего процесса
  }

  // Освобождение ресурсов
  sem_destroy(sem_parent);
  sem_destroy(sem_child);
  munmap(sem_parent, sizeof(sem_t));
  munmap(sem_child, sizeof(sem_t));
  munmap(shmem, SIZE * sizeof(int));

  return 0;
}
