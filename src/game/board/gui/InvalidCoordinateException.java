package game.board.gui;

/**
 * Created by daan.vanbeek on 15-12-16.
 */
@SuppressWarnings("serial")
public class InvalidCoordinateException extends Exception {

	public InvalidCoordinateException(String message) {
        super(message);
    }
}
