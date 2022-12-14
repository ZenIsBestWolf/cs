# import stdio
import sys

sys.argv.pop(0)
x, y, z = (int(a) for a in sys.argv)
# HAHA! I REMEMBERED HOW TO DO THIS NOW
# I am too lazy to go back and change all previous code
# But for the last problem it's cool to show this neat trick off
# Makes things like these where you're sequentially taking args just way easier to manage.

# Anyways, onto the code...
minNum = min(x, y, z)
maxNum = max(x, y, z)
midNum = x+y+z-minNum-maxNum

print(minNum, midNum, maxNum)

# You can cheese this very easily though!
# Literally just use Python's built-in sorter!
#
# import sys
# arr = []
# for i in range(1,4):
#   arr.append(int(sys.argv[i]));
# print(arr.sort)
