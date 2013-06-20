package cls.island.view.component.island;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javafx.scene.image.Image;
import cls.island.utils.LocCalculator;
import cls.island.utils.LocCalculator.Grid;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.treasury.card.Type;

public class Island {

	public static enum IslandName {
		BreakersBridge, BronzeGate, CaveOfEmbers, CaveOfShadows, CliffsOfAbandon, CopperGate, CoralPalace, CrimsonForest, DunesOfDeception, FoolsLanding, GoldGate, HowlingGarden, IronGate, LostLagoon, MistyMarsh, Observatory, PhantomRock, SilverGate, TempleOfTheMoon, TempleOfTheSun, TidalPalace, TwilightHollow, Watchtower, WhisperingGarden;
	}

	public static class Model {

		protected Grid grid;
		protected Piece[] pieceSpots = new Piece[4];
		protected boolean flooded;
		protected final String name;
		protected boolean sunk;
		protected final LocCalculator locCalculator;
		protected boolean floodBorder;
		protected Type treasure;

		public Model(Grid grid, Piece[] pieceSpots, Type treasure, boolean flooded, String name,
				boolean sunk, boolean floodBorder, LocCalculator locCalculator) {
			this.grid = grid;
			this.pieceSpots = pieceSpots;
			this.flooded = flooded;
			this.name = name;
			this.sunk = sunk;
			this.floodBorder = floodBorder;
			this.locCalculator = locCalculator;
			this.treasure = treasure;
		}

		public Grid getGrid() {
			return grid;
		}

		public Piece[] getPieceSpots() {
			return pieceSpots;
		}

		public boolean isFlooded() {
			return flooded;
		}

		public String getName() {
			return name;
		}

		public boolean isSunk() {
			return sunk;
		}

		public LocCalculator getLocCalculator() {
			return locCalculator;
		}

		public boolean isFloodBorder() {
			return floodBorder;
		}
	}

	private final Model model;
	private IslandView islandView;

	public Island(Image tileImage, Model model) {
		this.model = model;
		islandView = new IslandView(tileImage, model, this);
	}

	public String getName() {
		return model.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((model.name == null) ? 0 : model.name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Island other = (Island) obj;
		if (model.name == null) {
			if (other.getName() != null)
				return false;
		} else if (!model.name.equals(other.getName()))
			return false;
		return true;
	}

	public Grid getGrid() {
		return model.grid;
	}

	public List<Piece> getPieces() {
		List<Piece> pieces = new ArrayList<>();
		for (Piece piece : model.pieceSpots) {
			if (piece != null) {
				pieces.add(piece);
			}
		}
		return pieces;
	}

	public void removePiece(Piece piece) {
		for (int i = 0; i < model.pieceSpots.length; i++) {
			if (model.pieceSpots[i] == piece) {
				model.pieceSpots[i] = null;
				return;
			}
		}
	}

	public void setGrid(int row, int col) {

		model.grid = new Grid(row, col);
	}

	/**
	 * adds the piece on this island tile. The IslandView is the owner of the
	 * IslandView-PieceView bidirectional relationship , hence it sets this tile
	 * to the piece "islandView" field.
	 * 
	 * @param piece
	 * @param onFinish
	 * @returns the position in tile where the piece will be added. -1 if the
	 *          piece is not added.
	 */
	public int addPiece(Piece piece) {
		for (int i = 0; i < model.pieceSpots.length; i++) {
			if (model.pieceSpots[i] == null) {
				model.pieceSpots[i] = piece;
				piece.setIsland(this);
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the position of the piece on the tile.
	 * 
	 * @param piece
	 *            the piece for which we want to dind the position
	 * @return <code>Position.UP</code> if it is in the upper row.
	 *         <code>Position.DOWN</code> otherwise
	 * @throws IllegalArgumentException
	 *             if the tile does not contain the piece.
	 */
	public int getPiecePosition(Piece piece) {
		for (int i = 0; i < 4; i++) {
			if (model.pieceSpots[i].equals(piece))
				return i;
		}
		return -1;
	}

	public SortedMap<Integer, Piece> getPiecesAndPositions() {
		SortedMap<Integer, Piece> map = new TreeMap<>();

		for (int i = 0; i < model.pieceSpots.length; i++) {
			if (model.pieceSpots[i] != null) {
				map.put(Integer.valueOf(i), model.pieceSpots[i]);
			}
		}
		return map;
	}

	public void sink() {
		model.sunk = true;
	}

	public boolean isSunk() {
		return model.sunk;
	}

	/**
	 * sets an island to flood/unflood state. Does not make any business checks
	 * on the current island flood status. Used only when you want to change the
	 * flood status outside the scope of the game (aka init, load game). In any
	 * other case use the methods <code> flood </code> and
	 * <code> unflood </code>
	 * 
	 * @param flooded
	 */
	public void setFlooded(boolean flooded) {
		this.model.flooded = flooded;
	}

	public void flood() {
		if (model.flooded)
			throw new IllegalArgumentException(this + " is aldready flooded");
		model.flooded = true;
	}

	public void unFlood() {
		if (!model.flooded)
			throw new IllegalStateException(this + " should be flooded in order to unflood it");
		model.flooded = false;
	}

	public boolean isFlooded() {
		return model.flooded;
	}

	public IslandView getComponent() {
		return islandView;
	}

	@Override
	public String toString() {
		return "Island [name =" + model.name + " sunk =" + model.sunk + " isFlooded ="
				+ model.isFlooded() + "]";
	}

	public boolean hasTreasure() {
		return model.treasure != null;
	}

	public Type getTreasure() {
		return model.treasure;
	}

}
