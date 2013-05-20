package cls.island.view.components;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import cls.island.utils.Animations;
import cls.island.utils.LocCalculator.Loc;

public class CardBase extends AbstractComponent {

	public CardBase(String name, Color background) {
		super(true);
		Rectangle rect = new Rectangle(210, 170);
		rect.setFill(background);
		getChildren().add(rect);

		Text text = new Text(name);
		text.setEffect(new DropShadow());

		text.getStyleClass().add("white-text-button");
		text.setFill(Color.WHITE);
		text.relocate(20, 10);

		Text text2 = new Text("Discard Pile");
		text2.getTransforms().add(new Rotate(-90));
		text2.setFill(Color.WHEAT);
		text2.setFont(new Font(16));
		text2.relocate(110, 110);
		getChildren().add(text);
		getChildren().add(text2);

	}

	private static final Loc PILE_LOC = new Loc(10, 40);
	private static final Loc DISCARD_PILE_LOC = new Loc(120, 40);

	List<Card> pile = new ArrayList<>();
	List<Card> discardPile = new ArrayList<>();

	public void addCardtoPile(Card card) {
		pile.add(card);
		Loc cardLoc = locCalculator.cardLocationInPile(pile.size() - 1).add(PILE_LOC);
		card.relocate(this.getLoc().add(cardLoc));

	}

	public void addAllToPile(List<Card> cards) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	public void removeCardFromPile(Card card) {
		pile.remove(card);
		rearrangePile(pile);
	}

	public void addCardtoDiscardPile(Card card) {
		discardPile.add(card);
		Animations.teleportCardToLocation(
				card,
				this.getLoc().add(DISCARD_PILE_LOC)
						.add(locCalculator.cardLocationInPile(discardPile.size() - 1)));
	}

	public List<Loc> addAllToDiscardPile(List<Card> cards) {
		discardPile.addAll(cards);
		return null;
	}

	public void removeCardFromDiscardPile(Card card) {
		discardPile.remove(card);
		rearrangePile(discardPile);
	}

	private void rearrangePile(List<Card> pile) {
		Loc pileLoc = pile == this.pile ? PILE_LOC : DISCARD_PILE_LOC;
		for (int i = 0; i < pile.size(); i++) {
			pile.get(i).relocate(
					this.getLoc().add(locCalculator.cardLocationInPile(i)).add(pileLoc));
		}
	}

	public List<Card> getPile() {
		return new ArrayList<>(pile);
	}

	public Card getTopPileCard() {
		Card card = null;
		if (pile.size() > 0) {
			card = pile.get(pile.size() - 1);
		}
		return card;
	}

	public boolean containsInPile(Card card) {
		return pile.contains(card);
	}

	public boolean containsInDiscardPile(Card card) {
		return discardPile.contains(card);
	}
	
	@Override
	public boolean isSelectable() {
		return false;
	}

}
