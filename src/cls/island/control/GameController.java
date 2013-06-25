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
		NEXT_TURN, MOVE, SHORE_UP, TRADE, OK, COLLECT_TREASURE, USE, DISCARD;
	}

	volatile private GameState gameState;
	private MainController mainController;
	private IslandScreen islandScreen;
	private final GameModel gameModel;

	public GameController(MainController mainController, GameModel gameModel) {
		this.mainController = mainController;
		this.gameModel = gameModel;
	}

	public void buttonPressed(final ButtonAction action) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				GameState state = gameState.buttonPressed(action);
				if (state != null) {
					setGameState(state);
				}
			}
		}).start();
	}

	public void mouseClicked(final MouseEvent event) {
		islandScreen.c_setAnimationInProgress(true);
		new Thread(new Runnable() {

			@Override
			public void run() {
				GameState state = gameState.mouseClicked(event);
				if (state != null) {
					setGameState(state);
				} else {
					islandScreen.c_setAnimationInProgress(false);
				}

			}
		}).start();

	}

	private void setGameState(GameState state) {
		islandScreen.c_setAnimationInProgress(true);
		if (state == null)
			throw new IllegalArgumentException();
		this.gameState = state;
		GameState newState = gameState.start();
		if (newState != null) {
			setGameState(newState);
		}
		islandScreen.c_setAnimationInProgress(false);
	}

	public void backToMainScreen() {
		mainController.goToMainScreen(islandScreen);

	}

	public void startNewGame() {
		if (islandScreen == null)
			throw new RuntimeException("IslandScrren should not be null");
		gameState = new NormalState(this, islandScreen, gameModel);
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

	public void setIslandScreen(IslandScreen islandScreen) {
		this.islandScreen = islandScreen;
	}

	public GameState getGameState() {
		return gameState;
	}

	public MainController getMainController() {
		return mainController;
	}
}
