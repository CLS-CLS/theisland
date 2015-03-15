package cls.island.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import cls.island.control.Config;
import cls.island.control.Options;
import cls.island.control.PlayerAndColor;
import cls.island.model.IslandGrid.Direction;
import cls.island.model.player.Player;
import cls.island.model.player.PlayerFactory;
import cls.island.utils.LocCalculator;
import cls.island.utils.LocCalculator.Grid;
import cls.island.view.component.actionsleft.ActionsLeft;
import cls.island.view.component.island.Island;
import cls.island.view.component.island.Island.IslandName;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.piece.PieceColor;
import cls.island.view.component.player.base.PlayerBase;
import cls.island.view.component.treasurebag.TreasureBag;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.TreasuryCard.ViewStatus;
import cls.island.view.component.treasury.card.Type;
import cls.island.view.component.treasury.card.Type.Ability;
import cls.island.view.component.treasury.pile.TreasuryPile;
import cls.island.view.component.treasury.pile.TreasuryPile.PileType;
import cls.island.view.component.waterlevel.WaterLevel;

public class GameModel {
	private Map<PieceColor, Island> colorToIsland = new HashMap<>();
	private static final int SANDBAG_CARD_QUANTITY = 2;
	private static final int HELICOPTER_CARD_QUANTITY = 3;
	private static final int WATER_RISE_CARD_QUANTITY = 3;
	private static final int TREASURY_CARD_QUANTITY = 5;
	public static final int DRAW_CARDS_PER_TURN = 2;
	public static final int MAX_CARDS_ALLOWED_IN_HAND = 5;
	private static final int MAX_WATER_LEVEL = 9;

	private final Config config;
	private Options options;

	private WaterLevel waterLevel;
	private List<TreasuryCard> treasuryCards = new ArrayList<>();
	private List<Island> islands = new ArrayList<>();
	private IslandGrid<Island> islandGrid = new IslandGrid<>(6, 6);
	private List<Player> players = new ArrayList<>();
	private int currentTurn = 0;

	private List<Island> discardedIslandsToFlood = new ArrayList<>();
	private List<Island> islandsToFlood = new ArrayList<>();

	private TreasuryPile treasuryPile;
	private TreasureBag treasureBag;
	private ActionsLeft actionsLeft;

	public GameModel(Options options, Config config) {
		this.config = config;
		this.options = options;
		initIslands();
		initColorToIslandMap();
		initTreasuryPile();
		initTreasuryCards();
		initPlayers();
		initTreasuryBag();
		initActionLeft();
	}

	private void initActionLeft() {
		actionsLeft = new ActionsLeft();
	}

	public ActionsLeft getActionsLeft() {
		return actionsLeft;
	}

	private void initTreasuryBag() {
		this.treasureBag = new TreasureBag(config.earth, config.chalice, config.fire, config.wind);

	}

	private void initColorToIslandMap() {
		for (Island island : islands) {
			if (island.getName().equals(Island.IslandName.SilverGate)) {
				colorToIsland.put(PieceColor.WHITE, island);
			}
			if (island.getName().equals(Island.IslandName.BronzeGate)) {
				colorToIsland.put(PieceColor.RED, island);
			}
			if (island.getName().equals(Island.IslandName.CopperGate)) {
				colorToIsland.put(PieceColor.GREEN, island);
			}
			if (island.getName().equals(Island.IslandName.FoolsLanding)) {
				colorToIsland.put(PieceColor.BLUE, island);
			}
			if (island.getName().equals(Island.IslandName.GoldGate)) {
				colorToIsland.put(PieceColor.YELLOW, island);
			}
			if (island.getName().equals(Island.IslandName.IronGate)) {
				colorToIsland.put(PieceColor.BLACK, island);
			}
		}
	}

