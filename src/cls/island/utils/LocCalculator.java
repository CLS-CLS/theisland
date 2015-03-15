package cls.island.utils;


public class LocCalculator {

	private static final int BASE_Y = 30;
	private static final int BASE_X = 350;
	private static final LocCalculator INSTANCE = new LocCalculator();

	private LocCalculator() {
	}

	public static LocCalculator getInstance() {
		return INSTANCE;
	}

	public Loc gridToCoords(int row, int col) {
		return new Loc(BASE_X + col * 130, BASE_Y + row * 130);
	}

	public Grid coordsToGrid(double x, double y) {
		// TODO computation
		return new Grid(0, 0);
	}

	public Loc playerBasePositionToLoc(int position) {
		double base_x = 5;
		double base_y = 5;
		double delta_y = 222;
		return new Loc(base_x, base_y + position * delta_y);
	}

	public Loc cardLocationInCardHolder(int i) {
		Loc location = null;
		if (i < 2) {
			location = new Loc(100 + 85 * i, 10);
		} else {
			location = new Loc(15 + 85 * (i - 2), 110);
		}
		return location;
	}

	public static class Loc {
		public final double x, y;

		public Loc(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public Loc add(Loc loc) {
			return new Loc(this.x + loc.x, this.y + loc.y);
		}

		public Loc subtract(Loc loc) {
			return new Loc(this.x - loc.y, this.y - loc.y);
		}

		@Override
		public String toString() {
			return "Loc(" + x + ", " + y + ")";
		}
	}

	public static class Grid {
		public final int col, row;

		public Grid(int row, int col) {
			this.col = col;
			this.row = row;
		}

		@Override
		public String toString() {
			return Grid.class.getName() + "(r" + this.row + ", c" + this.col + ")";
		}
	}

	public Loc cardLocationInPile(int index) {
		return new Loc(index / 2, -index / 2);
	}

	public Loc pieceLocationOnIslandTile(int index) {
		Loc loc = null;
		if (index < 2) {
			loc = new Loc(20 + 40 * index, -20);
		} else {
			loc = new Loc(20 + 40 * (index - 2), 40);
		}
		return loc;
	}

	public Loc gridToCoords(Grid grid) {
		return gridToCoords(grid.row, grid.col);
	}

	public Loc markerLocation(float waterLevel) {
		return new Loc(-10, 195 - 21 * waterLevel);

	}

	public Loc pieceLocInSelectPiecePopUp(int i) {
		return new Loc(20 + i*30, 20);
	}

}
