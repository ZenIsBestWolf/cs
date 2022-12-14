import stdio

theInts = stdio.readAllInts()

minimum = theInts[0]
maximum = theInts[0]

for i in theInts:
    if i < minimum:
        minimum = i
    elif i > maximum:
        maximum = i
stdio.writeln("Minimum = " + str(minimum))
stdio.writeln("Maximum = " + str(maximum))
