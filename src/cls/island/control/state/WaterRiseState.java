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
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.TreasuryCardView;
import cls.island.view.component.treasury.card.Type;
import cls.island.view.screen.IslandComponent;
import cls.island.view.screen.IslandScreen;

public class WaterRiseState implements GameState {

	private final GameController gameController;
	private final IslandScreen islandScreen;
	private final GameState fromState;
	private final GameModel gameModel;
	private Player currentPlayer;
	

	public WaterRiseState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel,  GameState fromState) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.fromState = fromState;
		currentPlayer = gameModel.getCurrentTurnPlayer();
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		// check left click
		if (event.getButton() != MouseButton.PRIMARY)
			return;
		// check clicked on treasy card
		IslandComponent component = ViewUtils.findIslandComponent((Node) event.getTarget());
		if (component == null || !(component instanceof TreasuryCardView))
			return;
		final TreasuryCard selectedCard = ((TreasuryCardView) component).getParentModel();

		// check that is a sandbag
		if (Type.SANDBAGS != selectedCard.getType())
			return;

		// check that it belongs to a player
		boolean belongsToPlayer = false;
		for (Player player : gameModel.getPlayers()) {
			for (TreasuryCard card : player.getTreasuryCards()) {
				if (card == selectedCard) {
					belongsToPlayer = true;
					break;
				}
			}
		}
		if (!belongsToPlayer)
			return;

		islandScreen.c_hideMessagePanel();
		gameController.setGameState(new UseShoreUpCardState(gameController, islandScreen,
				gameModel, selectedCard, WaterRiseState.this));
	}

	@Override
	public void buttonPressed(ButtonAction action) {
		// TODO change to OK
		if (action == ButtonAction.NEXT_TURN) {
			gameController.setGameState(fromState.createGameState());
		}
	}

	@Override
	public GameState getFromState() {
		return fromState;
	}

	@Override
	public void start() {
		if (gameModel.checkLooseCondition() != null) {
			gameController.setGameState(new GameLostState(gameController, islandScreen, gameModel,
					this));
			return;
		}
		boolean shoreUpCardsExist = false;
		for (Player player : gameModel.getPlayers()) {
			for (TreasuryCard card : player.getTreasuryCards()) {
				if (card.getType() == Type.SANDBAGS) {
					shoreUpCardsExist = true;
					break;
				}
			}
		}
		boolean floodTileExist = false;
		for (Island island : gameModel.getIslands())
			if (island.isFlooded() && !island.isSunk()){
				floodTileExist = true;
				break;
		}
		if (shoreUpCardsExist && floodTileExist) {
			islandScreen
					.c_showMessagePanel("Select a SANDBAG card and save an Island \nClick OK when ready");
		} else {
			gameController.setGameState(fromState.createGameState());
		}
	}

	@Override
	public GameState createGameState() {
		return new WaterRiseState(gameController, islandScreen, gameModel, 	getFromState());
	}

}
