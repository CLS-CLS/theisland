package cls.island.view.components;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Scale;
import cls.island.control.Config;
import cls.island.control.Options;
import cls.island.control.Options.PlayerType;
import cls.island.utils.Animations;
import cls.island.utils.LocCalculator.Loc;

public class PlayerCardHolder extends AbstractComponent {

	private List<Card> cards = new ArrayList<>();

	public PlayerCardHolder(Options.PlayerType playerType, Config config) {
		super(true);
		getChildren().add(new ImageView(config.getPlayerCardHolder()));
		ImageView playerImage = new ImageView(getConfigImage(playerType, config));
		playerImage.getTransforms().add(new Scale(0.7, 0.7));

		getChildren().add(playerImage);
		playerImage.relocate(17, 15);
	}

	private Image getConfigImage(PlayerType playerType, Config config) {
		switch (playerType) {
		default:
			return config.getLolImagePlayer();
		}
	}

	public void addCard(Card card) {
		cards.add(card);
		Animations.moveComponentToLocation(card,
				this.getLoc().add(locCalculator.cardLocationInCardHolder(cards.indexOf(card))), null, null);
		// return locCalculator.cardLocationInCardHolder(cards.indexOf(card));
	}

	public void removeCard(Card card) {
		cards.remove(card);
		List<Card> cardsToMove = new ArrayList<>(cards);
		List<Loc> locationToMove = new ArrayList<>();
		for (int i = 0; i < cards.size(); i++) {
			locationToMove.add(this.getLoc().add(locCalculator.cardLocationInCardHolder(i)));
		}
		Animations.rearrangeCardsInCardHolder(cardsToMove, locationToMove);
	}
	
	public List<Card> getCards(){
		return new ArrayList<>(cards);
	}

	public boolean contains(Card card) {
		for (Card cardInCards : cards) {
			if (card == cardInCards)
				return true;
		}
		return false;
	}
	
	@Override
	public boolean isSelectable() {
		return false;
	}

}
