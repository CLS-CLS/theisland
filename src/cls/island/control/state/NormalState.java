package cls.island.control.state;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.GameState;
import cls.island.control.action.RevertableAction;
import cls.island.model.GameModel;
import cls.island.model.player.PilotPlayer;
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

	public void mouseClickedOnIslandTile(final Island island) {
		final PieceView movingPiece = gameModel.getCurrentTurnPlayer().getPiece().getComponent();
		Island from = movingPiece.getParentModel().getIsland();

		if (!gameModel.getCurrentTurnPlayer().hasAction())
			return;
		if (island.isSunk())
			return;

		if (!gameModel.getCurrentTurnPlayer().isValidMove(from, island, gameModel.getIslandGrid())) {
			return;
		}
		
		gameController.executeAction(new RevertableAction(){
			Island previousIsland = null;
			int actionsLeft = gameModel.getCurrentTurnPlayer().getActionsLeft();
			
			public void execute(){
				previousIsland = gameModel.getCurrentTurnPlayer().getPiece().getIsland();
				int addedIndex = gameModel.getCurrentTurnPlayer().moveToIsland(island);
				islandScreen.c_movePiece(movingPiece, island.getComponent(), addedIndex);
				updateActionButtons();
			}
			
			public GameState revert(){
				int addedIndex = gameModel.getCurrentTurnPlayer().setToIsland(previousIsland);
				gameModel.getCurrentTurnPlayer().setActionsLeft(actionsLeft);
				islandScreen.c_movePiece(movingPiece, previousIsland.getComponent(), addedIndex);
				return NormalState.this;
			}
		});
		
	}

	@Override
	public GameState mouseClicked(MouseEvent event) {
		final IslandComponent islandComponent = ViewUtils.findIslandComponent((Node) event
				.getTarget());
		if (islandComponent instanceof IslandView) {
			IslandView islandView = (IslandView) islandComponent;
			Island island = islandView.getParentModel();
			mouseClickedOnIslandTile(island);
		} else if (islandComponent instanceof TreasuryCardView) {
			TreasuryCardView cardView = (TreasuryCardView) islandComponent;
			return mouseClickedOnTreasuryCard(cardView.getParentModel());
		}
		return null;

	}

	private GameState mouseClickedOnTreasuryCard(TreasuryCard card) {
		// check that the treasury card belongs to one player
		boolean playerHasit = false;
		for (Player player : gameModel.getPlayers()) {
			if (player.getTreasuryCards().contains(card)) {
				playerHasit = true;
				break;
			}
		}
		if (!playerHasit)
			return null;
		if (card.getType().equals(Type.SANDBAGS)) {
			return new UseShoreUpCardState(gameController, islandScreen, gameModel, card, this);
		} else if (card.getType().equals(Type.HELICOPTER)) {
			return new UseHelicopterCardState(gameController, islandScreen, gameModel, card, this);
		}
		return null;
	}

	@Override
	public GameState buttonPressed(ButtonAction action) {
		switch (action) {
		case NEXT_TURN:
			return nextTurn();
		case SHORE_UP:
			return new ShoreUpState(gameController, islandScreen, gameModel);
		case COLLECT_TREASURE:
			return new CollectTreasureState(gameController, islandScreen, gameModel, this);
		case TRADE:
			return new TradeCardState(gameController, islandScreen, gameModel, this);
		case FLY:
			return new FlyState(gameController, islandScreen, gameModel, this);
		case MOVE_OTHER:
			return null;
		default:
		}
		return null;
	}

	private GameState nextTurn() {
		gameModel.getCurrentTurnPlayer().resetActions();
		return new DrawCardState(gameController, islandScreen, gameModel);
	}

	@Override
	public GameState getFromState() {
		// TODO Auto-generated method stub
		return null;
	}

	private void updateActionButtons(){
		islandScreen.enableButtons();
		Player currentTurnPlayer = gameModel.getCurrentTurnPlayer();
		if (!currentTurnPlayer.hasAction()){
			islandScreen.disableButtons(ButtonAction.MOVE);
			if (currentTurnPlayer instanceof PilotPlayer){
				islandScreen.disableButtons(ButtonAction.FLY);
			}
		}
		if (!gameModel.canTrade()){
			islandScreen.disableButtons(ButtonAction.TRADE);
		}
		if (!currentTurnPlayer.canShoreUp()){
			islandScreen.disableButtons(ButtonAction.SHORE_UP);
		}
		if (!gameModel.canCollectTreasure(currentTurnPlayer)){
			islandScreen.disableButtons(ButtonAction.COLLECT_TREASURE);
		}
		if (currentTurnPlayer instanceof PilotPlayer){
			if (!((PilotPlayer)currentTurnPlayer).canFly()){
				islandScreen.disableButtons(ButtonAction.FLY);
			}
		}
	}

	@Override
	public GameState start() {
		updateActionButtons();
		islandScreen.c_setSelectedActionButton(ButtonAction.MOVE);
		if (gameModel.getCurrentTurnPlayer().getTreasuryCards().size() > GameModel.MAX_CARDS_ALLOWED_IN_HAND) {
			return new UseOrDiscardCardState(gameController, islandScreen, gameModel, this);
		}
		return null;
	}

	@Override
	public void end() {
	}
}
