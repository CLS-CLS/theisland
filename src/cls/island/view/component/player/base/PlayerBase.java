package cls.island.view.component.player.base;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.paint.LinearGradient;
import cls.island.control.PlayerAndColor;
import cls.island.control.Options.PlayerType;
import cls.island.utils.LocCalculator;
import cls.island.view.component.treasury.card.TreasuryCard;

public class PlayerBase {

	private PlayerType playerType;
	private volatile List<TreasuryCard> treasuryCards = new ArrayList<>();
	private PlayerBaseView playerBaseView;

	public PlayerBase(PlayerAndColor playerType, Image playerBaseImg, Image playerImg, int index) {
		this(playerType, new ArrayList<TreasuryCard>(), playerBaseImg, playerImg, index);
	}

	public PlayerBase(PlayerAndColor playerType, List<TreasuryCard> treasuryCards, Image playerBaseImg, Image playerImg,
			int index) {
		this.playerType = playerType.getPlayer();
		this.treasuryCards.addAll(treasuryCards);
	
		this.playerBaseView = new PlayerBaseView(this, playerBaseImg, playerImg, 
				LinearGradient.valueOf("linear-gradient(to bottom right, beige, BURLYWOOD,white)"),LocCalculator.getInstance(), index);
	}

	public List<TreasuryCard> getTreasuryCards() {
		return new ArrayList<TreasuryCard>(treasuryCards);
	}

	public PlayerType getPlayerType() {
		return playerType;
	}
	
	/**
	 * 
	 * @param card
	 * @return the index of the card in the players hand
	 */
	public int addCard(final TreasuryCard card) {
		treasuryCards.add(card);
		return treasuryCards.size() - 1;
	}

	public void removeCard(TreasuryCard card) {
		boolean removed = treasuryCards.remove(card);
		if (!removed)
			throw new IllegalArgumentException("Treasury Card " + card + " is not contained it the list");
	}

	public PlayerBaseView getComponent() {
		return playerBaseView;
	}

}
