package cls.island.control.state;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.utils.ViewUtils;
import cls.island.view.components.IslandTile;
import cls.island.view.screen.IslandComponent;
import cls.island.view.screen.IslandScreen;

public class UseShoreUpCard implements GameState {

	private final IslandScreen islandScreen;
	private GameController stateContext;
	private final GameModel gameModel;

	public UseShoreUpCard(GameController stateContext, IslandScreen islandScreen, GameModel gameModel) {
		this.stateContext = stateContext;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		islandScreen.c_showMessagePanel("Select a flooded island to Shore-up \nRight Click to cancel");

	}

	public void mouseClickedOnIslandTile(IslandTile island) {
		if (island.model.isFlooded()) {
			island.model.setFlooded(false);
			island.unFlood();
			islandScreen.c_hideMessagePanel();
			stateContext.setGameState(new Normal(stateContext, islandScreen, gameModel));
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		final IslandComponent islandComponent = ViewUtils.findIslandComponentParent((Node) event.getTarget());
		if (event.getButton() == MouseButton.PRIMARY && islandComponent instanceof IslandTile) {
			IslandTile islandTile = (IslandTile) islandComponent;
			mouseClickedOnIslandTile(islandTile);
		}
		if (event.getButton() == MouseButton.SECONDARY) {
			cancel();
		}
	}

	private void cancel() {
		stateContext.setGameState(new Normal(stateContext, islandScreen, gameModel));
		islandScreen.c_hideMessagePanel();

	}

}
