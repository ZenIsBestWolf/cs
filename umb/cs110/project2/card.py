import stdio
import stdrandom

suits = ["Clubs", "Spades", "Hearts", "Diamonds"]
nums = ["Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"]

stdio.writeln(nums[stdrandom.uniformint(0, 13)] + " of " + suits[stdrandom.uniformint(0, 4)])
