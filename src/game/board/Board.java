package game.board;

import java.util.ArrayList;
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
//	
//	/** List of adjacent intersections occupied by a stone. */
//	private List<Intersection> adjacentIntersections;
	
	/** List of intersections occupied by a stone. */
	private List<Intersection> occupiedIntersections;
	
	/** List of intersection groups. */
	private List<IntersectionGroup> intersectionGroups;
	
	/** The color of the most recent placed stone. */
	private StoneColor color;
	
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
		this.occupiedIntersections = new ArrayList<Intersection>();
		this.intersectionGroups = new ArrayList<IntersectionGroup>();
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
	 * Return the intersection at the provided position.
	 * @param position
	 * 			The position.
	 * @return
	 * 			The intersection.
	 */
	public Intersection getIntersection(Position position) {
		return intersections[position.getX()][position.getY()];
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
		this.getIntersection(new Position(x, y)).setStone(color);
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
//		int otherColorCount = 0;
		List<StoneColor> stoneColorFirstAdjacentIntersection = new ArrayList<StoneColor>();
		List<StoneColor> stoneColorSecondAdjacentIntersection = new ArrayList<StoneColor>();
		List<StoneColor> stoneColorThirdAdjacentIntersection = new ArrayList<StoneColor>();
		List<StoneColor> stoneColorFourthAdjacentIntersection = new ArrayList<StoneColor>();
		List<Intersection> adjacentIntersections = getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x, y)));
		adjustLibertiesOfAdjacentIntersectionsWithStone(adjacentIntersections, this.getIntersection(new Position(x, y)));
		checkForSuicideMove(adjacentIntersections, getIntersection(new Position(x, y)));
		// One closed in stone
		// First find a list of adjacent intersections of each of the adjacent intersections of the newly placed stone
