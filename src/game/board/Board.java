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
		List<Intersection> adjacentIntersections = getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x, y)));
		adjustLibertiesOfAdjacentIntersectionsWithStone(adjacentIntersections, this.getIntersection(new Position(x, y)));
		addSetStoneToGroup(adjacentIntersections, new Position(x, y));
		checkForSuicideMove(adjacentIntersections, getIntersection(new Position(x, y)));
		removesStonesWithZeroLiberties();
		System.out.println("Size of adjacentStones list: " + adjacentIntersections.size());
	}

	/**
	 * Add the set stone to a group or create a new group or do not add to a group.
	 * @param adjacentIntersections
	 * 			The adjacent intersections 
	 * @param position
	 * 			The position of the intersection.
	 */
	private void addSetStoneToGroup(List<Intersection> adjacentIntersections, Position position) {
		if (this.getIntersection(position).isOccupied()) {
			for (Intersection adjacentIntersection : adjacentIntersections) {
				if (adjacentIntersection.getStone().getColor().equals(this.getIntersection(position).getStone().getColor())) {
					if (!intersectionGroups.isEmpty()) {
						Iterator<IntersectionGroup> intersectionGroupsIterator = intersectionGroups.iterator();
						List<IntersectionGroup> tempIntersectionGroups = new ArrayList<IntersectionGroup>();
						List<IntersectionGroup> tempNewIntersectionGroups = new ArrayList<IntersectionGroup>();
						while (intersectionGroupsIterator.hasNext()) {
							IntersectionGroup intersectionGroup = intersectionGroupsIterator.next();
							if (intersectionGroup.getIntersections().get(0).getStone().getColor().equals(adjacentIntersection.getStone().getColor())) {
								if (intersectionGroup.getIntersections().contains(adjacentIntersection)) {
									tempIntersectionGroups.add(intersectionGroup);
								} else {
									IntersectionGroup newIntersectionGroup = new IntersectionGroup();
									newIntersectionGroup.addIntersection(this.getIntersection(position));
									newIntersectionGroup.addIntersection(adjacentIntersection);
									tempNewIntersectionGroups.add(newIntersectionGroup);
								}
							} else {
								IntersectionGroup newIntersectionGroup = new IntersectionGroup();
								newIntersectionGroup.addIntersection(this.getIntersection(position));
								newIntersectionGroup.addIntersection(adjacentIntersection);
								tempNewIntersectionGroups.add(newIntersectionGroup);
							}
						}
						for (IntersectionGroup intersectionGroup : tempIntersectionGroups) {
							intersectionGroup.addIntersection(this.getIntersection(position));
						}
						for (IntersectionGroup newIntersectionGroup : tempNewIntersectionGroups) {
							intersectionGroups.add(newIntersectionGroup);
						}
					} else {
						IntersectionGroup newIntersectionGroup = new IntersectionGroup();
						newIntersectionGroup.addIntersection(this.getIntersection(position));
						newIntersectionGroup.addIntersection(adjacentIntersection);
						intersectionGroups.add(newIntersectionGroup);
					}
				}
				List<IntersectionGroup> intersectionGroupsContainingSetStone = new ArrayList<IntersectionGroup>();
				Iterator<IntersectionGroup> intersectionGroupsIterator = intersectionGroups.iterator();
				while (intersectionGroupsIterator.hasNext()) {
					IntersectionGroup intersectionGroup = intersectionGroupsIterator.next();
					if (intersectionGroup.getIntersections().contains(this.getIntersection(position))) {
						intersectionGroupsContainingSetStone.add(intersectionGroup);
					}
				}
				if (intersectionGroupsContainingSetStone.size() > 1) {
					IntersectionGroup resultingIntersectionGroup = intersectionGroupsContainingSetStone.get(0);
					for (int i = 1; i < intersectionGroupsContainingSetStone.size(); i++) {
						List<Intersection> intersectionsInGroup = intersectionGroupsContainingSetStone.get(i).getIntersections();
						for (int j = 0; j < intersectionsInGroup.size(); j++) {
							resultingIntersectionGroup.addIntersection(intersectionsInGroup.get(j));
						}
						intersectionGroups.remove(intersectionGroupsContainingSetStone.get(i));
					}
				}
			}
		} 
//		updateGroupLiberties();
	}

