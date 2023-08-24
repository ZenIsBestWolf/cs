import stdio
import stdrandom
import sys

n = int(sys.argv[1])
rand1 = stdrandom.uniformint(1, n + 1)
rand2 = stdrandom.uniformint(1, n + 1)

stdio.writeln(rand1 + rand2)

# As a one liner:
# stdio.writeln(stdrandom.uniformint(1, int(sys.argv[1]) + 1) + stdrandom.uniformint(1, int(sys.argv[1]) + 1))