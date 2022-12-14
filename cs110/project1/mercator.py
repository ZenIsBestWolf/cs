import math
# import stdio
import sys

inLon = math.radians(float(sys.argv[1]))
lat = float(sys.argv[2])
lon = math.log((1 + math.sin(inLon))/(1 - math.sin(inLon)))/2

print(lat, lon)