	public void newGame() {
		setUpRandomTiles();
		Collections.shuffle(treasuryCards);
		List<TreasuryCard> clonedTreasureCards = new ArrayList<>(treasuryCards);
		// remove 2* number of players not water cards
		List<TreasuryCard> initialCards = new ArrayList<>();
		for (TreasuryCard trCard : treasuryCards) {
			if (!trCard.getType().equals(Type.WATER_RISE)) {
				initialCards.add(trCard);
				clonedTreasureCards.remove(trCard);
				if (initialCards.size() == getPlayers().size() * 2) {
					break;
				}
			}
		}
		treasuryCards = clonedTreasureCards;
		Collections.shuffle(treasuryCards);

		treasuryCards.addAll(initialCards);

		for (TreasuryCard treasuryCard : treasuryCards) {
			treasuryPile.addToPile(treasuryCard, PileType.NORMAL);
		}

		waterLevel = new WaterLevel(options.getFloodStartingLevel(), config.waterLevelImage, config.waterLevelMarkerImage);

		for (Player player : players) {
			player.setToIsland(colorToIsland.get(player.getPiece().getColor()));
		}

		islandsToFlood.addAll(islands);
		Collections.shuffle(islandsToFlood);

		actionsLeft.setPlayer(getCurrentTurnPlayer());

		// draw six flood cards
		for (int i = 0; i < 6; i++) {
			Island island = getNextIslantToFlood();
			floodIsland(island);
		}
	}

	private void initPlayers() {
		int index = 0;
		List<PlayerAndColor> players = options.getPlayers();
		Collections.shuffle(players);
		for (PlayerAndColor playerType : players) {
			Image playerBaseImg = config.playerCardHolder;
			this.players.add(PlayerFactory.createPlayer(playerType.getPlayerType(), new Piece(playerType.getPlayerType().name(),
					playerType.getPieceColor()),
					new PlayerBase(playerType, playerBaseImg, config.getPlayerImage(playerType.getPlayerType()), index)));
			index++;
		}
	}

	private void initTreasuryPile() {
		treasuryPile = new TreasuryPile("Treasury Cards", Color.GREEN);
	}

	private void initTreasuryCards() {
		for (Type type : Type.values()) {
			int repeats = 0;
			if (type.getAbility() == Ability.TREASURE) {
				repeats = TREASURY_CARD_QUANTITY;
			} else if (type == Type.WATER_RISE) {
				repeats = WATER_RISE_CARD_QUANTITY;
			} else if (type == Type.HELICOPTER) {
				repeats = HELICOPTER_CARD_QUANTITY;
			} else if (type == Type.SANDBAGS) {
				repeats = SANDBAG_CARD_QUANTITY;
			}
			for (int i = 0; i < repeats; i++) {
				treasuryCards.add(new TreasuryCard(config.getTreasureCard(type), config.islandBackCard,
						new TreasuryCard.Model(type, ViewStatus.FACE_UP), config.treasuryCardUseImg,
						config.treasuryCardDiscardImg));
			}
		}
	}

	// TODO maybe the islands should be passed in Constructor from external
	// controller
	private void initIslands() {
		for (Island.IslandName name : Island.IslandName.values()) {
			Type treasure = null;
			if (name == IslandName.CaveOfEmbers)
				treasure = Type.CRYSTAL_OF_FIRE;
			if (name == IslandName.CaveOfShadows)
				treasure = Type.CRYSTAL_OF_FIRE;
			if (name == IslandName.CoralPalace)
				treasure = Type.OCEAN_CHALICE;
			if (name == IslandName.TidalPalace)
				treasure = Type.OCEAN_CHALICE;
			if (name == IslandName.HowlingGarden)
				treasure = Type.STATUE_OF_WIND;
			if (name == IslandName.WhisperingGarden)
				treasure = Type.STATUE_OF_WIND;
			if (name == IslandName.TempleOfTheMoon)
				treasure = Type.EARTH_STONE;
			if (name == IslandName.TempleOfTheSun)
				treasure = Type.EARTH_STONE;
			if (treasure == null) {
				islands.add(new Island(config.getIslandTilesImages().get(name.name()), new Island.Model(new Grid(0, 0),
						new Piece[4], treasure, false, name, false, false, LocCalculator.getInstance())));
			} else {
				islands.add(new Island(config.getIslandTilesImages().get(name.name()), new Island.Model(new Grid(0, 0),
						new Piece[4], treasure, false, name, false, false, LocCalculator.getInstance()), config
						.getTreasureImage(treasure)));
			}
		}
	}

