#!/bin/bash
set -e  # Прерывание при ошибке

# ====================
# Группа 1: Инициализация репозитория
# ====================
git init                              # Инициализируем новый репозиторий
git branch -m branch1                 # Переименовываем ветку по умолчанию в branch1
git add .                             # Добавляем все файлы
git commit -m "r0"                    # Первый коммит (r0) на ветке branch1

# ====================
# Группа 2: Создание первых веток от branch1
# ====================
git checkout -b branch2               # Создание ветки branch2 (от branch1)
git add .
git commit -m "r1"                    # Коммит r1 в branch2

git checkout -b branch3               # Создание ветки branch3 (от branch1)
git add .
git commit -m "r2"                    # Коммит r2 в branch3

git checkout -b branch4               # Создание ветки branch4 из branch3
git commit -m "r3" --allow-empty       # Пустой коммит r3 в branch4

git checkout -b branch5               # Создание ветки branch5 от branch4
git commit -m "r4" --allow-empty       # Пустой коммит r4 в branch5

git checkout -b branch6               # Создание ветки branch6
git add .
git commit -m "r5"                    # Коммит r5 в branch6

git merge branch5                     # Слияние branch5 в branch6

# ====================
# Группа 3: Развитие функционала (branch4, branch7, branch8, branch9, branch10, branch11)
# ====================
git checkout branch4
git add .
git commit -m "r6"                    # Коммит r6 в branch4

git checkout -b branch7               # Создание branch7 от branch4
git add .
git commit -m "r7"                    # Коммит r7 в branch7

git checkout -b branch8               # Создание branch8 (на основе branch7)
git commit -m "r8"                    # Коммит r8 в branch8

git checkout -b branch9               # Создание branch9 (на основе branch8)
git commit -m "r9" --allow-empty       # Пустой коммит r9 в branch9

git checkout -b branch10              # Создание branch10 (на основе branch9)
git commit -m "r10" --allow-empty      # Пустой коммит r10 в branch10

git checkout -b branch11              # Создание branch11 (на основе branch10)
git commit -m "r11" --allow-empty      # Пустой коммит r11 в branch11

# ====================
# Группа 4: Ветвление от branch2: создание branch12, branch13, branch14, branch15 и слияния
# ====================
git checkout branch2
git add .
git commit -m "r12"                   # Коммит r12 в branch2

git checkout -b branch12             # Создание branch12 от branch2
git add .
git commit -m "r13"                   # Коммит r13 в branch12

git checkout -b branch13             # Создание branch13 (от branch12)
git commit -m "r14" --allow-empty      # Пустой коммит r14 в branch13

git checkout -b branch14             # Создание branch14
git commit -m "r15" --allow-empty      # Пустой коммит r15 в branch14

git checkout -b branch15             # Создание branch15
git add .
git commit -m "r16"                   # Коммит r16 в branch15

git merge branch14                   # Слияние branch14 в branch15 (без fast-forward)

# ====================
# Группа 5: Ветвление от branch3: создание branch16, branch17, branch18
# ====================
git checkout branch3
git add .
git commit -m "r17"                   # Коммит r17 в branch3

git checkout -b branch16             # Создание branch16 от branch3
git add .
git commit -m "r18"                   # Коммит r18 в branch16

git checkout -b branch17             # Создание branch17 от branch16
git commit -m "r19" --allow-empty      # Пустой коммит r19 в branch17

git checkout -b branch18             # Создание branch18 от branch17
git commit -m "r20" --allow-empty      # Пустой коммит r20 в branch18

# ====================
# Группа 6: Дополнительные изменения: branch15, branch12, branch6, branch19, branch20, branch21
# ====================
git checkout branch15
git add .
git commit -m "r21"                   # Коммит r21 в branch15

git checkout branch12
git add .
git commit -m "r22"                   # Коммит r22 в branch12

git checkout branch6
git add .
git commit -m "r23"                   # Коммит r23 в branch6

git checkout -b branch19             # Создание branch19 от branch6
git add .
git commit -m "r24"                   # Коммит r24 в branch19

