import stdio
import stdaudio

bruh = stdio.readAllInts()
minuets = []
trios = []
print(bruh)
for i in range(16):
    minuets.append(bruh[0])
    bruh.pop(0)
for i in range(16):
    trios.append(bruh[0])
    bruh.pop(0)


print(trios)
print(minuets)

stdaudio.playFile('data/mozart')