	private void setUpRandomTiles() {
		Collections.shuffle(islands);
		int islandIndex = 0;
		LevelDesign ld = new LevelDesign();
		boolean[][] level = ld.getLevelAsArray(0);
		for (int row = 0; row < LevelDesign.ROWS; row++) {
			for (int col = 0; col < LevelDesign.COLUMNS; col++) {
				if (level[row][col]) {
					islands.get(islandIndex).setGrid(row, col);
					islandGrid.addElement(islands.get(islandIndex), row, col);
					islandIndex++;
				}
			}
		}
	}

	public List<TreasuryCard> getTreasuryCards() {
		return new ArrayList<TreasuryCard>(treasuryCards);
	}

	public boolean isAdjacentIslands(Island from, Island to) {
		Direction[] directions = new Direction[] { Direction.LEFT, Direction.UP, Direction.RIGHT, Direction.DOWN };
		return islandGrid.isAdjacent(from, to, directions);
	}

	public List<Island> getIslands() {
		return new ArrayList<Island>(islands);
	}

	public TreasuryPile getTreasuryPile() {
		return treasuryPile;
	}

	/**
	 * 
	 * @return the playerType that will play after this turn is over.
	 */
	public Player getNextTurnPlayer() {
		int nextPlayerIndex = (currentTurn == players.size() - 1) ? 0 : currentTurn + 1;
		return players.get(nextPlayerIndex);
	}

	public List<Player> getPlayers() {
		return new ArrayList<>(players);
	}

	/**
	 * Progress to next turn
	 * 
	 * @return The playerType that is now playing
	 */
	public Player nextTurn() {
		currentTurn = (currentTurn == players.size() - 1) ? 0 : currentTurn + 1;
		Player currentPlayer = players.get(currentTurn);
		actionsLeft.setPlayer(currentPlayer);
		return currentPlayer;
	}

	public Player getCurrentTurnPlayer() {
		return players.get(currentTurn);
	}

	/**
	 * Removes the card from the players pile and puts the card in the discarded
	 * pile
	 * 
	 * @param playerType
	 *            the playerType who this card belongs to
	 * @param treasuryCard
	 *            the card to be discarded
	 */
	public void discardCard(Player player, TreasuryCard treasuryCard) {
		treasuryPile.addToPile(treasuryCard, PileType.DISCARD);
		player.getBase().removeCard(treasuryCard);
	}

	/**
	 * 
	 * @param playerType
	 * @param card
	 * @return the index of the card in the playerType 's hand
	 */
	public int giveCardToPlayerFromTreasurePile(Player player, TreasuryCard card) {
		if (!treasuryPile.containsInPile(card)) {
			throw new IllegalArgumentException("treasuryBase does not have the card " + card);
		}
		treasuryPile.removeFromPile(card, PileType.NORMAL);
		return player.addCard(card);
	}

	public Island getNextIslantToFlood() {
		Island toFloodIsland = islandsToFlood.get(islandsToFlood.size() - 1);
		return toFloodIsland;
	}

	/**
	 * floods an island and updates the floodlist
	 * 
	 * @param island
	 */
	public void floodIsland(Island island) {
		if (island.isFlooded())
			throw new IllegalStateException(island + " is already flooded");
		islandsToFlood.remove(island);
		discardedIslandsToFlood.add(island);
		island.flood();
	}

	public void sinkIsland(Island island) {
		if (island.isSunk())
			throw new IllegalStateException(island + " is already sunk");
		discardedIslandsToFlood.remove(island);
		islandsToFlood.remove(island);
		island.sink();
	}

	public boolean hasIslandToFlood() {
		return islandsToFlood.size() > 0;
	}

	public void shuffleDiscardedAndPutBackToNormalPile() {
		Collections.shuffle(discardedIslandsToFlood);
		islandsToFlood.addAll(discardedIslandsToFlood);
		discardedIslandsToFlood.clear();
	}

