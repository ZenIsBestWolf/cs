# import stdio
# import stdrandom
from random import randint
# YES we could call random.randint, but we're not using anything else from the library, so why not.
import sys

print(randint(1, int(sys.argv[1])) + randint(1, int(sys.argv[1])))

# This was too easy to write as a one-liner, so I just had to.
# Sorry I know it's ugly. Here's cleaner/clearer sample code.
#
# n = int(sys.argv[1])
# rand1 = randint(1,n)
# rand2 = randint(1,n)
#
# Option 1:
# print(rand1 + rand2)
#
# Option 2:
# solution = rand1 + rand2
# print(solution)
