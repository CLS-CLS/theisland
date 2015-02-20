package cls.island.control.state;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.GameState;
import cls.island.control.action.RevertableAction;
import cls.island.model.GameModel;
import cls.island.model.player.Player;
import cls.island.utils.ViewUtils;
import cls.island.view.component.island.Island;
import cls.island.view.component.island.IslandView;
import cls.island.view.component.piece.PieceView;
import cls.island.view.screen.IslandComponent;
import cls.island.view.screen.IslandScreen;

public class MoveOtherStepTwoState implements GameState {

	private final GameState fromState;
	private final IslandScreen islandScreen;
	private final GameController gameController;
	private final GameModel gameModel;
	private PieceView pieceToMove;

	public MoveOtherStepTwoState(GameController gameController, IslandScreen islandScreen, GameModel gameModel,
			PieceView pieceToMove, GameState fromState) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.pieceToMove = pieceToMove;
		this.fromState = fromState;
	}

	@Override
	public GameState mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY) {
			return goToState(fromState);
		}

		// various checks --start
		if (event.getButton() != MouseButton.PRIMARY) {
			return null;
		}
		IslandComponent islandComponent = ViewUtils.findIslandComponent((Node) event.getTarget());
		if (islandComponent == null)
			return null;
		if (!(islandComponent instanceof IslandView))
			return null;

		Island island = ((IslandView) islandComponent).getParentModel();
		if (island.equals(pieceToMove.getParentModel().getIsland())) {
			return null;
		}
		if (island.isSunk()) {
			return null;
		}
		// various checks -- end
		return moveToIsland(island);
	}

	private GameState moveToIsland(Island island) {
		gameController.executeAction(new RevertableAction() {
			Island islandFrom = pieceToMove.getParentModel().getIsland();

			@Override
			public void execute() {
				int index = gameController.getPlayerWithPiece(pieceToMove.getParentModel()).setToIsland(island);
				islandScreen.c_movePiece(pieceToMove, island.getComponent(), index);
			}

			@Override
			public GameState revert() {
				Player player = ViewUtils.getPlayerById(gameModel, pieceToMove.getParentModel().getPlayerId());
				int index = player.setToIsland(islandFrom);
				islandScreen.c_movePiece(player.getPiece().getComponent(), islandFrom.getComponent(), index);
				player.setActionsLeft(player.getActionsLeft() + 1);
				return fromState;
			}
		});
		return goToState(getFromState().getFromState());
	}


	private GameState goToState(GameState state) {
		islandScreen.c_hideMessagePanel();
		gameModel.getIslands().forEach((island) -> island.getComponent().setValidToCkickEffect(false));
		pieceToMove.setValidToCkickEffect(false);
		return state;
	}

	@Override
	public GameState buttonPressed(ButtonAction action) {
		throw new UnsupportedOperationException();
	}

	@Override
	public GameState getFromState() {
		throw new UnsupportedOperationException();
	}

	@Override
	public GameState start() {
		islandScreen.c_showMessagePanel("Select Island to move to\nRight Click to cancel");
		pieceToMove.setValidToCkickEffect(true);
		enableValidIslandsEffect();
		return null;
	}

	private void enableValidIslandsEffect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void end() {
	}

}