	/**
	 * convenient method
	 * 
	 * @return
	 */
	public float increaseFloodMeter() {
		waterLevel.setWaterLevel(waterLevel.getWaterLevel() + 1);
		return waterLevel.getWaterLevel();
	}

	public WaterLevel getWaterLevel() {
		return waterLevel;
	}

	public int getNumberOfIslandsToSink() {
		int floodMeter = waterLevel.getWaterLevel();
		if (floodMeter == 0 || floodMeter == 1) {
			return 2;
		}
		if (floodMeter == 2 || floodMeter == 3 || floodMeter == 4) {
			return 3;
		}
		if (floodMeter == 5 || floodMeter == 6) {
			return 4;
		}
		if (floodMeter == 7 || floodMeter == 8) {
			return 5;
		}
		return 6;
	}

	public IslandGrid<Island> getIslandGrid() {
		return islandGrid;
	}

	public boolean checkLooseCondition(LooseCondition type, Object... infos) {
		switch (type) {
		case MAX_WATER_LEVEL_REACHED:
			if (waterLevel.getWaterLevel() >= MAX_WATER_LEVEL) {
				return true;
			}
		case FOOLS_LANDING_LOST:
			for (Island island : getIslands()) {
				if (island.getName() == IslandName.FoolsLanding && island.isSunk()) {
					return true;
				}
			}
		case TREASURE_SUNK:
			List<Type> remainingTreasures = Type.getTypesWithAbility(Ability.TREASURE);
			remainingTreasures.removeAll(getTreasureBag().getAcquiredTreaures());
			for (Type remainingTreasure : remainingTreasures) {
				int sinked = 0;
				for (Island island : islands) {
					if (island.hasTreasure() && island.getTreasure() == remainingTreasure && island.isSunk()) {
						sinked++;
					}
				}
				if (sinked == 2) {
					if (infos.length > 0) {
						infos[0] = remainingTreasure.name();
					}
					return true;
				}
			}
		default:
			break;
		}
		return false;
	}

	/**
	 * checks if any of the loose condition is met.
	 * 
	 * @param infos
	 *            object to store more information about the lost conditions
	 * @return the kind of the loose condition. <code> null </code> if none is
	 *         met.
	 */
	public LooseCondition findLooseConditional(Object... infos) {

		// Lost because of water level
		if (waterLevel.getWaterLevel() >= MAX_WATER_LEVEL) {
			return LooseCondition.MAX_WATER_LEVEL_REACHED;
		}
		// lost because a treasure is sunk
		List<Type> remainingTreasures = Type.getTypesWithAbility(Ability.TREASURE);
		remainingTreasures.removeAll(getTreasureBag().getAcquiredTreaures());
		for (Type remainingTreasure : remainingTreasures) {
			int sinked = 0;
			for (Island island : islands) {
				if (island.hasTreasure() && island.getTreasure() == remainingTreasure && island.isSunk()) {
					sinked++;
				}
			}
			if (sinked == 2) {
				if (infos.length > 0) {
					infos[0] = remainingTreasure.name();
				}
				return LooseCondition.TREASURE_SUNK;
			}
		}
		return null;
	}

	public TreasureBag getTreasureBag() {
		return treasureBag;
	}

	public void setTreasureBag(TreasureBag treasureBag) {
		this.treasureBag = treasureBag;
	}

	/**
	 * Collects a treasure for the current turn playerType. 1)discards all the
	 * collection cards from the playerType hand 2)Adds the collected treasure to
	 * the treasure bug 3)decreases the actions of the playerType by 1.
	 * 
	 * @param collectionCards
	 *            the cards that form the collection.
	 * @return the type of the treasure collected.
	 */
	public Type collectTreasure(List<TreasuryCard> collectionCards) {
		Player currentPlayer = getCurrentTurnPlayer();
		getTreasureBag().addTreasure(collectionCards.get(0).getType());
		currentPlayer.decreaseActionLeft();
		for (TreasuryCard card : collectionCards) {
			discardCard(currentPlayer, card);
		}
		return collectionCards.get(0).getType();
	}

