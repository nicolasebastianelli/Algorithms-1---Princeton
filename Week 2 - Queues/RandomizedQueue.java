import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private myQueue queue;

    private class myQueue {
        Item[] q;
        int n;
        int tail;
        int head;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int j = 0;
        for (int i = 0; i < queue.n; i++)
            if (queue.q[i] != null)
                copy[j++] = queue.q[i];
        queue.q = copy;
        queue.n = capacity;
        queue.head = 0;
        queue.tail = j;
    }

    private void compress(int indx) {
        if (indx <= (queue.tail - queue.head) / 2) {
            for (int i = indx; i > queue.head; i--) {
                queue.q[i] = queue.q[i - 1];
            }
            queue.head++;
        }
        else {
            for (int i = indx; i < queue.tail; i++) {
                queue.q[i] = queue.q[i + 1];
            }
            queue.tail--;
        }
    }

    private class RQueueIterator implements Iterator<Item> {
        private Item[] current;
        private int[] permArray;
        private int nElem;
        private int j;

        private void resize() {
            current = (Item[]) new Object[queue.tail - queue.head + 1];
            nElem = 0;
            for (int i = 0; i < queue.tail; i++)
                if (queue.q[i] != null)
                    current[nElem++] = queue.q[i];
        }

        public RQueueIterator() {
            resize();
            //StdRandom.setSeed(System.currentTimeMillis());
            permArray = StdRandom.permutation(nElem);
            j = 0;
        }

        public boolean hasNext() {
            return (j != nElem);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            else {
                return current[permArray[j++]];
            }
        }
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = new myQueue();
        queue.n = 1;
        queue.q = (Item[]) new Object[queue.n];
        queue.head = 0;
        queue.tail = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (queue.tail - queue.head) == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return queue.tail - queue.head;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        else {
            if (queue.tail == queue.n) resize(2 * queue.n);
            queue.q[queue.tail++] = item;
        }
    }

    // remove and return a random item
    public Item dequeue() {
        int indx = StdRandom.uniform(queue.head, queue.tail);
        Item item = queue.q[indx];
        queue.q[indx] = null;
        if (queue.tail > 0 && queue.tail - queue.head - 1 == queue.n / 4)
            resize(queue.n / 2);
        else
            compress(indx);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        return queue.q[StdRandom.uniform(queue.head, queue.tail)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RQueueIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        String elemString = "A B C D E F G H I";
        System.out.print("String: " + elemString + "\n" + "----------------" + "\n");
        if (args.length == 1) {
            elemString = args[0];
        }
        String[] elemArray = elemString.split(" ");
        RandomizedQueue<String> rqueue = new RandomizedQueue<String>();
        System.out.print("\n\nAdding Phase" + "\n" + "----------------" + "\n");
        for (String elem : elemArray) {
            System.out.print("isEmpty: " + rqueue.isEmpty() + ", Size: " + rqueue.size() + "\n");
            System.out.print("Inserting: " + elem + "\n");
            rqueue.enqueue(elem);
            System.out.print("isEmpty: " + rqueue.isEmpty() + ", Size: " + rqueue.size() + "\n");
            System.out.print("Random Sample: " + rqueue.sample() + "\n");
            System.out.print("Queue: ");
            for (String elemPrint : rqueue) {
                System.out.print(elemPrint + ", ");
            }
            System.out.print("\n" + "----------------" + "\n");

        }

        System.out.print("\n\nRemoving Phase" + "\n" + "----------------" + "\n");
        while (!rqueue.isEmpty()) {
            System.out.print("isEmpty: " + rqueue.isEmpty() + ", Size: " + rqueue.size() + "\n");
            System.out.print("Removed: " + rqueue.dequeue() + "\n");
            System.out.print("isEmpty: " + rqueue.isEmpty() + ", Size: " + rqueue.size() + "\n");
            System.out.print("Queue: ");
            for (String elemPrint : rqueue) {
                System.out.print(elemPrint + ", ");
            }
            System.out.print("\n" + "----------------" + "\n");

        }
    }


}