package cls.island.control.state;

import java.util.List;

import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.model.player.Player;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.Type;
import cls.island.view.screen.IslandScreen;

public class CollectTreasureState implements GameState {

	private final GameController gameController;
	private final IslandScreen islandScreen;
	private final GameModel gameModel;
	private final GameState fromState;

	public CollectTreasureState(GameController gameController, IslandScreen islandScreen, GameModel gameModel,
			GameState fromState) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.fromState = fromState;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
	}

	@Override
	public void buttonPressed(ButtonAction action) {
	}

	@Override
	public GameState getFromState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start() {
		Player currentPlayer = gameModel.getCurrentTurnPlayer();
		List<TreasuryCard> collectionCards = currentPlayer.getTreasureCollection();
		Type treasureOnIsland = currentPlayer.getPiece().getIsland().getTreasure();
		if (!currentPlayer.hasAction() || collectionCards.size() == 0
				|| !collectionCards.get(0).getType().equals(treasureOnIsland)
				|| gameModel.getTreasureBag().isTreasureCollected(collectionCards.get(0).getType())) {
			gameController.setGameState(fromState.createGameState());
			return;
		}
		
		gameModel.collectTreasure(collectionCards);
		for (TreasuryCard card : collectionCards) {
			islandScreen.c_discardPlayerCard(currentPlayer.getBase().getComponent(), card.getComponent());
		}
		gameController.setGameState(fromState.createGameState());

	}

	@Override
	public GameState createGameState() {
		throw new UnsupportedOperationException();
	}

}
