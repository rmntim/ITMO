org 0x431
ARRAY_START: word 0x446
ARRAY_END: word 0x200
ARRAY_LEN: word 0xe000
ODD_CNT: word 0x200

;; PROGRAM START
start:
  cla
  st ODD_CNT
  ld #4
  st ARRAY_LEN
  add ARRAY_START
  st ARRAY_END
  begin_loop: ld -(ARRAY_END)
  ror
  bcs is_odd
  jump iter
  is_odd: rol
  ld ODD_CNT
  inc
  st ODD_CNT
  loop $ARRAY_LEN
  iter: jump begin_loop
  hlt

;; DATA
ARRAY: word 0x7432, 0x6436, 0xf501, 0x2435
