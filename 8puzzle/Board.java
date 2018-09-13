public class Board {

	private int[][] blocks; // current board
	private short N; // Dimension of board
	private short blankRow;
	private short blankCol;
	private short manhattan; // sum of Manhattan distances between blocks and
								// goal

	public Board(int[][] blocks) {
		// construct a board from an N-by-N array of blocks
		// (where blocks[i][j] = block in row i, column j)
		this.N = (short) blocks.length; // Outer array size
		this.blocks = new int[N][N];

		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++) {
				int value = blocks[i][j];
				this.blocks[i][j] = value;

				if (value == 0) {
					blankRow = (short) i;
					blankCol = (short) j;
				} else { // calculate manhattan distance
					int x = (value - 1) / N;
					int y = (value - 1) % N;
					manhattan += Math.abs(i - x) + Math.abs(j - y);
				}
				// if (value != 0 && value != i * N + j + 1)
				// hamming++; // calculate hamming distance
			}

	}

	public int dimension() {
		return (int) N;
	}

	public int hamming() {
		int hamming = 0; // number of blocks out of place

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (blocks[i][j] != 0 && blocks[i][j] != i * N + j + 1)
					hamming++;
			}
		}
		return hamming;
	}

	public int manhattan() {
		return manhattan;
	}

	public boolean isGoal() {

		return hamming() == 0;
	} // is this board the goal board?

	public Board twin() {
		Board aTwinBoard = null;

		if (blocks[0][0] != 0 && blocks[0][1] != 0) {
			// safe to exchange (0,0) and (0,1) since not blanks
			exch(0, 0, 0, 1);
			aTwinBoard = new Board(blocks);
			exch(0, 0, 0, 1);
		} else {
			// safe to exchange (1,0) and (1,1) (or any other) since blank was
			// in row 0
			exch(1, 0, 1, 1);
			aTwinBoard = new Board(blocks);
			exch(1, 0, 1, 1);

		}
		return aTwinBoard;
	} // a board obtained by exchanging two adjacent blocks in the same row

	public boolean equals(Object y) {
		if (y == this)
			return true;
		if (y == null)
			return false;

		if (y.getClass() != this.getClass())
			return false;

		Board that = (Board) y;
		if (that.dimension() != this.dimension())
			return false;
		return isEqual(this.blocks, that.blocks);

	} // does this board equal y?

	public Iterable<Board> neighbors() {
		Queue<Board> que = new Queue<Board>();

		if ((blankRow - 1) > -1) { // try moving blank north
			addNeighbor(que, blankRow - 1, blankCol);
		}
		if ((blankRow + 1) < N) { // try moving blank south
			addNeighbor(que, blankRow + 1, blankCol);
		}
		if ((blankCol - 1) > -1) { // try moving blank west
			addNeighbor(que, blankRow, blankCol - 1);
		}
		if ((blankCol + 1) < N) { // try moving blank east
			addNeighbor(que, blankRow, blankCol + 1);
		}

		return que;

	}// all neighboring boards

	private void addNeighbor(Queue<Board> que, int row, int col) {

		exch(blankRow, blankCol, row, col);
		que.enqueue(new Board(blocks)); // create the new board .
		exch(blankRow, blankCol, row, col);
	}

	// string representation of this board (in the output format specified
	public String toString() {

		StringBuilder sb = new StringBuilder(); // default capacity is 16 chars
		sb.append(N + "\n");
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				sb.append(String.format("%2d ", blocks[i][j]));
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	private boolean isEqual(final int[][] arg1, final int[][] arg2) {

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (arg1[i][j] != arg2[i][j])
					return false;
			}
		}
		return true;
	}

	private void exch(int x1, int y1, int x2, int y2) {
		int temp = blocks[x1][y1];
		blocks[x1][y1] = blocks[x2][y2];
		blocks[x2][y2] = temp;
	}

	public static void main(String[] args) {

	}

}
