package game.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import game.board.stone.Stone;
import game.board.stone.StoneColor;
import gui.GoGUIIntegrator;

/**
 * The Go board.
 * @author janine.kleinrot
 */
public class Board {
	
	/** Two-dimensional array of intersections of the board. */
	private Intersection[][] intersections;
	
	/** The size of the board. */
	private int size;
	
	/**	The GUI of the board. */
	private GoGUIIntegrator goGUI;
	
	/** Wheter a GoGUI should be used. */
	private boolean isGoGUI;
	
	/**List of adjacent stones. */
	private List<Intersection> adjacentIntersections;
	
	/** 
	 * Initialize a Go board with the given width.
	 * The minimum size is 5 x 5. If the given size is smaller than 5, the size is set to 5.
	 * The maximum size is 19 x 19. If the given size is larger than 19, the size is set to 19.
	 * Initially all intersections are unoccupied.
	 * @param width
	 * 			The width of the board.
	 * @param goGUI
	 * 			Whether a GoGUI should be used.
	 */
	public Board(int size, boolean isGoGUI) {
		this.isGoGUI = isGoGUI;
		this.adjacentIntersections = new ArrayList<Intersection>();
		if (size < 5) {
			intersections = new Intersection[5][5];
			this.size = 5;
		} else if (size > 19) {
			intersections = new Intersection[19][19];
			this.size = 19;
		} else {
			intersections = new Intersection[size][size];
			this.size = size;
		}
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				intersections[i][j] = new Intersection();
			}
		}
		if (isGoGUI) {
			goGUI = new GoGUIIntegrator(false, true, size);
			goGUI.startGUI();
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
		if (isGoGUI) {
			boolean isWhite;
			if (color.equals(StoneColor.WHITE)) {
				isWhite = true;
			} else {
				isWhite = false;
			}
			goGUI.addStone(x, y, isWhite);
		}
		setInitialLiberties(x, y);
		adjustLiberties(x, y);
	}
	
	/**
	 * Adjust the liberties of the newly set stone and the surrounding stones or groups.
	 * @param x
	 * 			The x coordinate of the intersection at the board.
	 * @param y
	 * 			The y coordinate of the intersection at the board.
	 */
	private void adjustLiberties(int x, int y) {
		adjacentIntersections = getAdjacentIntersectionsWithStone(x, y);
		Iterator<Intersection> adjacentIntersectionsIterator = adjacentIntersections.iterator();
		while (adjacentIntersectionsIterator.hasNext()) {
			Intersection adjacentIntersection = adjacentIntersectionsIterator.next();
			adjacentIntersection.getStone().setLiberties(adjacentIntersection.getStone().
					getLiberties() - 1);
			this.getStone(x, y).setLiberties(this.getStone(x, y).getLiberties() - 1);
		}
		if (this.getStone(x, y).getLiberties() == 0) {
			this.removeStone(x, y);
		}
		System.out.println("Size of adjacentStones list: " + adjacentIntersections.size());
		adjacentIntersections.clear();
	}

	/**
	 * Return a list of adjacent intersections to the stone occupied by a stone. 
	 * @param x
	 * 			The x coordinate of the intersection at the board.
	 * @param y
	 * 			The y coordinate of the intersection at the board.
	 * @return
	 * 			A list of adjacent intersections occupied by a stone.
	 */
	private List<Intersection> getAdjacentIntersectionsWithStone(int x, int y) {
		if (this.getStone(x, y).getLiberties() == 2) {
			if (x == 0 && y == 0) {
				adjacentIntersections.add(this.getIntersection(x + 1, y));
				adjacentIntersections.add(this.getIntersection(x, y + 1));
			} else if (x == size - 1 && y == 0) {
				adjacentIntersections.add(this.getIntersection(x - 1, y));
				adjacentIntersections.add(this.getIntersection(x, y + 1));
			} else if (x == 0 && y == size - 1) {
				adjacentIntersections.add(this.getIntersection(x, y - 1));
				adjacentIntersections.add(this.getIntersection(x + 1, y));
			} else if (x == size - 1 && y == size - 1) {
				adjacentIntersections.add(this.getIntersection(x, y - 1));
				adjacentIntersections.add(this.getIntersection(x - 1, y));
			}
		} else if (this.getStone(x, y).getLiberties() == 3) {
			if (x == 0) {
				adjacentIntersections.add(this.getIntersection(x, y - 1));
				adjacentIntersections.add(this.getIntersection(x, y + 1));
				adjacentIntersections.add(this.getIntersection(x + 1, y));
			} else if (x == size - 1) {
				adjacentIntersections.add(this.getIntersection(x, y - 1));
				adjacentIntersections.add(this.getIntersection(x, y + 1));
				adjacentIntersections.add(this.getIntersection(x - 1, y));
			} else if (y == 0) {
				adjacentIntersections.add(this.getIntersection(x - 1, y));
				adjacentIntersections.add(this.getIntersection(x + 1, y));
				adjacentIntersections.add(this.getIntersection(x, y + 1));
			} else if (y == size - 1) {
				adjacentIntersections.add(this.getIntersection(x - 1, y));
				adjacentIntersections.add(this.getIntersection(x + 1, y));
				adjacentIntersections.add(this.getIntersection(x, y - 1));
			}
		} else {
			adjacentIntersections.add(this.getIntersection(x, y - 1));
			adjacentIntersections.add(this.getIntersection(x, y + 1));
			adjacentIntersections.add(this.getIntersection(x - 1, y));
			adjacentIntersections.add(this.getIntersection(x + 1, y));
		}
		Iterator<Intersection> adjacentIntersectionsIterator = adjacentIntersections.iterator();
		while (adjacentIntersectionsIterator.hasNext()) {
			Intersection adjacentIntersection = adjacentIntersectionsIterator.next();
			if (!adjacentIntersection.isOccupied())
			adjacentIntersectionsIterator.remove();
		}
		return adjacentIntersections;
	}

	/**
	 * Set the initial liberties of the stone at the intersection 
	 * at the provided x and y coordinate.
	 * @param x
	 * 			The x coordinate of the stone.
	 * @param y
	 * 			The y coordinate of the stone.
	 */
	private void setInitialLiberties(int x, int y) {
		if (x == 0 || x == size - 1 || y == 0 || y == size - 1) {
			if ((x == 0 || x == size - 1) && (y == 0 || y == size - 1)) {
				this.getStone(x, y).setLiberties(2);
			} else {
				this.getStone(x, y).setLiberties(3);
			}
		}
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
		if (isGoGUI) {
			goGUI.removeStone(x, y);
		}
	}
	
	/**
	 * Get the size of the Go board.
	 * @return
	 * 			The size of the board.
	 */
	public int getSize() {
		return size;
	}

}
