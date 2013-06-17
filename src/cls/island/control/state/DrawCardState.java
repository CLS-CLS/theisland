package cls.island.control.state;

import java.util.List;

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

	private void drawCard() {

		if (numberOfDrawCards > GameModel.DRAW_CARDS_PER_TURN) {
			if (waterRised){
				waterRised = false;
				gameController.setGameState(new WaterRiseState(gameController, islandScreen, gameModel, DrawCardState.this));
			}else {
				gameController.setGameState(new IslandTurnState(gameController, islandScreen,
						gameModel, DrawCardState.this));
				
			}
			return;
		}

		islandScreen.setAnimationInProgress(true);

		final TreasuryCard treasuryCard = getTopPileCard();

		if (treasuryCard.getType() == Type.WATER_RISE) {
			islandScreen.setAnimationInProgress(false);
			islandScreen.c_WaterCardDrawnPopUp();
			waterRised = true;
			gameModel.discardCard(currentPlayer, treasuryCard);
			gameModel.increaseFloodMeter();
			islandScreen.c_discardPlayerCard(currentPlayer.getBase().getComponent(),
					treasuryCard.getComponent());
			gameModel.shuffleDiscardedAndPutBackToNormalPile();
			gameModel.getTreasuryPile().getComponent().rearrangePiles();
			
			
//			gameController.setGameState(new WaterRiseState(gameController, islandScreen, gameModel,
//					treasuryCard, DrawCardState.this));
			gameController.setGameState(createGameState());

		} else if (currentPlayer.getTreasuryCards().size() > 5) {
			islandScreen.setAnimationInProgress(false);
			gameController.setGameState(new ChooseDiscardCardState(gameController, islandScreen,
					gameModel, DrawCardState.this));

		} else {
			islandScreen.setAnimationInProgress(false);
			gameController.setGameState(DrawCardState.this.createGameState());
		}

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
	public void mouseClicked(MouseEvent event) {
	}

	@Override
	public void buttonPressed(ButtonAction action) {

	}

	public int getNumberOfDrawCards() {
		return numberOfDrawCards;
	}

	@Override
	public GameState getFromState() {
		return null;
	}

	@Override
	public void start() {
		drawCard();
	}

	@Override
	public GameState createGameState() {
		return new DrawCardState(getNumberOfDrawCards() + 1, gameController, islandScreen,
				gameModel, waterRised);

	}

}