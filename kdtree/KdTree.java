/**
 * @author Sohof A 2-dimensional tree. A 2d-tree is a generalization of a BST to
 *         two-dimensional keys. The idea is to build a BST with points in the
 *         nodes, using the x- and y-coordinates of the points as keys in
 *         strictly alternating sequence. We use it for representing a set of
 *         points in the unit square and in order to support efficient range
 *         search (find all of the points contained in a query rectangle) and
 *         nearest neighbor search (find a closest point to a query point)
 */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;

public class KdTree {
	private Node root;

	// constants used for determining along which dimension a comparison
	// needs to be made. In our case a 2d-Tree. Only 2 dim to bother with.
	private final static int XAXIS = 0;
	private final static int YAXIS = 1;

	/**
	 * There are several reasonable ways to represent a node in a 2d-tree. One
	 * approach is to include the point, a link to the left/bottom subtree, a
	 * link to the right/top subtree, and an axis-aligned rectangle
	 * corresponding to the node.
	 */
	private static class Node {

		private Point2D point; // the point which the node represents
		private RectHV rect;
		// the axis-aligned rectangle. corresponding to this node
		private Node lb; // the left/bottom subtree
		private Node rt; // the right/top subtree
		private int N; // we don't actually need the size for our given prob.
		private final int level;

		public Node(Point2D point, RectHV rect, int N, int level) {
			this.point = point;
			this.rect = rect;
			this.N = N;
			this.level = level;
		}
	}

	/**
	 * Enum helper class to assist with creating the correct rectangles for the
	 * nodes being inserted. The root node is has the unit square as rectangle.
	 * It is always the parent of node which basically decides what the correct
	 * rectangle for the node being inserted is.
	 * 
	 */
	private enum Direction {
		LEFT, RIGHT, UP, DOWN, ROOT // ROOT symbolizes No Direction
	};

	public boolean isEmpty() {
		return size() == 0;
	}

	public int size() {
		return size(root);
	}

	// return number of nodes/points in KD-tree rooted at x
	private int size(Node x) {
		if (x == null)
			return 0;

		return x.N;
	}

	public void insert(Point2D point) {
		if (point == null)
			throw new NullPointerException();
		// parent of root is always null and there is no direction for the root
		root = put(root, null, point, 0, Direction.ROOT);
	}

	/**
	 * The axis variable helps alternate between x or y coord. for comparisons.
	 * on even levels we use xcoord on odd levels we use ycoord. in general if
	 * we had k dimensions we would need to use axis = level mod k. We also need
	 * a parent of a node and direction to be able to set up each rectangle
	 * corresponding to a node. If the node to be put already exits in the tree
	 * we Just return the node it was equal to and don't do any Insertion!
	 * */
	private Node put(Node nod, Node parent, Point2D point, int level,
			Direction dir) {

		if (nod == null) { // The base case for the recursion.
			RectHV rect = createRect(parent, dir);
			return new Node(point, rect, 1, level % 2);
		}

		int axis = level % 2;
		// In our problem. We know that we are comparing doubles!
		// So we dont't need the more general compareTo method
		int cmp = 0;
		if (axis == XAXIS) {
			cmp = Double.compare(point.x(), nod.point.x());
		} else {
			cmp = Double.compare(point.y(), nod.point.y());
		}

		if (cmp == 0 && nod.point.equals(point)) {
			return nod; // Point being inserted already exits!
		}

		if (cmp < 0 && axis == XAXIS) {
			nod.lb = put(nod.lb, nod, point, ++level, Direction.LEFT);
		} else if (cmp >= 0 && axis == XAXIS) {
			nod.rt = put(nod.rt, nod, point, ++level, Direction.RIGHT);
		} else if (cmp < 0 && axis == YAXIS) {
			nod.lb = put(nod.lb, nod, point, ++level, Direction.DOWN);
		} else if (cmp >= 0 && axis == YAXIS) {
			nod.rt = put(nod.rt, nod, point, ++level, Direction.UP);
		}

		nod.N = 1 + size(nod.lb) + size(nod.rt);
		return nod;

	}

	/**
	 * Given a nodes parent p and direction the method creates the correct
	 * rectangle for the node being inserted. When inserting a root/first no
	 * node. There is no relevant direction, since the rectangle is stated to be
	 * the unit square. We simply use the symbol ROOT to check for this case.
	 * And then set its rectangle to the unit square.
	 * 
	 * @param p
	 *            parent node
	 * @param dir
	 *            direction
	 * @return a rectangle corresponding to the inserted node. Or null if for
	 *         some reason we could not create a rectangle.
	 */
	private RectHV createRect(Node p, Direction dir) {

		if (dir == Direction.ROOT)
			return new RectHV(0.0, 0.0, 1.0, 1.0);

		else if (dir == Direction.LEFT)
			return new RectHV(p.rect.xmin(), p.rect.ymin(), p.point.x(),
					p.rect.ymax());

		else if (dir == Direction.RIGHT)
			return new RectHV(p.point.x(), p.rect.ymin(), p.rect.xmax(),
					p.rect.ymax());

		else if (dir == Direction.DOWN)
			return new RectHV(p.rect.xmin(), p.rect.ymin(), p.rect.xmax(),
					p.point.y());

		else if (dir == Direction.UP)
			return new RectHV(p.rect.xmin(), p.point.y(), p.rect.xmax(),
					p.rect.ymax());
		else
			return null;
	}

