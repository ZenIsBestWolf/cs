# import stdio
import sys

p = int(sys.argv[1])
q = int(sys.argv[2])

while p % q != 0:
    temp = p
    p = q
    q = temp % q
print(q)
