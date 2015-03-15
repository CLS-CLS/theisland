package cls.island.control.state;

import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.model.LooseCondition;
import cls.island.view.component.island.Island;
import cls.island.view.screen.IslandScreen;

/**
 * Reveals one by one the new islands to be flooded/sunk.
 * 
 * @author lytsikas
 * 
 */
public class IslandTurnState implements GameState {
	
	private static int counter = 0;

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

	public IslandTurnState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel, GameState fromState) {
		this(0, gameController, islandScreen, gameModel, fromState);
	}

	@Override
	public GameState mouseClicked(MouseEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public GameState buttonPressed(ButtonAction action) {
		throw new UnsupportedOperationException();
	}

	@Override
	public GameState getFromState() {
		return fromState;
	}

	@Override
	public GameState start() {
		gameController.resetUndoableActions();
		numberOfDrawCards++;
		islandScreen.disableButtons();
		if (numberOfDrawCards > gameModel.getNumberOfIslandsToSink()) {
			gameModel.getCurrentTurnPlayer().getBase().getComponent().setActive(false);
			gameModel.getCurrentTurnPlayer().getPiece().getComponent().setValidToCkickEffect(false);
			gameModel.nextTurn();
			gameModel.getCurrentTurnPlayer().getPiece().getComponent().setValidToCkickEffect(true);
			gameModel.getCurrentTurnPlayer().getBase().getComponent().setActive(true);
			islandScreen.c_setUpButtonsForPlayer(gameModel.getCurrentTurnPlayer());
			counter = 0;
			gameModel.getPlayers().forEach((p)->p.resetActions());
			gameModel.getCurrentTurnPlayer().getPiece().getComponent().createValidToClick().switchEffectOn();
			return new NormalState(gameController, islandScreen, gameModel);

		}
		if (!gameModel.hasIslandToFlood()) {
			gameModel.shuffleDiscardedAndPutBackToNormalPile();
			gameModel.getIslands().forEach((island)->island.getComponent().deactivateSavedNode());
		}
		Island island = gameModel.getNextIslantToFlood();
		counter ++;
		if (island.isFlooded()) {
			gameModel.sinkIsland(island);
			island.getComponent().sink();
			Object[] infos = new Object[1];
			if (gameModel.checkLooseCondition(LooseCondition.TREASURE_SUNK, infos)) {
				return new GameLostState(LooseCondition.TREASURE_SUNK, gameController,
						islandScreen, gameModel, this, infos);
			}
			if (gameModel.checkLooseCondition(LooseCondition.FOOLS_LANDING_LOST)) {
				return new GameLostState(LooseCondition.FOOLS_LANDING_LOST, gameController,
						islandScreen, gameModel, this);
			}
			if (island.getPieces().size() > 0) {
				return new SwimToAdjacentIslandState(island, gameController, islandScreen,
						gameModel, this);
			}
		} else {
			gameModel.floodIsland(island);
			island.getComponent().flood();
			island.getComponent().activateSavedNode();
		}
		return this;
	}

	@Override
	public void end() {
	}

}
