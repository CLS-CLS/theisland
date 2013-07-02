package cls.island.control.state;

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
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.screen.IslandComponent;
import cls.island.view.screen.IslandScreen;

public class UseHelicopterCardState implements GameState {

	private final IslandScreen islandScreen;
	private final GameModel gameModel;
	private final TreasuryCard card;
	private final GameState fromState;
	private final GameController gameController;

	public UseHelicopterCardState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel, TreasuryCard card, GameState fromState) {
		this.gameController = gameController;
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
		if (island.isSunk()) {
			return null;
		}
		if (island.getPieces().size() == 0) {
			return null;
		}
		removeEffects();
		return new UseHelicopterCardStepTwoState(gameController, islandScreen, gameModel, card,
				island, this);
	}

	private void removeEffects() {
		islandScreen.c_hideMessagePanel();
		card.getComponent().setValidToCkickEffect(false);
		for (Island island : gameModel.getIslands()) {
			island.getComponent().setValidToCkickEffect(false);
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
		if (gameModel.hasWon()) {
			return new WinGameState(gameController, gameModel, islandScreen);
		}
		card.getComponent().setValidToCkickEffect(true);
		for (Island island : gameModel.getIslands()) {
			if (island.getPieces().size() > 0) {
				island.getComponent().setValidToCkickEffect(true);
			}
		}
		islandScreen.disableButtons();
		islandScreen.c_showMessagePanel("Select an Island to Fly from!\nRight Click to cancel");
		return null;
	}

	@Override
	public GameState createGameState() {
		return new UseHelicopterCardState(gameController, islandScreen, gameModel, card, fromState);
	}

}
