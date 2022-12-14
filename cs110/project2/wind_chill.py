# import stdio
import sys

t = float(sys.argv[1])
v = float(sys.argv[2])

if t > 50.0:
    print("Value of t must be <= 50 F")
    exit()
if v <= 3:
    print("Value of v must be > 3 mph")
    exit()

w = 35.74 + 0.6215*t + (0.4275*t - 35.75)*v**0.16
print(w)
