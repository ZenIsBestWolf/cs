import dsa.Set;

import java.util.Iterator;

import stdlib.StdOut;

// An iterable data type to represent an ordered set.
public class SetX<Key extends Comparable<Key>> implements Iterable<Key> {
    private Set<Key> set; // the underlying set.

    // Constructs an empty set.
    public SetX() {
        set = new Set<Key>();
    }

    // Constructs a set from the given keys.
    public SetX(Key[] keys) {
        this();
        for (Key k : keys) {
            set.add(k);
        }
    }

    // Returns true if this set is empty, and false otherwise.
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // Returns the number of keys in this set.
    public int size() {
        return set.size();
    }

    // Adds key to this set, if it is not already present.
    public void add(Key key) {
        set.add(key);
    }

    // Returns true if this set contains key, and false otherwise.
    public boolean contains(Key key) {
        return set.contains(key);
    }

    // Returns an iterator to iterate over the keys in this set in sorted order.
    public Iterator<Key> iterator() {
        return set.iterator();
    }

    // Returns a string representation of this set.
    public String toString() {
        return set.toString();
    }

    // Returns the union of this set and other, ie, the keys that are either in this set or other.
    public SetX<Key> union(SetX<Key> other) {
        SetX<Key> result = new SetX<>();

        for (Key key : this) {
            result.add(key);
        }
        for (Key key : other) {
            result.add(key);
        }

        return result;
    }

    // Returns the intersection of this set and other, ie, the keys that are in this set and other.
    public SetX<Key> intersection(SetX<Key> other) {
        SetX<Key> result = new SetX<>();

        for (Key cur : this) {
            if (other.contains(cur)) {
                result.add(cur);
            }
        }

        return result;
    }

    // Returns the difference of this set and other, ie, the keys that are in this set but not
    // other.
    public SetX<Key> difference(SetX<Key> other) {
        SetX<Key> result = new SetX<>();

        for (Key key : this) {
            if (!other.contains(key)) {
                result.add(key);
            }
        }

        return result;
    }

    // Returns the symmetric difference of this set and other, ie, the keys that are in this set
    // or other but not both.
    public SetX<Key> symmetricDifference(SetX<Key> other) {
        SetX<Key> result = new SetX<>();

        for (Key key : this) {
            if (!other.contains(key)) {
                result.add(key);
            }
        }
        for (Key key : other) {
            if (!this.contains(key)) {
                result.add(key);
            }
        }

        return result;
    }

    // Returns true if this set and other have the same keys, and false otherwise.
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        if (this.size() != ((SetX) other).size()) {
            return false;
        }
        for (Key key : this) {
            if (!((SetX) other).contains(key)) {
                return false;
            }
        }
        return true;
    }

    // Returns true if this set and other have no keys in common, and false otherwise.
    public boolean isDisjoint(SetX<Key> other) {
        for (Key key : this) {
            if (other.contains(key)) {
                return false;
            }
        }
        return true;
    }

    // Returns true if this set is contained in other, and false otherwise.
    public boolean isSubset(SetX<Key> other) {
        for (Key key : this) {
            if (!other.contains(key)) {
                return false;
            }
        }
        return true;
    }

    // Returns true if this set contains other, and false otherwise.
    public boolean isSuperset(SetX<Key> other) {
        for (Key key : other) {
            if (!this.contains(key)) {
                return false;
            }
        }
        return true;
    }

    // Unit tests the data type [DO NOT EDIT].
    public static void main(String[] args) {
        SetX<Character> a =
                new SetX<Character>(new Character[]{'e', 'i', 'n', 's', 't', 'e', 'i', 'n'});
        SetX<Character> b = new SetX<Character>(new Character[]{'e', 'i', 'n'});
        StdOut.println("a                        = " + a);
        StdOut.println("b                        = " + b);
        StdOut.println("a.union(b)               = " + a.union(b));
        StdOut.println("a.intersection(b)        = " + a.intersection(b));
        StdOut.println("a.difference(b)          = " + a.difference(b));
        StdOut.println("a.symmetricDifference(b) = " + a.symmetricDifference(b));
        StdOut.println("a.equals(b)              = " + a.equals(b));
        StdOut.println("a.isDisjoint(b)          = " + a.isDisjoint(b));
        StdOut.println("a.isSubset(b)            = " + a.isSubset(b));
        StdOut.println("a.isSuperset(b)          = " + a.isSuperset(b));
    }
}