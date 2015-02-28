package cls.island.utils;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.util.Duration;
import cls.island.control.Config;
import cls.island.utils.LocCalculator.Loc;
import cls.island.view.components.Card;
import cls.island.view.components.IslandTile;

public class Animations {

	public static void transtition(final Node from, final Node to, final Group root) {
		if (from != null && !root.getChildrenUnmodifiable().contains(from)) {
			throw new IllegalArgumentException("the \"from\" node is not child of root");
		}
		to.setOpacity(0);
		root.getChildren().add(to);
		TimelineSingle timeline = new TimelineSingle(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (from != null) {
					root.getChildren().remove(from);
					from.setOpacity(1);
				}
			}
		});

		timeline.getKeyFrames().add(
				new KeyFrame(new Duration(800), new KeyValue(to.opacityProperty(), 1),
						from != null ? new KeyValue(from.opacityProperty(), 0) : null));
		timeline.play();
	}

	public static void moveComponentToLocation(Node component, Loc location, Config config,
			EventHandler<ActionEvent> onFinish) {
		// TODO get duration from config.
		TimelineSingle timeline = onFinish != null ? new TimelineSingle(onFinish)
				: new TimelineSingle();
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.millis(200), new KeyValue(component.layoutXProperty(),
						location.x), new KeyValue(component.layoutYProperty(), location.y)));
		timeline.play();
	}
	
	/**
	 * makes a card transparent and instantly reappears it in the provided location.
	 * @param card
	 * @param location the location the card will reappear
	 */
	public static void teleportCardToLocation(Card card, Loc location) {
		final Loc fLoc = location;
		final Card fCard = card;
		TimelineSingle timeline =  new TimelineSingle();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200), new KeyValue(card.opacityProperty(),0)));
		timeline.setOnFinished(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				fCard.relocate(fLoc);
				fCard.setOpacity(1);
			}
		});
		
		timeline.play();
	}

	public static void floodTile(IslandTile islandTile, boolean flood) {
		double floodRate = flood ? 0.2D : 0D;
		TimelineSingle timeline = new TimelineSingle();
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.millis(500), new KeyValue(islandTile.floodRateProperty(),
						floodRate)));
		timeline.play();
	}

	public static void sinkTile(IslandTile islandTile) {
		TimelineSingle timeline = new TimelineSingle();
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.millis(500), new KeyValue(islandTile.opacityProperty(), 0)));
		timeline.play();

	}

	public static void rearrangeCardsInCardHolder(List<Card> cardsToMove, List<Loc> locationToMove) {
		List<KeyValue> keyValues = new ArrayList<>();
		for (int i = 0; i < cardsToMove.size(); i++) {
			Card card = cardsToMove.get(i);
			Loc location = locationToMove.get(i);
			KeyValue keyValueX = new KeyValue(card.layoutXProperty(), location.x);
			KeyValue keyValueY = new KeyValue(card.layoutYProperty(), location.y);
			keyValues.add(keyValueX);
			keyValues.add(keyValueY);
		}

		KeyFrame keyFrame = new KeyFrame(Duration.millis(200),
				keyValues.toArray(new KeyValue[keyValues.size() * 2]));
		TimelineSingle timeline = new TimelineSingle();
		timeline.getKeyFrames().add(keyFrame);
		timeline.play();
	}
}