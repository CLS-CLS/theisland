package cls.island.control.state;

import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.model.LooseCondition;
import cls.island.model.player.Player;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.Type;
import cls.island.view.component.treasury.pile.TreasuryPile;
import cls.island.view.screen.IslandScreen;

public class DrawCardState implements GameState {

	private final GameController gameController;
	private final IslandScreen islandScreen;
	private final GameModel gameModel;
	private final int numberOfDrawCards;
	private Player currentPlayer;
	private boolean waterRised;

	public DrawCardState(int numberOfDrawCards, GameController gameController,
			IslandScreen islandScreen, GameModel gameModel, boolean waterRised) {
		this.numberOfDrawCards = numberOfDrawCards;
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.waterRised = waterRised;
		currentPlayer = gameModel.getCurrentTurnPlayer();
	}

	public DrawCardState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel) {
		this(1, gameController, islandScreen, gameModel, false);
	}

	private TreasuryCard getTopPileCard() {
		final TreasuryPile treasuryPile = gameModel.getTreasuryPile();

		if (treasuryPile.isPileEmpty()) {
			treasuryPile.replenishNormalPile();
			treasuryPile.getComponent().rearrangePiles();
		}

		final TreasuryCard treasuryCard = treasuryPile.getTopPileCard();
		gameModel.giveCardToPlayerFromTreasurePile(gameModel.getCurrentTurnPlayer(), treasuryCard);
		islandScreen.c_moveTreasuryCardFromPileToPlayer(treasuryCard.getComponent(), currentPlayer
				.getBase().getComponent());
		return treasuryCard;
	}

	@Override
	public GameState mouseClicked(MouseEvent event) {
		return null;
	}

	@Override
	public GameState buttonPressed(ButtonAction action) {
		return null;
	}

	public int getNumberOfDrawCards() {
		return numberOfDrawCards;
	}

	@Override
	public GameState getFromState() {
		return null;
	}

	@Override
	public GameState start() {
		if (numberOfDrawCards > GameModel.DRAW_CARDS_PER_TURN) {
			if (waterRised) {
				waterRised = false;
				return new WaterRiseState(gameController, islandScreen, gameModel,
						DrawCardState.this);
			} else {
				return new IslandTurnState(gameController, islandScreen, gameModel,
						DrawCardState.this);
			}
		}
		final TreasuryCard treasuryCard = getTopPileCard();

		if (treasuryCard.getType() == Type.WATER_RISE) {
			islandScreen.c_setAnimationInProgress(false);
			islandScreen.c_WaterCardDrawnPopUp();
			islandScreen.c_setAnimationInProgress(true);
			waterRised = true;
			gameModel.discardCard(currentPlayer, treasuryCard);
			gameModel.increaseFloodMeter();
			if (gameModel.checkLooseCondition(LooseCondition.MAX_WATER_LEVEL_REACHED)) {
				return new GameLostState(LooseCondition.MAX_WATER_LEVEL_REACHED, gameController,
						islandScreen, gameModel, this);
			}
			islandScreen.c_discardPlayerCard(currentPlayer.getBase().getComponent(),
					treasuryCard.getComponent());
			gameModel.shuffleDiscardedAndPutBackToNormalPile();
			gameModel.getTreasuryPile().getComponent().rearrangePiles();

			return createGameState();

		}
		if (currentPlayer.getTreasuryCards().size() > 5) {
			islandScreen.c_setAnimationInProgress(false);
			return (new ChooseDiscardCardState(gameController, islandScreen, gameModel,
					DrawCardState.this));

		}

		islandScreen.c_setAnimationInProgress(false);
		return DrawCardState.this.createGameState();

	}

	@Override
	public GameState createGameState() {
		islandScreen.disableButtons();
		return new DrawCardState(getNumberOfDrawCards() + 1, gameController, islandScreen,
				gameModel, waterRised);

	}

}
