package cls.island.model.player;

import cls.island.control.Options.PlayerType;
import cls.island.view.component.island.Island;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.player.base.PlayerBase;

public class EngineerPlayer extends Player {

	/**
	 * the number of islands the engineer has shore-up in the same action.
	 */
	private int numberOfShoreUps = 0;

	/**
	 * holds the internal state before the shore-up. Uses to restore
	 * the state to undo a shore up
	 */
	private PreviousState previousState = null;

	protected EngineerPlayer(Piece piece, PlayerBase base) {
		super(PlayerType.ENGINEER, piece, base);
	}

	@Override
	public void shoreUp(Island island) {
		previousState = new PreviousState(actionsLeft.get(), numberOfShoreUps);
		island.unFlood();
		switch (numberOfShoreUps) {
		case 0:
			actionsLeft.set(getActionsLeft() - 1);
			numberOfShoreUps++;
			break;
		case 1:
			numberOfShoreUps = 0;
			break;
		}
	}

	@Override
	public int moveToIsland(Island island) {
		numberOfShoreUps = 0;
		return super.moveToIsland(island);
	}

	@Override
	public void resetActions() {
		super.resetActions();
		numberOfShoreUps = 0;
	}

	/**
	 * The engineer player can shore up 2 cards in an action. Having 
	 * the engineer take his last action to shore up a card, will lead to
	 * zero actions left. Even with no more action, the engineer can still 
	 * shore up one more card.  
	 * @return true if the engineer is allowed to shore up a card.
	 */
	@Override
	public boolean canShoreUp() {
		if (actionsLeft.getValue() > 0)
			return true;
		if (actionsLeft.getValue() == 0 && numberOfShoreUps == 1)
			return true;
		return false;
	}

	/**
	 * reverts the internal states as it was before the shore up/
	 * 
	 * special handling of undo shoreup action because of the
	 * special shore up ability of enginner
	 */
	public void undoShoreUp() {
		actionsLeft.set(previousState.actionsLeft);
		numberOfShoreUps = previousState.numberOfShoreUps;
	}

	private class PreviousState {
		int actionsLeft;
		int numberOfShoreUps;

		public PreviousState(int actionsLeft, int numberOfShoreUps) {
			this.actionsLeft = actionsLeft;
			this.numberOfShoreUps = numberOfShoreUps;
		}

	}

}
