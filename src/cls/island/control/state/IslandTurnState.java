package cls.island.control.state;

import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.view.component.island.Island;
import cls.island.view.screen.IslandScreen;

/**
 * Reveals one by one the new islands to be flooded/sunk.
 * 
 * @author lytsikas
 * 
 */
public class IslandTurnState implements GameState {

	private int numberOfDrawCards;
	private GameController gameController;
	private IslandScreen islandScreen;
	private GameModel gameModel;
	private final GameState fromState;

	public IslandTurnState(int numberOfDrawCards, GameController gameController,
			IslandScreen islandScreen, GameModel gameModel, GameState fromState) {

		this.numberOfDrawCards = numberOfDrawCards;
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.fromState = fromState;
	}

	private GameState drawCard() {

		if (numberOfDrawCards > gameModel.getNumberOfIslandsToSink()) {
			gameModel.getCurrentTurnPlayer().getBase().getComponent().setActive(false);
			gameModel.nextTurn();
			gameModel.getCurrentTurnPlayer().getBase().getComponent().setActive(true);
			return new NormalState(gameController, islandScreen, gameModel);
			
		}
		if (!gameModel.hasIslandToFlood()) {
			gameModel.shuffleDiscardedAndPutBackToNormalPile();
		}
		Island island = gameModel.getNextIslantToFlood();
		if (island.isFlooded()) {
			gameModel.sinkIsland(island);
			island.getComponent().sink();
		} else {
			gameModel.floodIsland(island);
			island.getComponent().flood();

		}
		return new IslandTurnState(numberOfDrawCards + 1, gameController,
				islandScreen, gameModel, fromState);

	}

	public IslandTurnState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel, GameState fromState) {
		this(1, gameController, islandScreen, gameModel, fromState);
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
		return fromState;
	}

	@Override
	public GameState start() {
		return drawCard();
	}

	@Override
	public GameState createGameState() {
		return new IslandTurnState(gameController, islandScreen, gameModel, getFromState());
	}

}
