import stdio
import stdrandom
import sys


# Generates and returns the public/private keys as a tuple (n, e, d). Prime numbers p and q
# needed to generate the keys are picked from the interval [lo, hi).
def keygen(lo, hi):
    p = _choice(_primes(lo, hi))
    q = _choice(_primes(lo, hi))
    while p == q:  # Same random primes will cause problems
        p = _choice(_primes(lo, hi))
        q = _choice(_primes(lo, hi))
    n = p*q  # p1 of PUBLIC key, product of two random prime numbers.
    m = (p-1)*(q-1)  # The totient (phi)
    e = _choice(_primes(lo, m))  # p2 of PUBLIC key: get a random prime number between lo & totient
    while m % e == 0:  # Regenerate p2 if it is divisible by the totient.
        e = _choice(_primes(lo, m))
    # d = pow(e, -1, m)
    # Above is MUCH MORE EFFICIENT but the functionality is not present on Python 3.6.9
    # which is the version the Docker container on Gradescope is running.
    d = None
    for i in range(1, m):  # Gets the inverse multiplicative (private key)
        if (e*i) % m == 1:
            d = i
    return n, e, d


# Encrypts x (int) using the public key (n, e) and returns the encrypted value.
def encrypt(x, n, e):
    return x**e % n


# Decrypts y (int) using the private key (n, d) and returns the decrypted value.
def decrypt(y, n, d):
    return y**d % n


# Returns the least number of bits needed to represent n.
def bitLength(n):
    return len(bin(n)) - 2


# Returns the binary representation of n expressed in decimal, having the given width, and padded
# with leading zeros.
def dec2bin(n, width):
    return format(n, '0%db' % (width))


# Returns the decimal representation of n expressed in binary.
def bin2dec(n):
    return int(n, 2)


# Returns a list of primes from the interval [lo, hi).
"""
This code was copy and pasted from my version of prime_counter.py in Project 2
and then subsequently modified to fit the needs of this project.
"""


def _primes(lo, hi):
    primes = []
    # Prevent 0 and 1 edge case
    if 0 <= lo < 2:
        lo = 2
    for i in range(lo, hi):
        # Set j (potential divisor of i) to 2.
        j = 2
        tripped = False
        while j <= i / j:
            # As long as j is less than or equal to i / j...

            if i / j == i // j:
                # If j divides i, break (i is not a prime).
                tripped = True
                break

            # Increment j by 1.
            j += 1

        # If you got here by exhausting the while loop, i is a prime, so increment count by 1.
        if not tripped:
            primes += [i]
    return primes


# Returns a list containing a random sample (without replacement) of k items from the list a.
def _sample(a, k):
    tmp = a[:k]
    b = []
    while len(tmp) > 0:
        tmpc = _choice(tmp)
        b += [tmpc]
        tmp.remove(tmpc)
    return b


# Returns a random item from the list a.
def _choice(a):
    return a[stdrandom.uniformInt(0, len(a))]


# Unit tests the library [DO NOT EDIT].
def _main():
    x = ord(sys.argv[1])
    n, e, d = keygen(25, 100)
    encrypted = encrypt(x, n, e)
    stdio.writef('encrypt(%c) = %d\n', x, encrypted)
    decrypted = decrypt(encrypted, n, d)
    stdio.writef('decrypt(%d) = %c\n', encrypted, decrypted)
    width = bitLength(x)
    stdio.writef('bitLength(%d) = %d\n', x, width)
    xBinary = dec2bin(x, width)
    stdio.writef('dec2bin(%d) = %s\n', x, xBinary)
    stdio.writef('bin2dec(%s) = %d\n', xBinary, bin2dec(xBinary))


if __name__ == '__main__':
    _main()
