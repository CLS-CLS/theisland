package cls.island.control.state;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.utils.ViewUtils;
import cls.island.view.component.island.Island;
import cls.island.view.component.island.IslandView;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.screen.IslandComponent;
import cls.island.view.screen.IslandScreen;

public class UseHelicopterCardStepTwoState implements GameState {

	private final GameState fromState;
	private final Island takeOffIsland;
	private final TreasuryCard card;
	private final IslandScreen islandScreen;
	private final GameController gameController;

	public UseHelicopterCardStepTwoState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel, TreasuryCard card, Island takeOffIsland, GameState fromState) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.card = card;
		this.takeOffIsland = takeOffIsland;
		this.fromState = fromState;
	}

	@Override
	public GameState mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY) {
			return goToState(fromState.createGameState());
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
		if (island.equals(takeOffIsland))
			return null;
		if (island.isSunk())
			return null;
		// various checks -- end

		if (takeOffIsland.getPieces().size() == 1) {
			flyToIsland(island, takeOffIsland.getPieces().get(0));
			return goToState(fromState.getFromState().createGameState());
		} else {
			List<Piece> result = islandScreen
					.c_showSelectPieceToFlyPopUp(takeOffIsland.getPieces());
			if (result.size() == 0) {
				return goToState(fromState.createGameState());
			} else {
				for (Piece piece : result) {
					flyToIsland(island, piece);
				}
				return goToState(fromState.getFromState().createGameState());
			}
		}
	}

	private void flyToIsland(Island island, Piece piece) {
		int index = gameController.getPlayerWithPiece(piece).setToIsland(island);
		islandScreen.c_movePiece(piece.getComponent(), island.getComponent(), index);
	}

	private GameState goToState(GameState state) {
		islandScreen.c_hideMessagePanel();
		takeOffIsland.getComponent().setValidToCkickEffect(false);
		card.getComponent().setValidToCkickEffect(false);
		return state;
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
		islandScreen.c_showMessagePanel("Select Island to Fly to\nRight Click to cancel");
		card.getComponent().setValidToCkickEffect(true);
		takeOffIsland.getComponent().setValidToCkickEffect(true);
		return null;
	}

	@Override
	public GameState createGameState() {
		// TODO Auto-generated method stub
		return null;
	}

}
