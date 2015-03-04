package cls.island.view.component.treasury.pile;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import cls.island.utils.Animations;
import cls.island.utils.FxThreadBlock;
import cls.island.utils.LocCalculator.Loc;
import cls.island.view.component.AbstractView;
import cls.island.view.component.treasury.card.TreasuryCardView;
import cls.island.view.component.treasury.pile.TreasuryPile.PileType;

public class PileView extends AbstractView<TreasuryPile> {

	public PileView(String name, Color background, TreasuryPile treasuryPile) {
		super(true, treasuryPile);
		Rectangle rect = new Rectangle(210, 170);
		rect.setFill(background);
		getChildren().add(rect);
		Text text = new Text(name);
		text.setEffect(new DropShadow());
		text.getStyleClass().add("white-text-button");
		text.setFill(Color.WHITE);
		text.setTranslateX(20);
		text.setTranslateY(10);
		Text text2 = new Text("Discard Pile");
		text2.getTransforms().add(new Rotate(-90));
		text2.setFill(Color.WHEAT);
		text2.setFont(new Font(16));
		text2.getTransforms().add(new Translate(110, 140));
		getChildren().add(text);
		getChildren().add(text2);

	}

	private static final Loc PILE_LOC = new Loc(10, 40);
	private static final Loc DISCARD_PILE_LOC = new Loc(120, 40);

	public void moveCardtoPile(final TreasuryCardView treasuryCard, PileType pileType) {
		Loc loc = (pileType == PileType.NORMAL) ? PILE_LOC : DISCARD_PILE_LOC;
		int index = pileType == PileType.NORMAL ? getParentModel().getNormalPileCards().size() - 1 : getParentModel()
				.getDiscardPileCards().size() - 1;
		final Loc cardLoc = locCalculator.cardLocationInPile(index).add(loc);
		FxThreadBlock block = new FxThreadBlock();
		block.execute(() -> Animations.moveComponentToLocation(treasuryCard, 
				PileView.this.getLoc().add(cardLoc), null, block));
		treasuryCard.setSelectable(false);

	}


	/**
	 * Z-orders the cards treasure cards. Called after shuffling 
	 */
	public void rearrangePiles() {
		for (int i = 0; i < getParentModel().getDiscardPileCards().size(); i++) {
			TreasuryCardView card = getParentModel().getDiscardPileCards().get(i).getComponent();
			card.toFront();
			card.translate(PileView.this.getLoc().add(locCalculator.cardLocationInPile(i)).add(DISCARD_PILE_LOC));
			card.setSelectable(false);
		}
		for (int i = 0; i < getParentModel().getNormalPileCards().size(); i++) {
			TreasuryCardView card = getParentModel().getNormalPileCards().get(i).getComponent();
			card.toFront();
			card.translate(PileView.this.getLoc().add(locCalculator.cardLocationInPile(i)).add(PILE_LOC));
			card.setSelectable(false);
		}
	}

	@Override
	public boolean isSelectable() {
		return false;
	}

	@Override
	public void translate(Loc loc) {
		super.translate(loc);
		rearrangePiles();
	}

	@Override
	public void translate(double x, double y) {
		super.translate(x, y);
		rearrangePiles();
	}

}
