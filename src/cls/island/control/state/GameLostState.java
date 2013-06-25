package cls.island.control.state;

import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.view.screen.IslandScreen;

/**
 * Game is over. Shows final pop-up screen and goes back to main menu. 
 *
 */
public class GameLostState implements GameState {

	private final IslandScreen islandScreen;
	private final GameController gameController;
	private final GameModel gameModel;

	public GameLostState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel, GameState fromState) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
	}

	@Override
	public GameState start() {
		islandScreen.c_showLooseGamePopUp(gameModel.findLooseCondition());

		// Go to back to main Screen. Ensure that from now we are in FX-Thread
		// as
		// the game is over and the main screens work on FX-Thread.
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				gameController.backToMainScreen();
				
			}
		});
		return null;
	}

	@Override
	public GameState createGameState() {
		throw new UnsupportedOperationException();
	}

	@Override
	public GameState mouseClicked(MouseEvent event) {
		return null;

	}

	@Override
	public GameState buttonPressed(ButtonAction action) {
		return null;

	}

	@Override
	public GameState getFromState() {
		throw new UnsupportedOperationException();
	}

}
