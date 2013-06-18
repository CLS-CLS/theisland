package cls.island.control;

import javafx.scene.input.MouseEvent;
import cls.island.control.state.NormalState;
import cls.island.model.GameModel;
import cls.island.model.player.Player;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.pile.TreasuryPile;
import cls.island.view.screen.IslandScreen;

public class GameController {
	public static enum ButtonAction {
		NEXT_TURN, MOVE, SHORE_UP, TRADE, OK, COLLECT_TREASURE, USE, DISCARD; // OK when shore up is completed
												// after water rise card is
												// drawn
	}

	volatile private GameState gameState;
	private MainController mainController;
	private IslandScreen islandScreen;
	private final GameModel gameModel;

	public GameController(MainController mainController, GameModel gameModel,
			IslandScreen islandScreen) {
		this.mainController = mainController;
		this.gameModel = gameModel;
		this.islandScreen = islandScreen;
		gameState = new NormalState(this, islandScreen, gameModel);
	}

	public void buttonPressed(final ButtonAction action) {
		islandScreen.c_setAnimationInProgress(true);
		new Thread(new Runnable() {
	
			@Override
			public void run() {
				setGameState(gameState.buttonPressed(action));
			}
		}).start();
	}

	public void mouseClicked(final MouseEvent event) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				setGameState(gameState.mouseClicked(event));
			}
		}).start();

	}

	private void setGameState(GameState state) {
		this.gameState = state;
		GameState newState = gameState.start();
		if (newState != null){
			setGameState(newState);
		}
	}

	public void backToMainScreen() {
		mainController.goToMainScreen(islandScreen);

	}

	public void startNewGame() {
		for (Player player : gameModel.getPlayers()) {
			for (int i = 0; i < 2; i++) {
				TreasuryPile treasuryPile = gameModel.getTreasuryPile();
				TreasuryCard treasuryCard = treasuryPile.getTopPileCard();
				gameModel.giveCardToPlayerFromTreasurePile(player, treasuryCard);
				islandScreen.c_moveTreasuryCardFromPileToPlayer(treasuryCard.getComponent(), player
						.getBase().getComponent());
			}
		}
	}

	public GameState getGameState() {
		return gameState;
	}

	public MainController getMainController() {
		return mainController;
	}
}
