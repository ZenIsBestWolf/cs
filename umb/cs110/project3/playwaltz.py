import stdaudio
import stdio

"""
Play the provided waltz (integer list) to standard audio output.
The first 16 items in the list [0,15] are considered minuets.
The last 16 [16,31] are considered trios.
All are played respectively.
"""


def playWaltz(waltz):
    # "fUnCtIoN nAmE sHoUlD bE lOwErCaSe" - PyCharm, whining.
    # I don't care.
    # camelCase my beloved.

    # Using splicing, grab the first 16 measures which are all minuets
    # and doublecheck their validity.
    for i in waltz[:16]:
        if not (0 < i < 177):
            stdio.writeln("A minuet measure must be from [1, 176]")
            exit()
    # Repeat for the last 16 but as trios.
    for i in waltz[16:]:
        if not (0 < i < 97):
            stdio.writeln("A trio measure must be from [1, 96]")
            exit()
    # Play the respective measures
    for i in waltz[:16]:
        stdaudio.playFile('data/M' + str(i))
    for i in waltz[16:]:
        stdaudio.playFile('data/T' + str(i))


"""
When executed as main, read all integers from standard input.
Then, pass that list to playWaltz (to... play the waltz).
"""
if __name__ == '__main__':
    myWaltz = stdio.readAllInts()
    # camelCase my beloved.
    # Even the authors of stdio know camelCase is best case.
    playWaltz(myWaltz)
