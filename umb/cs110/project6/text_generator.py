from markov_model import MarkovModel
import stdio
import sys


# Entry point.
def main():
    k = int(sys.argv[1])
    n = int(sys.argv[2])
    text = sys.stdin.read()
    # Make a markov based off of text to order k
    myMarkov = MarkovModel(text, k)
    # Generate text as if it was extending the spliced text
    randomMarkov = myMarkov.gen(text[:k], n)
    stdio.writeln(randomMarkov)


if __name__ == '__main__':
    main()
