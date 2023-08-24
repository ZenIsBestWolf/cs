import java.util.Iterator;
import java.util.NoSuchElementException;

import stdlib.StdOut;
import stdlib.StdRandom;

// A data type to represent a double-ended queue (aka deque), implemented using a doubly-linked
// list as the underlying data structure.
public class LinkedDeque<Item> implements Iterable<Item> {
    private Node first; // The first node in the deque
    private Node last; // The last node in the deque
    private int n; // The amount of nodes in the deque

    // Constructs an empty deque.
    public LinkedDeque() {
        this.n = 0;
    }

    // Returns true if this deque is empty, and false otherwise.
    public boolean isEmpty() {
        return n == 0;
    }

    // Returns the number of items in this deque.
    public int size() {
        return n;
    }

    // Sets up the first item
    private void setFirst(Item item) {
        first = new Node();
        first.item = item;
        last = first;
        n++;
    }

    // Adds item to the front of this deque.
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException("item is null");
        }
        // Handle the corner case of the deque being empty
        if (n == 0) {
            setFirst(item);
            return;
        }
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        oldFirst.prev = first;
        n++;
    }

    // Adds item to the back of this deque.
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException("item is null");
        }
        // Handle the corner case of the deque being empty
        if (n == 0) {
            setFirst(item);
            return;
        }
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.prev = oldLast;
        oldLast.next = last;
        n++;
    }

    // Helper function to empty the deque
    private void removeSoloElement() {
        first = null;
        last = null;
    }

    // Returns the item at the front of this deque.
    public Item peekFirst() {
        if (n == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return first.item;
    }

    // Removes and returns the item at the front of this deque.
    public Item removeFirst() {
        if (n == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        Item oldFirst = first.item;
        // Handle the corner case of the deque being emptied out
        if (n == 1) {
            removeSoloElement();
        } else {
            first = first.next;
        }
        n--;
        return oldFirst;
    }

    // Returns the item at the back of this deque.
    public Item peekLast() {
        if (n == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        return last.item;
    }

    // Removes and returns the item at the back of this deque.
    public Item removeLast() {
        if (n == 0) {
            throw new NoSuchElementException("Deque is empty");
        }
        Item oldLast = last.item;
        // Handle the corner case of the deque being emptied out
        if (n == 1) {
            removeSoloElement();
        } else {
            last = last.prev;
        }
        n--;
        return oldLast;
    }

    // Returns an iterator to iterate over the items in this deque from front to back.
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // Returns a string representation of this deque.
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Item item : this) {
            sb.append(item);
            sb.append(", ");
        }
        return n > 0 ? "[" + sb.substring(0, sb.length() - 2) + "]" : "[]";
    }

    // A deque iterator.
    private class DequeIterator implements Iterator<Item> {
        private Node current; // The current node in the iteration

        // Constructs an iterator.
        public DequeIterator() {
            this.current = first;
        }

        // Returns true if there are more items to iterate, and false otherwise.
        public boolean hasNext() {
            return current != null;
        }

        // Returns the next item.
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Iterator is empty");
            }
            Item curItem = current.item;
            current = current.next;
            return curItem;
        }
    }

    // A data type to represent a doubly-linked list. Each node in the list stores a generic item
    // and references to the next and previous nodes in the list.
    private class Node {
        private Item item;  // the item
        private Node next;  // the next node
        private Node prev;  // the previous node
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        LinkedDeque<Character> deque = new LinkedDeque<Character>();
        String quote = "There is grandeur in this view of life, with its several powers, having " +
                "been originally breathed into a few forms or into one; and that, whilst this " +
                "planet has gone cycling on according to the fixed law of gravity, from so simple" +
                " a beginning endless forms most beautiful and most wonderful have been, and are " +
                "being, evolved. ~ Charles Darwin, The Origin of Species";
        int r = StdRandom.uniform(0, quote.length());
        StdOut.println("Filling the deque...");
        for (int i = quote.substring(0, r).length() - 1; i >= 0; i--) {
            deque.addFirst(quote.charAt(i));
        }
        for (int i = 0; i < quote.substring(r).length(); i++) {
            deque.addLast(quote.charAt(r + i));
        }
        StdOut.printf("The deque (%d characters): ", deque.size());
        for (char c : deque) {
            StdOut.print(c);
        }
        StdOut.println();
        StdOut.println("Emptying the deque...");
        double s = StdRandom.uniform();
        for (int i = 0; i < quote.length(); i++) {
            if (StdRandom.bernoulli(s)) {
                deque.removeFirst();
            } else {
                deque.removeLast();
            }
        }
        StdOut.println("deque.isEmpty()? " + deque.isEmpty());
    }
}
