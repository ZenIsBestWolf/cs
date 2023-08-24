import stdio
import sys

n = int(sys.argv[1])

a = 1
b = 1
i = 3
while i <= n:
    temp = a
    a = b
    b = temp+b
    i += 1
stdio.writeln(b)
