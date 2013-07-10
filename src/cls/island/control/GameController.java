package cls.island.control;

import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import cls.island.control.state.NormalState;
import cls.island.model.GameModel;
import cls.island.model.player.Player;
import cls.island.utils.concurrent.ThreadUtil;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.pile.TreasuryPile;
import cls.island.view.screen.IslandScreen;

public class GameController {
	public static enum ButtonAction {
		NEXT_TURN, MOVE, SHORE_UP, TRADE, OK, COLLECT_TREASURE, USE, DISCARD, FLY;
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
		if (islandScreen.c_isAnimationInProgress()){
			return;
		}
		System.out.println("mouse Clicked animations is : " + islandScreen.c_isAnimationInProgress());
		islandScreen.c_setAnimationInProgress(true);
		
		ThreadUtil.Runlater(new Runnable() {

			@Override
			public void run() {
				GameState state = gameState.buttonPressed(action);
				if (state != null) {
					setGameState(state);
				}
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						islandScreen.c_setAnimationInProgress(false);

					}
				});
			}
		});
	}

	public void mouseClicked(final MouseEvent event) {
		System.out.println("mouse Clicked animations is : " + islandScreen.c_isAnimationInProgress());
		islandScreen.c_setAnimationInProgress(true);
		ThreadUtil.Runlater(new Runnable() {

			@Override
			public void run() {
				GameState state = gameState.mouseClicked(event);
				if (state != null) {
					setGameState(state);
				}
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						islandScreen.c_setAnimationInProgress(false);

					}
				});
			}
		});

	}

	private void setGameState(GameState state) {
		if (state == null)
			throw new IllegalArgumentException();
		this.gameState = state;
		GameState newState = gameState.start();
		if (newState != null) {
			setGameState(newState);
		}
	}

	public void backToMainScreen() {
		mainController.goToMainScreen(islandScreen);

	}

	public void startNewGame() {
		if (islandScreen == null)
			throw new RuntimeException("IslandScrren should not be null");
		setGameState(new NormalState(this, islandScreen, gameModel));
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

	/**
	 * Finds the player who owns the provided piece
	 * @param piece
	 * @return
	 */
	public Player getPlayerWithPiece(Piece piece) {
		Player resultPlayer = null;
		for (Player player : gameModel.getPlayers()) {
			if (player.getPiece().equals(piece)) {
				resultPlayer = player;
				break;
			}
		}
		return resultPlayer;
	}
}
