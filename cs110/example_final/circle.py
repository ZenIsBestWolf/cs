import stdio
import math


class Circle:
    def __init__(self, h=0, k=0, r=1):
        self._h = h
        self._k = k
        self._r = r

    def __str__(self):
        return f'({self._h}, {self._k}, {self._r})'

    def __eq__(self, other):
        sameX = self._h == other._h
        sameY = self._k == other._k
        sameR = self._r == other._r
        return sameX and sameY and sameR

    def __lt__(self, other):
        return self.area() < other.area()

    def __gt__(self, other):
        return self.area() > other.area()

    def area(self):
        return math.pi * self._r ** 2

    def circumference(self):
        return 2 * math.pi * self._r

    def contains(self, x, y):
        isContained = False
        if (self._h - x)**2 + (self._k - y)**2 <= self._r**2:
            isContained = True
        return isContained

    def scale(self, r):
        return Circle(self._h, self._k, r)

    def distanceTo(self, d):
        euclidianDistance = ((self._h - d._h) ** 2 + (self._k - d._k) ** 2) ** .5
        return euclidianDistance


if __name__ == '__main__':
    c = Circle()
    d = Circle(1, 1, 2)
    area = c.area()
    circ = c.circumference()
    cContainsTest = c.contains(1, 1)
    cScaled = c.scale(2)
    cToD = c.distanceTo(d)
    cIsDOrC = c == d or c == c
    isDMoreThanC = d < c
    stdio.writeln(f"c\t\t\t= {c}")
    stdio.writeln(f"d\t\t\t= {d}")
    stdio.writeln(f"c.area()\t\t= {area}")
    stdio.writeln(f"c.circumference()\t= {circ}")
    stdio.writeln(f"c.contains(1, 1)\t= {cContainsTest}")
    stdio.writeln(f"c.scale(2)\t\t= {cScaled}")
    stdio.writeln(f"c.distanceTo(d)\t\t= {cToD}")
    stdio.writeln(f"c == d or c == c\t= {cIsDOrC}")
    stdio.writeln(f"d < c\t\t\t= {isDMoreThanC}")
