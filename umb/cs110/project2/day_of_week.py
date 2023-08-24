import stdio
import sys

m = int(sys.argv[1])
d = int(sys.argv[2])
y = int(sys.argv[3])

yO = y - (14-m)//12
xO = yO + (yO//4) - (yO//100) + (yO//400)
mO = m + 12 * ((14-m)//12) - 2
dow = (d + xO + (31 * mO)//12) % 7

days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"]
stdio.writeln(days[dow])
