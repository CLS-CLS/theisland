package cls.island.model.player;

import java.util.ArrayList;
import java.util.List;

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
	 * Checks if the player can make a valid standard move. Players
	 * with special move abilities should override this method.
	 * 
	 * @param fromIsland the island the player is going to move from 
	 * @param toIsland the island the player will go
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
	 * checks if the player can shore up the specific island. The checks are: 1)
	 * if the island is adjacent to player 2) the island is not sunk. 3) the
	 * island is the same island the player is on
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

	/**
	 * Convenient method. Finds all the cards that forms a collection of 4 cards
	 * of the same treasure type
	 * 
	 * @return the cards forming the treasure collection
	 */
	public List<TreasuryCard> getTreasureCollection() {
		ArrayList<TreasuryCard> collectionCards = new ArrayList<>();
		int[] collectionQuantity = new int[10];
		for (TreasuryCard card : getTreasuryCards()) {
			if (card.getType().getAbility() == Ability.TREASURE) {
				collectionQuantity[card.getType().ordinal()]++;
			}
		}
		int index = -1;
		for (int i = 0; i < 10; i++) {
			if (collectionQuantity[i] >= 4) {
				index = i;
				break;
			}
		}
		if (index != -1) {
			for (TreasuryCard treasuryCard : getTreasuryCards()) {
				if (treasuryCard.getType() == Type.values()[index]) {
					collectionCards.add(treasuryCard);
					if (collectionCards.size() == 4)
						break;
				}
			}
		}
		return collectionCards;

	}

	public void decreaseActionLeft() {
		actionsLeft.setValue(actionsLeft.getValue() - 1);
	}

	/**
	 * Gives a treasure card (with ability TREASURE) to another player and
	 * updates all the model attributes (Player' s actions left) according to
	 * the game's rules. In case no model update (except the ownership update of
	 * the card) is desired use the {@link setGiveCard} method.
	 * 
	 * The method does not validate the game rules, and gives the card
	 * unconditionally. Use the {@link canGiveCard} method to ensure that game
	 * rules are met
	 * 
	 * @param player
	 *            the player to receice the card
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
	 * Ensures that this player can give the provided card to his co-player
	 * according to the game rules. Override this method in order to provide
	 * specific game rules. As default a player can give to his co-player the
	 * card when 1) they are at the same tile 2) the giver has actions
	 * 
	 * @param player
	 *            the player to receice the card
	 * @param card
	 *            the card to give
	 * @return true if the card can be given
	 */
	public boolean canGiveCard(Player player, TreasuryCard card) {
		if (!this.getTreasuryCards().contains(card))
			throw new IllegalArgumentException(this + " does not have tha card");
		if (card.getType().getAbility() != Ability.TREASURE)
			throw new IllegalArgumentException(card + " is not of type TREASURE");
		if (actionsLeft.getValue() ==0) return false;
		if (!this.getPiece().getIsland().equals(player.getPiece().getIsland()))return false;
		return true;
	}

	/**
	 * gives the specified card to the provided player unconditionally, and does
	 * not apply any game rules.
	 * 
	 * @param player
	 *            the player to receice the card
	 * @param card
	 *            the card to give
	 */
	public void setGiveCard(Player player, TreasuryCard card) {
		this.getBase().removeCard(card);
		player.addCard(card);
	}

}
