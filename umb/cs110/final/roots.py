from quadratic import Quadratic
import stdio
import sys


# Entry point.
def main():
    x = float(sys.argv[1])
    while not stdio.isEmpty():
        a = stdio.readFloat()
        b = stdio.readFloat()
        c = stdio.readFloat()
        q = Quadratic(a, b, c)

        # The separation of these lines allows for the proper
        # notation required by Gradescope (6 zeros)
        stdio.write(f"{q}; ")
        stdio.writef("%.6f", q[x])
        stdio.write("; ")
        stdio.write(f"{q.roots()}; ")
        stdio.writef("%.6f", q.sum())
        stdio.write("; ")
        stdio.writef("%.6f", q.prod())
        stdio.writeln()


if __name__ == '__main__':
    main()
