import math
import stdio


# Entry point.
def main():
    radialDistancesSq = list(map(lambda x: (x*0.175*10**-6)**2, stdio.readAllFloats()))
    var = sum(radialDistancesSq)
    var /= 2*len(radialDistancesSq)
    ETA = 9.135 * 10**-4
    RHO = 0.5 * 10**-6
    T = 297
    R = 8.31457
    boltzmann = (6 * math.pi * var * ETA * RHO) / T
    avogardo = R / boltzmann
    stdio.writef('%e', boltzmann)
    stdio.write(" ")
    stdio.writef('%e', avogardo)
    stdio.writeln()


if __name__ == '__main__':
    main()
