package cls.island.model.player;

import cls.island.control.Config.PlayerType;
import cls.island.view.component.island.Island;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.player.base.PlayerBase;

public class EngineerPlayer extends Player {

	/**
	 * the number of islands the engineer has shore-up in the same action.
	 */
	private boolean consequentShoreUps = false;

	protected EngineerPlayer(Piece piece, PlayerBase base) {
		super(PlayerType.ENGINEER, piece, base);
	}

	@Override
	public void shoreUp(Island island) {
		island.unFlood();
		if (!consequentShoreUps){
			actionsLeft.set(getActionsLeft() - 1);
		}
		consequentShoreUps = !consequentShoreUps;
	}

	@Override
	public int moveToIsland(Island island) {
		consequentShoreUps = false;
		return super.moveToIsland(island);
	}

	@Override
	public void resetActions() {
		super.resetActions();
		consequentShoreUps = false;
	}

	/**
	 * The engineer playerType can shore up 2 cards in an action. Having the
	 * engineer take his last action to shore up a card, will lead to zero
	 * actions left. Even with no more action, the engineer can still shore up
	 * one more card.
	 * 
	 * @return true if the engineer is allowed to shore up a card.
	 */
	@Override
	public boolean canShoreUp() {
		if (actionsLeft.getValue() > 0)
			return true;
		if (actionsLeft.getValue() == 0 && consequentShoreUps)
			return true;
		return false;
	}

	public boolean isConsequentShoreUp() {
		return consequentShoreUps;
	}

	/**
	 * reverts the internal states as it was before the shore up/
	 * 
	 * special handling of undo shoreup action because of the special shore up
	 * ability of engineer
	 */
	public void undoShoreUp(int actionsLeft, boolean consequentShoreUps) {
		this.actionsLeft.set(actionsLeft);
		this.consequentShoreUps = consequentShoreUps;
	}

}
