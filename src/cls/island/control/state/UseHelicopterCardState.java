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
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.screen.IslandComponent;
import cls.island.view.screen.IslandScreen;

public class UseHelicopterCardState implements GameState {

	private final GameController gameController;
	private final IslandScreen islandScreen;
	private final GameModel gameModel;
	private final TreasuryCard card;
	private final GameState fromState;

	public UseHelicopterCardState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel, TreasuryCard card, GameState fromState) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.card = card;
		this.fromState = fromState;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY) {
			islandScreen.c_hideMessagePanel();
			gameController.setGameState(fromState);
		} else {
			IslandComponent islandComponent = ViewUtils.findIslandComponent((Node) event
					.getTarget());
			if (islandComponent != null && islandComponent instanceof IslandView) {
				handleClickOnIsland((IslandView) islandComponent);
			}
		}

	}

	private void handleClickOnIsland(IslandView islandComponent) {
		final Island island = islandComponent.getParentModel();
		if (island.isSunk())
			return;
		Player playerWithHeliCard = ViewUtils.findPlayerHoldingCard(gameModel, card);
		if (playerWithHeliCard == null) return;

		// move player to selected island
		int moveIndex = gameModel.getCurrentTurnPlayer().setToIsland(island);
		islandScreen.c_movePiece(gameModel.getCurrentTurnPlayer().getPiece().getComponent(),
				island.getComponent(), moveIndex);

		// discard the card from the players hand
		
		gameModel.discardCard(playerWithHeliCard, card);
		islandScreen.c_discardPlayerCard(playerWithHeliCard.getBase().getComponent(),
				card.getComponent());

		goToNextState();

	}

	private void goToNextState() {
		islandScreen.c_hideMessagePanel();
		if (fromState instanceof ChooseDiscardCardState) {
			gameController.setGameState(fromState.getFromState().createGameState());
		} else {
			gameController.setGameState(fromState.createGameState());
		}

	}

	@Override
	public void buttonPressed(ButtonAction action) {
		// TODO Auto-generated method stub

	}

	@Override
	public GameState getFromState() {
		return fromState;
	}

	@Override
	public void start() {
		islandScreen.c_showMessagePanel("Select an Island to Fly to!\nRight Click to cancel");

	}

	@Override
	public GameState createGameState() {
		throw new UnsupportedOperationException("no need to support it!!");
	}

}
