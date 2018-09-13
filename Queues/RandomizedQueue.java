import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdIn;


public class RandomizedQueue<Item> implements Iterable<Item> {

	private Item[] rque;
	private int N;

	@SuppressWarnings("unchecked")
	public RandomizedQueue() {
		rque = (Item[]) new Object[1];
	}

	public boolean isEmpty() {
		return (N == 0);
	}

	public int size() {
		return N;
	}

	@SuppressWarnings("unchecked")
	private void resize(int max) {
		// Move stack to a new array of size max.
		Item[] temp = (Item[]) new Object[max];
		for (int i = 0; i < N; i++) {
			temp[i] = rque[i];
		}
		rque = temp;
	}

	public void enqueue(Item item) {
		if (item == null) {
			throw new NullPointerException();
		}
		if (N == rque.length)
			resize(2 * rque.length);
		rque[N++] = item;
	}

	public Item dequeue() { // remove and return a random item
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		// Returns an integer uniformly between 0 (inclusive) and N (exclusive).
		int randIndex = StdRandom.uniform(N);
		Item item = rque[randIndex]; // get a random item between pos 0 - (N-1)
		// overwrite random item position with last item
		rque[randIndex] = rque[--N];
		rque[N] = null; // set prev. last item pos to null to avoid loitering

		if (N > 0 && (N == rque.length / 4))
			resize(rque.length / 2);

		return item;
	}

	public Item sample() { // return (but do not remove) a random item
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		// get a random item between pos 0 - (N-1)
		Item item = rque[StdRandom.uniform(N)];
		return item;
	}

	public Iterator<Item> iterator() {
		// return an independent iterator over items in random order
		return new RandomQueIterator();
	}

	private class RandomQueIterator implements Iterator<Item> {
		private int itSize = N;
		private Item[] tempArray;

		@SuppressWarnings("unchecked")
		public RandomQueIterator() {
			tempArray = (Item[]) new Object[itSize];
			for (int i = 0; i < itSize; i++) {
				tempArray[i] = rque[i]; // copy items to temp array.
			}
			StdRandom.shuffle(tempArray);
		}

		public boolean hasNext() {
			return (itSize > 0);
		}

		public Item next() {
			if (itSize == 0) {
				throw new NoSuchElementException();
			}
			return tempArray[--itSize];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public static void main(String[] args) {
	
	}

}
