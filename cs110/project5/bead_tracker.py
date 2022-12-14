import math
import stdio
import sys
from blob_finder import BlobFinder
from picture import Picture


# Entry point
def main():
    pixels = int(sys.argv[1])
    tau = float(sys.argv[2])
    delta = float(sys.argv[3])
    beads = sys.argv[4:]
    while len(beads) > 1:
        prevBeads = BlobFinder(Picture(beads[0]), tau).getBeads(pixels)
        currBeads = BlobFinder(Picture(beads[1]), tau).getBeads(pixels)
        for i in currBeads:
            minDist = delta+1
            for j in prevBeads:
                if i.distanceTo(j) <= delta and i.distanceTo(j) < minDist:
                    minDist = i.distanceTo(j)
            if minDist <= delta:
                stdio.writef('%.4f\n', minDist)
        stdio.writeln()
        beads.pop(0)


if __name__ == '__main__':
    main()
