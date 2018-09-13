import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Brute {
	private static Point[] array;
	private static ArrayList<Point> tmplist;

	public static void main(String[] args) {
		In input = new In(args[0]);
		int size = input.readInt();
		array = new Point[size];
		tmplist = new ArrayList<Point>();

		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		int index = 0;
		while (!input.isEmpty()) {

			array[index] = new Point(input.readInt(), input.readInt());
			array[index++].draw();

		}

		int N = array.length;
		for (int i = 0; i < N; i++) {
			for (int j = i + 1; j < N; j++) {
				for (int k = j + 1; k < N; k++) {
					double slope_p1p2 = array[i].slopeTo(array[j]);
					double slope_p1p3 = array[i].slopeTo(array[k]);

					for (int m = k + 1; m < N; m++) {
						// if loop is reached then first 3 points are collinear
						// tmplist.add(array[m]);
						double slope_p1p4 = array[i].slopeTo(array[m]);
						if (slope_p1p2 == slope_p1p4
								&& slope_p1p2 == slope_p1p3) { // All 4 pts
																// collinear
							tmplist.add(array[i]);
							tmplist.add(array[j]);
							tmplist.add(array[k]);
							tmplist.add(array[m]);
							Collections.sort(tmplist);
							tmplist.get(0).drawTo(
									tmplist.get(tmplist.size() - 1));

							System.out.println(tmplist.get(0) + " -> "
									+ tmplist.get(1) + " -> " + tmplist.get(2)
									+ " -> " + tmplist.get(3));

							tmplist.clear();
						}
					}
				}
			}
		}

	}
}
