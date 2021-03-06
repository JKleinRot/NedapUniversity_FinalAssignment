package game.board.stone;

/**
 * A stone on the Go board.
 * @author janine.kleinrot
 */
public class Stone {
	
	/** The color of the stone. */
	private StoneColor color;
	
	/** The amount of liberties of the stone. */
	private int liberties;
	
	/** The amount of initial liberties. */
	private int initialLiberties;
	
	/** 
	 * Create a stone with the provided color.
	 * @param color
	 * 			The color of the stone.
	 */
	public Stone(StoneColor color) {
		this.color = color;
		liberties = 4;
		initialLiberties = 4;
	}
	
	/** 
	 * Return the amount of liberties of the stone. 
	 * @return The amount of liberties of the stone.
	 */
	public int getLiberties() {
		return liberties;
	}
	
	/** Set the amount of liberties of the stone. 
	 * @param liberties
	 * 			The new amount of liberties.
	 */
	public void setLiberties(int liberties) {
		this.liberties = liberties; 
	}
	
	/** 
	 * Return the color of the stone.
	 * @return
	 * 		The color of the stone.
	 */
	public StoneColor getColor() {
		return color;
	}
	
	/**
	 * Set the amount of initial liberties.
	 * @param liberties
	 * 			The initial amount of liberties.
	 */
	public void setInitialLiberties(int initialLiberties) {
		this.initialLiberties = initialLiberties;
	}
	
	/**
	 * Return the amount of initial liberties.
	 * @return
	 * 			The amount of initial liberties.
	 */
	public int getInitialLiberties() {
		return initialLiberties;
	}

	/**
	 * Create a copy of the stone.
	 * @return
	 * 			The copy of the stone.
	 */
	public Stone copy() {
		Stone copy = new Stone(color);
		copy.liberties = liberties;
		copy.initialLiberties = initialLiberties;
		return copy;
	}
}
