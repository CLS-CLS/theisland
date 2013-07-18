package cls.island.control.state;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.action.RevertableAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.model.player.PilotPlayer;
import cls.island.model.player.Player;
import cls.island.utils.ViewUtils;
import cls.island.view.component.island.Island;
import cls.island.view.component.island.IslandView;
import cls.island.view.screen.IslandComponent;
import cls.island.view.screen.IslandScreen;

public class FlyState implements GameState {

	private final IslandScreen islandScreen;
	private final GameModel gameModel;
	private final GameState fromState;
	private final GameController gameController;

	public FlyState(GameController gameController, IslandScreen islandScreen, GameModel gameModel,
			GameState fromState) {
		this.gameController = gameController;
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
			final Island island = ((IslandView) islandComponent).getParentModel();
			if (island.isSunk())
				return null;
			if (island.equals(gameModel.getCurrentTurnPlayer().getPiece().getIsland())) {
				return null;
			}
			gameController.executeAction(new RevertableAction() {
				
				private Island islandFrom = gameModel.getCurrentTurnPlayer().getPiece().getIsland();
				private Player currentTurnPlayer = gameModel.getCurrentTurnPlayer();
				
				@Override
				public GameState revert() {
					
					int index = currentTurnPlayer.setToIsland(islandFrom);
					islandScreen.c_movePiece(currentTurnPlayer.getPiece().getComponent(), islandFrom.getComponent(), index);
					currentTurnPlayer.setActionsLeft(currentTurnPlayer.getActionsLeft()+1);
					((PilotPlayer)currentTurnPlayer).setCanFly(true);
					return fromState;
				}
				
				@Override
				public void execute() {
					int index = ((PilotPlayer) currentTurnPlayer).fly(island);
					islandScreen.c_movePiece(currentTurnPlayer.getPiece().getComponent(),
							island.getComponent(), index);
				}
			});
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
