package cls.island.control.state;

import java.util.List;

import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameState;
import cls.island.control.GameController.ButtonAction;
import cls.island.model.GameModel;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.screen.IslandScreen;

public class CollectTreasureState implements GameState {

	private final GameController gameController;
	private final IslandScreen islandScreen;
	private final GameModel gameModel;

	public CollectTreasureState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel) {
				this.gameController = gameController;
		// TODO Auto-generated constructor stub
				this.islandScreen = islandScreen;
				this.gameModel = gameModel;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void buttonPressed(ButtonAction action) {
		// TODO Auto-generated method stub

	}

	@Override
	public GameState getFromState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start() {
		List<TreasuryCard> treasureCards = gameModel.getCurrentTurnPlayer().getTreasureCollection();

	}

	@Override
	public GameState createGameState() {
		// TODO Auto-generated method stub
		return null;
	}

}
