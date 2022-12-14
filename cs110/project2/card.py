# import stdio
# import stdrandom
import random

suits = ["Clubs", "Spades", "Hearts", "Diamonds"]
nums = ["Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"]

print(nums[random.randint(0, 12)] + " of " + suits[random.randint(0, 3)])
