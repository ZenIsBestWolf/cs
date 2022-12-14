# import stdio
import sys

# Accept n (int) as command-line argument.
n = int(sys.argv[1])

# Set i (potential divisor of n) to 2.
i = 2

tripped = False

while i <= n/i:
    # As long as i is less than or equal to n / i...

    if n % i == 0:
        # If i divides n, break (n is not a prime).
        tripped = True
        break
    # Increment i by 1.
    i += 1

if not tripped:
    # If you got here by exhausting the loop, n is prime. Write True to standard output.
    print("True")
else:
    # If you got here by prematurely exiting the loop, n is not a prime. Write False to standard
    # output.
    print("False")
