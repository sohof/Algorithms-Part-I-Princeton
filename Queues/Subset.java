import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdIn;
public class Subset {

    public static void main(String[] args) {

	int k = Integer.parseInt(args[0]);
	RandomizedQueue<String> q = new RandomizedQueue<String>();
	while (!StdIn.isEmpty()){
	    q.enqueue(StdIn.readString());
	}
	Iterator<String> it = q.iterator();
	while (it.hasNext() && k > 0) {
	    System.out.println(it.next());
	    k--;
	}
    }

}
