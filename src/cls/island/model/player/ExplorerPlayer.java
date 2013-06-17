package cls.island.model.player;

import cls.island.control.Options.PlayerType;
import cls.island.model.IslandGrid;
import cls.island.view.component.island.Island;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.player.base.PlayerBase;

public class ExplorerPlayer extends Player {
	
	public ExplorerPlayer(Piece piece, PlayerBase base) {
		super(PlayerType.EXPLORER, piece, base);
	}
	
	@Override
	public boolean isValidMove(Island fromIsland, Island toIsland, IslandGrid<Island> grid) {
		if (toIsland.isSunk()) return false;
		if (grid.isAdjacent(fromIsland, toIsland))return true;
		return false;
	}
	
	@Override
	public boolean isValidShoreUp(Island fromIsland, Island toIsland, IslandGrid<Island> grid) {
		if (toIsland.isSunk())
			return false;
		if (fromIsland == toIsland)return true;
		if (grid.isAdjacent(fromIsland, toIsland)) return true;
		return false;
	}

}