	/**
	 * finds all the cards which the current playerType can trade, and the available
	 * players the playerType can trade with.
	 * 
	 * @param cards
	 *            a list that will be filled with the cards that can be traded.
	 * @param players
	 *            list that will be filled with the players the current playerType
	 *            can trade with.
	 */
	public void findCurrentPlayerEligibleCardsAndPlayersToTrade(List<TreasuryCard> cards, List<Player> players) {
		for (TreasuryCard card : getCurrentTurnPlayer().getTreasuryCards()) {
			if (card.getType().getAbility() == Ability.TREASURE) {
				cards.add(card);
			}
		}
		if (cards.size() == 0) {
			return;
		}
		// Tests which players can receive the card
		for (Player player : getPlayers()) {
			if (player.equals(getCurrentTurnPlayer()))
				continue;
			if (getCurrentTurnPlayer().canGiveCard(player, cards.get(0))) {
				players.add(player);
			}
		}
	}

	/**
	 * Convenient method. Checks if the current turn playerType can give cards to
	 * another playerType. The method uses the
	 * {@link findCurrentPlayerEligibleCardsAndPlayersToTrade} to find all the
	 * players and cards that can be traded.
	 * 
	 * @return
	 */
	public boolean canTrade() {
		List<TreasuryCard> cards = new ArrayList<>();
		List<Player> players = new ArrayList<>();
		findCurrentPlayerEligibleCardsAndPlayersToTrade(cards, players);
		return cards.size() > 0 && players.size() > 0;
	}

	/**
	 * Checks if the treasure can be collected by the specified playerType. A
	 * treasure can be collected when : 1) the playerType has actions left 2) the
	 * playerType has a treasure set 3) is on the island containing the same
	 * treasure with his treasure set.
	 * 
	 * @param playerType
	 *            the playerType the check will be performed against
	 * @return
	 */
	public boolean canCollectTreasure(Player player) {
		boolean canCollect = true;
		List<TreasuryCard> collectionCards = getTreasureCollection(player);
		Type treasureOnIsland = player.getPiece().getIsland().getTreasure();
		if (!player.hasAction() || collectionCards.size() == 0 || !collectionCards.get(0).getType().equals(treasureOnIsland)
				|| getTreasureBag().isTreasureCollected(collectionCards.get(0).getType())) {
			canCollect = false;
		}
		return canCollect;
	}

	/**
	 * Finds all the cards that forms a collection of 4 cards of the same
	 * treasure type
	 * 
	 * @param playerType
	 *            the playerType holding the cards to check.
	 * @return the cards forming the treasure collection
	 */
	public List<TreasuryCard> getTreasureCollection(Player player) {
		ArrayList<TreasuryCard> collectionCards = new ArrayList<>();
		int[] collectionQuantity = new int[10];
		for (TreasuryCard card : player.getTreasuryCards()) {
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
			for (TreasuryCard treasuryCard : player.getTreasuryCards()) {
				if (treasuryCard.getType() == Type.values()[index]) {
					collectionCards.add(treasuryCard);
					if (collectionCards.size() == 4)
						break;
				}
			}
		}
		return collectionCards;
	}

	/**
	 * Checks if the playerType has met the win conditions
	 * 
	 * @return true if the playerType has won, false otherwise
	 */
	public boolean hasWon() {
		Island island = getCurrentTurnPlayer().getPiece().getIsland();
		if (island.getName() == IslandName.FoolsLanding
				&& island.getPiecesAndPositions().keySet().size() == getPlayers().size()
				&& getTreasureBag().getAcquiredTreaures().size() == 4) {
			return true;
		}
		return false;
	}

	/**
	 * gives back to playerType the specified discarded card
	 * 
	 * @param cardHolder
	 * @param selectedTreasureCard
	 * @return the index of the card in the playerType's hand
	 */
	public int undiscardCard(Player cardHolder, TreasuryCard discardedCard) {
		treasuryPile.removeFromPile(discardedCard, PileType.DISCARD);
		return cardHolder.addCard(discardedCard);

	}

}
