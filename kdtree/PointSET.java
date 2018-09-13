import java.util.TreeSet;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;

public class PointSET {

	private TreeSet<Point2D> set;

	public PointSET() {

		set = new TreeSet<Point2D>();

	}

	public boolean isEmpty() {
		return set.isEmpty();
	}

	public int size() {
		return set.size();
	}

	public void insert(Point2D p) {

		if (p == null)
			throw new NullPointerException();
		set.add(p);

	} // add the point to the set (if it is not already in the set)

	public boolean contains(Point2D p) {

		if (p == null)
			throw new NullPointerException();

		return set.contains(p);
	} // does the set contain point p?

	public void draw() {

		StdDraw.clear();

		for (Point2D p : set)
			StdDraw.point(p.x(), p.y());

	} // draw all points to standard draw

	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new NullPointerException();

		Queue<Point2D> q = new Queue<Point2D>();
		for (Point2D p : set) {
			if (rect.contains(p))
				q.enqueue(p);
		}

		return q;
	} // all points that are inside the rectangle

	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new NullPointerException();

		if (set.isEmpty())
			return null;

		Point2D nearestNeighbor = set.first(); // Just init our variables to
		double minDistance = p.distanceTo(nearestNeighbor); // first point in
															// the set.
		for (Point2D that : set) {
			double currDistance = p.distanceTo(that);
			if (currDistance < minDistance) {
				minDistance = currDistance;
				nearestNeighbor = that;
			}
		}

		return nearestNeighbor;

	} // a nearest neighbor in the set to point p; null if the set is empty

	public static void main(String[] args) {

	}

}
