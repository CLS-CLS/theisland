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

public class UseShoreUpCardState implements GameState {

	private final IslandScreen islandScreen;
	private final GameModel gameModel;
	private final GameState fromState;
	private final TreasuryCard selectedTreasureCard;

	public UseShoreUpCardState(GameController stateContext, IslandScreen islandScreen,
			GameModel gameModel, TreasuryCard selectedTreasureCard, GameState fromState) {
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.selectedTreasureCard = selectedTreasureCard;
		this.fromState = fromState;
	}

	/**
	 * UseShoreUp ---- > Normal (if from normal) UseShoreUp ---- > DrawCard (if
	 * discardCard && drawCard)
	 */
	private GameState changeState() {
		for (Island island : gameModel.getIslands()) {
			island.getComponent().setValidToCkickEffect(false);
		}
		selectedTreasureCard.getComponent().setValidToCkickEffect(false);
		islandScreen.c_hideMessagePanel();
		if (fromState instanceof UseOrDiscardCardState) {
			if (!(fromState.getFromState() instanceof DrawCardState)) {
				throw new RuntimeException(
						"UseOrDiscardState expected to have been triggered by DrawCarsState only but found "
								+ fromState.getFromState().getClass());
			}
			return fromState.getFromState();
		} else {
			return fromState;
		}
	}

	@Override
	public GameState mouseClicked(final MouseEvent event) {

		if (event.getButton() == MouseButton.SECONDARY) {
			return cancel();
		}

		final IslandComponent islandComponent = ViewUtils.findIslandComponent((Node) event
				.getTarget());
		if (!(islandComponent instanceof IslandView))
			return null;
		IslandView islandView = (IslandView) islandComponent;
		if (!islandView.getParentModel().isFlooded() || islandView.getParentModel().isSunk())
			return null;

		// primary button pressed on flooded island
		islandView.getParentModel().unFlood();
		islandView.unFlood();
		Player player = ViewUtils.findPlayerHoldingCard(gameModel, selectedTreasureCard);
		gameModel.discardCard(player, selectedTreasureCard);
		islandScreen.c_discardPlayerCard(player.getBase().getComponent(),
				selectedTreasureCard.getComponent());
		return changeState();

	}

	private GameState cancel() {
		for (Island island : gameModel.getIslands()) {
			island.getComponent().setValidToCkickEffect(false);
		}
		selectedTreasureCard.getComponent().setValidToCkickEffect(false);
		islandScreen.c_hideMessagePanel();
		return fromState;
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
		islandScreen.disableButtons();
		for (Island island : gameModel.getIslands()) {
			if (island.isFlooded() && !island.isSunk()) {
				island.getComponent().setValidToCkickEffect(true);
			}
		}
		islandScreen.c_showMessagePanel("Select a flooded island to Shore-up"
				+ "\nRight Click to cancel");
		return null;
	}

}
