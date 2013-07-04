package cls.island.control.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.model.LooseCondition;
import cls.island.model.player.Player;
import cls.island.utils.ViewUtils;
import cls.island.view.component.island.Island;
import cls.island.view.component.island.IslandView;
import cls.island.view.component.piece.Piece;
import cls.island.view.screen.IslandComponent;
import cls.island.view.screen.IslandScreen;

public class SwimToAdjacentIslandState implements GameState {

	private final Island sinkedIsland;
	private final GameController gameController;
	private final IslandScreen islandScreen;
	private final GameModel gameModel;
	private final GameState fromState;
	private Player currentPlayer;
	Map<Player, List<Island>> possibleMovePerPlayer = new HashMap<>();

	public SwimToAdjacentIslandState(Island island, GameController gameController, IslandScreen islandScreen,
			GameModel gameModel, GameState fromState) {
		this.sinkedIsland = island;
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.fromState = fromState;
	}

	@Override
	public GameState mouseClicked(MouseEvent event) {
		IslandComponent compenent = ViewUtils.findIslandComponent((Node) event.getTarget());
		if (compenent == null) {
			return null;
		}
		Island island = null;
		if (compenent instanceof IslandView) {
			island = ((IslandView) compenent).getParentModel();
		} else {
			return null;
		}
		if (island.isSunk())
			return null;
		if (!currentPlayer.isValidMove(currentPlayer.getPiece().getIsland(), island, gameModel.getIslandGrid())){
			return null;
		}
		int index = currentPlayer.setToIsland(island);
		islandScreen.c_movePiece(currentPlayer.getPiece().getComponent(), island.getComponent(), index);
		possibleMovePerPlayer.remove(currentPlayer);
		currentPlayer = null;
		for (Player player : possibleMovePerPlayer.keySet()) {
			if (possibleMovePerPlayer.get(player).size() > 1) {
				currentPlayer = player;
				islandScreen.c_hideMessagePanel();
				islandScreen.c_showMessagePanel("Move " + currentPlayer.getPiece().getColor() + " piece to an island");
			}
		}
		if (currentPlayer == null){
			islandScreen.c_hideMessagePanel();
			return fromState.createGameState();
		}
		return null;
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
		// check if all player can move to adjacent tile. If not the end
		// if yes
		// auto move players with one options, manual move player with more
		// options
		for (Piece piece : sinkedIsland.getPieces()) {
			Player player = gameController.getPlayerWithPiece(piece);
			List<Island> possibleMoves = new ArrayList<>();
			for (Island island : gameModel.getIslands()) {
				if (island.isSunk())
					continue;
				if (player.isValidMove(sinkedIsland, island, gameModel.getIslandGrid())) {
					possibleMoves.add(island);
				}
			}
			if (possibleMoves.size() == 0) {
				return new GameLostState(LooseCondition.PLAYER_SUNK, gameController, islandScreen, gameModel, fromState);
			}
			possibleMovePerPlayer.put(player, possibleMoves);
		}
		// automatic move player
		boolean manualMove = false;
		for (Player player : possibleMovePerPlayer.keySet()) {
			if (possibleMovePerPlayer.get(player).size() == 1) {
				Island toIsland = possibleMovePerPlayer.get(player).get(0);
				int index = player.setToIsland(toIsland);
				islandScreen.c_movePiece(player.getPiece().getComponent(), toIsland.getComponent(), index);
			} else {
				manualMove = true;
			}
		}

		if (!manualMove) {
			return fromState.createGameState();
		}

		for (Player player : possibleMovePerPlayer.keySet()) {
			if (possibleMovePerPlayer.get(player).size() > 1) {
				currentPlayer = player;
				break;
			}
		}
		islandScreen.c_showMessagePanel("Move " + currentPlayer.getPiece().getColor() + " piece to an island");
		return null;
	}

	@Override
	public GameState createGameState() {
		// TODO Auto-generated method stub
		return null;
	}

}
