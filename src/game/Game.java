package game;

/**
 * A game of Go.
 * @author janine.kleinrot
 */
public interface Game extends Runnable {

	public void run();
	
	/**
	 * Confirms the move made by the GoClient.
	 * @param move
	 * 			The move.
	 */
	public void confirmMove(String move);
}
