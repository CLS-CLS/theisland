package cls.island.model.player;

import cls.island.control.Config.PlayerType;
import cls.island.view.component.island.Island;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.player.base.PlayerBase;

public class PilotPlayer extends Player {

	private boolean canFly = true;
	
	protected PilotPlayer(Piece piece, PlayerBase base) {
		super(PlayerType.PILOT, piece, base);
	}
	
	/**
	 * Moves the playerType to the provided island using his flying ability and removes one action.
	 * @param toIsland
	 */
	public int fly(Island toIsland){
		if (piece.getIsland().equals(toIsland)) throw new IllegalArgumentException("Cannot fly to the same island");
		if (!canFly()) throw new RuntimeException("Cannot fly again in this round");
		canFly = false;
		return moveToIsland(toIsland);
	}
	
	/**
	 * Checks if the playerType has used his flying ability in his turn
	 * @return true if he has not used this ability in this turn.
	 */
	public boolean canFly(){
		return canFly;
	}
	
	@Override
	public void resetActions() {
		super.resetActions();
		canFly = true;
	}
	
	/**
	 * for initialization purposes only
	 * @param canFly
	 */
	public void setCanFly(boolean canFly){
		this.canFly = canFly;
	}

}
