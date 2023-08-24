import stdio

# Get a list of all provided integers
theInts = stdio.readAllInts()
# Set a counter for output
out = 0
# Iterate through each integer and square it, then add that to the output.
for i in theInts:
    out += i**2
# Write final result to the standard output.
stdio.writeln(out)
