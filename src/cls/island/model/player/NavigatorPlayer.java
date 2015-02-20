package cls.island.model.player;

import java.util.ArrayList;
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

	private boolean moveOtherPlayer = true;

	/**
	 * Moves the player to the provided island using his flying ability and
	 * removes one action.
	 * 
	 * @param toIsland
	 */
	public int moveOtherPlayer(Player otherPlayer, Island toIsland) {
		moveOtherPlayer = false;
		actionsLeft.set(actionsLeft.get() - 1);
		return otherPlayer.setToIsland(toIsland);
	}

	public boolean isValidOtherPlayerMove(Player otherPlayer, Island toIsland, IslandGrid<Island> islandGrid) {
		Island otherIsland = otherPlayer.getPiece().getIsland();
		List<Island> tier_1_islands = new ArrayList<>();
		for (Direction d : Direction.values()) {
			Island adjIsland = islandGrid.getAdjacent(otherIsland, d);
			if (adjIsland != null && !adjIsland.isSunk()) {
				tier_1_islands.add(adjIsland);
			}
		}
		Optional<Island> result = tier_1_islands.stream()
				.filter((island) -> islandGrid.findDirection(island, toIsland) != null).findFirst();
		return result.isPresent();
	}

	public boolean canMoveOtherPlayer() {
		return moveOtherPlayer;
	}

	@Override
	public void resetActions() {
		super.resetActions();
		moveOtherPlayer = true;
	}

	/**
	 * for initialization purposes only
	 */
	public void setMoveOtherPlayer(boolean canMoveOtherPlayer) {
		this.moveOtherPlayer = canMoveOtherPlayer;
	}

}
