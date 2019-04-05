// Board.java
package tetris;

import java.util.Arrays;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean[][] xGrid;
	private int [] widths;
	private int [] xWidths;
	private int [] heights;
	private int [] xHeights;
	private int maxHeight;
	private int xMaxHeight;
	
	private boolean DEBUG = true;
	boolean committed;
	
	
	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;
		widths = new int[height];
		heights = new int[width];
		maxHeight = 0;
	}
	
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() { 
		if (!DEBUG) return;
		int checkMaxHeight = 0;
		int [] checkWidth = new int[height];
		int [] checkHeight = new int[width];
		for(int i = 0 ; i < width; i++) {
			for(int j = 0 ; j < height; j++) {
				if(! grid[i][j] ) continue;
				checkMaxHeight = Math.max(checkMaxHeight, j + 1);
				checkWidth[j]++;
				checkHeight[i] = j + 1;
			}
		}
		if(!Arrays.equals(widths, checkWidth)) throw new RuntimeException("Problem with widths");
		if(!Arrays.equals(heights, checkHeight)) throw new RuntimeException("Problem with heights");
		if(checkMaxHeight != maxHeight) throw new RuntimeException("Problem with max Height" + checkMaxHeight + " " + maxHeight);
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int h = 0;
		int[] skirt = piece.getSkirt();
		for(int i = 0 ; i < skirt.length; i++) {
			h = Math.max(heights[i + x] - skirt[i], h);
		}
		return h;
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x]; // YOUR CODE HERE
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y]; // YOUR CODE HERE
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if(outOfBounds(x , y)) return true;
		return grid[x][y]; // YOUR CODE HERE 
	}
	
	
	private boolean outOfBounds(int x, int y) {
		if(x < 0 || x >= width || y < 0 || y >= height) return true;
		return false;
	}


	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
		committed = false;
		copyInfo();	
		int result = PLACE_OK;
		result = placePiece(piece , x , y);
		return result;
	}
	
	
	private int placePiece(Piece piece , int x , int y) {
		int result = PLACE_OK;
		TPoint [] points = piece.getBody();
		for(int i = 0 ; i < points.length; i++) {
			int currX = x + points[i].x;
			int currY = y + points[i].y;
			if(outOfBounds(currX, currY)) return PLACE_OUT_BOUNDS;
			if(grid[currX][currY]) return PLACE_BAD;
			maxHeight = Math.max(maxHeight, currY + 1);
			heights[currX] = Math.max(heights[currX], currY + 1);
			grid[currX][currY] = true;
			widths[currY]++;
			if(widths[currY] == width) result = PLACE_ROW_FILLED;
		}
		return result;
	}



	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		if(committed) copyInfo();
		committed = false;
		int rowsCleared = 0;
		int currMaxHeight = maxHeight;
		// set all heights zero to update
		maxHeight = 0;
		for(int i = 0 ; i < width; i++) {
			heights[i] = 0; 
		}
		int to = 0;
		while(to < currMaxHeight && widths[to] != width) {
			updateHeights(to);
			to++;
		}
		int from = to;
		// we have to pointers from and to , we need to take row "from" into row "to"
		while(from < currMaxHeight) {
			if(widths[from] == width) {
				from++;
				rowsCleared++;
			}else { 
				copyRow(to , from);
				to++;
				from++;		
			}
		}	
		if(widths[to] != width) updateHeights(to);
		for(int i = to; i < from ; i++) {
			if(widths[i] == width) clearRow(i);
		}
		sanityCheck();
		return rowsCleared;
	}
	
	
	private void updateHeights(int to) {
		for(int i = 0 ; i < width; i++) {
			if(grid[i][to]) {
				heights[i] = Math.max(heights[i], to + 1);
				maxHeight = Math.max(maxHeight, heights[i]);
			}
		}
	}



	private void clearRow(int r) {
		widths[r] = 0;
		for(int i = 0 ; i < width ; i++) {
			grid[i][r] = false;
		}
	}



	private void copyRow(int to, int from) {
		widths[to] = widths[from];
		widths[from] = 0;
		for(int i = 0 ; i < width; i++) {
			if(grid[i][from]) {
				heights[i] = Math.max(heights[i], to + 1);
				maxHeight = Math.max(maxHeight, heights[i]);
			}
			grid[i][to] = grid[i][from];
			grid[i][from] = false;
		}	
	}
 
	private void copyInfo() {
		xGrid = new boolean[width][height];
		xHeights = new int[width]; 
		xWidths = new int[height];
		for(int i = 0 ; i < grid.length; i++) {
			System.arraycopy(grid[i], 0, xGrid[i], 0, grid[i].length);
		}
		System.arraycopy(widths, 0, xWidths, 0, widths.length);
		System.arraycopy(heights, 0, xHeights,  0, heights.length);
		xMaxHeight = maxHeight;
	}

	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if(committed) return;
		widths = xWidths;
		heights = xHeights;
		grid = xGrid;
		maxHeight = xMaxHeight;
		committed = true;
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


