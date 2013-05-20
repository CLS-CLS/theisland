package cls.island.control;

import javafx.scene.input.MouseEvent;
import cls.island.control.state.Normal;
import cls.island.model.GameModel;
import cls.island.model.Island;
import cls.island.view.components.IslandTile;
import cls.island.view.screen.IslandScreen;

public class GameController {
	
	private GameState gameState;
	private  MainController mainController;
	private  IslandScreen islandScreen;
	private final GameModel gameModel;

	public GameController(MainController mainController, GameModel gameModel, IslandScreen islandScreen){
		this.mainController = mainController;
		this.gameModel = gameModel;
		this.islandScreen = islandScreen;
		gameState = new Normal(this, islandScreen, gameModel);
	}
	
	public void startGame(){
		
	}

	public void mouseClicked(MouseEvent event){
		gameState.mouseClicked(event);
	}

	public void sinkOneLevel(IslandTile island) {
		if (island.model.isSunk()) return;
		if (island.model.isFlooded()){
			island.model.setSunk(true);
			island.sink();
		}else {
			island.model.setFlooded(true);
			island.flood();
		}
	}
	
	public void setGameState(GameState state){
		this.gameState = state;
	}

	public GameState getGameState() {
		return gameState;
	}

	public MainController getMainController() {
		return mainController;
	}

	public void nextTurnPressed() {
		
		
	}
	
	

	
	
	

}
