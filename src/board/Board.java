package board;

import stone.Stone;
import stone.StoneColor;

/**
 * The Go board.
 * @author janine.kleinrot
 */
public class Board {
	
	/** Two-dimensional array of intersections of the board. */
	private Intersection[][] intersections;
	
	/** The size of the board. */
	private final int SIZE;
	
	/** 
	 * Initialize a Go board with the given width.
	 * The minimum size is 5 x 5. If the given size is smaller than 5, the size is set to 5.
	 * The maximum size is 19 x 19. If the given size is larger than 19, the size is set to 19.
	 * Initially all intersections are unoccupied.
	 * @param width
	 * 			The width of the board.
	 */
	public Board(int size) {
		if (size < 5) {
			intersections = new Intersection[5][5];
			SIZE = 5;
		} else if (size > 19) {
			intersections = new Intersection[19][19];
			SIZE = 19;
		} else {
			intersections = new Intersection[size][size];
			SIZE = size;
		}
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				intersections[i][j] = new Intersection();
			}
		}
	}
	
	/**
	 * Return the intersection at the provided x and y coordinate of the board.
	 * @param x
	 * 			The x coordinate of the intersection at the board.
	 * @param y
	 * 			The y coordinate of the intersection at the board.
	 * @return
	 * 			The intersection at the board at the provided coordinates.
	 */
	public Intersection getIntersection(int x, int y) {
		return intersections[x][y];
	}
	
	/**
	 * Set a stone with the provided color at the intersection at the provided x and y coordinate.
	 * @param x
	 * 			The x coordinate of the intersection at the board.
	 * @param y
	 * 			The y coordinate of the intersection at the board.
	 * @param color
	 * 			The color of the stone set at the intersection.
	 */
	public void setStone(int x, int y, StoneColor color) {
		this.getIntersection(x, y).setStone(color);
	}
	
	/**
	 * Get the stone at the intersection at the provided x and y coordinate of the board.
	 * @param x
	 * 			The x coordinate of the intersection at the board.
	 * @param y
	 * 			The y coordinate of the intersection at the board.
	 * @return 
	 * 			The stone at the intersection.
	 */
	public Stone getStone(int x, int y) {
		return this.getIntersection(x, y).getStone();
	}
	
	/**
	 * Remove the stone at the intersection at the provided x and y coordinate of the board.
	 * @param x
	 * 			The x coordinate of the intersection at the board.
	 * @param y
	 * 			The y coordinate of the intersection at the board.
	 */
	public void removeStone(int x, int y) {
		this.getIntersection(x, y).removeStone();
	}
	
	public int getSize() {
		return SIZE;
	}

}
