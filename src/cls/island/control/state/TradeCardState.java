package cls.island.control.state;

import java.util.ArrayList;
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
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.TreasuryCardView;
import cls.island.view.screen.IslandComponent;
import cls.island.view.screen.IslandScreen;

public class TradeCardState implements GameState {

	private final GameController gameController;
	private final IslandScreen islandScreen;
	private final GameModel gameModel;
	private final GameState fromState;
	volatile private List<Player> eligiblePlayers;
	volatile private List<TreasuryCard> eligibleCards;

	public TradeCardState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel, GameState fromState) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.fromState = fromState;
	}
	
	/**
	 * if there is exactly one playerType eligible the selected card is given
	 * automatically to that playerType
	 */
	@Override
	public GameState mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY) {
			return previousState();
		}
		if (event.getButton() != MouseButton.PRIMARY) {
			return null;
		}
		IslandComponent islandComponent = ViewUtils.findIslandComponent((Node) event.getTarget());
		TreasuryCardView cardView = null;
		if (islandComponent instanceof TreasuryCardView) {
			cardView = (TreasuryCardView) islandComponent;
		}else {
			return null;
		}
		if (!eligibleCards.contains(cardView.getParentModel()))return null;
		end();
		return new TradeCardStateStepTwo(gameController, islandScreen, gameModel,
				cardView.getParentModel(), eligiblePlayers, this);

	}

	private GameState previousState() {
		end();
		return fromState;
	}

	@Override
	public GameState buttonPressed(ButtonAction action) {
		if (action == ButtonAction.TRADE){
			return previousState();
		}
		return null;
	}

	@Override
	public GameState getFromState() {
		return fromState;
	}
	
	/**
	 * starts the state if 
	 * 1)the playerType has at least one action
	 * 2)there are eligible cards to give
	 * 3)there are eligible players to receive cards
	  */
	@Override
	public GameState start() {
		islandScreen.c_setSelectedActionButton(ButtonAction.TRADE);
		islandScreen.disableAllButtonsExcluding(ButtonAction.TRADE);
		eligiblePlayers = new ArrayList<>();
		eligibleCards = new ArrayList<>();
		gameModel.findCurrentPlayerEligibleCardsAndPlayersToTrade(eligibleCards, eligiblePlayers);
		if (eligibleCards.size() == 0){
			return fromState;
		}
		if (eligiblePlayers.size() == 0){
			return fromState;
		}
		islandScreen.c_showMessagePanel("Choose a card to give \nRight-Click to cancel");
		for (TreasuryCard card :eligibleCards){
			card.getComponent().setValidToCkickEffect(true);
		}
		return null;

	}

	@Override
	public void end() {
		islandScreen.c_hideMessagePanel();
		for (TreasuryCard card :eligibleCards){
			card.getComponent().setValidToCkickEffect(false);
		}
				
	}


}
