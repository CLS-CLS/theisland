package cls.island.control.state;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.model.Island;
import cls.island.utils.ViewUtils;
import cls.island.view.components.Card;
import cls.island.view.components.IslandTile;
import cls.island.view.components.Piece;
import cls.island.view.screen.IslandComponent;
import cls.island.view.screen.IslandScreen;

public class Normal implements GameState {

	private GameController stateContext;
	private IslandScreen islandScreen;
	private final GameModel gameModel;

	public Normal(GameController stateContext, IslandScreen islandScreen, GameModel gameModel) {
		this.stateContext = stateContext;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
	}

	
	public void mouseClickedOnIslandTile(IslandTile island) {
		if (island.model.isSunk())	return;
		Piece movingPiece = islandScreen.c_getPieces().get(0);
		Island from = movingPiece.model.getIsland();
		if (from!=null && !gameModel.isAdjacentIslands(from, island.model)) return;
		islandScreen.c_movePieceToTile(movingPiece, island);
		
		
	}

	
	public void mouseClickedOnCard(Card card) {
		stateContext.setGameState(new UseShoreUpCard(stateContext, islandScreen,gameModel));
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		final IslandComponent islandComponent = ViewUtils
				.findIslandComponentParent((Node) event.getTarget());
		if (islandComponent instanceof IslandTile) {
			IslandTile islandTile = (IslandTile) islandComponent;
			mouseClickedOnIslandTile(islandTile);
		}else if (islandComponent instanceof Card) {
			Card card = (Card) islandComponent;
			mouseClickedOnCard(card);
		}
		
	}

}
