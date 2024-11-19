#include <stdbool.h>
#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>

#define HEAP_BLOCKS 16
#define BLOCK_CAPACITY 1024

enum block_status { BLK_FREE = 0, BLK_ONE, BLK_FIRST, BLK_CONT, BLK_LAST };

struct heap {
  struct block {
    char contents[BLOCK_CAPACITY];
  } blocks[HEAP_BLOCKS];
  enum block_status status[HEAP_BLOCKS];
} global_heap = {0};

struct block_id {
  size_t value;
  bool valid;
  struct heap *heap;
};

struct block_id block_id_new(size_t value, struct heap *from) {
  return (struct block_id){.valid = true, .value = value, .heap = from};
}
struct block_id block_id_invalid(void) {
  return (struct block_id){.valid = false};
}

bool block_id_is_valid(struct block_id bid) {
  return bid.valid && bid.value < HEAP_BLOCKS;
}

/* Find block */

bool block_is_free(struct block_id bid) {
  if (!block_id_is_valid(bid))
    return false;
  return bid.heap->status[bid.value] == BLK_FREE;
}

/* Allocate */
struct block_id block_allocate(struct heap *heap, size_t size) {
  if (size == 0 || size > HEAP_BLOCKS) {
    return block_id_invalid();
  }

  size_t free_count = 0;
  size_t start_index = 0;

  // Find a sequence of free blocks
  for (size_t i = 0; i < HEAP_BLOCKS; i++) {
    if (heap->status[i] == BLK_FREE) {
      if (free_count == 0) {
        start_index = i;
      }
      free_count++;
      if (free_count == size) {
        break;
      }
    } else {
      free_count = 0;
    }
  }

  if (free_count < size) {
    return block_id_invalid(); // Not enough contiguous blocks
  }

  // Allocate the blocks
  heap->status[start_index] = (size == 1) ? BLK_ONE : BLK_FIRST;
  for (size_t i = 1; i < size - 1; i++) {
    heap->status[start_index + i] = BLK_CONT;
  }
  if (size > 1) {
    heap->status[start_index + size - 1] = BLK_LAST;
  }

  return block_id_new(start_index, heap);
}

/* Free */
void block_free(struct block_id bid) {
  if (!block_id_is_valid(bid)) {
    return;
  }

  enum block_status status = bid.heap->status[bid.value];
  if (status != BLK_ONE && status != BLK_FIRST) {
    return; // Can only free BLK_ONE or BLK_FIRST
  }

  size_t i = bid.value;
  while (i < HEAP_BLOCKS &&
         (bid.heap->status[i] == BLK_FIRST || bid.heap->status[i] == BLK_CONT ||
          bid.heap->status[i] == BLK_LAST || bid.heap->status[i] == BLK_ONE)) {
    bid.heap->status[i] = BLK_FREE;
    if (bid.heap->status[i] == BLK_LAST || bid.heap->status[i] == BLK_ONE) {
      break;
    }
    i++;
  }
}

/* Printer */
const char *block_repr(struct block_id b) {
  static const char *const repr[] = {[BLK_FREE] = " .",
                                     [BLK_ONE] = " *",
                                     [BLK_FIRST] = "[=",
                                     [BLK_LAST] = "=]",
                                     [BLK_CONT] = " ="};
  if (b.valid)
    return repr[b.heap->status[b.value]];
  else
    return "INVALID";
}

void block_debug_info(struct block_id b, FILE *f) {
  fprintf(f, "%s", block_repr(b));
}

void block_foreach_printer(struct heap *h, size_t count,
                           void printer(struct block_id, FILE *f), FILE *f) {
  for (size_t c = 0; c < count; c++)
    printer(block_id_new(c, h), f);
}

void heap_debug_info(struct heap *h, FILE *f) {
  block_foreach_printer(h, HEAP_BLOCKS, block_debug_info, f);
  fprintf(f, "\n");
}
/*  -------- */

int main(void) {
  heap_debug_info(&global_heap, stdout);

  struct block_id id1 = block_allocate(&global_heap, 1);
  heap_debug_info(&global_heap, stdout);

  struct block_id id2 = block_allocate(&global_heap, 3);
  heap_debug_info(&global_heap, stdout);

  block_free(id1);
  heap_debug_info(&global_heap, stdout);

  block_free(id2);
  heap_debug_info(&global_heap, stdout);

  return 0;
}
