package cls.island.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.util.Duration;
import cls.island.utils.LocCalculator.Loc;
import cls.island.view.component.island.IslandView;
import cls.island.view.component.treasury.card.TreasuryCardView;

public class Animations {

	/**
	 * Transition between screens and menus
	 * @param from
	 * @param to
	 * @param root
	 */
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

	public static void moveComponentToLocation(final Node component, Loc location,
			final EventHandler<ActionEvent> onFinish, final FxThreadBlock block) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					component.toFront();
				}
			});
		} else {
			component.toFront();
		}
		TimelineSingle timeline = new TimelineSingle();
		if (onFinish != null) {
			timeline.setOnFinished((event) -> {
				onFinish.handle(event);
				if (block != null)
					block.unpause();
			});
		} else if (block != null) {
			timeline.setOnFinished((ev) -> block.unpause());
		}

		timeline.getKeyFrames().add(
				new KeyFrame(Duration.millis(200), new KeyValue(component.layoutXProperty(),
						location.x), new KeyValue(component.layoutYProperty(), location.y)));
		timeline.play();
	}

	/**
	 * makes a card transparent and instantly reappears it in the provided
	 * location.
	 * 
	 * @param treasuryCardView
	 * @param location
	 *            the location the card will reappear
	 */
	public static void teleportCardToLocation(TreasuryCardView treasuryCardView, Loc location,
			final Condition condition) {
		final Loc fLoc = location;
		final TreasuryCardView fCard = treasuryCardView;
		TimelineSingle timeline = new TimelineSingle();
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.millis(200), new KeyValue(treasuryCardView.opacityProperty(),
						0)));
		timeline.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				fCard.relocate(fLoc);
				fCard.setOpacity(1);
				condition.signal();
			}
		});

		timeline.play();
	}

	/**
	 * instantly disappear a card and make it visible in another location
	 */
	public static void teleportCardToLocationReverse(TreasuryCardView treasuryCardView,
			Loc location, final FxThreadBlock block){
		System.out.println(Thread.currentThread().getName() + " Start teleporting");
		treasuryCardView.toFront();
		treasuryCardView.setOpacity(0);
		treasuryCardView.relocate(location);
		TimelineSingle timeline = new TimelineSingle();
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.millis(200), new KeyValue(treasuryCardView.opacityProperty(),
						1)));
		timeline.setOnFinished((event) -> {
			System.out.println(Thread.currentThread().getName()
					+ " fininsed Teleporting");
			block.unpause();
		});

		timeline.play();
	}

	public static void floodTile(List<IslandView> islands, List<DoubleProperty> animateProps,
			boolean flood, final FxThreadBlock block) {
		if (flood) {
			shake(null, islands.toArray(new IslandView[islands.size()]));
		}
		double brightness = flood ? 0.37 : 0;
		double contrast = flood ? 0.34 : 0;
		double saturation = flood ? -1 : 0;
		double opacity = flood ? 0.5 : 0;
		TimelineSingle timeline = new TimelineSingle();
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.millis(500), new KeyValue(animateProps.get(0), brightness),
						new KeyValue(animateProps.get(1), contrast), new KeyValue(animateProps
								.get(2), saturation), new KeyValue(animateProps.get(3), opacity)));
		timeline.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				block.unpause();
			}
		});
		timeline.play();
	}

	public static void sinkTile(IslandView islandView, final FxThreadBlock block) {
		shake(null, islandView);
		TimelineSingle timeline = new TimelineSingle();
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.millis(500), new KeyValue(islandView.opacityProperty(), 0)));
		timeline.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				block.unpause();
			}
		});
		timeline.play();
	}

	public static void rearrangeCardsInCardHolder(List<TreasuryCardView> cardsToMove,
			List<Loc> locationToMove, final FxThreadBlock block) {
		List<KeyValue> keyValues = new ArrayList<>();
		for (int i = 0; i < cardsToMove.size(); i++) {
			TreasuryCardView treasuryCardView = cardsToMove.get(i);
			Loc location = locationToMove.get(i);
			KeyValue keyValueX = new KeyValue(treasuryCardView.layoutXProperty(), location.x);
			KeyValue keyValueY = new KeyValue(treasuryCardView.layoutYProperty(), location.y);
			keyValues.add(keyValueX);
			keyValues.add(keyValueY);
		}

		KeyFrame keyFrame = new KeyFrame(Duration.millis(200),
				keyValues.toArray(new KeyValue[keyValues.size() * 2]));
		TimelineSingle timeline = new TimelineSingle();
		timeline.getKeyFrames().add(keyFrame);
		timeline.setOnFinished((event) -> block.unpause());
		timeline.play();
	}

	public static void shake(final Condition condition, Node... nodes) {
		TimelineSingle timeline = new TimelineSingle();
		double timeStep = 10D;
		double moveStep = 2;
		Duration totalDuration = Duration.millis(0);
		for (int j = 0; j < 10; j++) {
			totalDuration = new Duration(totalDuration.toMillis() + timeStep);
			KeyValue[] keyValues = new KeyValue[nodes.length];
			for (int i = 0; i < nodes.length; i++) {
				keyValues[i] = new KeyValue(nodes[i].layoutXProperty(), nodes[i].layoutXProperty()
						.getValue() - moveStep);
			}
			timeline.getKeyFrames().add(new KeyFrame(totalDuration, keyValues));

			totalDuration = new Duration(totalDuration.toMillis() + timeStep * 2);
			KeyValue[] keyValues2 = new KeyValue[nodes.length];
			for (int i = 0; i < nodes.length; i++) {
				keyValues2[i] = new KeyValue(nodes[i].layoutXProperty(), nodes[i].layoutXProperty()
						.getValue() + moveStep * 2);
			}
			timeline.getKeyFrames().add(new KeyFrame(totalDuration, keyValues2));

			totalDuration = new Duration(totalDuration.toMillis() + timeStep);
			KeyValue[] keyValues3 = new KeyValue[nodes.length];
			for (int i = 0; i < nodes.length; i++) {
				keyValues3[i] = new KeyValue(nodes[i].layoutXProperty(), nodes[i].layoutXProperty()
						.getValue() - moveStep);
			}
			timeline.getKeyFrames().add(new KeyFrame(totalDuration, keyValues3));
		}
		if (condition != null) {
			timeline.setOnFinished(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					condition.signal();
				}
			});
		}
		timeline.play();
	}
}