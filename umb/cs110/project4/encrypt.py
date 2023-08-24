import rsa
import stdio
import sys


# Entry point.
def main():
    # Predefined variables as per instructions
    n = int(sys.argv[1])
    e = int(sys.argv[2])
    width = rsa.bitLength(n)
    message = ""
    # While the standard input is open
    # Read the line, concat to the string, and re-add the \n stripped by readLine()
    while stdio.hasNextLine():
        message += stdio.readLine() + "\n"
    # Select each character, convert to num (understood by computer),
    # encrypt that number, then convert it to binary, and finally write it to stdout.
    for c in message:
        stdio.write(rsa.dec2bin(rsa.encrypt(ord(c), n, e), width))
    # finish it all up
    stdio.writeln()


if __name__ == '__main__':
    main()
