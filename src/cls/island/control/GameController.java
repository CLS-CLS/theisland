package cls.island.control;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import cls.island.control.action.Action;
import cls.island.control.action.RevertableAction;
import cls.island.control.state.NormalState;
import cls.island.model.GameModel;
import cls.island.model.player.Player;
import cls.island.utils.ViewUtils;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.TreasuryCardView;
import cls.island.view.component.treasury.pile.TreasuryPile;
import cls.island.view.component.treasury.pile.TreasuryPile.PileType;
import cls.island.view.screen.IslandScreen;

public class GameController {
	public static enum ButtonAction {
		NEXT_TURN, MOVE, SHORE_UP, TRADE, OK, COLLECT_TREASURE, USE, DISCARD, FLY, UNDO, MOVE_OTHER, SEND;
	}

	private GameState gameState;
	private MainController mainController;
	private IslandScreen islandScreen;
	private final GameModel gameModel;
	private List<Action> lastActions = new ArrayList<>();

	/**
	 * used by {@link IslandScreen} to bind the disable function of undo button.
	 */
	private volatile SimpleBooleanProperty undoAction = new SimpleBooleanProperty(false);
	private MediaPlayer mediaPlayer;

	public GameController(MainController mainController, GameModel gameModel) {
		this.mainController = mainController;
		this.gameModel = gameModel;
		mediaPlayer = new MediaPlayer(mainController.config.getBackgoundSound());
		mediaPlayer.setVolume(0.15);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
	}

	public void buttonPressed(final ButtonAction action) {
		System.out.println("Animation in progress  = " + islandScreen.c_isAnimationInProgress());
		if (islandScreen.c_isAnimationInProgress()) {
			return;
		}
		islandScreen.c_setAnimationInProgress(true);
		mainController.config.getClickBtnSound().play();

		if (action == ButtonAction.UNDO) {
			gameState.end();
			GameState gameState = ((RevertableAction) lastActions.get(lastActions.size() - 1)).revert();
			lastActions.remove(lastActions.size() - 1);
			undoAction.set(lastActions.size() != 0);
			if (gameState != null) {
				setGameState(gameState);
			}
		} else {
			GameState state = gameState.buttonPressed(action);
			System.out.println("Setting gameState " + (state == null ? "NULL" : state));
			if (state != null) {
				setGameState(state);
			}
		}
		islandScreen.c_setAnimationInProgress(false);
	}

	public void mouseClicked(final MouseEvent event) {
		islandScreen.c_setAnimationInProgress(true);
		mainController.config.getClickSound().play();
		List<Node> parents = ViewUtils.getNodeHierarcyAsList((Node) event.getTarget());
		TreasuryCardView treasuryCardView = (TreasuryCardView) parents.stream().filter((n) -> n instanceof TreasuryCardView)
				.findFirst().orElse(null);
		if (treasuryCardView != null) {
			if (gameModel.getTreasuryPile().containsInDiscardPile(treasuryCardView.getParentModel())) {
				islandScreen.c_showTreasurePilePopUp(PileType.DISCARD);
			} else if (gameModel.getTreasuryPile().containsInPile(treasuryCardView.getParentModel())) {
				islandScreen.c_showTreasurePilePopUp(PileType.NORMAL);
			}
		}

		GameState state = gameState.mouseClicked(event);

		if (state != null) {
			setGameState(state);
		}

		islandScreen.c_setAnimationInProgress(false);

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
		mediaPlayer.stop();
		mainController.goToMainScreen(islandScreen);

	}

	public void startNewGame() {
		if (islandScreen == null)
			throw new RuntimeException("IslandScrren should not be null");
		mediaPlayer.play();

		setGameState(new NormalState(GameController.this, islandScreen, gameModel));
		for (Player player : gameModel.getPlayers()) {
			for (int i = 0; i < 2; i++) {
				TreasuryPile treasuryPile = gameModel.getTreasuryPile();
				TreasuryCard treasuryCard = treasuryPile.getTopPileCard();
				gameModel.giveCardToPlayerFromTreasurePile(player, treasuryCard);
				islandScreen
						.c_moveTreasuryCardFromPileToPlayer(treasuryCard.getComponent(), player.getBase().getComponent());
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
	 * 
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

	public void executeAction(Action action) {
		if (action instanceof RevertableAction) {
			lastActions.add(action);
			undoAction.set(true);
		} else {
			lastActions.clear();
			undoAction.set(false);
		}
		action.execute();
	}

	public ReadOnlyBooleanProperty undoActionProperty() {
		return undoAction;
	}

	/**
	 * Used by controllers to notify that the undoable actions should be reset
	 * and no further undo can be done. Usually this happens when the last
	 * action that initially could be undone, triggers other actions that change
	 * the internal state of the game.
	 */
	public void resetUndoableActions() {
		lastActions.clear();
		undoAction.set(false);
	}

}
