import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Sohof
 * 
 */
public class Deque<Item> implements Iterable<Item> {

	private int N;
	private Node first;
	private Node last;

	private class Node {
		Item item;
		Node next;
		Node previous;
	}

	public boolean isEmpty() {
		return (N == 0);
	}

	public int size() {
		return N;
	}

	public void addFirst(Item item) {

		if (item == null) {
			throw new NullPointerException();
		}

		if (isEmpty()) {
			addBeginning(item);
		} else {
			Node oldFirst = first;
			first = new Node();
			first.item = item;
			first.next = oldFirst;
			first.previous = null;
			oldFirst.previous = first;
		}
		N++;
	}

	public void addLast(Item item) {

		if (item == null) {
			throw new NullPointerException();
		}

		if (isEmpty()) {
			addBeginning(item);
		} else {
			Node oldLast = last;// add the item to the end
			last = new Node();
			last.item = item;
			last.next = null;
			last.previous = oldLast;
			oldLast.next = last;
		}
		N++;
	}

	private void addBeginning(Item item) {
		first = new Node();
		last = first;
		first.item = item;
	}

	public Item removeFirst() {

		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		Item item = first.item; // get the item

		if (size() == 1) { // last item is being deleted.
			first = last = null;
		} else {
			first = first.next; // advance to next item
			first.previous = null; // next items prev.pointer should be set to
									// null
		}
		N--;
		return item;
	}

	public Item removeLast() {

		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		Item item = last.item;
		if (size() == 1) { // last item is being deleted.
			first = last = null;
		} else {
			last = last.previous; // last is set to previous node
			last.next = null; // the previous node is the new last node now
		}
		N--;
		return item;
	}

	public Iterator<Item> iterator() {
		return new DequeIterator();
	}

	private class DequeIterator implements Iterator<Item> {
		Node tempFirst = first;

		public boolean hasNext() {
			return (tempFirst != null);
		}

		public Item next() {
			if (tempFirst == null) {
				throw new NoSuchElementException();
			}
			Item item = tempFirst.item;
			tempFirst = tempFirst.next;
			return item;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public static void main(String[] args) {

	}

}
