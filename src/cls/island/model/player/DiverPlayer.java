package cls.island.model.player;

import java.util.ArrayList;
import java.util.List;

import cls.island.control.Options.PlayerType;
import cls.island.model.IslandGrid;
import cls.island.model.IslandGrid.Direction;
import cls.island.view.component.island.Island;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.player.base.PlayerBase;

public class DiverPlayer extends Player{
	
	private Direction[] adjDirections = new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

	protected DiverPlayer(Piece piece, PlayerBase base) {
		super(PlayerType.DIVER, piece, base);
	}
	
	
	
	@Override
	public boolean isValidMove(Island fromIsland, Island toIsland, IslandGrid<Island> grid) {
		boolean valid = super.isValidMove(fromIsland, toIsland, grid);
		if (valid) return true;
		else return isValidUnderWaterMovement(fromIsland, toIsland,  grid);
	}


	private boolean isValidUnderWaterMovement(Island fromIsland, Island toIsland,
			IslandGrid<Island> grid) {
		if (toIsland.isSunk()){
			return false;
		}
		ArrayList<Island> visitedIslands = new ArrayList<>();
		return isValidUnderWaterMovement(fromIsland, toIsland, grid, visitedIslands);
	}


	private boolean isValidUnderWaterMovement(Island fromIsland, Island toIsland, IslandGrid<Island> grid,
			ArrayList<Island> visitedIslands) {
		List<Island> adjIslands = new ArrayList<>();
		visitedIslands.add(fromIsland);
		for (Direction direction : adjDirections){
			Island currentIsland = grid.getAdjacent(fromIsland, direction);
			if (currentIsland != null) { 
				adjIslands.add(currentIsland);
			}
		}
		for (Island currentIsland : adjIslands){
			if (currentIsland.equals(toIsland)){
				return true;
			}else if (!visitedIslands.contains(currentIsland) && (currentIsland.isSunk() || currentIsland.isFlooded())){
				boolean result = isValidUnderWaterMovement(currentIsland, toIsland, grid, visitedIslands);
				if (result) return result;
			}
		}
		adjIslands.remove(fromIsland);
		return false;
		
	}

}
