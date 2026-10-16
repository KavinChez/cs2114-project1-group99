/**
 * A generic chain of linked nodes. Use it as the storage inside your
 * own classes, and extend or modify it as your project needs.
 *
 * This class owns storage only: nodes, the head and tail references,
 * and the count. It contains no application logic. A class that needs
 * a collection should hold a LinkedChain field and call its methods.
 *
 * add() places the new entry at the END of the chain, preserving
 * insertion order. addToEnd() provides the same behavior.
 * Duplicate entries are allowed, but null entries are prohibited.
 *
 * Time complexity, where n is the number of entries:
 * add O(1)
 * addToEnd O(1)
 * remove() O(1)
 * removeFirst() O(1)
 * remove(T) O(n)
 * removeAt(int) O(n)
 * clear O(1)
 * get O(n)
 * contains O(n)
 * count O(n)
 * size O(1)
 * isEmpty O(1)
 * toArray O(n)
 * toString O(n)
 *
 * @author CS 2114 Staff
 * @version September 2026
 *
 * @param <T>
 *            the type of entries stored in the chain
 */
public class LinkedChain<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    /** Creates an empty chain. O(1). */
    public LinkedChain() {
        head = null;
        tail = null;
        size = 0;
    }


    /**
     * Appends an entry to the end of the chain. O(1).
     *
     * @param item
     *            entry to append
     * @throws IllegalArgumentException
     *             if item is null
     */
    public void add(T item) {
        requireNonNull(item);
        Node<T> newNode = new Node<T>(item);

        if (tail == null) {
            head = newNode;
        }
        else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }


    /**
     * Compatibility method for the starter code and existing GameTest.
     * Appends in O(1), just like add().
     *
     * @param item
     *            entry to append
     * @return true after insertion
     */
    public boolean addToEnd(T item) {
        add(item);
        return true;
    }


    /**
     * Removes the first entry equal to item. O(n).
     *
     * @param item
     *            entry to find (using equals)
     * @return true if an entry was removed; false if absent
     * @throws IllegalArgumentException
     *             if item is null
     */
    public boolean remove(T item) {
        requireNonNull(item);
        if (head == null) {
            return false;
        }
        if (item.equals(head.data)) {
            removeFirst();
            return true;
        }

        Node<T> previous = head;
        Node<T> current = head.next;
        while (current != null) {
            if (item.equals(current.data)) {
                previous.next = current.next;
                if (current == tail) {
                    tail = previous;
                }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }


    /**
     * Removes and returns the first entry. O(1).
     *
     * @return the removed entry
     * @throws IllegalStateException
     *             if the chain is empty
     */
    public T removeFirst() {
        if (head == null) {
            throw new IllegalStateException(
                "Cannot remove from an empty chain");
        }

        T result = head.data;
        head = head.next;
        size--;
        if (head == null) {
            tail = null;
        }
        return result;
    }


    /**
     * Compatibility method for the starter code and existing GameTest.
     * Unlike removeFirst(), this returns null when the chain is empty.
     * O(1).
     *
     * @return first entry, or null if empty
     */
    public T remove() {
        return isEmpty() ? null : removeFirst();
    }


    /**
     * Removes and returns an entry by zero-based index. O(n).
     *
     * @param index
     *            index of the entry to remove
     * @return the removed entry
     * @throws IndexOutOfBoundsException
     *             if index is invalid
     */
    public T removeAt(int index) {
        checkIndex(index);
        if (index == 0) {
            return removeFirst();
        }

        Node<T> previous = head;
        for (int i = 0; i < index - 1; i++) {
            previous = previous.next;
        }

        Node<T> removed = previous.next;
        previous.next = removed.next;
        if (removed == tail) {
            tail = previous;
        }
        size--;
        return removed.data;
    }


    /**
     * Returns an entry by zero-based index without removing it. O(n).
     *
     * @param index
     *            index of the requested entry
     * @return the entry at index
     * @throws IndexOutOfBoundsException
     *             if index is invalid
     */
    public T get(int index) {
        checkIndex(index);
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }


    /**
     * Checks whether an equal entry is present. O(n).
     *
     * @param item
     *            entry to search for
     * @return whether the chain contains an equal entry
     * @throws IllegalArgumentException
     *             if item is null
     */
    public boolean contains(T item) {
        requireNonNull(item);
        Node<T> current = head;
        while (current != null) {
            if (item.equals(current.data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }


    /**
     * Counts entries equal to item. O(n).
     *
     * @param item
     *            entry to count
     * @return number of matching entries
     * @throws IllegalArgumentException
     *             if item is null
     */
    public int count(T item) {
        requireNonNull(item);
        int matches = 0;
        Node<T> current = head;
        while (current != null) {
            if (item.equals(current.data)) {
                matches++;
            }
            current = current.next;
        }
        return matches;
    }


    /** Returns the number of entries. O(1). */
    public int size() {
        return size;
    }


    /** Returns whether the chain is empty. O(1). */
    public boolean isEmpty() {
        return size == 0;
    }


    /** Removes every entry. O(1). */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }


    /**
     * Copies entries into a supplied array in insertion order. O(n).
     * Retained for compatibility with the starter code.
     *
     * @param values
     *            destination array of length at least size()
     * @return the same destination array
     * @throws IllegalArgumentException
     *             if values is null or too small
     */
    public T[] toArray(T[] values) {
        if (values == null || values.length < size) {
            throw new IllegalArgumentException(
                "Destination array is null or too small");
        }

        Node<T> current = head;
        int i = 0;
        while (current != null) {
            values[i++] = current.data;
            current = current.next;
        }
        return values;
    }


    /** Returns a listing in insertion order, e.g., [A, B, C]. O(n). */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            result.append(current.data);
            if (current.next != null) {
                result.append(", ");
            }
            current = current.next;
        }
        return result.append("]").toString();
    }


    private void requireNonNull(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Chain entries cannot be null");
        }
    }


    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index
                + " out of bounds for size " + size);
        }
    }

    /** Single linked node. */
    private static class Node<E> {
        private E data;
        private Node<E> next;

        private Node(E data) {
            this.data = data;
            next = null;
        }
    }
}
