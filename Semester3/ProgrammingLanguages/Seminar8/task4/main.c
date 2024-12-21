#include <inttypes.h>
#include <pthread.h>
#include <semaphore.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

sem_t sem_begin0, sem_begin1, sem_end;

int x, y, read0, read1;

void *thread0_impl(void *param) {
  for (;;) {
    sem_wait(&sem_begin0); // ждёт пост из мейна

    x = 1;

    // Полный барьер компиляции и процессора
    __asm__ __volatile__("mfence" ::: "memory"); // Барьер компиляции
    // Гарантирует, что компилятор не поменяет порядок операций.

    //__sync_synchronize();                 // Барьер процессора
    //  Гарантирует, что процессор выполнит все предыдущие операции
    //  записи/чтения до следующей инструкции.

    read0 = y;

    sem_post(&sem_end); // увелич. значение, стук мейну
  }
  return NULL;
}

void *thread1_impl(void *param) {
  for (;;) {
    sem_wait(&sem_begin1);

    y = 1;

    // Полный барьер компиляции и процессора
    __asm__ __volatile__("mfence" ::: "memory"); // Барьер компиляции
    //__sync_synchronize();                 // Барьер процессора

    read1 = x;

    sem_post(&sem_end);
  }
  return NULL;
}

int main(void) {
  sem_init(&sem_begin0, 0, 0);
  sem_init(&sem_begin1, 0, 0);
  sem_init(&sem_end, 0, 0);

  pthread_t thread0, thread1;
  pthread_create(&thread0, NULL, thread0_impl, NULL);
  pthread_create(&thread1, NULL, thread1_impl, NULL);

  for (uint64_t i = 0; i < 1000000; i++) {
    x = 0;
    y = 0;
    sem_post(&sem_begin0); // начало работы 0 треда
    sem_post(&sem_begin1); // начало работы 1 треда

    sem_wait(&sem_end); // главный поток ждёт завершения
    sem_wait(&sem_end);

    if (read0 == 0 && read1 == 0) {
      printf("reordering happened on iteration %" PRIu64 "\n", i);
      exit(0);
    }
  }
  puts("No reordering detected during 1000000 iterations");
  return 0;
}

// Семафоры в программе обеспечивают синхронизацию между потоками,
// чтобы они начинали и завершали свою работу одновременно.
//  Барьеры устраняют возможность реордеринга, гарантируя корректный порядок
//  операций

// Важно: Мьютексы и семафоры на низком уровне уже содержат
// барьеры, поэтому они автоматически предотвращают реордеринг вокруг своих
// вызовов
