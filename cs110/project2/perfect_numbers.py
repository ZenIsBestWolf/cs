# import stdio
import sys

n = int(sys.argv[1])

for i in range(2, n):
    total = 0
    for j in range(1, (i//2)+1):
        if i % j == 0:
            total += j
    if total == i:
        print(i)
