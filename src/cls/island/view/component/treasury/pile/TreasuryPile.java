package cls.island.view.component.treasury.pile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.paint.Color;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.TreasuryCard.ViewStatus;

public class TreasuryPile {

	public static enum PileType {
		DISCARD, NORMAL;
	}

	private PileView treasuryPileView;
	protected volatile List<TreasuryCard> pile = new ArrayList<>();
	protected volatile List<TreasuryCard> discardPile = new ArrayList<>();

	public TreasuryPile(String name, Color background) {
		this(new ArrayList<TreasuryCard>(), new ArrayList<TreasuryCard>(), name, background);
	}

	public TreasuryPile(List<TreasuryCard> pile, List<TreasuryCard> discardPile, String name,
			Color background) {
		treasuryPileView = new PileView(name, background, this);
		this.pile.addAll(pile);
		this.discardPile.addAll(discardPile);
	}

	public void addToPile(final TreasuryCard card, final PileType pileType) {
		final List<TreasuryCard> targetPile = (pileType == PileType.NORMAL ? this.pile : this.discardPile);
		if (pileType==PileType.NORMAL){
			card.setViewStatus(ViewStatus.FACE_DOWN);
		}else {
			card.setViewStatus(ViewStatus.FACE_UP);
		}
		targetPile.add(card);
	}

	public void removeFromPile(final TreasuryCard card, final PileType pileType) {
		final List<TreasuryCard> targetPile = (pileType == PileType.NORMAL ? this.pile : this.discardPile);
		targetPile.remove(card);
	}

	public TreasuryCard getTopPileCard() {
		TreasuryCard treasuryCard = null;
		if (pile.size() > 0) {
			treasuryCard = pile.get(pile.size() - 1);
		}
		return treasuryCard;
	}
	
	public TreasuryCard getTopDiscardPileCard() {
		TreasuryCard treasuryCard = null;
		if (discardPile.size() > 0) {
			treasuryCard = discardPile.get(discardPile.size() - 1);
		}
		return treasuryCard;
	}

	public boolean containsInPile(TreasuryCard treasuryCard) {
		return pile.contains(treasuryCard);
	}

	public boolean containsInDiscardPile(TreasuryCard treasuryCard) {
		return discardPile.contains(treasuryCard);
	}

	public PileView getComponent() {
		return treasuryPileView;
	}
	
	
	protected List<TreasuryCard> getDiscardPileCards(){
		return new ArrayList<>(discardPile);
	}
	
	protected List<TreasuryCard> getNormalPileCards(){
		return new ArrayList<>(pile);
	}

	public List<TreasuryCard> getTreasuryCards(PileType pileType) {
		switch (pileType) {
		case DISCARD:
			return getDiscardPileCards();
		default:
			return getNormalPileCards();
		}
	}
	
	public boolean isPileEmpty(){
		return pile.size()==0;
	}
	
	public void replenishNormalPile(){
		Collections.shuffle(discardPile);
		pile.addAll(discardPile);
		discardPile.clear();
		for (TreasuryCard  card: pile){
			card.setViewStatus(ViewStatus.FACE_DOWN);
		}
	}
}
