import rsa
import stdio
import sys


# Entry point.
def main():
    # Predefined variables as per instructions
    n = int(sys.argv[1])
    d = int(sys.argv[2])
    width = rsa.bitLength(n)
    message = ""
    # Read strings until the stdin is empty.
    while not stdio.isEmpty():
        message += stdio.readString()
    # Create a range such that the values will allow for a slice to be created that is
    # equal to one set of the encrypted binary.
    # Then, revert the binary back to a numerical representation and decrypt.
    # Finally, write it all out to the stdout
    for i in range(0, len(message)-1, width):
        s = message[i:i+width]
        y = rsa.decrypt(rsa.bin2dec(s), n, d)
        stdio.write(chr(y))


if __name__ == '__main__':
    main()
