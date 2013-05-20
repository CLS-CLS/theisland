package cls.island.model;

import cls.island.utils.LocCalculator.Grid;

public class IslandGrid<E> {

	public enum Direction {
		UP {
			@Override
			public Grid nextGrid(Grid currentGrid) {
				return new Grid(currentGrid.row - 1, currentGrid.col);
			}

			@Override
			public Direction opposite() {
				return DOWN;
			}
		},
		UP_RIGHT {
			@Override
			public Grid nextGrid(Grid currentGrid) {
				return new Grid(currentGrid.row - 1, currentGrid.col + 1);
			}

			@Override
			public Direction opposite() {
				return DOWN_LEFT;
			}
		},
		RIGHT {
			@Override
			public Grid nextGrid(Grid currentGrid) {
				return new Grid(currentGrid.row, currentGrid.col + 1);
			}

			@Override
			public Direction opposite() {
				return LEFT;
			}
		},
		DOWN_RIGHT {
			@Override
			public Grid nextGrid(Grid currentGrid) {
				return new Grid(currentGrid.row + 1, currentGrid.col + 1);
			}

			@Override
			public Direction opposite() {
				return UP_LEFT;
			}
		},
		DOWN {
			@Override
			public Grid nextGrid(Grid currentGrid) {
				return new Grid(currentGrid.row + 1, currentGrid.col);
			}

			@Override
			public Direction opposite() {
				return UP;
			}
		},
		DOWN_LEFT {
			@Override
			public Grid nextGrid(Grid currentGrid) {
				return new Grid(currentGrid.row + 1, currentGrid.col - 1);
			}

			@Override
			public Direction opposite() {
				return UP_RIGHT;
			}
		},
		LEFT {
			@Override
			public Grid nextGrid(Grid currentGrid) {
				return new Grid(currentGrid.row, currentGrid.col - 1);
			}

			@Override
			public Direction opposite() {
				return RIGHT;
			}
		},
		UP_LEFT {
			@Override
			public Grid nextGrid(Grid currentGrid) {
				return new Grid(currentGrid.row - 1, currentGrid.col - 1);
			}

			@Override
			public Direction opposite() {
				return DOWN_RIGHT;
			}
		};

		public abstract Grid nextGrid(Grid currentGrid);

		public abstract Direction opposite();
	}

	private int rows, cols;

	private GridNode[][] data;

	public IslandGrid(int cols, int rows) {
		this.rows = rows;
		this.cols = cols;
		data = new GridNode[rows][cols];
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				data[row][col] = new GridNode(null);
			}
		}
	}

	public void addElement(E element, Grid grid) {
		if (!isValidGrid(grid))
			throw new IllegalArgumentException(grid + " is not valid");
		data[grid.row][grid.col] = new GridNode(element);
	}

	public void addElement(E element, int row, int col) {
		addElement(element, new Grid(row, col));
	}

	public E getAdjacent(E element, Direction direction) {
		Grid grid = findGridOfElemet(element);
		if (grid == null || !isValidGrid(grid))
			throw new IllegalArgumentException("Element does not exist");

		Grid adjGrid = direction.nextGrid(grid);
		if (!isValidGrid(adjGrid))
			return null;
		@SuppressWarnings("unchecked")
		E result = (E) data[adjGrid.row][adjGrid.col].e;
		return result;
	}

	private boolean hasAdjacent(E element, Direction direction) {
		E adjElem = getAdjacent(element, direction);
		return adjElem != null;
	}

	/**
	 * 
	 * @param element
	 * @param direction
	 *            the directions to look for adjacent elements;
	 * @return true if the element has at least one adjacent element in the
	 *         provided directions.
	 */
	public boolean hasAdjacent(E element, Direction... direction) {
		boolean hasAdjacent = false;
		for (Direction d : direction) {
			if (hasAdjacent(element, d)) {
				hasAdjacent = true;
				break;
			}
		}
		return hasAdjacent;
	}

	/**
	 * Convenient method. Is same as calling
	 * <code>hasAdjacent(E element, Direction... direction)</code> with
	 * direction parameter all the directions.
	 * 
	 * @param element
	 * @return
	 */
	public boolean hasAdjacent(E element) {
		boolean hasAdjacent = false;
		for (Direction d : Direction.values()) {
			if (hasAdjacent(element, d)) {
				hasAdjacent = true;
				break;
			}
		}
		return hasAdjacent;
	}

	public Direction findDirection(E from, E to) {
		for (Direction d : Direction.values()) {
			if (to.equals(getAdjacent(from, d))) {
				return d;
			}
		}
		return null;
	}

	private Grid findGridOfElemet(E element) {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (element.equals(data[row][col].e)) {
					return new Grid(row, col);
				}
			}
		}
		return null;

	}

	public boolean isValidGrid(Grid grid) {
		if (grid.col < 0 || grid.row < 0)
			return false;
		return grid.col < cols && grid.row < rows;
	}

	private static class GridNode {
		Object e;

		public GridNode(Object e) {
			this.e = e;
		}
	}

	/**
	 * Checks if the e2 is adjacent to e1 at the provided directions. The
	 * directions are considered from e1.
	 * 
	 * @param e1
	 * @param e2
	 * @param directions
	 *            the directions of e1 to search for adjacency.
	 */
	public boolean isAdjacent(E e1, E e2, Direction... directions) {
		boolean adjacent = false;
		Grid grid = findGridOfElemet(e1);
		for (Direction d : directions) {
			Grid nextGrid = d.nextGrid(grid);
			if (!isValidGrid(nextGrid))
				continue;
			if (data[nextGrid.row][nextGrid.col].e == e2) {
				adjacent = true;
			}
		}
		return adjacent;
	}

	public boolean isAdjacent(E e1, E e2) {
		Direction[] directions = Direction.values();
		return isAdjacent(e1, e2, directions);
	}

}
