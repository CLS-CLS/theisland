package cls.island.view.component.island;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import cls.island.utils.Animations;
import cls.island.utils.TimelineSingle;
import cls.island.utils.TimelineSingle.Process;
import cls.island.utils.concurrent.NoSignalingRunnable;
import cls.island.utils.concurrent.SignaledRunnable;
import cls.island.utils.concurrent.ThreadBlockingRunnable;
import cls.island.view.component.AbstractView;
import cls.island.view.component.island.Island.Model;

public class IslandView extends AbstractView<Island> {
	private static final boolean FLOOD = true;
	private static final boolean UNFLOOD = false;

	public enum Position {
		UP, DOWN;
	}

	private Rectangle flood;
	private ImageView islandImage;
	private double width;
	private double height;
	private Node savedNode;
	private ImageView treasureImageView;
	final ColorAdjust floodEffect;

	public IslandView(Image tileImage, Model model, Island component) {
		this(tileImage, model, component, null);
	}

	public IslandView(Image tileImage, Model model, Island component, Image treasureImg) {
		super(true, component);
		floodEffect = new ColorAdjust(0, 0, 0, 0);
		init(tileImage);
		if (model.isFloodBorder()) {
			activateSavedNode();
		}
		if (model.isFlooded()) {
			flood();
		}
		if (model.isSunk()) {
			sink();
		}
		if (treasureImg != null) {
			treasureImageView = new ImageView(treasureImg);
			getChildren().add(treasureImageView);
			treasureImageView.relocate(0, 50);
		}
	}

	private void init(Image tileImage) {
		width = tileImage.getRequestedWidth();
		height = tileImage.getRequestedHeight();
		islandImage = new ImageView(tileImage);
		savedNode = createBorder();
		// if (new Random().nextBoolean()){
		flood = new Rectangle(width, height, Color.BLUE);
		flood.opacityProperty().set(0);
		flood.setBlendMode(BlendMode.OVERLAY);
		// }

		islandImage.setEffect(floodEffect);
		getChildren().add(islandImage);
		if (flood != null)
			getChildren().add(flood);
	}

	private Node createBorder() {
		return new ImageView(new Image("images/other/saved.png", 20, 20, false, true));
	}

	public void activateSavedNode() {
		execute(new NoSignalingRunnable() {
			@Override
			public void run() {
				IslandView.this.getChildren().add(savedNode);
				savedNode.relocate(0, 100);
			}
		});

	}

	public void deactivateSavedNode() {
		TimelineSingle timelineSingle = new TimelineSingle(Process.ASYNC);
		timelineSingle.getKeyFrames().add(
				new KeyFrame(Duration.millis(200), new KeyValue(savedNode.opacityProperty(), 0)));
		timelineSingle.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				IslandView.this.getChildren().remove(savedNode);
				savedNode.setOpacity(1);
			}
		});
		timelineSingle.play();
	}

	public void sink() {
		execute(new ThreadBlockingRunnable() {

			@Override
			public boolean willSignal() {
				return true;
			}

			@Override
			public void run() {
				Animations.sinkTile(IslandView.this, this);
			}
		});
	}

	/**
	 * make an island flooded without anim.
	 */
	public void setFlood() {
		// TODO Fix this ugly code.. same properties are in animation .. yack!!
		final double brightness = 0.37;
		final double contrast = 0.34;
		final double saturation = -1;
		final double opacity = 0.5;
		floodEffect.setBrightness(brightness);
		floodEffect.setContrast(contrast);
		floodEffect.setSaturation(saturation);
		flood.setOpacity(opacity);
	}

	public void flood() {
		execute(new ThreadBlockingRunnable() {
			@Override
			public void run() {
				animate(FLOOD, this);
			}
		});

	}

	private void animate(boolean floodBool, SignaledRunnable runnable) {
		List<DoubleProperty> animateProps = new ArrayList<>();
		animateProps.add(floodEffect.brightnessProperty());
		animateProps.add(floodEffect.contrastProperty());
		animateProps.add(floodEffect.saturationProperty());
		animateProps.add(flood.opacityProperty());
		Animations.floodTile(Arrays.asList(new IslandView[] { this }), animateProps, floodBool,
				runnable);

	}

	public void unFlood() {
		execute(new ThreadBlockingRunnable() {

			@Override
			public boolean willSignal() {
				return true;
			}

			@Override
			public void run() {
				animate(UNFLOOD, this);
			}
		});
	}

	/**
	 * returns the properties of the effect
	 * @return
	 */
	public Map<String, DoubleProperty> exposeEffect() {
		Map<String, DoubleProperty> list = new HashMap<>();
		list.put("Brightness", floodEffect.brightnessProperty());
		list.put("Contrast", floodEffect.contrastProperty());
		list.put("Hue", floodEffect.hueProperty());
		list.put("Saturation", floodEffect.saturationProperty());
		return list;
	}

	public void setSaved(final boolean b) {
		execute(new NoSignalingRunnable() {
			@Override
			public void run() {
				if (b) {
					getChildren().add(savedNode);
				} else {
					getChildren().remove(savedNode);
				}
			}
		});

	}
}
