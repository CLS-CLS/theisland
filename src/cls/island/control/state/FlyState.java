package cls.island.control.state;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.model.player.PilotPlayer;
import cls.island.utils.ViewUtils;
import cls.island.view.component.island.Island;
import cls.island.view.component.island.IslandView;
import cls.island.view.screen.IslandComponent;
import cls.island.view.screen.IslandScreen;

public class FlyState implements GameState {

	private final IslandScreen islandScreen;
	private final GameModel gameModel;
	private final GameState fromState;

	public FlyState(GameController gameController, IslandScreen islandScreen, GameModel gameModel,
			GameState fromState) {
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.fromState = fromState;
	}

	@Override
	public GameState mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY){
			return toNormalState();
		}
		if (event.getButton() != MouseButton.PRIMARY){
			return null;
		}
		IslandComponent islandComponent = ViewUtils.findIslandComponent((Node) event.getTarget());
		if (islandComponent == null)
			return null;
		if (islandComponent instanceof IslandView) {
			Island island = ((IslandView) islandComponent).getParentModel();
			if (island.isSunk())
				return null;
			if (island.equals(gameModel.getCurrentTurnPlayer().getPiece().getIsland())) {
				return null;
			}
			int index = ((PilotPlayer) gameModel.getCurrentTurnPlayer()).fly(island);
			islandScreen.c_movePiece(gameModel.getCurrentTurnPlayer().getPiece().getComponent(),
					island.getComponent(), index);
			return toNormalState();
		}
		return null;
	}

	@Override
	public GameState buttonPressed(ButtonAction action) {
		if (action == ButtonAction.FLY) {
			return toNormalState();
		}
		return null;
	}

	private GameState toNormalState() {
		islandScreen.c_hideMessagePanel();
		return fromState;
	}

	@Override
	public GameState getFromState() {
		return fromState;
	}

	@Override
	public GameState start() {
		if (!((PilotPlayer) gameModel.getCurrentTurnPlayer()).canFly()) {
			return fromState;
		}
		islandScreen.c_setSelectedActionButton(ButtonAction.FLY);
		islandScreen.disableAllButtonsExcluding(ButtonAction.FLY);
		islandScreen.c_showMessagePanel("Select tile to Fly\nRight Click to cancel");
		return null;
	}

}
