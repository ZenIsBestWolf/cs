# import stdio
# import stdrandom
# This library is also propriatary.
import random
import sys

# Accept a (int) and b (int) as command-line arguments.
a = int(sys.argv[1])
b = int(sys.argv[2])

# Set r to a random integer between a and b, obtained by calling stdrandom.uniformInt().
r = random.randint(a, b-1)

# Write r to standard output.
# stdio.writeln(...)
print(r)
