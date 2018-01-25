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
	
	/** List of adjacent intersections occupied by a stone. */
	private List<Intersection> adjacentIntersections;
	
	/** List of intersections occupied by a stone. */
	private List<Intersection> occupiedIntersections;
	
	/** List of intersection groups. */
	private List<IntersectionGroup> intersectionGroups;
	
	/** The color of the most recent placed stone. */
	private StoneColor color;
	
	private List<int[][]> adjacentIntersectionsCoordinates;
	
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
		this.occupiedIntersections = new ArrayList<Intersection>();
		this.intersectionGroups = new ArrayList<IntersectionGroup>();
		this.adjacentIntersectionsCoordinates = new ArrayList<int[][]>();
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
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				intersections[x][y] = new Intersection(new Position(x, y));
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
	public void setStone(int x, int y, StoneColor aColor) {
		this.color = aColor;
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
		updateBoard(x, y);
	}

	/**
	 * Update the liberties of the newly set stone and the surrounding stones or groups.
	 * Update the stones on the board.
	 * @param x
	 * 			The x coordinate of the intersection at the board.
	 * @param y
	 * 			The y coordinate of the intersection at the board.
	 */
	private void updateBoard(int x, int y) {
		int otherColorCount = 0;
		List<StoneColor> stoneColorFirstAdjacentIntersection = new ArrayList<StoneColor>();
		List<StoneColor> stoneColorSecondAdjacentIntersection = new ArrayList<StoneColor>();
		List<StoneColor> stoneColorThirdAdjacentIntersection = new ArrayList<StoneColor>();
		List<StoneColor> stoneColorFourthAdjacentIntersection = new ArrayList<StoneColor>();
		adjacentIntersections = getAdjacentIntersectionsWithStone(x, y);
		Iterator<Intersection> adjacentIntersectionsIterator = adjacentIntersections.iterator();
		while (adjacentIntersectionsIterator.hasNext()) {
			Intersection adjacentIntersection = adjacentIntersectionsIterator.next();
			if (!adjacentIntersection.getStone().getColor().equals(color)) {
				otherColorCount++;
			}
		}		
		// One closed in stone
		// First find a list of adjacent intersections of each of the adjacent intersections of the newly placed stone
		List<Intersection> adjacentIntersectionsOfFirstAdjacent = new ArrayList<Intersection>();
		List<Intersection> adjacentIntersectionsOfSecondAdjacent = new ArrayList<Intersection>();
		List<Intersection> adjacentIntersectionsOfThirdAdjacent = new ArrayList<Intersection>();
		List<Intersection> adjacentIntersectionsOfFourthAdjacent = new ArrayList<Intersection>();
		Intersection firstAdjacentIntersection = new Intersection(new Position(0, 0));
		Intersection secondAdjacentIntersection = new Intersection(new Position(0, 0));
		Intersection thirdAdjacentIntersection = new Intersection(new Position(0, 0));
		Intersection fourthAdjacentIntersection = new Intersection(new Position(0, 0));
		if (x == 0 || x == size - 1 || y == 0 || y == size - 1) {
			if ((x == 0 || x == size - 1) && (y == 0 || y == size - 1)) {
				if (x == 0 && y == 0) {
					firstAdjacentIntersection = this.getIntersection(x + 1, y);
					adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(x + 1, y);
					secondAdjacentIntersection = this.getIntersection(x, y + 1);
					adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(x, y + 1);
				} else if (x == size - 1 && y == 0) {
					firstAdjacentIntersection = this.getIntersection(x - 1, y);
					adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(x - 1, y);
					secondAdjacentIntersection = this.getIntersection(x, y + 1);
					adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(x, y + 1);
				} else if (x == 0 && y == size - 1) {
					firstAdjacentIntersection = this.getIntersection(x, y - 1);
					adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(x, y - 1);
					secondAdjacentIntersection = this.getIntersection(x + 1, y);
					adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(x + 1, y);
				} else if (x == size - 1 && y == size - 1) {
					firstAdjacentIntersection = this.getIntersection(x, y - 1);
					adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(x, y - 1);
					secondAdjacentIntersection = this.getIntersection(x - 1, y);
					adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(x - 1, y);
				}
			} else {
				if (x == 0) {
					firstAdjacentIntersection = this.getIntersection(x, y - 1);
					adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(x, y - 1);
					secondAdjacentIntersection = this.getIntersection(x, y + 1);
					adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(x, y + 1);
					thirdAdjacentIntersection = this.getIntersection(x + 1, y);
					adjacentIntersectionsOfThirdAdjacent = this.getAdjacentIntersectionsWithStone(x + 1, y);
				} else if (x == size - 1) {
					firstAdjacentIntersection = this.getIntersection(x, y - 1);
					adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(x, y - 1);
					secondAdjacentIntersection = this.getIntersection(x, y + 1);
					adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(x, y + 1);
					thirdAdjacentIntersection = this.getIntersection(x - 1, y);
					adjacentIntersectionsOfThirdAdjacent = this.getAdjacentIntersectionsWithStone(x - 1, y);
				} else if (y == 0) {
					firstAdjacentIntersection = this.getIntersection(x - 1, y);
					adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(x - 1, y);
					secondAdjacentIntersection = this.getIntersection(x + 1, y);
					adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(x + 1, y);
					thirdAdjacentIntersection = this.getIntersection(x, y + 1);
					adjacentIntersectionsOfThirdAdjacent = this.getAdjacentIntersectionsWithStone(x, y + 1);
				} else if (y == size - 1) {
					firstAdjacentIntersection = this.getIntersection(x - 1, y);
					adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(x - 1, y);
					secondAdjacentIntersection = this.getIntersection(x + 1, y);
					adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(x + 1, y);
					thirdAdjacentIntersection = this.getIntersection(x, y - 1);
					adjacentIntersectionsOfThirdAdjacent = this.getAdjacentIntersectionsWithStone(x, y - 1);
				}
			}
		} else {
			firstAdjacentIntersection = this.getIntersection(x, y - 1);
			adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(x, y - 1);
			secondAdjacentIntersection = this.getIntersection(x, y + 1);
			adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(x, y + 1);
			thirdAdjacentIntersection = this.getIntersection(x - 1, y);
			adjacentIntersectionsOfThirdAdjacent = this.getAdjacentIntersectionsWithStone(x - 1, y);
			fourthAdjacentIntersection = this.getIntersection(x + 1, y);
			adjacentIntersectionsOfFourthAdjacent = this.getAdjacentIntersectionsWithStone(x + 1, y);
		}
		if (this.getIntersection(x, y).isOccupied()) {
			int newStoneLiberties = 0;
			if (firstAdjacentIntersection.isOccupied()) {
				Iterator<Intersection> adjacentIntersectionsOfFirstAdjacentIterator = adjacentIntersectionsOfFirstAdjacent.iterator();
				while (adjacentIntersectionsOfFirstAdjacentIterator.hasNext()) {
					Intersection adjacentIntersection = adjacentIntersectionsOfFirstAdjacentIterator.next();
					if (!adjacentIntersection.isOccupied()) {
						adjacentIntersectionsOfFirstAdjacentIterator.remove();
					} else {
						stoneColorFirstAdjacentIntersection.add(adjacentIntersection.getStone().getColor());
					}
				}
				firstAdjacentIntersection.getStone().setLiberties(firstAdjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfFirstAdjacent.size());
				newStoneLiberties++;
				
			}
			if (secondAdjacentIntersection.isOccupied()) {
				Iterator<Intersection> adjacentIntersectionsOfSecondAdjacentIterator = adjacentIntersectionsOfSecondAdjacent.iterator();
				while (adjacentIntersectionsOfSecondAdjacentIterator.hasNext()) {
					Intersection adjacentIntersection = adjacentIntersectionsOfSecondAdjacentIterator.next();
					if (!adjacentIntersection.isOccupied()) {
						adjacentIntersectionsOfSecondAdjacentIterator.remove();
					} else {
						stoneColorSecondAdjacentIntersection.add(adjacentIntersection.getStone().getColor());
					}
				}
				secondAdjacentIntersection.getStone().setLiberties(secondAdjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfSecondAdjacent.size());
				newStoneLiberties++;
			}
			if (thirdAdjacentIntersection.isOccupied()) {
				Iterator<Intersection> adjacentIntersectionsOfThirdAdjacentIterator = adjacentIntersectionsOfThirdAdjacent.iterator();
				while (adjacentIntersectionsOfThirdAdjacentIterator.hasNext()) {
					Intersection adjacentIntersection = adjacentIntersectionsOfThirdAdjacentIterator.next();
					if (!adjacentIntersection.isOccupied()) {
						adjacentIntersectionsOfThirdAdjacentIterator.remove();
					} else {
						stoneColorThirdAdjacentIntersection.add(adjacentIntersection.getStone().getColor());
					}
				}
				thirdAdjacentIntersection.getStone().setLiberties(thirdAdjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfThirdAdjacent.size());
				newStoneLiberties++;
			}
			if (fourthAdjacentIntersection.isOccupied()) {
				Iterator<Intersection> adjacentIntersectionsOfFourthAdjacentIterator = adjacentIntersectionsOfFourthAdjacent.iterator();
				while (adjacentIntersectionsOfFourthAdjacentIterator.hasNext()) {
					Intersection adjacentIntersection = adjacentIntersectionsOfFourthAdjacentIterator.next();
					if (!adjacentIntersection.isOccupied()) {
						adjacentIntersectionsOfFourthAdjacentIterator.remove();
					} else {
						stoneColorFourthAdjacentIntersection.add(adjacentIntersection.getStone().getColor());
					}
				}
				fourthAdjacentIntersection.getStone().setLiberties(fourthAdjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfFourthAdjacent.size());
				newStoneLiberties++;
			}
			this.getStone(x, y).setLiberties(this.getStone(x, y).getLiberties() - newStoneLiberties);
			// Suicide
			if (otherColorCount == adjacentIntersections.size() && 
					this.getStone(x, y).getLiberties() == 0) {
				removeStone(x, y);
			}
		} else {
			if (firstAdjacentIntersection.isOccupied()) {
				Iterator<Intersection> adjacentIntersectionsOfFirstAdjacentIterator = adjacentIntersectionsOfFirstAdjacent.iterator();
				while (adjacentIntersectionsOfFirstAdjacentIterator.hasNext()) {
					Intersection adjacentIntersection = adjacentIntersectionsOfFirstAdjacentIterator.next();
					if (!adjacentIntersection.isOccupied()) {
						adjacentIntersectionsOfFirstAdjacentIterator.remove();
					}
				}
				if (adjacentIntersectionsOfFirstAdjacent.isEmpty()) {
					firstAdjacentIntersection.getStone().setLiberties(firstAdjacentIntersection.getStone().getInitialLiberties());
				} else {
					firstAdjacentIntersection.getStone().setLiberties(firstAdjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfFirstAdjacent.size() + 1);
				}
			}
			if (secondAdjacentIntersection.isOccupied()) {
				Iterator<Intersection> adjacentIntersectionsOfSecondAdjacentIterator = adjacentIntersectionsOfSecondAdjacent.iterator();
				while (adjacentIntersectionsOfSecondAdjacentIterator.hasNext()) {
					Intersection adjacentIntersection = adjacentIntersectionsOfSecondAdjacentIterator.next();
					if (!adjacentIntersection.isOccupied()) {
						adjacentIntersectionsOfSecondAdjacentIterator.remove();
					}
				}
				if (adjacentIntersectionsOfSecondAdjacent.isEmpty()) {
					secondAdjacentIntersection.getStone().setLiberties(secondAdjacentIntersection.getStone().getInitialLiberties());
				} else {
					secondAdjacentIntersection.getStone().setLiberties(secondAdjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfSecondAdjacent.size() + 1);
				}
			}
			if (thirdAdjacentIntersection.isOccupied()) {
				Iterator<Intersection> adjacentIntersectionsOfThirdAdjacentIterator = adjacentIntersectionsOfThirdAdjacent.iterator();
				while (adjacentIntersectionsOfThirdAdjacentIterator.hasNext()) {
					Intersection adjacentIntersection = adjacentIntersectionsOfThirdAdjacentIterator.next();
					if (!adjacentIntersection.isOccupied()) {
						adjacentIntersectionsOfThirdAdjacentIterator.remove();
					}
				}
				if (adjacentIntersectionsOfThirdAdjacent.isEmpty()) {
					thirdAdjacentIntersection.getStone().setLiberties(thirdAdjacentIntersection.getStone().getInitialLiberties());
				} else {
					thirdAdjacentIntersection.getStone().setLiberties(thirdAdjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfThirdAdjacent.size() + 1);
				}
			}
			if (fourthAdjacentIntersection.isOccupied()) {
				Iterator<Intersection> adjacentIntersectionsOfFourthAdjacentIterator = adjacentIntersectionsOfFourthAdjacent.iterator();
				while (adjacentIntersectionsOfFourthAdjacentIterator.hasNext()) {
					Intersection adjacentIntersection = adjacentIntersectionsOfFourthAdjacentIterator.next();
					if (!adjacentIntersection.isOccupied()) {
						adjacentIntersectionsOfFourthAdjacentIterator.remove();
					}
				}
				if (adjacentIntersectionsOfFourthAdjacent.isEmpty()) {
					fourthAdjacentIntersection.getStone().setLiberties(fourthAdjacentIntersection.getStone().getInitialLiberties());
				} else {
					fourthAdjacentIntersection.getStone().setLiberties(fourthAdjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfFourthAdjacent.size() + 1);
				}				
			}
		}
//		int notEqualColorCount = 0;
		for (int xx = 0; xx < size; xx++) {
			for (int yy = 0; yy < size; yy++) {
				if (this.getIntersection(xx, yy).isOccupied()) {
					System.out.println("Stone at " + xx + yy + this.getIntersection(xx, yy).getStone().getLiberties());
					if (this.getIntersection(xx, yy).getStone().getLiberties() == 0) {
						int notEqualColorCount = 0;
						List<Intersection> list = getAdjacentIntersectionsWithStone(xx, yy);
						Iterator<Intersection> listIterator = list.iterator();
						while (listIterator.hasNext()) {
							Intersection adjacentIntersection = listIterator.next();
							if (!adjacentIntersection.getStone().getColor().equals(this.getStone(xx, yy).getColor())) {
								notEqualColorCount++;
							}
						}
						if (notEqualColorCount == list.size() && !list.isEmpty())
							removeStone(xx, yy);
					}
				}
			}
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
		List<Intersection> adjacentIntersectionsList = getAdjacentIntersections(this.getIntersection(x, y));
		Iterator<Intersection> adjacentIntersectionsIterator = adjacentIntersectionsList.iterator();
		while (adjacentIntersectionsIterator.hasNext()) {
			Intersection adjacentIntersection = adjacentIntersectionsIterator.next();
			if (!adjacentIntersection.isOccupied()) {
				adjacentIntersectionsIterator.remove();
			}
		}
		return adjacentIntersectionsList;
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
				this.getStone(x, y).setInitialLiberties(2);
			} else {
				this.getStone(x, y).setLiberties(3);
				this.getStone(x, y).setInitialLiberties(3);
			}
		} else {
			this.getStone(x, y).setInitialLiberties(4);
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
		updateBoard(x, y);
	}
	
	/**
	 * Get the size of the Go board.
	 * @return
	 * 			The size of the board.
	 */
	public int getSize() {
		return size;
	}
	
	private List<Intersection> getAdjacentIntersections(Intersection intersection) {
		List<Intersection> adjacentIntersections = new ArrayList<Intersection>();
		List<Position> adjacentPositions = getAdjacentPositions(intersection.getPosition());
		Iterator<Position> adjacentPositionsIterator = adjacentPositions.iterator();
		while (adjacentPositionsIterator.hasNext()) {
			Position adjacentPosition = adjacentPositionsIterator.next();
			adjacentIntersections.add(intersections[adjacentPosition.getX()][adjacentPosition.getY()]);
		}
		return adjacentIntersections;
	}

	private List<Position> getAdjacentPositions(Position position) {
		List<Position> adjacentPositions = new ArrayList<Position>();
		adjacentPositions = addPositionIfValid(new Position(position.getX(), position.getY() + 1), adjacentPositions);
		adjacentPositions = addPositionIfValid(new Position(position.getX(), position.getY() - 1), adjacentPositions);
		adjacentPositions = addPositionIfValid(new Position(position.getX() + 1, position.getY()), adjacentPositions);
		adjacentPositions = addPositionIfValid(new Position(position.getX() - 1, position.getY()), adjacentPositions);
		return adjacentPositions;
	}
	
	private List<Position> addPositionIfValid(Position position, List<Position> positionList) {
		if (position.getX() >= 0 && position.getX() < size && position.getY() >= 0 && position.getY() < size) {
			positionList.add(position);
		}
		return positionList;
	}
}
