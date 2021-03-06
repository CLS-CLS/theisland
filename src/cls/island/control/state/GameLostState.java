package cls.island.control.state;

import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.model.LooseCondition;
import cls.island.view.screen.IslandScreen;

/**
 * Game is over. Shows final pop-up screen and goes back to main menu. 
 *
 */
public class GameLostState implements GameState {

	private final IslandScreen islandScreen;
	private final GameController gameController;
	private final LooseCondition looseCondition;
	private final Object[] infos;

	public GameLostState(LooseCondition looseCondition, GameController gameController,
			IslandScreen islandScreen, GameModel gameModel, GameState fromState, Object... infos) {
		this.looseCondition = looseCondition;
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.infos = infos;
	}

	@Override
	public GameState start() {
		islandScreen.c_showLooseGamePopUp(looseCondition, infos);
		gameController.backToMainScreen();
		return null;
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
	public void end() {
	}

}
