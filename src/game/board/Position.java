package game.board;

/**
 * A position a the board.
 * @author janine.kleinrot
 */
public class Position {
	
	/** The x coordinate of the position. */
	private int x;
	
	/** The y coordinate of the position. */
	private int y;
	
	/**
	 * Create a new position with the provided x and y coordinate.
	 * @param x
	 * 			The x coordinate.
	 * @param y
	 * 			The y coordinate.
	 */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Return the x coordinate of the position.
	 * @return
	 * 			The x coordinate.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Return the y coordinate of the position.
	 * @return
	 * 			The y coordinate.
	 */
	public int getY() {
		return y;
	}

}
