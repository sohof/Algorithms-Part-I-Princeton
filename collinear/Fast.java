import java.util.*;

public class Fast {

	private static Point[] array, arFixed;
	private static ArrayList<Point> tmplist;

	// We keep of list of collinear points to be drawn, and print if at least 4
	// points.

	public static void main(String[] args) {

		tmplist = new ArrayList<Point>();

		In input = new In(args[0]);
		int size = input.readInt();
		// First Int in file is the # of points in the file.

		array = new Point[size]; //
		arFixed = new Point[size];

		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		int index = 0;
		Point point;
		while (!input.isEmpty()) {
			// read input from file create Point save to arrays
			point = new Point(input.readInt(), input.readInt());
			array[index] = point;
			arFixed[index] = point;
			array[index++].draw(); // draw point then increment index

		}
		System.out.println("");

		int N = arFixed.length;
		Point pivotPoint;
		if (N > 3) {
			for (int i = 0; i < N; ++i) {

				Arrays.sort(array, arFixed[i].SLOPE_ORDER);
				tmplist.clear();
				tmplist.add(array[0]); // add our current pivot point to list.
				pivotPoint = array[0]; // Keep a reference to the pivot point.

				double slope = array[0].slopeTo(array[1]);
				tmplist.add(array[1]);
				int countNrPts = 1;

				for (int j = 2; j < N; j++) {

					double nextSlope = array[0].slopeTo(array[j]);

					if (slope == nextSlope) {
						countNrPts++;
						tmplist.add(array[j]);

					}

					if (slope != nextSlope || (j == N - 1)) {

						if (countNrPts >= 3) {
							Collections.sort(tmplist);

							if (pivotPoint.compareTo(tmplist.get(0)) <= 0) {
								tmplist.get(0).drawTo(
										tmplist.get(tmplist.size() - 1));
								System.out.print(tmplist.get(0));
								for (int n = 1; n < tmplist.size(); n++) {
									System.out.print(" -> " + tmplist.get(n));
								}
								System.out.println("");
							}

						}
						tmplist.clear(); // clear list to prepare for new
											// possible
						tmplist.add(pivotPoint); // add pivot back
						tmplist.add(array[j]); // we also need to add the new
												// 2nd pt
						slope = nextSlope; // update slope from pivot to new 2nd
											// pt
						countNrPts = 1;
					}
				}

			}
		}
	}

}
