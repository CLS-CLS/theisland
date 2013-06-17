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
import cls.island.utils.ViewUtils;
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
		initPlayers(options.getPlayers());
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
		setTreasureBag(new TreasureBag());

	}

	private void initColorToIslandMap() {
		for (Island island : islands) {
			if (island.getName().equals(Island.IslandName.SilverGate.name())) {
				colorToIsland.put(PieceColor.WHITE, island);
			}
			if (island.getName().equals(Island.IslandName.BronzeGate.name())) {
				colorToIsland.put(PieceColor.RED, island);
			}
			if (island.getName().equals(Island.IslandName.CopperGate.name())) {
				colorToIsland.put(PieceColor.GREEN, island);
			}
			if (island.getName().equals(Island.IslandName.FoolsLanding.name())) {
				colorToIsland.put(PieceColor.BLUE, island);
			}
			if (island.getName().equals(Island.IslandName.GoldGate.name())) {
				colorToIsland.put(PieceColor.YELLOW, island);
			}
			if (island.getName().equals(Island.IslandName.IronGate.name())) {
				colorToIsland.put(PieceColor.BROWN, island);
			}
		}
	}

	public void setUpNewGame() {
		setUpRandomTiles();
		Collections.shuffle(treasuryCards);

		// ensure the the first n*2 cards(where n the number of players )are not
		// flood cards. 1)Remove the flood cards if any in the first n*2
		// positions
		// 2) add them back after the n*2 position.
		ArrayList<TreasuryCard> treasuryCardsMock = new ArrayList<>(treasuryCards);
		List<TreasuryCard> removedCards = new ArrayList<>();
		for (int i = treasuryCardsMock.size() - 1; i >= treasuryCardsMock.size() - 6; i--) {
			if (treasuryCardsMock.get(i).getType().getAbility() == Ability.DANGER) {
				removedCards.add(treasuryCards.remove(i));
			}
		}
		int minimumAddIndex = getPlayers().size() * DRAW_CARDS_PER_TURN;
		for (TreasuryCard card : removedCards) {
			treasuryCards.add(ViewUtils.getRandomInt(0, treasuryCards.size() - minimumAddIndex), card);
		}

		for (TreasuryCard treasuryCard : treasuryCards) {
			treasuryPile.addToPile(treasuryCard, PileType.NORMAL);
		}

		waterLevel = new WaterLevel(options.getFloodStartingLevel(), config.getWaterLevelImage(),
				config.getWaterLevelMarkerImage());

		for (Player player : players) {
			player.setToIsland(colorToIsland.get(player.getPiece().getColor()));
		}

		islandsToFlood.addAll(islands);
		Collections.shuffle(islandsToFlood);
		
		actionsLeft.setPlayer(getCurrentTurnPlayer());
	}

	private void initPlayers(List<PlayerAndColor> players) {
		int index = 0;
		for (PlayerAndColor playerType : options.getPlayers()) {
			Image playerBaseImg = config.getPlayerCardHolder();
			Image playerImg = config.getLolImagePlayer();
			this.players.add(PlayerFactory.createPlayer(
					playerType.getPlayer(),
					new Piece(config.getPieceImage(playerType.getColor()), playerType.getPlayer().name(), playerType
							.getColor()), new PlayerBase(playerType, playerBaseImg, playerImg, index)));
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
				treasuryCards.add(new TreasuryCard(config.getTreasureIslandImgFront(type),
						config.getTreasureIslandImgBack(), new TreasuryCard.Model(type, ViewStatus.FACE_UP), config
								.getTreasuryCardUseImg(), config.getTreasuryCardDiscardImg()));
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

			islands.add(new Island(config.getIslandTilesImages().get(name.name()), new Island.Model(new Grid(0, 0),
					new Piece[4], treasure, false, name.name(), false, false, LocCalculator.getInstance())));
		}
	}

	private void setUpRandomTiles() {
		Collections.shuffle(islands);
		int row = 0;
		int colStart = 2;
		int colEnd = 3;
		int col = colStart;
		for (int i = 0; i < 12; i++) {
			if (col > colEnd) {
				colStart--;
				colEnd++;
				row++;
				col = colStart;
			}
			islands.get(i).setGrid(row, col);
			islandGrid.addElement(islands.get(i), row, col);
			col++;
		}
		col = colStart;
		row++;
		for (int i = 12; i < 24; i++) {
			if (col > colEnd) {
				colStart++;
				colEnd--;
				row++;
				col = colStart;
			}
			islands.get(i).setGrid(row, col);
			islandGrid.addElement(islands.get(i), row, col);
			col++;
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
	 * @return the player that will play after this turn is over.
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
	 * @return The player that is now playing
	 */
	public Player nextTurn() {
		currentTurn = (currentTurn == players.size() - 1) ? 0 : currentTurn + 1;
		Player currentPlayer =  players.get(currentTurn);
		actionsLeft.setPlayer(currentPlayer);
		return currentPlayer;
	}

	public Player getCurrentTurnPlayer() {
		return players.get(currentTurn);
	}

	public void discardCard(Player player, TreasuryCard treasuryCard) {
		treasuryPile.addToPile(treasuryCard, PileType.DISCARD);
		player.getBase().removeCard(treasuryCard);
	}

	/**
	 * 
	 * @param player
	 * @param card
	 * @return the index of the card in the player 's hand
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

	/**
	 * checks if any of the loose condition is met.
	 * 
	 * @return the kind of the loose condition. <code> null </code> if none is
	 *         met.
	 */
	public LooseCondition checkLooseCondition() {

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
	 * Collects a treasure for the current turn player.
	 * 1)discards all the collection cards from the player hand 
	 * 2)Adds the collected treasure to the treasure bug
	 * 3)decreases the actions of the  player by 1.
	 * 
	 * @param collectionCards
	 *            the cards that form the collection.
	 */
	public void collectTreasure(List<TreasuryCard> collectionCards) {
		Player currentPlayer = getCurrentTurnPlayer();
		getTreasureBag().addTreasure(collectionCards.get(0).getType());
		currentPlayer.decreaseActionLeft();
		for (TreasuryCard card : collectionCards) {
			discardCard(currentPlayer, card);
		}
	}

}
