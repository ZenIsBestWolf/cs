# import stdio
import sys

n = int(sys.argv[1])
k = int(sys.argv[2])

total = 0

for i in range(n+1):
    total += i**k
print(total)
