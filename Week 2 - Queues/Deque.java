import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int len;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            else {
                Item item = current.item;
                current = current.next;
                return item;
            }
        }
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        len = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null && last == null;
    }

    // return the number of items on the deque
    public int size() {
        return len;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        else {
            Node oldFirst = first;
            first = new Node();
            first.item = item;
            first.next = oldFirst;
            if (oldFirst != null) {
                oldFirst.prev = first;
            }
            else {
                last = first;
            }
            len++;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        else {
            Node oldLast = last;
            last = new Node();
            last.item = item;
            last.prev = oldLast;
            if (oldLast != null) {
                oldLast.next = last;
            }
            else {
                first = last;
            }
            len++;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        else {
            Item item = first.item;
            first = first.next;
            if (first != null)
                first.prev = null;
            else
                last = null;
            len--;
            return item;
        }
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        else {
            Item item = last.item;
            last = last.prev;
            if (last != null)
                last.next = null;
            else
                first = null;
            len--;
            return item;
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        String elemString = "A B C D E F G H I";
        System.out.print("String: " + elemString + "\n" + "----------------" + "\n");
        if (args.length == 1) {
            elemString = args[0];
        }
        boolean discriminator = true;
        String[] elemArray = elemString.split(" ");
        Deque<String> deque = new Deque<String>();
        System.out.print("\n\nAdding Phase" + "\n" + "----------------" + "\n");
        for (String elem : elemArray) {
            System.out.print("isEmpty: " + deque.isEmpty() + ", Size: " + deque.size() + "\n");
            System.out.print("Inserting: " + elem + " , inFront: " + discriminator + "\n");
            if (discriminator) {
                deque.addFirst(elem);
                discriminator = false;
            }
            else {
                deque.addLast(elem);
                discriminator = true;
            }
            System.out.print("isEmpty: " + deque.isEmpty() + ", Size: " + deque.size() + "\n");
            System.out.print("Queue: ");
            for (String elemPrint : deque) {
                System.out.print(elemPrint + ", ");
            }
            System.out.print("\n" + "----------------" + "\n");

        }

        System.out.print("\n\nRemoving Phase" + "\n" + "----------------" + "\n");
        String rem;
        while (!deque.isEmpty()) {

            System.out.print("Removing inFront: " + discriminator + "\n");
            if (discriminator) {
                rem = deque.removeFirst();
                discriminator = false;
            }
            else {
                rem = deque.removeLast();
                discriminator = true;
            }
            System.out.print("Removed : " + rem + "\n");
            System.out.print("isEmpty: " + deque.isEmpty() + ", Size: " + deque.size() + "\n");
            System.out.print("Queue: ");
            for (String elemPrint : deque) {
                System.out.print(elemPrint + ", ");
            }
            System.out.print("\n" + "----------------" + "\n");

        }
        System.out.print("isEmpty: " + deque.isEmpty() + ", Size: " + deque.size() + "\n");

    }

}