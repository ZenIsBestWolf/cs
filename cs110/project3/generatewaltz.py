import stdarray
import stdrandom
import stdio


"""
Constructs a randomized waltz from the given dataset (integer list).
Using random number generation, a series of 16 random minuets and 16 random trio
measures are selected (represented as integers themselves) and then organized into a list.
This list is then returned.
"""


def generateWaltz(rawIn):
    minuets = stdarray.create2D(11, 16)
    trios = stdarray.create2D(6, 16)
    for i in range(11):
        for j in range(16):
            # By getting the first value and popping it, we don't have to
            # calculate where to start and stop, just mindlessly loop through.
            minuets[i][j] = rawIn[0]
            rawIn.pop(0)
    for i in range(6):
        for j in range(16):
            trios[i][j] = rawIn[0]
            rawIn.pop(0)
    output = []
    # Construct the two random outputs
    # First starting with minuets...
    for i in range(16):
        n = stdrandom.uniformInt(1, 7) + stdrandom.uniformInt(1, 7)
        output.append(minuets[n-2][i])
    # ...then moving onto trios.
    for i in range(16):
        n = stdrandom.uniformInt(1, 7)
        output.append(trios[n-1][i])
    return output


"""
When ran as main, read all inputted integers, then pass that list into generateWaltz
Once done, iterate through the list to write every item to standard output.
"""
if __name__ == '__main__':
    theIn = stdio.readAllInts()
    newWaltz = generateWaltz(theIn)
    out = ""
    for i in newWaltz:
        out += str(i) + " "
    out = out.strip()
    stdio.writeln(out)
