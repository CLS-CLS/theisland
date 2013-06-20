package cls.island.control.state;

import java.util.List;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.model.player.Player;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.screen.IslandScreen;

public class TradeCardStateStepTwo implements GameState {

	private final GameController gameController;
	private final IslandScreen islandScreen;
	private final GameModel gameModel;
	private final TreasuryCard selectedCard;
	private final GameState fromState;
	private final List<Player> eligiblePlayers;

	public TradeCardStateStepTwo(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel, TreasuryCard selectedCard, List<Player> eligiblePlayers, GameState fromState) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.selectedCard = selectedCard;
		this.eligiblePlayers = eligiblePlayers;
		this.fromState = fromState;
	}

	@Override
	public GameState mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY){
			return previousState();
		}
		return null;
	}

	private GameState previousState() {
		islandScreen.c_hideMessagePanel();
		selectedCard.getComponent().setValidToCkickEffect(false);
		for (Player player :eligiblePlayers){
			player.getBase().getComponent().setValidToCkickEffect(false);
		}
		return fromState.createGameState();
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
		islandScreen.c_showMessagePanel("Choose a Player to give the card\nRight-Click to cancel");
		selectedCard.getComponent().setValidToCkickEffect(true);
		for (Player player :eligiblePlayers){
			player.getBase().getComponent().setValidToCkickEffect(true);
		}
		return null;
		
	}

	@Override
	public GameState createGameState() {
		throw new UnsupportedOperationException();
	}

}
