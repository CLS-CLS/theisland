package cls.island.control.state;

import java.util.List;

import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.action.UnrevertableAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.model.player.Player;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.Type;
import cls.island.view.screen.IslandScreen;

public class CollectTreasureState implements GameState {

	private final IslandScreen islandScreen;
	public final GameModel gameModel;
	private final GameState fromState;
	private final GameController gameController;

	public CollectTreasureState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel, GameState fromState) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.fromState = fromState;
	}

	@Override
	public GameState mouseClicked(MouseEvent event) {
		return null;
	}

	@Override
	public GameState buttonPressed(ButtonAction action) {
		return null;
	}

	@Override
	public GameState getFromState() {
		throw new UnsupportedOperationException();
	}

	@Override
	public GameState start() {
		islandScreen.disableButtons();
		gameController.executeAction(new UnrevertableAction() {

			@Override
			public void execute() {
				Player currentPlayer = gameModel.getCurrentTurnPlayer();
				List<TreasuryCard> collectionCards = gameModel.getTreasureCollection(currentPlayer);
				if (gameModel.canCollectTreasure(currentPlayer)) {
					Type collectedType = gameModel.collectTreasure(collectionCards);
					gameModel.getTreasureBag().getComponent().removeEffect(collectedType);
					for (TreasuryCard card : collectionCards) {
						islandScreen.c_discardPlayerCard(currentPlayer.getBase().getComponent(),
								card.getComponent());
					}
				}
			}
		});

		return fromState;
	}

}
