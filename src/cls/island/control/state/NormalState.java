package cls.island.control.state;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.model.player.Player;
import cls.island.utils.ViewUtils;
import cls.island.view.component.island.Island;
import cls.island.view.component.island.IslandView;
import cls.island.view.component.piece.PieceView;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.TreasuryCardView;
import cls.island.view.component.treasury.card.Type;
import cls.island.view.screen.IslandComponent;
import cls.island.view.screen.IslandScreen;

public class NormalState implements GameState {

	private GameController gameController;
	private IslandScreen islandScreen;
	private final GameModel gameModel;

	public NormalState(GameController stateContext, IslandScreen islandScreen, GameModel gameModel) {
		this.gameController = stateContext;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
	}

	private void checkForOverloadedTreasuryCards() {
		if (gameModel.getCurrentTurnPlayer().getTreasuryCards().size() > GameModel.MAX_CARDS_ALLOWED_IN_HAND) {
			gameController.setGameState(new ChooseDiscardCardState(gameController, islandScreen,
					gameModel, this));
		}

	}

	public void mouseClickedOnIslandTile(Island island) {
		PieceView movingPiece = gameModel.getCurrentTurnPlayer().getPiece().getComponent();
		Island from = movingPiece.getParentModel().getIsland();

		if (!gameModel.getCurrentTurnPlayer().hasAction())
			return;
		if (island.isSunk())
			return;

		// TODO remove from!=null when ready (aka when the piece is set on the
		// island on the beginning)
		if (from != null
				&& !gameModel.getCurrentTurnPlayer().isValidMove(from, island,
						gameModel.getIslandGrid())) {
			return;
		}
		int addedIndex = gameModel.getCurrentTurnPlayer().moveToIsland(island);
		islandScreen.c_movePiece(movingPiece, island.getComponent(), addedIndex);
	}

	public void mouseClickedOnCard(TreasuryCardView treasuryCardView) {
		gameController.setGameState(new UseShoreUpCardState(gameController, islandScreen,
				gameModel, treasuryCardView.getParentModel(), this));
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		final IslandComponent islandComponent = ViewUtils.findIslandComponent((Node) event
				.getTarget());
		if (islandComponent instanceof IslandView) {
			IslandView islandView = (IslandView) islandComponent;
			Island island = islandView.getParentModel();
			mouseClickedOnIslandTile(island);
		} else if (islandComponent instanceof TreasuryCardView) {
			TreasuryCardView cardView = (TreasuryCardView) islandComponent;
			mouseClickedOnTreasuryCard(cardView.getParentModel());
		}

	}

	private void mouseClickedOnTreasuryCard(TreasuryCard card) {
		// check that the treasury card belongs to one player
		boolean playerHasit = false;
		for (Player player : gameModel.getPlayers()) {
			if (player.getTreasuryCards().contains(card)){
				playerHasit = true;
				break;
			}
		}
		if (!playerHasit)
			return;
		if (card.getType().equals(Type.SANDBAGS)) {
			gameController.setGameState(new UseShoreUpCardState(gameController, islandScreen,
					gameModel, card, this));
		} else if (card.getType().equals(Type.HELICOPTER)) {
			gameController.setGameState(new UseHelicopterCardState(gameController, islandScreen,
					gameModel, card, this));
		}
	}

	@Override
	public void buttonPressed(ButtonAction action) {
		switch (action) {
		case NEXT_TURN:
			nextTurn();
			break;
		case SHORE_UP:
			gameController.setGameState(new ShoreUpState(gameController, islandScreen, gameModel));
			break;
		case COLLECT_TREASURE:
			gameController.setGameState(new CollectTreasureState(gameController, islandScreen, gameModel, this));
		default:
			break;
		}
	}

	private void nextTurn() {
		gameModel.getCurrentTurnPlayer().resetActions();
		gameController.setGameState(new DrawCardState(gameController, islandScreen, gameModel));
	}

	@Override
	public GameState getFromState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start() {
		checkForOverloadedTreasuryCards();
	}

	@Override
	public GameState createGameState() {
		return new NormalState(gameController, islandScreen, gameModel);
	}
}
