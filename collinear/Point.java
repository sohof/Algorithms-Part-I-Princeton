/*************************************************************************
 * Name:
 * Dependencies: StdDraw.java
 * Description: An immutable data type for points in the plane.
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

	// compare points by slope
	public final Comparator<Point> SLOPE_ORDER;
	private final int x; // x coordinate
	private final int y; // y coordinate
	private static final double EPSILON = 1E-14;

	private class SlopeOrder implements Comparator<Point> {

		public int compare(Point arg1, Point arg2) {

			double slopTo1 = slopeTo(arg1);
			double slopeTo2 = slopeTo(arg2);

			if (slopTo1 == slopeTo2)
				return 0;
			if (slopTo1 < slopeTo2)
				return -1;
			else
				return 1;
		}
	}

	// create the point (x, y)
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
		SLOPE_ORDER = new SlopeOrder();
	}

	// plot this point to standard drawing
	public void draw() {
		StdDraw.point(x, y);
	}

	// draw line between this point and that point to standard drawing
	public void drawTo(Point that) {
		StdDraw.line(this.x, this.y, that.x, that.y);
	}

	// slope between this point and that point
	public double slopeTo(Point that) {

		double num = that.y - this.y;
		double denom = that.x - this.x;

		if (Math.abs(num) <= EPSILON && Math.abs(denom) <= EPSILON)
			return Double.NEGATIVE_INFINITY;
		// Degenerate case a point compared with itself.
		else if (Math.abs(denom) <= EPSILON)
			return Double.POSITIVE_INFINITY; // Vertical line

		else if (Math.abs(num) <= EPSILON) // Horizontal line
			return 0.0;

		else
			return (num / denom); // return slope

	}

	// is this point lexicographically smaller than that one?
	// comparing y-coordinates and breaking ties by x-coordinates
	public int compareTo(Point that) {

		if (this.y < that.y)
			return -1;
		else if (this.y == that.y && this.x < that.x)
			return -1;
		else if (this.y == that.y && this.x == that.x)
			return 0;
		else
			return 1;
	}

	// return string representation of this point
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public static void main(String[] args) {
		/* YOUR CODE HERE */
	}
}