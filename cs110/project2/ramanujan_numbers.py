# import stdio
import sys

n = int(sys.argv[1])

a = 1
while a*a*a <= n:
    b = a+1
    while b*b*b <= n-(a*a*a):
        c = a+1
        while c*c*c <= n:
            d = c+1
            while d*d*d <= n-(c*c*c):
                part1 = a*a*a+b*b*b
                if part1 == c*c*c+d*d*d:
                    w = str(a)
                    x = str(b)
                    y = str(c)
                    z = str(d)
                    # This code is ugly because of the Pycodestyle requirements
                    print(str(part1)+" = "+w+"^3 + "+x+"^3 = "+y+"^3 + "+z+"^3")
                d += 1
            c += 1
        b += 1
    a += 1