//		List<Intersection> adjacentIntersectionsOfFirstAdjacent = new ArrayList<Intersection>();
//		List<Intersection> adjacentIntersectionsOfSecondAdjacent = new ArrayList<Intersection>();
//		List<Intersection> adjacentIntersectionsOfThirdAdjacent = new ArrayList<Intersection>();
//		List<Intersection> adjacentIntersectionsOfFourthAdjacent = new ArrayList<Intersection>();
//		Intersection firstAdjacentIntersection = new Intersection(new Position(0, 0));
//		Intersection secondAdjacentIntersection = new Intersection(new Position(0, 0));
//		Intersection thirdAdjacentIntersection = new Intersection(new Position(0, 0));
//		Intersection fourthAdjacentIntersection = new Intersection(new Position(0, 0));
//		if (x == 0 || x == size - 1 || y == 0 || y == size - 1) {
//			if ((x == 0 || x == size - 1) && (y == 0 || y == size - 1)) {
//				if (x == 0 && y == 0) {
//					firstAdjacentIntersection = this.getIntersection(new Position(x + 1, y));
//					adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x + 1, y)));
//					secondAdjacentIntersection = this.getIntersection(new Position(x, y + 1));
//					adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x, y + 1)));
//				} else if (x == size - 1 && y == 0) {
//					firstAdjacentIntersection = this.getIntersection(new Position(x - 1, y));
//					adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x - 1, y)));
//					secondAdjacentIntersection = this.getIntersection(new Position(x, y + 1));
//					adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x, y + 1)));
//				} else if (x == 0 && y == size - 1) {
//					firstAdjacentIntersection = this.getIntersection(new Position(x, y - 1));
//					adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x, y - 1)));
//					secondAdjacentIntersection = this.getIntersection(new Position(x + 1, y));
//					adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x + 1, y)));
//				} else if (x == size - 1 && y == size - 1) {
//					firstAdjacentIntersection = this.getIntersection(new Position(x, y - 1));
//					adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x, y - 1)));
//					secondAdjacentIntersection = this.getIntersection(new Position(x - 1, y));
//					adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x - 1, y)));
//				}
//			} else {
//				if (x == 0) {
//					firstAdjacentIntersection = this.getIntersection(new Position(x, y - 1));
//					adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x, y - 1)));
//					secondAdjacentIntersection = this.getIntersection(new Position(x, y + 1));
//					adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x, y + 1)));
//					thirdAdjacentIntersection = this.getIntersection(new Position(x + 1, y));
//					adjacentIntersectionsOfThirdAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x + 1, y)));
//				} else if (x == size - 1) {
//					firstAdjacentIntersection = this.getIntersection(new Position(x, y - 1));
//					adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x, y - 1)));
//					secondAdjacentIntersection = this.getIntersection(new Position(x, y + 1));
//					adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x, y + 1)));
//					thirdAdjacentIntersection = this.getIntersection(new Position(x - 1, y));
//					adjacentIntersectionsOfThirdAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x - 1, y)));
//				} else if (y == 0) {
//					firstAdjacentIntersection = this.getIntersection(new Position(x - 1, y));
//					adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x - 1, y)));
//					secondAdjacentIntersection = this.getIntersection(new Position(x + 1, y));
//					adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x + 1, y)));
//					thirdAdjacentIntersection = this.getIntersection(new Position(x, y + 1));
//					adjacentIntersectionsOfThirdAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x, y + 1)));
//				} else if (y == size - 1) {
//					firstAdjacentIntersection = this.getIntersection(new Position(x - 1, y));
//					adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x - 1, y)));
//					secondAdjacentIntersection = this.getIntersection(new Position(x + 1, y));
//					adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x + 1, y)));
//					thirdAdjacentIntersection = this.getIntersection(new Position(x, y - 1));
//					adjacentIntersectionsOfThirdAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x, y - 1)));
//				}
//			}
//		} else {
//			firstAdjacentIntersection = this.getIntersection(new Position(x, y - 1));
//			adjacentIntersectionsOfFirstAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x, y - 1)));
//			secondAdjacentIntersection = this.getIntersection(new Position(x, y + 1));
//			adjacentIntersectionsOfSecondAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x, y + 1)));
//			thirdAdjacentIntersection = this.getIntersection(new Position(x - 1, y));
//			adjacentIntersectionsOfThirdAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x - 1, y)));
//			fourthAdjacentIntersection = this.getIntersection(new Position(x + 1, y));
//			adjacentIntersectionsOfFourthAdjacent = this.getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x + 1, y)));
//		}
		// If in this run of update a new stone is placed
