     ; Лабораторная работа №3, вариант sum_even_n для архитектуры acc32
     ; Программа суммирует чётные числа от 1 до n.
     ; Ввод: число n из ячейки 0x80.
     ; Вывод: результат в ячейке 0x84.
     ; Если n ≤ 0, то вывод -1.
     ; Если возникает переполнение, то вывод 0xCCCCCCCC.

    .text
_start:
    ; Считываем n из адреса 0x80 и сохраняем в переменную n
    load_addr    0x80                        ; acc <- mem[0x80]
    store_addr   n

    ; Если n == 0, переходим к обработке ошибки (возврат -1)
    load_addr    n
    beqz         _error                      ; если n == 0 -> ошибка

    ; Если n < 0, также переходим к _error
    load_addr    n
    ble          _error                      ; если n < 0 -> ошибка

    ; Инициализируем total = 0
    load_imm     0
    store_addr   total

    ; Инициализируем счетчик i = 1
    load_imm     1
    store_addr   i

_loop:
    ; Проверяем условие цикла: если i > n, то выходим
    load_addr    n                           ; acc <- n
    sub          i                           ; acc <- n - i
    ble          _done                       ; если (n - i) < 0, то i > n

    ; Определяем, является ли i чётным:
    load_addr    i                           ; acc <- i
    and          const1                      ; вычисляем i & 1
    beqz         _add_even                   ; если результат 0, число чётное
    jmp          _inc                        ; иначе пропускаем сложение

_add_even:
    load_addr    total                       ; acc <- total
    add          i                           ; total + i
    bvs          _overflow                   ; если установлен флаг переполнения, переполнение
    store_addr   total                       ; сохраняем новую total

_inc:
    ; Увеличиваем i: i = i + 1
    load_addr    i
    add          const1                      ; прибавляем 1
    store_addr   i
    jmp          _loop

_done:
    ; Цикл завершён: записываем результат в ячейку 0x84
    load_addr    total
    store_addr   0x84
    halt

_overflow:
    ; В случае переполнения записываем 0xCCCCCCCC в ячейку 0x84
    load_imm     0xCCCCCCCC
    store_addr   0x84
    halt

_error:
    ; Если входное значение невалидно, возвращаем -1
    load_imm     -1
    store_addr   0x84
    halt

    .data
n:               .word  0
total:           .word  0
i:               .word  0
const1:          .word  1

