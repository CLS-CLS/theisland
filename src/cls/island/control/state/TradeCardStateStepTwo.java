package cls.island.control.state;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.GameState;
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
			return previousState();
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
		gameModel.getCurrentTurnPlayer().giveCard(selectedPlayer, selectedCard);
		selectedPlayer.getBase().getComponent().moveToBase(selectedCard.getComponent());
		islandScreen.c_hideMessagePanel();
		selectedCard.getComponent().setValidToCkickEffect(false);
		for (Player player : eligiblePlayers) {
			player.getBase().getComponent().setValidToCkickEffect(false);
		}
		return new NormalState(gameController, islandScreen, gameModel);

	}

	private GameState previousState() {
		islandScreen.c_hideMessagePanel();
		selectedCard.getComponent().setValidToCkickEffect(false);
		for (Player player : eligiblePlayers) {
			player.getBase().getComponent().setValidToCkickEffect(false);
		}
		return fromState;
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
		islandScreen.c_setSelectedActionButton(ButtonAction.TRADE);
		islandScreen.c_showMessagePanel("Choose a Player to give the card\nRight-Click to cancel");
		selectedCard.getComponent().setValidToCkickEffect(true);
		for (Player player : eligiblePlayers) {
			player.getBase().getComponent().setValidToCkickEffect(true);
		}
		return null;

	}

}
