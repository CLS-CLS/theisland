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

	private final IslandScreen islandScreen;
	private final GameModel gameModel;
	private final TreasuryCard card;
	private final GameState fromState;

	public UseHelicopterCardState(GameController gameController, IslandScreen islandScreen, GameModel gameModel,
			TreasuryCard card, GameState fromState) {
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.card = card;
		this.fromState = fromState;
	}

	@Override
	public GameState mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY) {
			islandScreen.c_hideMessagePanel();
			return fromState.createGameState();
		}
		IslandComponent islandComponent = ViewUtils.findIslandComponent((Node) event.getTarget());
		if (islandComponent != null && islandComponent instanceof IslandView) {
			return handleClickOnIsland((IslandView) islandComponent);
		}
		return null;

	}

	private GameState handleClickOnIsland(IslandView islandComponent) {
		final Island island = islandComponent.getParentModel();
		if (island.isSunk())
			return null;
		// cannot fly to the island that he already is
		if (island == gameModel.getCurrentTurnPlayer().getPiece().getIsland())
			return null;;
		Player playerWithHeliCard = ViewUtils.findPlayerHoldingCard(gameModel, card);
		if (playerWithHeliCard == null)
			return null;

		// move player to selected island
		int moveIndex = gameModel.getCurrentTurnPlayer().setToIsland(island);
		islandScreen.c_movePiece(gameModel.getCurrentTurnPlayer().getPiece().getComponent(), island.getComponent(),
				moveIndex);

		// discard the card from the players hand

		gameModel.discardCard(playerWithHeliCard, card);
		islandScreen.c_discardPlayerCard(playerWithHeliCard.getBase().getComponent(), card.getComponent());

		return goToNextState();

	}

	private GameState goToNextState() {
		islandScreen.c_hideMessagePanel();
		if (fromState instanceof ChooseDiscardCardState) {
			return fromState.getFromState().createGameState();
		} else {
			return fromState.createGameState();
		}

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
		islandScreen.c_showMessagePanel("Select an Island to Fly to!\nRight Click to cancel");
		return null;
	}

	@Override
	public GameState createGameState() {
		throw new UnsupportedOperationException("no need to support it!!");
	}

}