//		if (this.getIntersection(new Position(x, y)).isOccupied()) {
//			int newStoneLiberties = 0;
//			if (firstAdjacentIntersection.isOccupied()) {
//				Iterator<Intersection> adjacentIntersectionsOfFirstAdjacentIterator = adjacentIntersectionsOfFirstAdjacent.iterator();
//				while (adjacentIntersectionsOfFirstAdjacentIterator.hasNext()) {
//					Intersection adjacentIntersection = adjacentIntersectionsOfFirstAdjacentIterator.next();
//					if (!adjacentIntersection.isOccupied()) {
//						adjacentIntersectionsOfFirstAdjacentIterator.remove();
//					} else {
//						stoneColorFirstAdjacentIntersection.add(adjacentIntersection.getStone().getColor());
//					}
//				}
//				firstAdjacentIntersection.getStone().setLiberties(firstAdjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfFirstAdjacent.size());
//				newStoneLiberties++;
//				
//			}
//			if (secondAdjacentIntersection.isOccupied()) {
//				Iterator<Intersection> adjacentIntersectionsOfSecondAdjacentIterator = adjacentIntersectionsOfSecondAdjacent.iterator();
//				while (adjacentIntersectionsOfSecondAdjacentIterator.hasNext()) {
//					Intersection adjacentIntersection = adjacentIntersectionsOfSecondAdjacentIterator.next();
//					if (!adjacentIntersection.isOccupied()) {
//						adjacentIntersectionsOfSecondAdjacentIterator.remove();
//					} else {
//						stoneColorSecondAdjacentIntersection.add(adjacentIntersection.getStone().getColor());
//					}
//				}
//				secondAdjacentIntersection.getStone().setLiberties(secondAdjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfSecondAdjacent.size());
//				newStoneLiberties++;
//			}
//			if (thirdAdjacentIntersection.isOccupied()) {
//				Iterator<Intersection> adjacentIntersectionsOfThirdAdjacentIterator = adjacentIntersectionsOfThirdAdjacent.iterator();
//				while (adjacentIntersectionsOfThirdAdjacentIterator.hasNext()) {
//					Intersection adjacentIntersection = adjacentIntersectionsOfThirdAdjacentIterator.next();
//					if (!adjacentIntersection.isOccupied()) {
//						adjacentIntersectionsOfThirdAdjacentIterator.remove();
//					} else {
//						stoneColorThirdAdjacentIntersection.add(adjacentIntersection.getStone().getColor());
//					}
//				}
//				thirdAdjacentIntersection.getStone().setLiberties(thirdAdjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfThirdAdjacent.size());
//				newStoneLiberties++;
//			}
//			if (fourthAdjacentIntersection.isOccupied()) {
//				Iterator<Intersection> adjacentIntersectionsOfFourthAdjacentIterator = adjacentIntersectionsOfFourthAdjacent.iterator();
//				while (adjacentIntersectionsOfFourthAdjacentIterator.hasNext()) {
//					Intersection adjacentIntersection = adjacentIntersectionsOfFourthAdjacentIterator.next();
//					if (!adjacentIntersection.isOccupied()) {
//						adjacentIntersectionsOfFourthAdjacentIterator.remove();
//					} else {
//						stoneColorFourthAdjacentIntersection.add(adjacentIntersection.getStone().getColor());
//					}
//				}
//				fourthAdjacentIntersection.getStone().setLiberties(fourthAdjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfFourthAdjacent.size());
//				newStoneLiberties++;
//			}
//			this.getIntersection(new Position(x, y)).getStone().setLiberties(this.getIntersection(new Position(x, y)).getStone().getLiberties() - newStoneLiberties);
//		} else {
//			if (firstAdjacentIntersection.isOccupied()) {
//				Iterator<Intersection> adjacentIntersectionsOfFirstAdjacentIterator = adjacentIntersectionsOfFirstAdjacent.iterator();
//				while (adjacentIntersectionsOfFirstAdjacentIterator.hasNext()) {
//					Intersection adjacentIntersection = adjacentIntersectionsOfFirstAdjacentIterator.next();
//					if (!adjacentIntersection.isOccupied()) {
//						adjacentIntersectionsOfFirstAdjacentIterator.remove();
//					}
//				}
//				if (adjacentIntersectionsOfFirstAdjacent.isEmpty()) {
//					firstAdjacentIntersection.getStone().setLiberties(firstAdjacentIntersection.getStone().getInitialLiberties());
//				} else {
//					firstAdjacentIntersection.getStone().setLiberties(firstAdjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfFirstAdjacent.size() + 1);
//				}
//			}
//			if (secondAdjacentIntersection.isOccupied()) {
//				Iterator<Intersection> adjacentIntersectionsOfSecondAdjacentIterator = adjacentIntersectionsOfSecondAdjacent.iterator();
//				while (adjacentIntersectionsOfSecondAdjacentIterator.hasNext()) {
//					Intersection adjacentIntersection = adjacentIntersectionsOfSecondAdjacentIterator.next();
//					if (!adjacentIntersection.isOccupied()) {
//						adjacentIntersectionsOfSecondAdjacentIterator.remove();
//					}
//				}
//				if (adjacentIntersectionsOfSecondAdjacent.isEmpty()) {
//					secondAdjacentIntersection.getStone().setLiberties(secondAdjacentIntersection.getStone().getInitialLiberties());
//				} else {
//					secondAdjacentIntersection.getStone().setLiberties(secondAdjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfSecondAdjacent.size() + 1);
//				}
//			}
//			if (thirdAdjacentIntersection.isOccupied()) {
//				Iterator<Intersection> adjacentIntersectionsOfThirdAdjacentIterator = adjacentIntersectionsOfThirdAdjacent.iterator();
//				while (adjacentIntersectionsOfThirdAdjacentIterator.hasNext()) {
//					Intersection adjacentIntersection = adjacentIntersectionsOfThirdAdjacentIterator.next();
//					if (!adjacentIntersection.isOccupied()) {
//						adjacentIntersectionsOfThirdAdjacentIterator.remove();
//					}
//				}
//				if (adjacentIntersectionsOfThirdAdjacent.isEmpty()) {
//					thirdAdjacentIntersection.getStone().setLiberties(thirdAdjacentIntersection.getStone().getInitialLiberties());
//				} else {
//					thirdAdjacentIntersection.getStone().setLiberties(thirdAdjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfThirdAdjacent.size() + 1);
//				}
//			}
//			if (fourthAdjacentIntersection.isOccupied()) {
//				Iterator<Intersection> adjacentIntersectionsOfFourthAdjacentIterator = adjacentIntersectionsOfFourthAdjacent.iterator();
//				while (adjacentIntersectionsOfFourthAdjacentIterator.hasNext()) {
//					Intersection adjacentIntersection = adjacentIntersectionsOfFourthAdjacentIterator.next();
//					if (!adjacentIntersection.isOccupied()) {
//						adjacentIntersectionsOfFourthAdjacentIterator.remove();
//					}
//				}
//				if (adjacentIntersectionsOfFourthAdjacent.isEmpty()) {
//					fourthAdjacentIntersection.getStone().setLiberties(fourthAdjacentIntersection.getStone().getInitialLiberties());
//				} else {
//					fourthAdjacentIntersection.getStone().setLiberties(fourthAdjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfFourthAdjacent.size() + 1);
//				}				
//			}
//		}
		removesStonesWithZeroLiberties();
		System.out.println("Size of adjacentStones list: " + adjacentIntersections.size());
	}

	/**
	 * Adjust the liberties of the surrounding stones of the altered intersection.
	 * @param adjacentIntersections
	 * 			The adjacent intersections.
	 * @param intersection
	 * 			The altered intersection.
	 */
	private void adjustLibertiesOfAdjacentIntersectionsWithStone(List<Intersection> adjacentIntersections, Intersection intersection) {
		Iterator<Intersection> adjacentIntersectionsIterator = adjacentIntersections.iterator();
		while (adjacentIntersectionsIterator.hasNext()) {
			Intersection adjacentIntersection = adjacentIntersectionsIterator.next();
			List<Intersection> adjacentIntersectionsOfAdjacentIntersection = getAdjacentIntersectionsWithStone(adjacentIntersection);
			if (intersection.isOccupied()) {
				int setStoneLiberties = 0;
				List<StoneColor> stoneColorAdjacentIntersectionOfAdjacentIntersection = new ArrayList<StoneColor>();
				Iterator<Intersection> adjacentIntersectionsOfAdjacentIntersectionIterator = adjacentIntersectionsOfAdjacentIntersection.iterator();
				while (adjacentIntersectionsOfAdjacentIntersectionIterator.hasNext()) {
					Intersection adjacentIntersectionOfAdjacentIntersection = adjacentIntersectionsOfAdjacentIntersectionIterator.next();
					stoneColorAdjacentIntersectionOfAdjacentIntersection.add(adjacentIntersectionOfAdjacentIntersection.getStone().getColor());
					adjacentIntersectionOfAdjacentIntersection.getStone().setLiberties(adjacentIntersectionOfAdjacentIntersection.getStone().getInitialLiberties() - adjacentIntersections.size());
					setStoneLiberties++;
				}
				adjacentIntersection.getStone().setLiberties(adjacentIntersection.getStone().getInitialLiberties() - setStoneLiberties);
			} else {
//				Iterator<Intersection> adjacentIntersectionsOfAdjacentIntersectionIterator = adjacentIntersectionsOfAdjacentIntersection.iterator();
//				while (adjacentIntersectionsOfAdjacentIntersectionIterator.hasNext()) {
//					Intersection adjacentIntersectionOfAdjacentIntersection = adjacentIntersectionsOfAdjacentIntersectionIterator.next();
//				
				if (adjacentIntersectionsOfAdjacentIntersection.isEmpty()) {
					adjacentIntersection.getStone().setLiberties(adjacentIntersection.getStone().getInitialLiberties());
				} else {
					adjacentIntersection.getStone().setLiberties(adjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfAdjacentIntersection.size() + 1);
				}
			}
		}
	}

	/**
	 * Check for suicide move of placed stone at provided intersection.
	 * @param adjacentIntersections
	 * 			The adjacent intersections of the placed stone.
	 * @param intersection
	 * 			The intersection at which the stone is placed.
	 */
	private void checkForSuicideMove(List<Intersection> adjacentIntersections, Intersection intersection) {
		int otherColorCount = 0;
		Iterator<Intersection> adjacentIntersectionsIterator = adjacentIntersections.iterator();
		while (adjacentIntersectionsIterator.hasNext()) {
			Intersection adjacentIntersection = adjacentIntersectionsIterator.next();
			if (!adjacentIntersection.getStone().getColor().equals(color)) {
				otherColorCount++;
			}
		}
		if (otherColorCount == adjacentIntersections.size() && 
				intersection.isOccupied() &&
				intersection.getStone().getLiberties() == 0) {
			removeStone(intersection.getPosition());
		}
	}

	/** 
	 * Loop through the board to remove stones with zero liberties.
	 */
	private void removesStonesWithZeroLiberties() {
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				Position position = new Position(x, y);
				if (this.getIntersection(position).isOccupied()) {
					System.out.println("Stone at " + x + y + 
					this.getIntersection(position).getStone().getLiberties());
					if (this.getIntersection(position).getStone().getLiberties() == 0) {
						int notEqualColorCount = 0;
						List<Intersection> adjacentIntersectionsWithStone = 
								getAdjacentIntersectionsWithStone(this.getIntersection(position));
						Iterator<Intersection> adjacentIntersectionsWithStoneIterator = 
								adjacentIntersectionsWithStone.iterator();
						while (adjacentIntersectionsWithStoneIterator.hasNext()) {
							Intersection adjacentIntersectionWithStone = 
									adjacentIntersectionsWithStoneIterator.next();
							if (!adjacentIntersectionWithStone.getStone().getColor().equals(
									this.getIntersection(position).getStone().getColor())) {
								notEqualColorCount++;
							}
						}
						if (notEqualColorCount == adjacentIntersectionsWithStone.size() && 
								!adjacentIntersectionsWithStone.isEmpty()) {
							removeStone(position);
						}
					}
				}
			}
		}
	}

	/**
	 * Return a list of adjacent intersections to the stone occupied by a stone. 
	 * @param intersection
	 * 			The intersection.
	 * @return
	 * 			A list of adjacent intersections occupied by a stone.
	 */
	private List<Intersection> getAdjacentIntersectionsWithStone(Intersection intersection) {
		List<Intersection> adjacentIntersectionsList = getAdjacentIntersections(intersection);
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
				this.getIntersection(new Position(x, y)).getStone().setLiberties(2);
				this.getIntersection(new Position(x, y)).getStone().setInitialLiberties(2);
			} else {
				this.getIntersection(new Position(x, y)).getStone().setLiberties(3);
				this.getIntersection(new Position(x, y)).getStone().setInitialLiberties(3);
			}
		} else {
			this.getIntersection(new Position(x, y)).getStone().setInitialLiberties(4);
		}
	}
	
	/**
	 * Remove the stone at the intersection at the provided position.
	 * @param position
	 * 			The position.
	 */
	public void removeStone(Position position) {
		this.getIntersection(position).removeStone();
		if (isGoGUI) {
			goGUI.removeStone(position.getX(), position.getY());
		}
		updateBoard(position.getX(), position.getY());
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
