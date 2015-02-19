package cls.island.control.state;

import java.util.List;

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
import cls.island.view.component.player.base.PlayerBaseView;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.screen.IslandComponent;
import cls.island.view.screen.IslandScreen;

public class TradeCardStateStepTwo implements GameState {

	private final GameController gameController;
	private final IslandScreen islandScreen;
	private final GameModel gameModel;
	private final TreasuryCard selectedCard;
	private final GameState fromState;
	private final List<Player> eligiblePlayers;

	public TradeCardStateStepTwo(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel, TreasuryCard selectedCard, List<Player> eligiblePlayers,
			GameState fromState) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.selectedCard = selectedCard;
		this.eligiblePlayers = eligiblePlayers;
		this.fromState = fromState;
	}

	@Override
	public GameState mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY) {
			end();
			return fromState;
		}
		IslandComponent component = ViewUtils.findIslandComponent((Node) event.getTarget());
		PlayerBaseView base = null;
		if (component instanceof PlayerBaseView) {
			base = (PlayerBaseView) component;
		}
		if (base == null) {
			return null;
		}
		Player selectedPlayer = null;
		for (Player player : eligiblePlayers) {
			if (player.getBase().getComponent() == base) {
				selectedPlayer = player;
				break;
			}
		}
		if (selectedPlayer == null) {
			return null;
		}

		final Player receiver = selectedPlayer;
		end();
		return giveCard(receiver);
	}
	
	@Override
	public void end() {
		islandScreen.c_hideMessagePanel();
		selectedCard.getComponent().setValidToCkickEffect(false);
		for (Player player : eligiblePlayers) {
			player.getBase().getComponent().setValidToCkickEffect(false);
		}
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
		if (eligiblePlayers.size() == 1) {
			end();
			return giveCard(eligiblePlayers.get(0));
		}
		for (TreasuryCard card : gameModel.getCurrentTurnPlayer().getTreasuryCards()) {
			card.getComponent().setValidToCkickEffect(false);
		}
		islandScreen.c_setSelectedActionButton(ButtonAction.TRADE);
		islandScreen.c_showMessagePanel("Choose a Player to give the card\nRight-Click to cancel");
		selectedCard.getComponent().setValidToCkickEffect(true);
		for (Player player : eligiblePlayers) {
			player.getBase().getComponent().setValidToCkickEffect(true);
		}
		return null;

	}

	/**
	 * Gives the card to the player, and returns the state that should move on
	 * @return the next state to go.s
	 */
	private GameState giveCard(Player receiver) {
		gameController.executeAction(new TradeAction(receiver));
		if (receiver.getTreasuryCards().size() <= 5) {
			return fromState.getFromState();
		} else {
			return new UseOrDiscardCardState(receiver, gameController, islandScreen, gameModel,
					fromState);
		}

	}

	private final class TradeAction implements RevertableAction {
		private final Player playerToGiveCard;
		TreasuryCard card = selectedCard;
		Player playerFrom = gameModel.getCurrentTurnPlayer();
		GameState stateToReturn = TradeCardStateStepTwo.this.fromState.getFromState();

		private TradeAction(Player playerToGiveCard) {
			this.playerToGiveCard = playerToGiveCard;
		}

		@Override
		public GameState revert() {
			playerToGiveCard.setGiveCard(playerFrom, card);
			playerFrom.setActionsLeft(playerFrom.getActionsLeft() + 1);
			playerFrom.getBase().getComponent().moveToBase(card.getComponent());
			return stateToReturn;
		}

		@Override
		public void execute() {
			gameModel.getCurrentTurnPlayer().giveCard(playerToGiveCard, selectedCard);
			playerToGiveCard.getBase().getComponent().moveToBase(selectedCard.getComponent());
			gameModel.getCurrentTurnPlayer().getBase().getComponent().rearrangeCards();
			islandScreen.c_hideMessagePanel();
			selectedCard.getComponent().setValidToCkickEffect(false);
			for (Player player : eligiblePlayers) {
				player.getBase().getComponent().setValidToCkickEffect(false);
			}

		}
	}
}