git checkout branch2
git add .
git commit -m "r25"                   # Коммит r25 в branch2

git checkout -b branch20             # Создание branch20 от branch2
git add .
git commit -m "r26"                   # Коммит r26 в branch20

git checkout branch15
git add .
git commit -m "r27"                   # Коммит r27 в branch15

git checkout -b branch21             # Создание branch21 от branch15
git add .
git commit -m "r28"                   # Коммит r28 в branch21

git merge branch15                   # Слияние branch15 в branch21 (без fast-forward)

# ====================
# Группа 7: Работа с ветками branch22, branch23, branch24 и merge с branch8
# ====================
git checkout -b branch22             # Создание branch22 от branch21
git add .
git commit -m "r29"                   # Коммит r29 в branch22

git checkout -b branch23             # Создание branch23 от branch22
git commit -m "r30" --allow-empty      # Пустой коммит r30 в branch23

git checkout branch6
git add .
git commit -m "r31"                   # Коммит r31 в branch6

git checkout branch23
git add .
git commit -m "r32"                   # Коммит r32 в branch23

git checkout -b branch24             # Создание branch24 от branch23
git add .
git commit -m "r33"                   # Коммит r33 в branch24

git checkout branch8
git merge branch24                   # Слияние branch24 в branch8

# ====================
# Группа 8: Доработки в branch25, branch9 и объединение в branch1
# ====================
git checkout -b branch25             # Создание branch25 от branch8
git commit -m "r35" --allow-empty      # Пустой коммит r35 в branch25

git checkout branch9
git add .
git commit -m "r36"                   # Коммит r36 в branch9

git checkout branch3
git merge branch9                   # Слияние branch9 в branch3

git checkout branch12
git add .
git commit -m "r38"                   # Коммит r38 в branch12

git checkout branch25
git add .
git commit -m "r39"                   # Коммит r39 в branch25

git checkout branch1
git merge branch25 --no-ff           # Слияние branch25 в branch1 (без fast-forward)

# ====================
# Группа 9: Слияния branch23, branch20, branch4, с интеграцией через branch17 и branch11
# ====================
git checkout branch23
git add .
git commit -m "r41"                   # Коммит r41 в branch23

git checkout branch20
git add .
git commit -m "r42"                   # Коммит r42 в branch20

git checkout branch4
git merge branch20 --no-ff           # Слияние branch20 в branch4

git checkout branch23
git add .
git commit -m "r44"                   # Коммит r44 в branch23

git checkout branch17
git merge branch23 --no-ff           # Слияние branch23 в branch17

# Разрешение конфликта: выбираем изменения из сливаемой ветки ("theirs")
git checkout --theirs .
git add .
git merge --continue                # Завершаем слияние

git checkout branch11
git merge branch17 --no-ff           # Слияние branch17 в branch11
git add .
git commit -m "r47"                   # Коммит r47 в branch11

# ====================
# Группа 10: Последовательные коммиты и слияния в branch16 и branch21
# ====================
git checkout branch16
git add .
git commit -m "r48"                   # Коммит r48 в branch16
git add .
git commit -m "r49"                   # Дополнительный коммит r49 в branch16

git checkout branch21
git add .
git commit -m "r50"                   # Коммит r50 в branch21

git checkout branch16
git merge branch21 --no-ff           # Слияние branch21 в branch16
git checkout --theirs .
git add .
git merge --continue                # Завершаем слияние

# ====================
# Группа 11: Слияние branch13 в branch19 и создание branch26
# ====================
git checkout branch4
git add .
git commit -m "r52"                   # Коммит r52 в branch4

git checkout branch13
git add .
git commit -m "r53"                   # Коммит r53 в branch13

git checkout branch19
git merge branch13 --no-ff           # Слияние branch13 в branch19

git checkout -b branch26             # Создание branch26 от branch19
git commit -m "r55" --allow-empty      # Пустой коммит r55 в branch26

