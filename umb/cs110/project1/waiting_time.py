import math
# import stdio
import sys

lambdaVar = float(sys.argv[1])
t = float(sys.argv[2])

p = math.e**(-1*lambdaVar*t)
print(p)
