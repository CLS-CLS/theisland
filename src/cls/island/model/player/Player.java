package cls.island.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import cls.island.control.Options.PlayerType;
import cls.island.model.IslandGrid;
import cls.island.model.IslandGrid.Direction;
import cls.island.view.component.island.Island;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.player.base.PlayerBase;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.Type;
import cls.island.view.component.treasury.card.Type.Ability;

/**
 * Base class for all players
 * 
 * @author lytsikas
 * 
 */
public class Player {

	private static final int NUMBER_OF_PLAYER_ACTION = 3;
	protected final PlayerType type;
	protected final String playerId;
	protected final ReadOnlyIntegerWrapper actionsLeft = new ReadOnlyIntegerWrapper(NUMBER_OF_PLAYER_ACTION);
	protected final Piece piece;
	protected final PlayerBase base;

	protected Player(PlayerType type, Piece piece, PlayerBase base) {
		super();
		this.type = type;
		this.piece = piece;
		this.base = base;
		playerId = type.name();
	}

	public PlayerType getType() {
		return type;
	}

	public String getPlayerId() {
		return playerId;
	}

	public int getActionsLeft() {
		return actionsLeft.get();
	}

	public Piece getPiece() {
		return piece;
	}

	public PlayerBase getBase() {
		return base;
	}

	/**
	 * 
	 * @param card
	 *            the index of the card in the players hand
	 */
	public int addCard(TreasuryCard card) {
		return base.addCard(card);
	}

	public void removeCard(TreasuryCard card) {
		base.removeCard(card);
	}

	public void giveCardToPlayer(TreasuryCard card, Player player) {
		base.removeCard(card);
		player.base.addCard(card);
	}

	public List<TreasuryCard> getTreasuryCards() {
		return new ArrayList<>(base.getTreasuryCards());
	}

	public boolean hasAction() {
		return actionsLeft.get() > 0;
	}

	public void resetActions() {
		actionsLeft.set(NUMBER_OF_PLAYER_ACTION);
	}

	/**
	 * Returns all the islands the player can make a valid move with i its
	 * standard move. Players with special move abilities should override this
	 * method.
	 * 
	 * @param from
	 * @param grid
	 * @return
	 */
	public List<Island> validIslandsToMove(Island from, IslandGrid<Island> grid) {
		throw new UnsupportedOperationException();
	}
	
	

	/**
	 * Checks if the player make a valid standard move to the a island. Players
	 * with special move abilities should override this method.
	 * 
	 * @param fromIsland
	 * @param toIsland
	 * @param grid
	 * @return
	 */
	public boolean isValidMove(Island fromIsland, Island toIsland, IslandGrid<Island> grid) {
		if (toIsland.isSunk())
			return false;
		boolean valid = false;
		if (grid.isAdjacent(fromIsland, toIsland, Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)) {
			valid = true;
		}
		return valid;
	}
	
		
	/**
	 * checks if the player can shore up the specific island. The checks are:
	 * 1) if the island is adjacent to player 
	 * 2) the island is not sunk. 
	 * @param fromIsland
	 * @param toIsland
	 * @param grid
	 * @return
	 */
	public boolean isValidShoreUp(Island fromIsland, Island toIsland, IslandGrid<Island> grid) {
		if (toIsland.isSunk())
			return false;
		boolean valid = false;
		if (grid.isAdjacent(fromIsland, toIsland, Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)) {
			valid = true;
		}
		return valid;
		
	}

	/**
	 * Moves the player to an island and updates all the model attributes
	 * (Player' s actions left) according to the game's rules. Players with
	 * specific move abilities should override this method. In case the model
	 * update (except the location update) is not desired use the
	 * <code>setToIsland</code> method.
	 * 
	 * @param island
	 */
	public int moveToIsland(Island island) {
		if (island.isSunk())
			throw new IllegalStateException("cannot move to sunk island " + island);
		int index = setToIsland(island);
		actionsLeft.set(getActionsLeft() - 1);
		return index;
	}

	/**
	 * Moves the player's piece to the provided island without any restriction.
	 * The move is not bound to any rules and does not affect/update any other
	 * property. The method should be used only when the piece should be set
	 * directly to the desired island and the move is not part of the game (for
	 * example when initializing or loading an existing board)
	 * 
	 * @param island
	 */
	public final int setToIsland(final Island island) {
		if (piece.getIsland() != null) {
			Island islandTileFrom = piece.getIsland();
			islandTileFrom.removePiece(piece);
		}

		int index = island.addPiece(piece);
		return index;
	}

	/**
	 * Shores up and island and updates the model.
	 * Override this method for special shore up abilities.
	 * 
	 * @param island
	 */
	public void shoreUp(Island island) {
		actionsLeft.set(getActionsLeft() - 1);
		island.unFlood();
	}
	

	public ReadOnlyIntegerProperty actionsLeftProperty() {
		return actionsLeft.getReadOnlyProperty();
	}

	/**
	 * Convenient method. Finds all the cards that forms a collection of 
	 * 4 cards of the same treasure type 
	 * 
	 * @return the cards forming the treasure collection
	 */
	public List<TreasuryCard> getTreasureCollection() {
		
		return null;
		
	}

}
