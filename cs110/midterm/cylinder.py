import math
import stdio
import sys

# Accept two float args, radius and height (r and h respectively)
r = float(sys.argv[1])
h = float(sys.argv[2])

# Compute Surface Area (s) and Volume (v) of a cylinder
s = 2 * math.pi * r * (r+h)
v = math.pi * r**2 * h

# Write results to standard output
stdio.writeln("S = " + str(s))
stdio.writeln("V = " + str(v))
