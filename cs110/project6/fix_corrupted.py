from markov_model import MarkovModel
import stdio
import sys


# Entry point.
def main():
    k = int(sys.argv[1])
    s = sys.argv[2]
    chars = sys.stdin.read()
    # generate a markov model with given parameters
    markov = MarkovModel(chars, k)
    # correct the string & write it to stdout
    stdio.writeln(markov.replace_unknown(s))


if __name__ == '__main__':
    main()
