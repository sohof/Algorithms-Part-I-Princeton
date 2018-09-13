public class Solver {

	private boolean solved = false;
	private boolean solvedTwin = false;
	private MinPQ<ComBoard> minQue, minQueTwin;
	private ComBoard goalNode;
	private int minmoves;

	// Need to add some detail to board needed to solve the problem but
	// which were not allowed to be included in the original API

	private class ComBoard implements Comparable<ComBoard> {
		// inner class Comparable Board
		private Board board;
		private ComBoard parent; // need to track the parent of each Comboard.
		private int moves;
		private int priority;

		public ComBoard(Board b) { // Cons. for the first initial board
			board = b;
			parent = null; // initial board has no parent
			moves = 0;
			priority = b.manhattan();
			// Just manhattan for initial comBoard since moves equal 0
		}

		public ComBoard(Board b, ComBoard c) {
			board = b;
			parent = c;
			moves = c.moves + 1;
			priority = b.manhattan() + moves;
		}

		public int compareTo(ComBoard that) {
			if (this.priority < that.priority)
				return -1;
			if (this.priority == that.priority)
				return 0;
			else
				return 1;
		}

	}

	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board b) {

		Board initial = b;

		if (initial == null)
			throw new NullPointerException();

		// if twin is solvable means the initial board not solvable!
		Board twin = initial.twin();

		ComBoard init = new ComBoard(initial);
		ComBoard ctwin = new ComBoard(twin);
		minQue = new MinPQ<ComBoard>();
		minQueTwin = new MinPQ<ComBoard>();

		solvePuzzle(init, ctwin);
	}

	private void solvePuzzle(ComBoard init, ComBoard ctwin) {
		ComBoard temp;
		minQue.insert(init);
		minQueTwin.insert(ctwin);

		while (!solved && !solvedTwin) {
			ComBoard comb1 = minQue.delMin();
			ComBoard comb2 = minQueTwin.delMin();
			if (comb1.board.isGoal()) {
				solved = true;
				minmoves = comb1.moves;
				goalNode = comb1;
				break;
			}
			if (comb2.board.isGoal()) {
				solvedTwin = true;
				minmoves = -1; // sentinel value that int.board was not solvable
				break;
			}
			/**
			 * A critical optimization. Best-first search has one annoying
			 * feature: search nodes corresponding to the same board are
			 * enqueued on the priority queue many times. To reduce unnecessary
			 * exploration of useless search nodes, when considering the
			 * neighbors of a search node, don't enqueue a neighbor if its board
			 * is the same as the board of the previous search node.
			 * */
			for (Board b : comb1.board.neighbors()) {
				// The initial board will have a null parent!
				if (comb1.parent == null) {
					temp = new ComBoard(b, comb1);
					minQue.insert(temp);
				}

				else { // optimization see comment above
					if (!b.equals(comb1.parent.board)) {
						temp = new ComBoard(b, comb1);
						minQue.insert(temp);
					}
				}

			}
			// The same procedure for the twin node
			for (Board b : comb2.board.neighbors()) {
				if (comb2.parent == null) {
					temp = new ComBoard(b, comb2);
					minQueTwin.insert(temp);
				}

				else {
					if (!b.equals(comb2.parent.board)) {
						temp = new ComBoard(b, comb2);
						minQueTwin.insert(temp);
					}
				}

			}

		}
	}

	public boolean isSolvable() {
		return solved;
	} // is the initial board solvable?

	public int moves() {
		return minmoves;
	} // min number of moves to solve initial board; -1 if unsolvable

	public Iterable<Board> solution() {
		Stack<Board> stack;
		ComBoard goalNodeCopy = goalNode;
		if (solved) {
			stack = new Stack<Board>(); // Set up stack for solution path
			Board tmp = goalNodeCopy.board;
			stack.push(tmp);
			while (goalNodeCopy.parent != null) {
				tmp = goalNodeCopy.parent.board;
				stack.push(tmp);
				goalNodeCopy = goalNodeCopy.parent;
			}

			return stack;
		} else
			return null;
	} // sequence of boards in a shortest solution; null if unsolvable

	public static void main(String[] args) {

		// create initial board from file
		In in = new In(args[0]);
		int N = in.readInt();
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output

		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}

	}
}