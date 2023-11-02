import time
import main.main as main
import bonus1.main as bonus1
import bonus2.main as bonus2
import bonus3.main as bonus3

if __name__ == "__main__":
    input_string = open("../input.json", "r").read()
    start = time.perf_counter()
    for _ in range(100):
        main.parse(input_string)
    end = time.perf_counter()
    print("main task time: ", end - start)

    start = time.perf_counter()
    for _ in range(100):
        bonus1.parse(input_string)
    end = time.perf_counter()
    print("bonus1 task time: ", end - start)

    start = time.perf_counter()
    for _ in range(100):
        bonus2.parse(input_string)
    end = time.perf_counter()
    print("bonus2 task time: ", end - start)

    start = time.perf_counter()
    for _ in range(100):
        bonus3.parse(input_string)
    end = time.perf_counter()
    print("bonus3 task time: ", end - start)
