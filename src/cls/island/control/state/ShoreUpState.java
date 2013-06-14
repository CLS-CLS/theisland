package cls.island.control.state;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.model.player.Player;
import cls.island.utils.ViewUtils;
import cls.island.view.component.island.IslandView;
import cls.island.view.screen.IslandComponent;
import cls.island.view.screen.IslandScreen;

public class ShoreUpState implements GameState {

	private final GameController gameController;
	private final IslandScreen islandScreen;
	private final GameModel gameModel;

	public ShoreUpState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY)
			goToNormalState();
		final IslandComponent islandComponent = ViewUtils.findIslandComponent((Node) event
				.getTarget());
		if (!(islandComponent instanceof IslandView))
			return;
		IslandView islandView = (IslandView) islandComponent;
		if (!islandView.getParentModel().isFlooded() || islandView.getParentModel().isSunk())
			return;

		// primary button pressed on flooded island
		Player player = gameModel.getCurrentTurnPlayer();
		
		//and is a valid shore-up.
		if (! player.isValidShoreUp(player.getPiece().getIsland(), islandView.getParentModel(),
				gameModel.getIslandGrid())) {
			return;
		}
		player.shoreUp(islandView.getParentModel());
		islandView.unFlood();
		goToNormalState();

	}

	private void goToNormalState() {
		islandScreen.c_hideMessagePanel();
		gameController.setGameState(new NormalState(gameController, islandScreen, gameModel));
	}

	@Override
	public void buttonPressed(ButtonAction action) {
		// TODO Auto-generated method stub

	}

	@Override
	public GameState getFromState() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void start() {
		if (!gameModel.getCurrentTurnPlayer().hasAction()) {
			gameController.setGameState(new NormalState(gameController, islandScreen, gameModel));
			return;
		}
		islandScreen.c_showMessagePanel("Select a flooded island to Shore-up"
				+ "\nRight Click to cancel");

	}

	@Override
	public GameState createGameState() {
		return new ShoreUpState(gameController, islandScreen, gameModel);
	}

}
