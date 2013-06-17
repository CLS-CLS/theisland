package cls.island.view.component.player.base;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import cls.island.utils.Animations;
import cls.island.utils.LocCalculator;
import cls.island.utils.LocCalculator.Loc;
import cls.island.utils.SignaledRunnable;
import cls.island.view.component.AbstractView;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.TreasuryCardView;

public class PlayerBaseView extends AbstractView<PlayerBase> {

	private final LocCalculator locCalculator;
	private Node activeNode;

	public PlayerBaseView(PlayerBase model, Image playerBaseImg, Image playerImg,
			Color color, LocCalculator locCalculator, int index) {
		super(true, model);
		Rectangle rect = new Rectangle(283,220,color);
		getChildren().add(rect);
		this.locCalculator = locCalculator;
		activeNode = createActiveNode();
		getChildren().add(new ImageView(playerBaseImg));
		ImageView playerImage = new ImageView(playerImg);
		playerImage.getTransforms().add(new Scale(0.7, 0.7));
		getChildren().add(playerImage);
		playerImage.relocate(17, 15);
		super.relocate(locCalculator.playerBasePositionToLoc(index));
		rearrangeCards();
	}

	public void moveToBase(final TreasuryCardView treasuryCard) {
		execute(new SignaledRunnable() {

			@Override
			public boolean willSignal() {
				return true;
			}

			@Override
			public void run() {
				int index = PlayerBaseView.this.getParentModel().getTreasuryCards().size() - 1;
				treasuryCard.setSelectable(true);
				Animations.teleportCardToLocationReverse(treasuryCard, PlayerBaseView.this.getLoc()
						.add(locCalculator.cardLocationInCardHolder(index)), wait);
			}
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

		execute(new SignaledRunnable() {

			@Override
			public boolean willSignal() {
				return true;
			}

			@Override
			public void run() {
				Animations.rearrangeCardsInCardHolder(cardViews, locationToMove, wait);

			}
		});

	}

	/**
	 * Every time the base is moved we want to move all its containing cards
	 * with it;
	 */
	@Override
	public void relocate(Loc loc) {
		super.relocate(loc);
		rearrangeCards();
	}

	/**
	 * Every time the base is moved we want to move all its containing cards
	 * with it;
	 */
	@Override
	public void relocate(double x, double y) {
		super.relocate(x, y);
		rearrangeCards();
	}

	public void setActive(final boolean active) {
		execute(new SignaledRunnable() {

			@Override
			public boolean willSignal() {
				return false;
			}

			@Override
			public void run() {
				if (active && !getChildren().contains(activeNode)) {
					getChildren().add(activeNode);
				}
				if (!active) {
					getChildren().remove(activeNode);
				}

			}
		});

	}

	public Node createActiveNode() {
		Group group = new Group();
		int yTextCoord = 30;
		Rectangle rect = new Rectangle(270, 0, 20, 220);
		rect.setEffect(new InnerShadow());
		group.getChildren().add(rect);
		rect.setFill(Color.GREEN);
		for (String s : "A C T I V E".split(" ")) {
			Text t = new Text(s);
			t.setFill(Color.WHITE);
			t.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 16));
			t.setStrokeWidth(10);
			t.relocate(275, yTextCoord);
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
