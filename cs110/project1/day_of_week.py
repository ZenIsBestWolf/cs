import stdio
import sys

m = int(sys.argv[1])
d = int(sys.argv[2])
y = int(sys.argv[3])

tmp1 = y - (14-m)//12
tmp2 = tmp1 + tmp1//4 - tmp1//100 + tmp1//400
tmp3 = m + 12 * ((14 - m)//12) - 2
dow = (d + tmp2 + 31 * tmp3//12) % 7
stdio.writeln(dow)