//	/** 
//	 * Update the liberties of the groups.
//	 */
//	private void updateGroupLiberties() {
//		for (IntersectionGroup intersectionGroup : intersectionGroups) {
//			intersectionGroup.setLiberties();
//		}
//	}

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
					adjacentIntersectionOfAdjacentIntersection.getStone().setLiberties(adjacentIntersectionOfAdjacentIntersection.getStone().getInitialLiberties() - getAdjacentIntersectionsWithStone(adjacentIntersectionOfAdjacentIntersection).size());
					setStoneLiberties++;
				}
				adjacentIntersection.getStone().setLiberties(adjacentIntersection.getStone().getInitialLiberties() - setStoneLiberties);
			} else {
				if (adjacentIntersectionsOfAdjacentIntersection.isEmpty()) {
					adjacentIntersection.getStone().setLiberties(adjacentIntersection.getStone().getInitialLiberties());
				} else {
					adjacentIntersection.getStone().setLiberties(adjacentIntersection.getStone().getInitialLiberties() - adjacentIntersectionsOfAdjacentIntersection.size());
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
			List<Intersection> adjacentIntersectionsOfAdjacentIntersection = new ArrayList<Intersection>();
			adjacentIntersectionsOfAdjacentIntersection = getAdjacentIntersectionsWithStone(adjacentIntersection);
			Iterator<Intersection> adjacentIntersectionsOfAdjacentIntersectionIterator = adjacentIntersectionsOfAdjacentIntersection.iterator();
			int otherColorCountAdjacent = 0;
			while (adjacentIntersectionsOfAdjacentIntersectionIterator.hasNext()) {
				Intersection adjacentIntersectionOfAdjacentIntersection = adjacentIntersectionsOfAdjacentIntersectionIterator.next();
				if (!adjacentIntersectionOfAdjacentIntersection.getStone().getColor().equals(adjacentIntersection.getStone().getColor())) {
					otherColorCountAdjacent++;
				}
			}
			if (!adjacentIntersection.getStone().getColor().equals(color)) {
				otherColorCount++;
			}
			if (otherColorCountAdjacent == adjacentIntersectionsOfAdjacentIntersection.size() && adjacentIntersection.getStone().getLiberties() == 0) {
				removeStone(adjacentIntersection.getPosition());
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
//							int notInGroupCount = 0;
//							IntersectionGroup intersectionGroupPossiblyRemoved = new IntersectionGroup();
//							for (IntersectionGroup intersectionGroup : intersectionGroups) {
//								if (!intersectionGroup.getIntersections().contains(this.getIntersection(position))) {
//									notInGroupCount++;
//								} else {
//									intersectionGroupPossiblyRemoved = intersectionGroup;
//								}
//							}
//							if (notInGroupCount == intersectionGroups.size()) {
								removeStone(position);
//							} else {
						} else {
//							int notInGroupCount = 0;
							IntersectionGroup intersectionGroupPossiblyRemoved = new IntersectionGroup();
							for (IntersectionGroup intersectionGroup : intersectionGroups) {
								if (intersectionGroup.getIntersections().contains(this.getIntersection(position))) {
									intersectionGroupPossiblyRemoved = intersectionGroup;
								}
							}
							int zeroLibertiesCount = 0;
							List<Intersection> intersectionsPossiblyRemoved = intersectionGroupPossiblyRemoved.getIntersections();
							if (!intersectionsPossiblyRemoved.isEmpty()) {
								for (Intersection intersectionPossiblyRemoved : intersectionsPossiblyRemoved) {
									if (intersectionPossiblyRemoved.getStone().getLiberties() == 0) {
										zeroLibertiesCount++;
									}
								}
								if (zeroLibertiesCount == intersectionsPossiblyRemoved.size()) {
									removeStones(intersectionsPossiblyRemoved);
								}
							}
						}
					}
				}
			}
		}
	}

	/** 
	 * Remove a group of stones.
	 * @param intersections
	 * 			The list of intersections removed.
	 */
	private void removeStones(List<Intersection> intersectionsRemoved) {
		for (Intersection intersection : intersectionsRemoved) {
			if (isGoGUI) {
				goGUI.removeStone(intersection.getPosition().getX(), intersection.getPosition().getY());
				goGUI.removeStone(intersection.getPosition().getX(), intersection.getPosition().getY());
			}
			intersection.removeStone();
		}
		for (Intersection intersection: intersectionsRemoved) {
			updateBoard(intersection.getPosition().getX(), intersection.getPosition().getY());
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
	
	/**
	 * Return a list of adjacent intersections.
	 * @param intersection
	 * 			The intersection.
	 * @return
	 * 			The adjacent intersections.
	 */
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

	/**
	 * Return a list of adjacent positions.
	 * @param position
	 * 			The position.
	 * @return
	 * 			The adjacent positions.
	 */
	private List<Position> getAdjacentPositions(Position position) {
		List<Position> adjacentPositions = new ArrayList<Position>();
		adjacentPositions = addPositionIfValid(new Position(position.getX(), position.getY() + 1), adjacentPositions);
		adjacentPositions = addPositionIfValid(new Position(position.getX(), position.getY() - 1), adjacentPositions);
		adjacentPositions = addPositionIfValid(new Position(position.getX() + 1, position.getY()), adjacentPositions);
		adjacentPositions = addPositionIfValid(new Position(position.getX() - 1, position.getY()), adjacentPositions);
		return adjacentPositions;
	}
	
	/**
	 * Add position to a list of positions only if it is a valid position.
	 * @param position
	 * 			The position.
	 * @param positionList
	 * 			The list of positions.
	 * @return
	 * 			The list of valid positions.
	 */
	private List<Position> addPositionIfValid(Position position, List<Position> positionList) {
		if (position.getX() >= 0 && position.getX() < size && position.getY() >= 0 && position.getY() < size) {
			positionList.add(position);
		}
		return positionList;
	}
}
