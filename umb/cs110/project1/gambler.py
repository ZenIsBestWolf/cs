import stdio
import sys

n1 = int(sys.argv[1])
n2 = int(sys.argv[2])
p = float(sys.argv[3])
q = 1 - p

p1 = (1-(p/q)**n2)/(1-(p/q)**(n1+n2))
p2 = (1-(q/p)**n1)/(1-(q/p)**(n1+n2))

stdio.writeln(p1, p2)