# ====================
# Группа 12: Доработки в branch2 и branch22, слияние branch22 в branch2
# ====================
git checkout branch2
git add .
git commit -m "r56"                   # Коммит r56 в branch2
git add .
git commit -m "r57"                   # Коммит r57 в branch2

git checkout branch22
git add .
git commit -m "r58"                   # Коммит r58 в branch22

git checkout branch2
git merge branch22 --no-ff           # Слияние branch22 в branch2
git checkout --theirs .
git add .
git merge --continue                # Завершаем слияние

# ====================
# Группа 13: Объединение branch2 в branch6 и создание branch27
# ====================
git checkout branch6
git merge branch2 --no-ff            # Слияние branch2 в branch6

git checkout branch3
git add .
git commit -m "r61"                   # Коммит r61 в branch3

git checkout -b branch27             # Создание branch27 от branch3
git add .
git commit -m "r62"                   # Коммит r62 в branch27

git checkout branch11
git add .
git commit -m "r63"                   # Коммит r63 в branch11

git checkout branch7
git add .
git commit -m "r64"                   # Коммит r64 в branch7

git checkout branch4
git add .
git commit -m "r65"                   # Коммит r65 в branch4

git checkout branch27
git add .
git commit -m "r66"                   # Коммит r66 в branch27

git checkout branch11
git merge branch27 --no-ff           # Слияние branch27 в branch11

git checkout branch7
git add .
git commit -m "r68"                   # Коммит r68 в branch7
git add .
git commit -m "r69"                   # Дополнительный коммит r69 в branch7

# ====================
# Группа 14: Слияние branch16 в branch4 с выбором "ours"
# ====================
git checkout branch16
git add .
git commit -m "r70"                   # Коммит r70 в branch16

git checkout branch4
git merge branch16 --no-ff           # Слияние branch16 в branch4
git checkout --ours .               # При конфликте выбираем наши изменения ("ours")
git add .
git merge --continue                # Завершаем слияние
git add .
git commit -m "r72"                   # Финальный коммит r72 в branch4

# ====================
# Группа 15: Доработки в branch10 и branch8, слияние branch8 в branch26
# ====================
git checkout branch10
git add .
git commit -m "r73"                   # Коммит r73 в branch10

git checkout branch8
git add .
git commit -m "r74"                   # Коммит r74 в branch8

git checkout branch26
git merge branch8 --no-ff            # Слияние branch8 в branch26

# ====================
# Группа 16: Последовательные слияния с разрешением конфликтов
# ====================
git checkout branch3
git merge branch26 --no-ff           # Слияние branch26 в branch3
git checkout --ours .               # При конфликте выбираем "ours"
git add .
git merge --continue                # Завершаем слияние

git checkout branch11
git merge branch3 --no-ff            # Слияние branch3 в branch11

git checkout branch10
git merge branch11 --no-ff           # Слияние branch11 в branch10
git checkout --theirs .             # При конфликте выбираем "theirs"
git add .
git merge --continue                # Завершаем слияние

git checkout branch12
git merge branch10 --no-ff           # Слияние branch10 в branch12
git checkout --theirs .
git add .
git merge --continue                # Завершаем слияние

git checkout branch7
git merge branch12 --no-ff           # Слияние branch12 в branch7
git checkout --theirs .
git add .
git merge --continue                # Завершаем слияние

git checkout branch6
git merge branch12 --no-ff           # Слияние branch12 в branch6
git checkout --theirs .
git add .
git merge --continue                # Завершаем слияние

# ====================
# Группа 17: Финальные объединения
# ====================
git checkout branch19
git merge branch6 --no-ff            # Слияние branch6 в branch19

git checkout branch18
git merge branch19 --no-ff           # Слияние branch19 в branch18

git checkout branch4
git merge branch18 --no-ff           # Слияние branch18 в branch4
git checkout --theirs .             # При конфликте выбираем "theirs"
git add .
git merge --continue                # Завершаем слияние

git checkout branch1
git merge branch4 --no-ff            # Слияние branch4 в branch1
git checkout --theirs .             # При конфликте выбираем "theirs"
git add .
git merge --continue                # Завершаем слияние
