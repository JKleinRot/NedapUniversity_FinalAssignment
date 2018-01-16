package player;

import gui.GoGUIIntegrator;
import stone.StoneColor;
/**
 * Human player for Go.
 * @author janine.kleinrot
 */
public class HumanPlayer extends Player {
	
	/** GUI for the Go board. */
	private GoGUIIntegrator goGUI;
	
	/**
	 * Creates a human player with a given name and stone color.
	 * It starts the GUI of the Go board.
	 * @param name
	 * 			The name of the human player.
	 * @param color
	 * 			The color of the stones of the human player.
	 */
	public HumanPlayer(String name, StoneColor color, int boardSize) {
		super(name, color);
		goGUI = new GoGUIIntegrator(true, true, boardSize);
		goGUI.startGUI();
	}

}
