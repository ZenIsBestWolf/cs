import rsa
import stdio
import sys


# Entry point.
def main():
    # Accept predefined variables
    lo = int(sys.argv[1])
    hi = int(sys.argv[2])
    # Generate a key with given lo and hi
    keys = rsa.keygen(lo, hi)
    # Iterate on tuple and add to output
    for i in keys:
        stdio.write(str(i) + " ")
    stdio.writeln()


if __name__ == '__main__':
    main()
