import stdio


# returns the index of the first occurrence of the element k within the list a, and -1 if the element does not
# exist in the list
def find(a, k):
    for i in range(len(a)):
        if a[i] == k:
            return i
    return -1


# returns the index of the last occurrence of the element k within the list a, and -1 if the element does not
# exist in the list
def rfind(a, k):
    for i in range(len(a)-1,-1,-1):
        if a[i] == k:
            return i
    return -1


# returns the number of occurrences of the element k within the list a
def count(a, k):
    counter = 0
    for i in a:
        if i == k:
            counter+=1
    return counter


# returns True if the list a contains the element k, and False otherwise
def contains(a, k):
    for i in a:
        if a == k:
            return True
    return False

if __name__ == '__main__':
    a = [5, -1, 3, 4, 3, -2, 3, 5]

    stdio.writeln("a\t\t= " + str(a))
    stdio.writeln("find(a, 3)\t= " + str(find(a, 3)))
    stdio.writeln("rfind(a, 3)\t= " + str(rfind(a, 3)))
    stdio.writeln("count(a, 3)\t= " + str(count(a, 3)))
    stdio.writeln("contains(a, 6)\t= " + str(contains(a, 6)))
