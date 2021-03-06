package cls.island.model.player;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import cls.island.control.Config.PlayerType;
import cls.island.model.IslandGrid;
import cls.island.model.IslandGrid.Direction;
import cls.island.view.component.island.Island;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.player.base.PlayerBase;
import cls.island.view.component.treasury.card.TreasuryCard;
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
	protected final ReadOnlyIntegerWrapper actionsLeft = new ReadOnlyIntegerWrapper(
			NUMBER_OF_PLAYER_ACTION);
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
	 * Convenient method
	 * @param card the index of the card in the players hand
	 */
	public int addCard(TreasuryCard card) {
		return base.addCard(card);
	}
	
	/**
	 * convenient method
	 * @return the cards the playerType is holding
	 */
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
	 * Returns all the islands the playerType can make a valid move with i its
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
	 * Checks if the playerType can make a valid standard move. Players
	 * with special move abilities should override this method.
	 * 
	 * @param fromIsland the island the playerType is going to move from 
	 * @param toIsland the island the playerType will go
	 * @param grid the island grids
	 * @return true if it is valid move
	 */
	public boolean isValidMove(Island fromIsland, Island toIsland, IslandGrid<Island> grid) {
		if (toIsland.isSunk())
			return false;
		boolean valid = false;
		if (grid.isAdjacent(fromIsland, toIsland, Direction.UP, Direction.RIGHT, Direction.DOWN,
				Direction.LEFT)) {
			valid = true;
		}
		return valid;
	}

	/**
	 * checks if the playerType can shore up the specific island. The checks are: 1)
	 * if the island is adjacent to playerType 2) the island is not sunk. 3) the
	 * island is the same island the playerType is on
	 * 
	 * @param fromIsland
	 * @param toIsland
	 * @param grid
	 * @return
	 */
	public boolean isValidShoreUp(Island fromIsland, Island toIsland, IslandGrid<Island> grid) {
		if (toIsland.isSunk())
			return false;
		if (fromIsland == toIsland)
			return true;
		if (grid.isAdjacent(fromIsland, toIsland, Direction.UP, Direction.RIGHT, Direction.DOWN,
				Direction.LEFT))
			return true;
		return false;

	}
	
	/**
	 * Check if the playerType can perform a shore up. By default a playerType can perform a shore up
	 * if he has at least one action left. Override this method for specific checks.
	 * @return true if the playerType can perform a shore up.
	 */
	public boolean canShoreUp() {
		return actionsLeft.getValue() > 0;
		
	}

	/**
	 * Moves the playerType to an island and updates the actions left
	 * according to the game's rules. The island should not be sunk. Players with 
	 * specific move abilities should override this method. In case the model
	 * update (except the location update) is not desired use the
	 * <code>setToIsland</code> method.
	 * 
	 * @param island
	 * @return the exact position in the island model.
	 */
	public int moveToIsland(Island island) {
		if (island.isSunk())
			throw new IllegalStateException("cannot move to sunk island " + island);
		int index = setToIsland(island);
		actionsLeft.set(getActionsLeft() - 1);
		return index;
	}

	/**
	 * Moves the playerType's piece to the provided island without any restriction.
	 * The move is not bound to any rules and does not affect/update any other
	 * property. The method should be used only when the piece should be set
	 * directly to the desired island and the move is not part of the game (for
	 * example when initializing or loading an existing board)
	 * 
	 * @param island
	 */
	public final int setToIsland(final Island island) {
		if (island == null )throw new IllegalArgumentException("Island should not be null");
		if (piece.getIsland() != null) {
			Island islandTileFrom = piece.getIsland();
			islandTileFrom.removePiece(piece);
		}

		int index = island.addPiece(piece);
		return index;
	}

	/**
	 * Shores up and island and updates the model. Override this method for
	 * special shore up abilities.
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

	

	public void decreaseActionLeft() {
		actionsLeft.setValue(actionsLeft.getValue() - 1);
		if (actionsLeft.get() <0 ) throw 
			new RuntimeException("negative number after decreasing the actions");
	}

	/**
	 * Gives a treasure card (with ability TREASURE) to another playerType and
	 * updates all the model attributes (Player' s actions left) according to
	 * the game's rules. In case no model update (except the ownership update of
	 * the card) is desired use the {@link setGiveCard} method.
	 * 
	 * The method does not validate the game rules, and gives the card
	 * unconditionally. Use the {@link canGiveCard} method to ensure that game
	 * rules are met
	 * 
	 * @param playerType
	 *            the playerType to receice the card
	 * @param card
	 *            the card to give
	 */
	public void giveCard(Player player, TreasuryCard card) {
		if (!this.getTreasuryCards().contains(card))
			throw new IllegalArgumentException(this + " does not have tha card");
		if (card.getType().getAbility() != Ability.TREASURE)
			throw new IllegalArgumentException(card + " is not of type TREASURE");
		setGiveCard(player, card);
		this.decreaseActionLeft();

	}

	/**
	 * Ensures that this playerType can give the provided card to his co-playerType
	 * according to the game rules. Override this method in order to provide
	 * specific game rules. As default a playerType can give to his co-playerType the
	 * card when 1) they are at the same tile 2) the giver has actions
	 * 
	 * @param playerType
	 *            the playerType to receice the card
	 * @param card the treasure card to give
	 * @return true if the card can be given
	 */
	public boolean canGiveCard(Player player, TreasuryCard card) {
		assert this.getTreasuryCards().contains(card) : this + " does not have that card";
		assert card.getType().getAbility() == Ability.TREASURE :card + " is not of type TREASURE";
		
		if (actionsLeft.getValue() == 0) return false;
		if (!this.getPiece().getIsland().equals(player.getPiece().getIsland()))return false;
		return true;
	}

	/**
	 * gives the specified card to the provided playerType unconditionally, and does
	 * not apply any game rules.
	 * 
	 * @param playerType
	 *            the playerType to receice the card
	 * @param card
	 *            the card to give
	 */
	public void setGiveCard(Player player, TreasuryCard card) {
		this.getBase().removeCard(card);
		player.addCard(card);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((playerId == null) ? 0 : playerId.hashCode());
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
		Player other = (Player) obj;
		if (playerId == null) {
			if (other.playerId != null)
				return false;
		} else if (!playerId.equals(other.playerId))
			return false;
		return true;
	}
	
	public void setActionsLeft(int actionsLeft) {
		this.actionsLeft.setValue(actionsLeft);
	}
	
	

}
