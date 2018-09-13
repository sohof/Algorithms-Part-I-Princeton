/**
 * Percolation class as required programming assignment. 
 * Coursera course algorithms part 1.
 *
 *@author Sohof Dastmard <dastmard@gmail.com>
 *@version 1.0
 */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {

    /**
     * Union find data structure for dynamically connecting sites.
     */
    private WeightedQuickUnionUF unionFind;
     /**
     * UF2 is used to hold separate data as to avoid the backwash problem.
     */
    private WeightedQuickUnionUF unionFind2;
   
    private boolean[][] matrix;
    // Matrix to hold info on whether a site is closed or open.
    private int matrixSize;
// the row/colum size of  matrix. Used for checking indexOutOfBondsExceptions
    private static final boolean CLOSED = false;
    // boolean flags for enhanching readibility
    private static final boolean OPEN = true;
    // boolean flags for enhanching readibility
    private static final int VIRTUALTOP = 0;
    // fixed to 0. Is always connected to the first row of the grid
    private int virtualBottom;
    // Represents virtual bottom, varies depending on size of the grid.


    /**
     * Initializes the percolation, creating: 2 union-find data structures
     *and the matrix. create N-by-N grid, with all sites blocked
     *@throws java.lang.IllegalArgumentException if N <=0
     *@param N the size of the grid to be created
     */
    public Percolation(final int n) {
	if (n <= 0) {
	    throw new IllegalArgumentException(Integer.toString(n));
	}
	matrixSize = n;
	int sizeunionFind = n * n;
	virtualBottom = sizeunionFind + 1;
	unionFind = new WeightedQuickUnionUF(sizeunionFind + 2);
	// add 2 extra sites for our 2 virtual nodes
	unionFind2 = new WeightedQuickUnionUF(sizeunionFind + 1);
// make room for one more site since, Weig.QuickUnionunionFind is zero indexed.
	matrix = new boolean[n][n];

	for (int i = 0; i < n; i++) {
	    for (int j = 0; j < n; j++) {
		matrix[i][j] = CLOSED;
	    }
	}
	for (int i = 1; i <= n; i++) {
	    unionFind.union(VIRTUALTOP, i);
	    // Virtualtop always connected tofirst row
	    unionFind2.union(VIRTUALTOP, i);
	}
    }


    private void validateIndices(final int i, final int j) {
	if (i > matrixSize || j > matrixSize || j < 1 || i < 1) {
	    // Make sure indices are within proper bounds
	    throw new IndexOutOfBoundsException(Integer.toString(i) + Integer.toString(j));
	}
    }

    // Given any position on the N-by-N grid, computes site ID between 1..N*N
    private int computeSiteID(final int i, final int j) {
	validateIndices(i, j);
	return ((i - 1) * matrixSize + j);
    }

    /**
     *Opens a site on the grid at position (i,j) if previously blocked.
     *Connects the open site to other open sites using union-find structure
     *@throws java.lang.IndexOutOfBoundsException if index outside of the grid
     *@param i  integer to define (x,_) position on grid
     *@param j  integer to define (_,y) position on grid
     */
    public void open(final int i, final int j) {

	validateIndices(i, j);
	int siteID = computeSiteID(i, j); // calculate corresponding siteID
	int row = i - 1;
	int col = j - 1;
// If not open already, then OPEN it. If already Open function does nothing.
	if (matrix[row][col] == CLOSED) {

	    matrix[row][col] = OPEN;
//Opened site is at last row of grid and should be connected to virtualbottom
	    if (i == matrixSize) {
		unionFind.union(virtualBottom, siteID);
	    }
	    if (i > 1) { // We are below first row, safe to try north
		if (isOpen(i - 1, j)) {
		    unionFind.union(siteID, computeSiteID(i - 1, j));
      		    unionFind2.union(siteID, computeSiteID(i - 1, j));
		}
	    }
	    // We are to the left of last column, safe to try east
	    if (j < matrixSize) {
		if (isOpen(i, j + 1)) {
		    unionFind.union(siteID, computeSiteID(i, j + 1));
	      	    unionFind2.union(siteID, computeSiteID(i, j + 1));
		}
	    }
	    // We are above last row, safe to try south
	    if (i < matrixSize) {
		if (isOpen(i + 1, j)){
		    unionFind.union(siteID, computeSiteID(i + 1, j));
		    unionFind2.union(siteID, computeSiteID(i + 1, j));
		}
	    }
	    // We are to the right of the first column safe to try west
	    if (j > 1) {
		if (isOpen(i, j - 1)) {
		    unionFind.union(siteID, computeSiteID(i, j - 1));
		    unionFind2.union(siteID, computeSiteID(i, j - 1));
		}
	    }

	}
    }

    /**
     *Check if site on the grid at position (i,j) is open.
     *@throws java.lang.IndexOutOfBoundsException if index outside of the grid
     *@param i  integer to define (x,_) position on grid
     *@param j  integer to define (_,y) position on grid
     *@return true if given site at (i,j) is open
     */
    public boolean isOpen(final int i, final int j) {

	validateIndices(i, j);
	return (matrix[i - 1][j - 1] == OPEN);
    }


    /**
     *Check if site on the grid at position (i,j) is full.
     *@throws java.lang.IndexOutOfBoundsException if index outside of the grid
     *@param i  integer to define (x,_) position on grid
     *@param j  integer to define (_,y) position on grid
     *@return true if given site at (i,j) is full
     */
    public boolean isFull(final int i, final int j) {

	validateIndices(i, j);
	int siteID = computeSiteID(i, j);

        if (isOpen(i, j) && unionFind.connected(siteID, VIRTUALTOP) && unionFind2.connected(siteID, VIRTUALTOP)) {
	    return true;
	}
	return false;

    }

    /**
     *@return <tt>true</tt> if system percolats
     */
    public boolean percolates() { // does the system percolate?

	return unionFind.connected(VIRTUALTOP, virtualBottom);
    }

}