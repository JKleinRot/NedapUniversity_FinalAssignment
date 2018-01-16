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
	
	/** 
	 * Initialize a Go board with the given width.
	 * Initially all intersections are unoccupied.
	 * @param width
	 * 			The width of the board.
	 */
	public Board(int size) {
		intersections = new Intersection[size - 1][size - 1];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
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

}
