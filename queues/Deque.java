/* *****************************************************************************
 *  Name: Guoqing Yu
 *  Date: 2022/05/02
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    // construct an empty deque
    private class DequeNode {
        private final Item val;
        private DequeNode prev, next;

        private DequeNode() {
            this(null, null, null);
        }

        private DequeNode(DequeNode prev, Item val, DequeNode next) {
            this.prev = prev;
            this.val = val;
            this.next = next;
        }
    }

    private final DequeNode sentinel;
    private int size;

    public Deque() {
        sentinel = new DequeNode();
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    private void clear() {
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        DequeNode curr = new DequeNode(sentinel, item, sentinel.next);
        sentinel.next.prev = curr;
        sentinel.next = curr;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        DequeNode curr = new DequeNode(sentinel.prev, item, sentinel);
        sentinel.prev.next = curr;
        sentinel.prev = curr;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Item first = sentinel.next.val;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size--;
        return first;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Item last = sentinel.prev.val;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;
        return last;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private DequeNode curr = sentinel.next;

        public boolean hasNext() {
            return curr != sentinel;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = curr.val;
            curr = curr.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void printDeque() {
        for (Item item : this) StdOut.print(item + " ");
        StdOut.println();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        for (int i = 0; i < 10; i++)
            deque.addFirst(i);
        deque.printDeque();
        StdOut.println("size: " + deque.size());
        StdOut.println("poll: " + deque.removeFirst());
        StdOut.println("pop: " + deque.removeLast());
        StdOut.println("size: " + deque.size());
        deque.printDeque();

        deque.clear();
        for (int i = 0; i < 10; i++)
            deque.addLast(i);
        deque.printDeque();
    }
}
