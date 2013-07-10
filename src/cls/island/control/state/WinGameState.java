package cls.island.control.state;

import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.view.screen.IslandScreen;

public class WinGameState implements GameState {
	
	private final IslandScreen islandScreen;
	private final GameModel gameModel;
	private final GameController gameController;

	public WinGameState(GameController gameController, GameModel gameModel, IslandScreen islandScreen) {
		this.gameController = gameController;
		this.gameModel = gameModel;
		this.islandScreen = islandScreen;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public GameState mouseClicked(MouseEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameState buttonPressed(ButtonAction action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameState getFromState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameState start() {
		//TODO change to win pop-up
		islandScreen.c_WaterCardDrawnPopUp();

		// Go to back to main Screen. Ensure that from now on we are in FX-Thread
		// as the game is over and the main screens work on FX-Thread.
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				gameController.backToMainScreen();

			}
		});
		return null;
	}

}
