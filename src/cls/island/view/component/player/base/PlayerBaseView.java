package cls.island.view.component.player.base;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import cls.island.utils.Animations;
import cls.island.utils.FxThreadBlock;
import cls.island.utils.LocCalculator;
import cls.island.utils.LocCalculator.Loc;
import cls.island.view.component.AbstractView;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.TreasuryCardView;

public class PlayerBaseView extends AbstractView<PlayerBase> {

	private final LocCalculator locCalculator;
	private Node activeNode;

	public PlayerBaseView(PlayerBase model, Image playerBaseImg, Image playerImg, LocCalculator locCalculator,
			int index) {
		super(true, model);
		this.locCalculator = locCalculator;
		Rectangle rect = new Rectangle(283, 220, Color.BLUE);
		rect.getStyleClass().add("player-base");
		getChildren().add(rect);
		activeNode = createActiveNode();
		ImageView image = new ImageView(playerBaseImg);
		getChildren().add(image);
		ImageView playerImage = new ImageView(playerImg);
		playerImage.getTransforms().add(new Scale(0.7, 0.7));
		getChildren().add(playerImage);
		playerImage.setTranslateX(5);
		playerImage.setTranslateY(15);
		super.translate(locCalculator.playerBasePositionToLoc(index));
		rearrangeCards();
	}

	// /**
	// * rearrange cards withouts animation
	// */
	// private void setUpCards() {
	// List<TreasuryCard> treasuryCardsInBase =
	// getParentModel().getTreasuryCards();
	// for (int i = 0; i < treasuryCardsInBase.size(); i++) {
	// treasuryCardsInBase.get(i).getComponent()
	// .translate(this.getLoc().add(locCalculator.cardLocationInCardHolder(i)));
	// }
	// }

	// /**
	// * moveToBase without animation
	// */
	// public void setToBase(TreasuryCardView treasuryCard) {
	// int index =
	// PlayerBaseView.this.getParentModel().getTreasuryCards().size() - 1;
	// treasuryCard.setSelectable(true);
	// treasuryCard.translate(PlayerBaseView.this.getLoc().add(
	// locCalculator.cardLocationInCardHolder(index)));
	// }

	public void moveToBase(final TreasuryCardView treasuryCard) {
		FxThreadBlock block = new FxThreadBlock();
		block.execute(() -> {
			int index = PlayerBaseView.this.getParentModel().getTreasuryCards().size() - 1;
			treasuryCard.setSelectable(true);
			Animations.teleportCardToLocationReverse(treasuryCard,
					PlayerBaseView.this.getLoc().add(locCalculator.cardLocationInCardHolder(index)), block);
		});

	}

	public void rearrangeCards() {
		final List<Loc> locationToMove = new ArrayList<>();
		List<TreasuryCard> treasuryCardsInBase = getParentModel().getTreasuryCards();
		final List<TreasuryCardView> cardViews = new ArrayList<>();
		for (int i = 0; i < treasuryCardsInBase.size(); i++) {
			locationToMove.add(this.getLoc().add(locCalculator.cardLocationInCardHolder(i)));
			cardViews.add(treasuryCardsInBase.get(i).getComponent());
		}
		FxThreadBlock block = new FxThreadBlock();
		block.execute(() -> Animations.rearrangeCardsInCardHolder(cardViews, locationToMove, block));
	}

	/**
	 * Every time the base is moved we want to move all its containing cards
	 * with it;
	 */
	@Override
	public void translate(Loc loc) {
		super.translate(loc);
		rearrangeCards();
	}

	/**
	 * Every time the base is moved we want to move all its containing cards
	 * with it;
	 */
	@Override
	public void translate(double x, double y) {
		super.translate(x, y);
		rearrangeCards();
	}

	public void setActive(final boolean active) {
		if (active && !getChildren().contains(activeNode)) {
			getChildren().add(activeNode);
		}
		if (!active) {
			getChildren().remove(activeNode);
		}

	}

	public Node createActiveNode() {
		Group group = new Group();
		int yTextCoord = 30;
		Rectangle rect = new Rectangle(270, 0, 20, 220);

		rect.getStyleClass().add("active-rect");
		DropShadow effect = new DropShadow();
		effect.setColor(Color.LIME);
		effect.setBlurType(BlurType.GAUSSIAN);
		effect.setSpread(0.5);
		effect.setRadius(25);
		rect.setEffect(effect);
		group.getChildren().add(rect);
		// rect.setFill(Color.GREEN);
		for (String s : "A C T I V E".split(" ")) {
			Text t = new Text(s);
			t.setFill(Color.WHITE);
			t.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 16));
			t.setStrokeWidth(10);
			t.setTranslateX(275);
			t.setTranslateY(yTextCoord);
			yTextCoord += 30;
			group.getChildren().add(t);

		}

		return group;
	}

	@Override
	public boolean isSelectable() {
		return false;
	}

}
