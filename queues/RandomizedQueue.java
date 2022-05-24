/* *****************************************************************************
 *  Name: Guoqing Yu
 *  Date: 2022/05/02
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int CAPACITY_BASE = 8;
    private static final int REFACTOR_SIZE = 2;
    private int size, capacity;
    private Item[] items;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this(CAPACITY_BASE);
    }

    private RandomizedQueue(int capacity) {
        items = (Item[]) new Object[capacity];
        this.capacity = capacity;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        items[size++] = item;
        if (size == capacity)
            reshape(capacity * REFACTOR_SIZE);
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniform(size);
        Item item = items[index];
        items[index] = items[size - 1];
        items[--size] = null;
        if (capacity > CAPACITY_BASE && size < capacity * 0.25)
            reshape(capacity / REFACTOR_SIZE);
        return item;
    }

    private void reshape(int newCapacity) {
        Item[] curr = (Item[]) new Object[newCapacity];
        System.arraycopy(items, 0, curr, 0, size);
        items = curr;
        capacity = newCapacity;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniform(size);
        return items[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandQueueIterator();
    }

    private class RandQueueIterator implements Iterator<Item> {
        private int pos;
        private final int[] indices;

        public RandQueueIterator() {
            indices = new int[size];
            for (int i = 0; i < size; i++) indices[i] = i;
            StdRandom.shuffle(indices);
        }

        public boolean hasNext() {
            return pos < size;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return items[indices[pos++]];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        for (int i = 0; i < 6; i++)
            queue.enqueue(i);

        for (int i = 0; i < 4; i++)
            StdOut.print(queue.dequeue() + " ");
        StdOut.println();

        for (int i = 6; i < 100; i++)
            queue.enqueue(i);

        for (int i = 0; i < 90; i++)
            queue.dequeue();

        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }
}
