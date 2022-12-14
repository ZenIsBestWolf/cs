import sys
import stdio

x = float(sys.argv[1])
y = float(sys.argv[2])
z = float(sys.argv[3])
a = float(sys.argv[4])

result = ((x-z)**2 + (y-a)**2)**.5
stdio.writeln(result)
