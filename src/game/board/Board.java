package game.board;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	
	/** Whether a GoGUI should be used. */
	private boolean isGoGUI;
	
	/** List of intersection groups. */
	private List<IntersectionGroup> intersectionGroups;
	
	/** List of empty intersection groups. */
	private List<IntersectionGroup> emptyIntersectionGroups;
	
	/** The color of the most recent placed stone. */
	private StoneColor color;
	
	/** The score of the black stones. */
	private int blackScore;
	
	/** The score of the white stones. */
	private int whiteScore;
	
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
		this.intersectionGroups = new ArrayList<IntersectionGroup>();
		this.emptyIntersectionGroups = new ArrayList<IntersectionGroup>();
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
		for (int x = 0; x < this.size; x++) {
			for (int y = 0; y < this.size; y++) {
				intersections[x][y] = new Intersection(new Position(x, y));
			}
		}
	}
	
	/**
	 * Create a copy of the current board.
	 * @return
	 * 			The copy of the board.
	 */
	public Board copy() {
		Board copy = new Board(size, false);
		copy.intersections = copyIntersections();
		copy.intersectionGroups = copyIntersectionGroups();
		copy.emptyIntersectionGroups = copyEmptyIntersectionGroups();
		copy.color = color;
		copy.blackScore = blackScore;
		copy.whiteScore = whiteScore;
		return copy;
	}
	
	/**
	 * Create a copy of the current list of empty intersection groups.
	 * @return
	 * 			The list of current empty intersection groups.			
	 */
	private List<IntersectionGroup> copyEmptyIntersectionGroups() {
		List<IntersectionGroup> copy = new ArrayList<IntersectionGroup>();
		for (IntersectionGroup emptyIntersectionGroup : emptyIntersectionGroups) {
			copy.add(emptyIntersectionGroup.copy());
		}
		return copy;
	}

	/**
	 * Create a copy of the current list of intersection groups.
	 * @return
	 * 			The list of current intersection groups.
	 */
	private List<IntersectionGroup> copyIntersectionGroups() {
		List<IntersectionGroup> copy = new ArrayList<IntersectionGroup>();
		for (IntersectionGroup intersectionGroup : intersectionGroups) {
			copy.add(intersectionGroup.copy());
		}
		return copy;
	}

	/**
	 * Create a copy of the two-dimensional array of intersections.
	 * @return
	 * 			The intersections.
	 */
	private Intersection[][] copyIntersections() {
		Intersection[][] copy = new Intersection[size][size];
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				copy[x][y] = intersections[x][y].copy();
			}
		}
		return copy;
	}

	/** 
	 * Start a GoGUI.
	 */
	public GoGUIIntegrator startGoGUI() {
		if (isGoGUI) {
			goGUI = new GoGUIIntegrator(false, true, size);
			goGUI.startGUI();
		}
		return goGUI;
	}
	
	/**
	 * Set the GoGUI of the board to the provided goGUI.
	 * @param goGUI
	 * 			The GoGUI.
	 */
	public void setGoGUI(GoGUIIntegrator goGUI) {
		this.goGUI = goGUI;
	}
	
	/**
	 * Set the size of the new board to the provided size.
	 * @param size
	 * 			The new board size.
	 */
	public void setSize(int size) {
		intersections = new Intersection[size][size];
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				intersections[x][y] = new Intersection(new Position(x, y));
			}
		}
		this.size = size;
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
		List<Intersection> adjacentIntersections = 
				getAdjacentIntersectionsWithStone(this.getIntersection(new Position(x, y)));
		adjustLibertiesOfAdjacentIntersectionsWithStone(adjacentIntersections, 
				this.getIntersection(new Position(x, y)));
		addSetStoneToGroup(adjacentIntersections, new Position(x, y));
		checkForRemovingStones(adjacentIntersections, getIntersection(new Position(x, y)));
