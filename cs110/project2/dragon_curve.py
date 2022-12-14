# import stdio
import sys

n = int(sys.argv[1])

dragon = "F"
nogard = "F"

for i in range(n):
    tempD = dragon
    dragon = dragon + "L" + nogard
    nogard = tempD + "R" + nogard
print(dragon)
