# Returns True if any value in the list a is True, and False otherwise.
def any(a):
    for i in a:
        if i:
            return True
    return False


# Returns True if all values in the list a are True, and False otherwise.
def all(a):
    for i in a:
        if not i:
            return False
    return True


# Returns True if exactly k values in the list a are True, and False otherwise.
def exactly(a, k):
    ticker = 0
    for i in a:
        if i:
            ticker += 1
    return ticker == k


# Returns the number of True values in the list a.
def count(a):
    ticker = 0
    for i in a:
        if i:
            ticker += 1
    return ticker


# Unit tests the library.
def _main():
    import stdio

    a = [False, False, True, False, True, True, True]
    stdio.writeln('a             = ' + str(a))
    stdio.writeln('any(a)        = ' + str(any(a)))
    stdio.writeln('all(a)        = ' + str(all(a)))
    stdio.writeln('exactly(a, 3) = ' + str(exactly(a, 3)))
    stdio.writeln('count(a)      = ' + str(count(a)))


if __name__ == '__main__':
    _main()
