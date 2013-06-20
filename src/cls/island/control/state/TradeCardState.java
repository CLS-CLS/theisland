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
import cls.island.view.component.treasury.card.Type.Ability;
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
		}
		if (!eligibleCards.contains(cardView.getParentModel()))return null;
		
		if (eligiblePlayers.size() == 1){
			gameModel.getCurrentTurnPlayer().giveCard(eligiblePlayers.get(0), cardView.getParentModel());
			eligiblePlayers.get(0).getBase().getComponent().moveToBase(cardView);
			return previousState();
		}
		islandScreen.c_hideMessagePanel();
		return new TradeCardStateStepTwo(gameController, islandScreen, gameModel,
				cardView.getParentModel(), eligiblePlayers, fromState);

	}

	private GameState previousState() {
		islandScreen.c_hideMessagePanel();
		return fromState.createGameState();
	}

	@Override
	public GameState buttonPressed(ButtonAction action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameState getFromState() {
		return fromState;
	}
	
	/**
	 * starts the state if 
	 * 1)the player has action
	 * 2)there are eligible cards to give
	 * 3)there are eligible players to receive cards
	 * if there is exactly one player eligible the card to be selected is given
	 * automatically to that player
	 */
	@Override
	public GameState start() {
		Player currentPlayer = gameModel.getCurrentTurnPlayer();
		if (!currentPlayer.hasAction()) {
			return fromState.createGameState();
		}
		eligiblePlayers = new ArrayList<>();
		eligibleCards = new ArrayList<>();

		for (TreasuryCard card : currentPlayer.getTreasuryCards()) {
			if (card.getType().getAbility() == Ability.TREASURE) {
				eligibleCards.add(card);
			}
		}
		if (eligibleCards.size() == 0){
			return fromState.createGameState();
		}
		//Tests for an eligible card which player are can receive the card
		for (Player player : gameModel.getPlayers()) {
			if (player.equals(currentPlayer))
				continue;
			if (currentPlayer.canGiveCard(player, eligibleCards.get(0))) {
				eligiblePlayers.add(player);
			}
		}
		if (eligiblePlayers.size() == 0){
			return fromState.createGameState();
		}
		islandScreen.c_showMessagePanel("Choose a card to give \nRight-Click to cancel");
		return null;

	}

	@Override
	public GameState createGameState() {
		return new TradeCardState(gameController, islandScreen, gameModel, fromState);

	}

}
