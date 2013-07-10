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
	public GameState mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY) {
			return normalState();
		}
		final IslandComponent islandComponent = ViewUtils.findIslandComponent((Node) event
				.getTarget());
		if (!(islandComponent instanceof IslandView))
			return null;
		IslandView islandView = (IslandView) islandComponent;
		if (!islandView.getParentModel().isFlooded() || islandView.getParentModel().isSunk())
			return null;

		// primary button pressed on flooded island
		Player player = gameModel.getCurrentTurnPlayer();
		
		//and is a valid shore-up.
		if (!player.isValidShoreUp(player.getPiece().getIsland(), islandView.getParentModel(),
				gameModel.getIslandGrid())) {
			return null;
		}
		player.shoreUp(islandView.getParentModel());
		islandView.unFlood();
		if (!player.canShoreUp()) return normalState();
		return null;
	}

	private GameState normalState() {
		islandScreen.c_hideMessagePanel();
		islandScreen.c_setCursorImage(ButtonAction.MOVE);
		return new NormalState(gameController, islandScreen, gameModel);
	}

	@Override
	public GameState buttonPressed(ButtonAction action) {
		if (action == ButtonAction.SHORE_UP){
			return normalState();
		}
		
		return null;

	}

	@Override
	public GameState getFromState() {
		throw new UnsupportedOperationException();
	}

	@Override
	public GameState start() {
		islandScreen.c_setSelectedActionButton(ButtonAction.SHORE_UP);
		islandScreen.disableAllButtonsExcluding(ButtonAction.SHORE_UP);
		Player currentPlayer = gameModel.getCurrentTurnPlayer();
		if (!currentPlayer.canShoreUp()) {
			return new NormalState(gameController, islandScreen, gameModel);
		}
		islandScreen.c_showMessagePanel("Select a flooded island to Shore-up"
				+ "\nRight Click to cancel");
		islandScreen.c_setCursorImage(ButtonAction.SHORE_UP);
		return null;
	}

}