//		removeStonesWithZeroLiberties();
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
				if (adjacentIntersection.getStone().getColor().equals(
						this.getIntersection(position).getStone().getColor())) {
					if (!intersectionGroups.isEmpty()) {
						Iterator<IntersectionGroup> intersectionGroupsIterator = 
								intersectionGroups.iterator();
						List<IntersectionGroup> tempIntersectionGroups = 
								new ArrayList<IntersectionGroup>();
						List<IntersectionGroup> tempNewIntersectionGroups = 
								new ArrayList<IntersectionGroup>();
						while (intersectionGroupsIterator.hasNext()) {
							IntersectionGroup intersectionGroup = intersectionGroupsIterator.next();
//							if (intersectionGroup.getIntersections().get(0).getStone().getColor()
//									.equals(adjacentIntersection.getStone().getColor())) {
								for (Intersection intersectionInGroup : intersectionGroup.getIntersections()) {
									if (intersectionInGroup.getPosition().equals(adjacentIntersection.getPosition())) {
										tempIntersectionGroups.add(intersectionGroup);
									} else {
										IntersectionGroup newIntersectionGroup = 
												new IntersectionGroup();
										newIntersectionGroup.addIntersection(
												this.getIntersection(position));
										newIntersectionGroup.addIntersection(adjacentIntersection);
										tempNewIntersectionGroups.add(newIntersectionGroup);
									}
								}
//							}
//							} else {
//								IntersectionGroup newIntersectionGroup = new IntersectionGroup();
//								newIntersectionGroup.addIntersection(
//										this.getIntersection(position));
//								newIntersectionGroup.addIntersection(adjacentIntersection);
//								tempNewIntersectionGroups.add(newIntersectionGroup);
//							}
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
			}
			List<IntersectionGroup> intersectionGroupsContainingAdjacentStone = 
					new ArrayList<IntersectionGroup>();
			Iterator<IntersectionGroup> intersectionGroupsIterator = 
					intersectionGroups.iterator();
			while (intersectionGroupsIterator.hasNext()) {
				IntersectionGroup intersectionGroup = intersectionGroupsIterator.next();
				if (intersectionGroup.getIntersections().contains(
						this.getIntersection(position))) {
					intersectionGroupsContainingAdjacentStone.add(intersectionGroup);
				}
			}
			if (intersectionGroupsContainingAdjacentStone.size() > 1) {
				IntersectionGroup resultingIntersectionGroup = 
						intersectionGroupsContainingAdjacentStone.get(0);
				for (int i = 1; i < intersectionGroupsContainingAdjacentStone.size(); i++) {
					List<Intersection> intersectionsInGroup = 
							intersectionGroupsContainingAdjacentStone.get(i).getIntersections();
					for (int j = 0; j < intersectionsInGroup.size(); j++) {
						resultingIntersectionGroup.addIntersection(intersectionsInGroup.get(j));
					}
					intersectionGroups.remove(intersectionGroupsContainingAdjacentStone.get(i));
				}
			}
		}
		List<IntersectionGroup> intersectionGroupsContainingSetStone = 
				new ArrayList<IntersectionGroup>();
		Iterator<IntersectionGroup> intersectionGroupsIterator = 
				intersectionGroups.iterator();
		while (intersectionGroupsIterator.hasNext()) {
			IntersectionGroup intersectionGroup = intersectionGroupsIterator.next();
			if (intersectionGroup.getIntersections().contains(
					this.getIntersection(position))) {
				intersectionGroupsContainingSetStone.add(intersectionGroup);
			}
		}
		if (intersectionGroupsContainingSetStone.size() > 1) {
			IntersectionGroup resultingIntersectionGroup = 
					intersectionGroupsContainingSetStone.get(0);
			for (int i = 1; i < intersectionGroupsContainingSetStone.size(); i++) {
				List<Intersection> intersectionsInGroup = 
						intersectionGroupsContainingSetStone.get(i).getIntersections();
				for (int j = 0; j < intersectionsInGroup.size(); j++) {
					resultingIntersectionGroup.addIntersection(intersectionsInGroup.get(j));
				}
				intersectionGroups.remove(intersectionGroupsContainingSetStone.get(i));
			}
		}
	}

	/**
	 * Adjust the liberties of the surrounding stones of the altered intersection.
	 * @param adjacentIntersections
	 * 			The adjacent intersections.
	 * @param intersection
	 * 			The altered intersection.
	 */
	private void adjustLibertiesOfAdjacentIntersectionsWithStone(
			List<Intersection> adjacentIntersections, Intersection intersection) {
		Iterator<Intersection> adjacentIntersectionsIterator = adjacentIntersections.iterator();
		while (adjacentIntersectionsIterator.hasNext()) {
			Intersection adjacentIntersection = adjacentIntersectionsIterator.next();
			List<Intersection> adjacentIntersectionsOfAdjacentIntersection = 
					getAdjacentIntersectionsWithStone(adjacentIntersection);
			if (intersection.isOccupied()) {
				int setStoneLiberties = 0;
				List<StoneColor> stoneColorAdjacentIntersectionOfAdjacentIntersection = 
						new ArrayList<StoneColor>();
				Iterator<Intersection> adjacentIntersectionsOfAdjacentIntersectionIterator = 
						adjacentIntersectionsOfAdjacentIntersection.iterator();
				while (adjacentIntersectionsOfAdjacentIntersectionIterator.hasNext()) {
					Intersection adjacentIntersectionOfAdjacentIntersection = 
							adjacentIntersectionsOfAdjacentIntersectionIterator.next();
					stoneColorAdjacentIntersectionOfAdjacentIntersection.add(
							adjacentIntersectionOfAdjacentIntersection.getStone().getColor());
					adjacentIntersectionOfAdjacentIntersection.getStone().setLiberties(
							adjacentIntersectionOfAdjacentIntersection.getStone()
							.getInitialLiberties() - getAdjacentIntersectionsWithStone(
									adjacentIntersectionOfAdjacentIntersection).size());
					setStoneLiberties++;
				}
				adjacentIntersection.getStone().setLiberties(adjacentIntersection.getStone()
						.getInitialLiberties() - setStoneLiberties);
			} else {
				if (adjacentIntersectionsOfAdjacentIntersection.isEmpty()) {
					adjacentIntersection.getStone().setLiberties(adjacentIntersection.getStone()
							.getInitialLiberties());
				} else {
					adjacentIntersection.getStone().setLiberties(adjacentIntersection.getStone()
							.getInitialLiberties() - 
							adjacentIntersectionsOfAdjacentIntersection.size());
				}
			}
		}
	}

	private void checkForRemovingStones(List<Intersection> adjacentIntersections, Intersection intersection) {
		if (intersection.isOccupied()) {
			// Make list of all adjacent intersections of the other color
			List<Intersection> adjacentIntersectionsOtherColor = new ArrayList<Intersection>();
			for (Intersection adjacentIntersection : adjacentIntersections) {
				if (!adjacentIntersection.getStone().getColor().equals(intersection.getStone().getColor())) {
					adjacentIntersectionsOtherColor.add(adjacentIntersection);
				}
			}
			// See if adjacent intersection with other color is in group with zero liberties
			List<IntersectionGroup> removedIntersectionGroups = new ArrayList<IntersectionGroup>();
			for (Intersection adjacentIntersectionOtherColor : adjacentIntersectionsOtherColor) {
				for (IntersectionGroup intersectionGroup : intersectionGroups) {
					if (intersectionGroup.getIntersections().contains(adjacentIntersectionOtherColor)) {
						int zeroLibertiesCount = 0;
						for (Intersection intersectionInGroup : intersectionGroup.getIntersections()) {
							if (intersectionInGroup.isOccupied()) {
								if (intersectionInGroup.getStone().getLiberties() == 0) {
									zeroLibertiesCount++;
								}
							}
						}
						if (zeroLibertiesCount == intersectionGroup.getIntersections().size()) {
							removeStones(intersectionGroup.getIntersections());
							removedIntersectionGroups.add(intersectionGroup);
							intersectionGroups.remove(intersectionGroup);
							return;
						}
					}
				}
			}
			for (IntersectionGroup removedIntersectionGroup : removedIntersectionGroups) {
				intersectionGroups.remove(removedIntersectionGroup);
			}
			// See if adjacent intersection with other color is not in group and has zero liberties
			for (Intersection adjacentIntersectionOtherColor : adjacentIntersectionsOtherColor) {
				if (adjacentIntersectionOtherColor.isOccupied()) {
					if (adjacentIntersectionOtherColor.getStone().getLiberties() == 0) {
						int otherColorCount = 0;
						List<Intersection> adjacentIntersectionsOfAdjacentIntersectionOtherColor = this.getAdjacentIntersectionsWithStone(adjacentIntersectionOtherColor);
						
						for (Intersection adjacentIntersectionOfAdjacentIntersectionOtherColor : adjacentIntersectionsOfAdjacentIntersectionOtherColor) {
							if (!adjacentIntersectionOfAdjacentIntersectionOtherColor.getStone().getColor().equals(adjacentIntersectionOtherColor.getStone().getColor())) {
								otherColorCount++;
							}
						}
						if (otherColorCount == adjacentIntersectionsOfAdjacentIntersectionOtherColor.size()) {
							removeStone(adjacentIntersectionOtherColor.getPosition());
							return;
						}
					}
				}
			}
			// See if the move is suicide
			if (adjacentIntersectionsOtherColor.size() == intersection.getStone().getInitialLiberties()) {
				removeStone(intersection.getPosition());
				return;
			}
			// See if the move is suicide for the group of stones
			for (IntersectionGroup intersectionGroup : intersectionGroups) {
				if (intersectionGroup.getIntersections().contains(intersection)) {
					int zeroLibertiesCount = 0;
					for (Intersection intersectionInGroup : intersectionGroup.getIntersections()) {
						if (intersectionInGroup.isOccupied()) {
							if (intersectionInGroup.getStone().getLiberties() == 0) {
								zeroLibertiesCount++;
							}
						}
					}
					if (zeroLibertiesCount == intersectionGroup.getIntersections().size()) {
						removeStones(intersectionGroup.getIntersections());
						removedIntersectionGroups.add(intersectionGroup);
						intersectionGroups.remove(intersectionGroup);
						return;
					}
				}
			}
			for (IntersectionGroup removedIntersectionGroup : removedIntersectionGroups) {
				intersectionGroups.remove(removedIntersectionGroup);
			}
		}
	}
	/**
	 * Check for suicide move of placed stone at provided intersection.
	 * Remove other (groups of) stones if it seems to be a suicide move but really is not.
	 * @param adjacentIntersections
	 * 			The adjacent intersections of the placed stone.
	 * @param intersection
	 * 			The intersection at which the stone is placed.
	 */
	private void checkForSuicideMove(List<Intersection> adjacentIntersections, 
			Intersection intersection) {
		int otherColorCount = 0;
		int inIntersectionGroupCount = 0;
		Iterator<Intersection> adjacentIntersectionsIterator = adjacentIntersections.iterator();
		while (adjacentIntersectionsIterator.hasNext()) {
			Intersection adjacentIntersection = adjacentIntersectionsIterator.next();
			IntersectionGroup adjacentIntersectionGroupRemove = new IntersectionGroup();
			int zeroLibertiesInGroupCount = 0;
			for (IntersectionGroup intersectionGroup : intersectionGroups) {
				if (intersectionGroup.getIntersections().contains(adjacentIntersection)) {
					for (Intersection intersectionInGroup : intersectionGroup.getIntersections()) {
						if (intersectionInGroup.getStone().getLiberties() == 0) {
							zeroLibertiesInGroupCount++;
						}
					}
					if (zeroLibertiesInGroupCount == intersectionGroup.getIntersections().size()) {
						adjacentIntersectionGroupRemove = intersectionGroup;
					}
				}
			}
			removeStones(adjacentIntersectionGroupRemove.getIntersections());
			List<Intersection> adjacentIntersectionsOfAdjacentIntersection = 
					new ArrayList<Intersection>();
			adjacentIntersectionsOfAdjacentIntersection = 
					getAdjacentIntersectionsWithStone(adjacentIntersection);
			Iterator<Intersection> adjacentIntersectionsOfAdjacentIntersectionIterator = 
					adjacentIntersectionsOfAdjacentIntersection.iterator();
			int otherColorCountAdjacent = 0;
			
			while (adjacentIntersectionsOfAdjacentIntersectionIterator.hasNext()) {
				Intersection adjacentIntersectionOfAdjacentIntersection = 
						adjacentIntersectionsOfAdjacentIntersectionIterator.next();
				if (!adjacentIntersectionOfAdjacentIntersection.getStone().getColor().equals(
						adjacentIntersection.getStone().getColor())) {
					otherColorCountAdjacent++;
				}
			}
			if (!adjacentIntersection.getStone().getColor().equals(color)) {
				otherColorCount++;
			}
			if (otherColorCountAdjacent == adjacentIntersectionsOfAdjacentIntersection.size() && 
					adjacentIntersection.getStone().getLiberties() == 0 && inIntersectionGroupCount == 0) {
				removeStone(adjacentIntersection.getPosition());
			} else {
				List<IntersectionGroup> intersectionGroupsRemoved = 
						new ArrayList<IntersectionGroup>();
				for (IntersectionGroup intersectionGroup : intersectionGroups) {
					if (intersectionGroup.getIntersections().contains(adjacentIntersection)) {
						int zeroLibertiesCount = 0;
						List<Intersection> intersectionsPossiblyRemoved = 
								intersectionGroup.getIntersections();
						for (Intersection intersectionPossiblyRemoved : 
							intersectionsPossiblyRemoved) {
							if (intersectionPossiblyRemoved.getStone().getLiberties() == 
									0) {
								zeroLibertiesCount++;
							}
						}
						if (zeroLibertiesCount == intersectionsPossiblyRemoved.size()) {
							removeStones(intersectionsPossiblyRemoved);
							intersectionGroupsRemoved.add(intersectionGroup);
						}
					}
				}
				for (IntersectionGroup intersectionGroup : intersectionGroupsRemoved) {
					intersectionGroups.remove(intersectionGroup);
				}
			}
		
			if (inIntersectionGroupCount != 0) {
				List<IntersectionGroup> intersectionGroupsRemoved = 
						new ArrayList<IntersectionGroup>();
				for (IntersectionGroup intersectionGroup : intersectionGroups) {
					if (intersectionGroup.getIntersections().contains(adjacentIntersection)) {
						int zeroLibertiesCount = 0;
						List<Intersection> intersectionsPossiblyRemoved = 
								intersectionGroup.getIntersections();
						for (Intersection intersectionPossiblyRemoved : 
							intersectionsPossiblyRemoved) {
							if (intersectionPossiblyRemoved.getStone().getLiberties() == 
									0) {
								zeroLibertiesCount++;
							}
						}
						if (zeroLibertiesCount == intersectionsPossiblyRemoved.size()) {
							removeStones(intersectionsPossiblyRemoved);
							intersectionGroupsRemoved.add(intersectionGroup);
						}
					}
				}
				for (IntersectionGroup intersectionGroup : intersectionGroupsRemoved) {
					intersectionGroups.remove(intersectionGroup);
				}
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
	private void removeStonesWithZeroLiberties() {
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				Position position = new Position(x, y);
				if (this.getIntersection(position).isOccupied()) {
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
						IntersectionGroup intersectionGroupPossiblyRemoved = 
								new IntersectionGroup();
						for (IntersectionGroup intersectionGroup : intersectionGroups) {
							if (intersectionGroup.getIntersections().contains(
									this.getIntersection(position))) {
								intersectionGroupPossiblyRemoved = intersectionGroup;
							}
							int zeroLibertiesCount = 0;
							List<Intersection> intersectionsPossiblyRemoved = 
									intersectionGroupPossiblyRemoved.getIntersections();
							for (Intersection intersectionPossiblyRemoved : 
								intersectionsPossiblyRemoved) {
								if (intersectionPossiblyRemoved.getStone()
										.getLiberties() == 0) {
									zeroLibertiesCount++;
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
				goGUI.removeStone(intersection.getPosition().getX(), 
						intersection.getPosition().getY());
				goGUI.removeStone(intersection.getPosition().getX(), 
						intersection.getPosition().getY());
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
			adjacentIntersections.add(intersections[adjacentPosition.getX()]
					[adjacentPosition.getY()]);
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
		adjacentPositions = addPositionIfValid(new Position(position.getX(), position.getY() + 1), 
				adjacentPositions);
		adjacentPositions = addPositionIfValid(new Position(position.getX(), position.getY() - 1), 
				adjacentPositions);
		adjacentPositions = addPositionIfValid(new Position(position.getX() + 1, position.getY()), 
				adjacentPositions);
		adjacentPositions = addPositionIfValid(new Position(position.getX() - 1, position.getY()), 
				adjacentPositions);
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
		if (position.getX() >= 0 && position.getX() < size && position.getY() >= 0 && 
				position.getY() < size) {
			positionList.add(position);
		}
		return positionList;
	}

	/**
	 * Calculate a winner for the current board situation.
	 */
	public void calculateWinner() {
		int blackArea = 0;
		int whiteArea = 0;
		int blackStone = 0;
		int whiteStone = 0;
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				Intersection intersection = this.getIntersection(new Position(x, y));
				if (!intersection.isOccupied()) {
					addEmptyIntersectionToEmptyIntersectionGroup(intersection);
				} else {
					if (intersection.getStone().getColor().equals(StoneColor.BLACK)) {
						blackStone++;
					} else {
						whiteStone++;
					}
				}
			}
		}
		for (IntersectionGroup emptyIntersectionGroup : emptyIntersectionGroups) {
			int blackCount = 0;
			int whiteCount = 0;
			List<Intersection> emptyIntersections = emptyIntersectionGroup.getIntersections();
			for (Intersection emptyIntersection : emptyIntersections) {
				List<Intersection> adjacentIntersections = 
						getAdjacentIntersections(emptyIntersection);
				for (Intersection adjacentIntersection : adjacentIntersections) {
					if (adjacentIntersection.isOccupied()) {
						if (adjacentIntersection.getStone().getColor().equals(StoneColor.BLACK)) {
							blackCount++;
						} else {
							whiteCount++;
						}
					}
				}
			}
			if (blackCount == 0) {
				whiteArea = whiteArea + emptyIntersectionGroup.getIntersections().size();
			} else if (whiteCount == 0) {
				blackArea = blackArea + emptyIntersectionGroup.getIntersections().size();
			} 
		}
		blackScore = blackArea + blackStone;
		whiteScore = whiteArea + whiteStone;
	}
	
	public int getBlackScore() {
		return blackScore;
	}
	
	public int getWhiteScore() {
		return whiteScore;
	}
	
	/**
	 * Add the empty intersection to a group of empty intersections.
	 * @param emptyIntersection
	 * 			The empty intersection.
	 */
	private void addEmptyIntersectionToEmptyIntersectionGroup(Intersection emptyIntersection) {
		List<Intersection> adjacentIntersections = getAdjacentIntersections(emptyIntersection);
		Iterator<Intersection> adjacentIntersectionsIterator = adjacentIntersections.iterator();
		while (adjacentIntersectionsIterator.hasNext()) {
			Intersection adjacentIntersection = adjacentIntersectionsIterator.next();
			if (!emptyIntersectionGroups.isEmpty()) {
				Iterator<IntersectionGroup> emptyIntersectionGroupsIterator = 
						emptyIntersectionGroups.iterator();
				List<IntersectionGroup> tempEmptyIntersectionGroups = 
						new ArrayList<IntersectionGroup>();
				List<IntersectionGroup> tempNewEmptyIntersectionGroups = 
						new ArrayList<IntersectionGroup>();
				while (emptyIntersectionGroupsIterator.hasNext()) {
					IntersectionGroup emptyIntersectionGroup = 
							emptyIntersectionGroupsIterator.next();
					if (emptyIntersectionGroup.getIntersections().contains(adjacentIntersection)) {
						tempEmptyIntersectionGroups.add(emptyIntersectionGroup);
					} else {
						IntersectionGroup newIntersectionGroup = new IntersectionGroup();
						newIntersectionGroup.addIntersection(emptyIntersection);
						tempNewEmptyIntersectionGroups.add(newIntersectionGroup);
					}
					IntersectionGroup newIntersectionGroup = new IntersectionGroup();
					newIntersectionGroup.addIntersection(emptyIntersection);
					tempNewEmptyIntersectionGroups.add(newIntersectionGroup);
				}
				for (IntersectionGroup intersectionGroup : tempEmptyIntersectionGroups) {
					intersectionGroup.addIntersection(emptyIntersection);
				}
				for (IntersectionGroup newIntersectionGroup : tempNewEmptyIntersectionGroups) {
					emptyIntersectionGroups.add(newIntersectionGroup);
				}
			} else {
				IntersectionGroup newIntersectionGroup = new IntersectionGroup();
				newIntersectionGroup.addIntersection(emptyIntersection);
				emptyIntersectionGroups.add(newIntersectionGroup);
			}
			List<IntersectionGroup> emptyIntersectionGroupsContainingSetStone = 
					new ArrayList<IntersectionGroup>();
			Iterator<IntersectionGroup> emptyIntersectionGroupsIterator = 
					emptyIntersectionGroups.iterator();
			while (emptyIntersectionGroupsIterator.hasNext()) {
				IntersectionGroup intersectionGroup = emptyIntersectionGroupsIterator.next();
				if (intersectionGroup.getIntersections().contains(emptyIntersection)) {
					emptyIntersectionGroupsContainingSetStone.add(intersectionGroup);
				}
			}
			if (emptyIntersectionGroupsContainingSetStone.size() > 1) {
				IntersectionGroup resultingEmptyIntersectionGroup = 
						emptyIntersectionGroupsContainingSetStone.get(0);
				for (int i = 1; i < emptyIntersectionGroupsContainingSetStone.size(); i++) {
					List<Intersection> intersectionsInGroup = 
							emptyIntersectionGroupsContainingSetStone.get(i).getIntersections();
					for (int j = 0; j < intersectionsInGroup.size(); j++) {
						resultingEmptyIntersectionGroup.addIntersection(
								intersectionsInGroup.get(j));
					}
					emptyIntersectionGroups.remove(emptyIntersectionGroupsContainingSetStone
							.get(i));
				}
			}
		}
	}
	
	/**
	 * Return the list of intersection groups.
	 */
	public List<IntersectionGroup> getIntersectionGroups() {
		return intersectionGroups;
	}
	
	/**
	 * Return the list of empty intersection groups.
	 */
	public List<IntersectionGroup> getEmptyIntersectionGroups() {
		return emptyIntersectionGroups;
	}
	
	/**
	 * Return the GoGUI.
	 * @return
	 * 			The GoGUI.
	 */
	public GoGUIIntegrator getGoGUI() {
		return goGUI;
	}
	
	/**
	 * Clear the board and the GoGUI.
	 */
	public void clear() {
		for (Intersection[] intersectionRows : intersections) {
			for (Intersection intersection : intersectionRows) {
				intersection.removeStone();
			}
		}
		if (isGoGUI) {
			goGUI.clearBoard();
		}
	}
}
