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
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.screen.IslandComponent;
import cls.island.view.screen.IslandScreen;

public class UseShoreUpCardState implements GameState {

	private final IslandScreen islandScreen;
	private final GameModel gameModel;
	private final GameState fromState;
	private final TreasuryCard selectedTreasureCard;
	private final GameController gameController;

	public UseShoreUpCardState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel, TreasuryCard selectedTreasureCard, GameState fromState) {
		this.gameController = gameController;
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
			GameState fromFromState = fromState.getFromState();
			assert fromFromState instanceof DrawCardState
					|| fromFromState instanceof TradeCardState : "UseOrDiscardState expected to have been triggered by DrawCardState or TradeCardState but found "
					+ fromFromState.getClass();
			if (fromFromState instanceof DrawCardState) {
				return fromFromState;
			} else {
				assert fromFromState.getFromState() instanceof NormalState : "normal state expected but was"
						+ fromFromState.getFromState();
				return fromFromState.getFromState(); // go to normal
			}
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
		final IslandView islandView = (IslandView) islandComponent;
		if (!islandView.getParentModel().isFlooded() || islandView.getParentModel().isSunk())
			return null;

		// primary button pressed on flooded island
		gameController.executeAction(new RevertableAction() {
			Player cardHolder = null;

			@Override
			public void execute() {
				islandView.getParentModel().unFlood();
				islandView.unFlood();
				cardHolder = ViewUtils.findPlayerHoldingCard(gameModel, selectedTreasureCard);
				gameModel.discardCard(cardHolder, selectedTreasureCard);
				islandScreen.c_discardPlayerCard(cardHolder.getBase().getComponent(),
						selectedTreasureCard.getComponent());
			}

			@Override
			public GameState revert() {
				islandView.getParentModel().flood();
				islandView.flood();
				gameModel.undiscardCard(cardHolder, selectedTreasureCard);
				islandScreen.c_moveTreasuryCardFromPileToPlayer(
						selectedTreasureCard.getComponent(), cardHolder.getBase().getComponent());
				return UseShoreUpCardState.this;
			}

		});

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

	@Override
	public void end() {
	}

}
