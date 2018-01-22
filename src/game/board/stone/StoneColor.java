package game.board.stone;

/** 
 * The color of the stone on the Go board.
 * The possible colors are WHITE and BLACK.
 * @author janine.kleinrot
 */
public enum StoneColor {
	
	BLACK, WHITE;
	
	public StoneColor other() {
		if (this == WHITE) {
			return BLACK;
		} else {
			return WHITE;
		}
	}

}
