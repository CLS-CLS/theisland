package cls.island.model.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import cls.island.control.Options.PlayerType;
import cls.island.model.IslandGrid;
import cls.island.model.IslandGrid.Direction;
import cls.island.view.component.island.Island;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.player.base.PlayerBase;

public class NavigatorPlayer extends Player {

	protected NavigatorPlayer(Piece piece, PlayerBase base) {
		super(PlayerType.NAVIGATOR, piece, base);
	}

	/**
	 * Moves the player to the provided island using his flying ability and
	 * removes one action.
	 * 
	 * @param toIsland
	 */
	public int moveOtherPlayer(Player otherPlayer, Island toIsland) {
		actionsLeft.set(actionsLeft.get() - 1);
		return otherPlayer.setToIsland(toIsland);
	}

	public boolean isValidOtherPlayerMove(Player otherPlayer, Island toIsland, IslandGrid<Island> islandGrid) {
		Island otherIsland = otherPlayer.getPiece().getIsland();
		List<Island> tier_1_islands = new ArrayList<>();
		tier_1_islands.add(otherIsland);
		List<Direction> horizAndVert = Arrays.asList(new Direction[]{Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT});
		for (Direction d : horizAndVert) {
			Island adjIsland = islandGrid.getAdjacent(otherIsland, d);
			if (adjIsland != null && !adjIsland.isSunk()) {
				tier_1_islands.add(adjIsland);
			}
		}
		
		Optional<Island> result = tier_1_islands.stream()
				.filter((island) -> {
					Direction d = islandGrid.findDirection(island, toIsland);
					return d != null && horizAndVert.contains(d);
				}).findFirst();
		return result.isPresent();
	}

	public boolean canMoveOtherPlayer() {
		return actionsLeft.get() > 0;
	}

	@Override
	public void resetActions() {
		super.resetActions();
	}


}