	public boolean contains(Point2D point) {

		return find(root, point, 0);

	}

	private boolean find(Node nod, Point2D point, int level) {

		if (nod == null)
			return false;
		int axis = level % 2;
		int cmp = 0;
		if (axis == XAXIS) {
			cmp = Double.compare(point.x(), nod.point.x());
		} else {
			cmp = Double.compare(point.y(), nod.point.y());
		}
		if (cmp == 0 && nod.point.equals(point))
			return true;
		else if (cmp < 0)
			return find(nod.lb, point, ++level);
		else
			return find(nod.rt, point, ++level);
	}

	public void draw() {

		Iterable<Node> nodes = levelOrder();

		StdDraw.setPenColor(StdDraw.BLACK);
		for (Node node : nodes) {

			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius(.01);

			StdDraw.point(node.point.x(), node.point.y());

			if (node.level == XAXIS) {
				StdDraw.setPenColor(StdDraw.RED); // drawing vertical lines
				StdDraw.setPenRadius();

				StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(),
						node.rect.ymax());
			}

			if (node.level == YAXIS) {
				StdDraw.setPenColor(StdDraw.BLUE);
				StdDraw.setPenRadius();

				StdDraw.line(node.rect.xmin(), node.point.y(),
						node.rect.xmax(), node.point.y());
			}

		}
	}

	private Iterable<Node> levelOrder() {
		Queue<Node> nodes = new Queue<Node>();
		Queue<Node> queue = new Queue<Node>();
		queue.enqueue(root);
		while (!queue.isEmpty()) {
			Node x = queue.dequeue();
			if (x == null)
				continue;
			nodes.enqueue(x);
			queue.enqueue(x.lb);
			queue.enqueue(x.rt);
		}
		return nodes;
	}

	public Point2D nearest(Point2D query) {

		if (isEmpty())
			return null;

		Node n = nearestSearch(root, query, 0, root);
		return n.point;
	}

	private Node nearestSearch(Node nod, Point2D query, int level, Node localMin) {

		Node lbMinNode = null, rtMinNode = null;
		if (nod == null)
			return localMin;

		double distance = query.distanceTo(nod.point);
		if (distance < query.distanceTo(localMin.point)) {
			localMin = nod;
		}

		int axis = level % 2;
		int cmp = 0;
		if (axis == XAXIS) {
			cmp = Double.compare(query.x(), nod.point.x());
		} else {
			cmp = Double.compare(query.y(), nod.point.y());
		}

		if (cmp < 0)
			lbMinNode = nearestSearch(nod.lb, query, ++level, localMin);

		if (cmp >= 0)
			rtMinNode = nearestSearch(nod.rt, query, ++level, localMin);

		if ((lbMinNode != null) && (nod.rt != null)) {
			if (query.distanceTo(lbMinNode.point) > nod.rt.rect
					.distanceTo(query)) {
				rtMinNode = nearestSearch(nod.rt, query, ++level, localMin);
			}
		}

		if ((rtMinNode != null) && (nod.lb != null)) {
			if (query.distanceTo(rtMinNode.point) > nod.lb.rect
					.distanceTo(query)) {
				lbMinNode = nearestSearch(nod.lb, query, ++level, localMin);
			}
		}

		return closest(lbMinNode, rtMinNode, query);
	}

	private Node closest(Node lbMinNode, Node rtMinNode, Point2D query) {

		if (lbMinNode == null)
			return rtMinNode;

		if (rtMinNode == null)
			return lbMinNode;

		if (query.distanceTo(lbMinNode.point) <= query
				.distanceTo(rtMinNode.point))
			return lbMinNode;
		else
			return rtMinNode;

	}

	public Iterable<Point2D> range(RectHV rect) {

		Queue<Point2D> points = new Queue<Point2D>();
		rangeSearch(root, rect, points);
		return points;
	}

	private void rangeSearch(Node nod, RectHV query, Queue<Point2D> points) {

		if (nod == null)
			return;

		if (query.contains(nod.point))
			points.enqueue(nod.point);

		if (nod.rt != null && query.intersects(nod.rt.rect))
			rangeSearch(nod.rt, query, points);

		if (nod.lb != null && query.intersects(nod.lb.rect))
			rangeSearch(nod.lb, query, points);

	}

	public static void main(String[] args) {
		String filename = args[0];
		In in = new In(filename);

		// StdDraw.show(0);
		KdTree kdtree = new KdTree();
		while (!in.isEmpty()) {
			double x = in.readDouble();
			double y = in.readDouble();
			Point2D p = new Point2D(x, y);
			kdtree.insert(p);
		}
		//
		// System.out.println("Size is " + kdtree.size());
		//
		// kdtree.draw();
		// Point2D p1 = new Point2D(0.024472, 0.3454992);
		// Point2D p2 = new Point2D(0.206107, 0.095492);
		// Point2D p3 = new Point2D(0.500000, 1.000300);
		//
		// System.out.println(kdtree.contains(p1));
		// System.out.println(kdtree.contains(p2));
		// System.out.println(kdtree.contains(p3));
	}
}
