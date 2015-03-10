package cls.island.model.player;

import cls.island.control.Config.PlayerType;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.player.base.PlayerBase;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.Type.Ability;

public class MessengerPlayer extends Player{

	protected MessengerPlayer(Piece piece, PlayerBase base) {
		super(PlayerType.MESSENGER, piece, base);
	}
	
	/**
	 * A messenger can give a treasure card to all players even if is not in the 
	 * same tile with them, as long as he has actions.
	 */
	@Override
	public boolean canGiveCard(Player player, TreasuryCard card) {
		assert this.getTreasuryCards().contains(card) : this + " does not have that card";
		assert card.getType().getAbility() == Ability.TREASURE :card + " is not of type TREASURE";
		return hasAction();
	}
	
}
