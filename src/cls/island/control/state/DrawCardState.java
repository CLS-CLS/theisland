package cls.island.control.state;

import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.action.UnrevertableAction;
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
	private int numberOfDrawCards;
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
		this(0, gameController, islandScreen, gameModel, false);
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
		numberOfDrawCards++;
		//after this state is triggered, no undo is allowed
		gameController.resetUndoableActions();
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
			System.out.println("water rise : " + islandScreen.c_isAnimationInProgress());
			islandScreen.c_WaterCardDrawnPopUp();
			waterRised = true;
			gameModel.discardCard(currentPlayer, treasuryCard);
			gameModel.increaseFloodMeter();
			if (gameModel.checkLooseCondition(LooseCondition.MAX_WATER_LEVEL_REACHED)) {
				return new GameLostState(LooseCondition.MAX_WATER_LEVEL_REACHED, gameController,
						islandScreen, gameModel, this);
			}
			gameController.executeAction(new UnrevertableAction() {
				
				@Override
				public void execute() {
					islandScreen.c_discardPlayerCard(currentPlayer.getBase().getComponent(),
							treasuryCard.getComponent());
					gameModel.shuffleDiscardedAndPutBackToNormalPile();
					gameModel.getTreasuryPile().getComponent().rearrangePiles();
				}
			});
			return this;

		}
		if (currentPlayer.getTreasuryCards().size() > 5) {
			return (new UseOrDiscardCardState(gameController, islandScreen, gameModel,
					DrawCardState.this));
		}
		return this;
	}

	@Override
	public void end() {
	}

}
