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
import cls.island.view.component.island.Island;
import cls.island.view.component.island.IslandView;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.piece.PieceView;
import cls.island.view.screen.IslandScreen;

public class MoveOtherState implements GameState {

	private final IslandScreen islandScreen;
	private final GameModel gameModel;
	private final GameState fromState;
	private final GameController gameController;

	public MoveOtherState(GameController gameController, IslandScreen islandScreen, GameModel gameModel,
			GameState fromState) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.fromState = fromState;
	}

	@Override
	public GameState mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY) {
			return cancel();
		}
		PieceView pv = ViewUtils.findPieceView((Node) event.getTarget());
		if (pv != null) {
			Piece piece = pv.getParentModel();
			if (gameModel.getCurrentTurnPlayer().getPiece() == piece) {
				return null;
			}
			handleClickOnPiece(pv);
		} else {
			return null;
		}
		return null;

	}

	private GameState cancel() {
		end();
		return fromState;
	}

	private GameState handleClickOnPiece(PieceView pieceView) {
		end();
		return new MoveOtherStepTwoState(gameController, islandScreen, gameModel, pieceView, this);
	}

	@Override
	public void end() {
		islandScreen.c_hideMessagePanel();
		gameModel.getPlayers().forEach((p) -> p.getPiece().getComponent().setValidToCkickEffect(false));
	}

	@Override
	public GameState buttonPressed(ButtonAction action) {
		return null;

	}

	@Override
	public GameState getFromState() {
		return fromState;
	}

	@Override
	public GameState start() {
		for (Player player : gameModel.getPlayers()) {
			if (player == gameModel.getCurrentTurnPlayer()) {
				continue;
			}
			player.getPiece().getComponent().setValidToCkickEffect(true);
		}
		islandScreen.disableButtons();
		islandScreen.c_showMessagePanel("Select the player to move!\nRight Click to cancel");
		return null;
	}

}
