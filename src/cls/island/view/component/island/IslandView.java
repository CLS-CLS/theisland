package cls.island.view.component.island;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.DoubleProperty;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import cls.island.control.Config;
import cls.island.utils.Animations;
import cls.island.utils.FxThreadBlock;
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
			treasureImageView.setTranslateX(0);
			treasureImageView.setTranslateY(50);
		}
		setCache(true);
		setCacheHint(CacheHint.QUALITY);
	}

	private void init(Image tileImage) {
		width = tileImage.getRequestedWidth();
		height = tileImage.getRequestedHeight();
		islandImage = new ImageView(tileImage);
		savedNode = createBorder();
		flood = new Rectangle(width, height, Color.BLUE);
		flood.opacityProperty().set(0);
		flood.setBlendMode(BlendMode.OVERLAY);

		islandImage.setEffect(floodEffect);
		getChildren().add(islandImage);
		if (flood != null)
			getChildren().add(flood);
	}

	private Node createBorder() {
//		return new ImageView(new Image("images/other/saved.png", 20, 20, false, true));
		Rectangle rect = new Rectangle(width + 4, height + 4 , Color.WHITE);
		rect.setStrokeType(StrokeType.INSIDE);
		rect.setStrokeWidth(3);
		rect.setStroke(Color.RED);
		rect.getStrokeDashArray().add(25D);
		return rect;
	}

	public void activateSavedNode() {
		getChildren().add(0, savedNode);
		savedNode.setTranslateX(-2);
		savedNode.setTranslateY(-2);
	}

	public void deactivateSavedNode() {
		getChildren().remove(savedNode);
	}

	public void sink() {
		FxThreadBlock block = new FxThreadBlock();
		Config.getInstance().getFireballSound().play();
		block.execute(()->Animations.sinkTile(IslandView.this, block));
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
		FxThreadBlock block = new FxThreadBlock();
		block.execute(() -> animate(FLOOD, block));
	}

	private void animate(boolean floodBool, FxThreadBlock block) {
		if (floodBool){
			Config.getInstance().getSplashSound().play();
		}
	
		List<DoubleProperty> animateProps = new ArrayList<>();
		animateProps.add(floodEffect.brightnessProperty());
		animateProps.add(floodEffect.contrastProperty());
		animateProps.add(floodEffect.saturationProperty());
		animateProps.add(flood.opacityProperty());
		Animations.floodTile(Arrays.asList(new IslandView[] { this }), animateProps, floodBool,
				block);

	}

	public void unFlood() {
		FxThreadBlock block = new FxThreadBlock();
		block.execute(() -> animate(UNFLOOD, block));
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
		if (b) {
			getChildren().add(savedNode);
		} else {
			getChildren().remove(savedNode);
		}

	}
}
