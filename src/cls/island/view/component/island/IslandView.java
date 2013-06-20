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
import cls.island.utils.SignaledRunnable;
import cls.island.utils.TimelineSingle;
import cls.island.utils.TimelineSingle.Process;
import cls.island.view.component.AbstractView;
import cls.island.view.component.island.Island.Model;

public class IslandView extends AbstractView<Island> {
	private static final boolean FLOOD = true;
	private static final boolean UNFLOOD = false;

	public enum Position {
		UP, DOWN;
	}

	private Rectangle flood;
	private ImageView islandView;
	private double width;
	private double height;
	private Node savedNode;
	final ColorAdjust floodEffect;

	public IslandView(Image tileImage, Model model, Island component) {
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
	}

	private void init(Image tileImage) {
		width = tileImage.getRequestedWidth();
		height = tileImage.getRequestedHeight();
		islandView = new ImageView(tileImage);
		savedNode = createBorder();
//		if (new Random().nextBoolean()){
			flood = new Rectangle(width, height, Color.BLUE);
			flood.opacityProperty().set(0);
			flood.setBlendMode(BlendMode.OVERLAY);
//		}
		
		islandView.setEffect(floodEffect);
		getChildren().add(islandView);
		if (flood !=null)getChildren().add(flood);
	}

	private Node createBorder() {
//		return new ImageView(new Image("images/other/border.png", 128, 128, false, true));
//		return new ImageView(new Image("images/other/exclamation.png", 20, 20, false, true));
		return new ImageView(new Image("images/other/saved.png", 20,20,false,true));

	}

	public void activateSavedNode() {
		execute(new SignaledRunnable() {

			@Override
			public boolean willSignal() {
				return false;
			}

			@Override
			public void run() {
				IslandView.this.getChildren().add(savedNode);
				savedNode.relocate(0,100);
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
		execute(new SignaledRunnable() {

			@Override
			public boolean willSignal() {
				return true;
			}

			@Override
			public void run() {
				Animations.sinkTile(IslandView.this, wait);
			}
		});
	}

	public void flood() {
		execute(new SignaledRunnable() {

			@Override
			public boolean willSignal() {
				return true;
			}

			@Override
			public void run() {
				animate(FLOOD);
				
			}
		});

	}
	
	private void animate(boolean floodBool) {
		List<DoubleProperty> animateProps = new ArrayList<>();
		animateProps.add(floodEffect.brightnessProperty());
		animateProps.add(floodEffect.contrastProperty());
		animateProps.add(floodEffect.saturationProperty());
		animateProps.add(flood.opacityProperty());
		Animations.floodTile(Arrays.asList(new IslandView[]{this}),animateProps, floodBool, wait);
		
	}

	public void unFlood() {
		execute(new SignaledRunnable() {

			@Override
			public boolean willSignal() {
				return true;
			}

			@Override
			public void run() {
				animate(UNFLOOD);
			}
		});
	}
	
	/**
	 * returns the properties of the effect
	 * @return
	 */
	public Map<String, DoubleProperty> exposeEffect(){
		Map<String, DoubleProperty> list = new HashMap<>();
		list.put("Brightness", floodEffect.brightnessProperty());
		list.put("Contrast", floodEffect.contrastProperty());
		list.put("Hue", floodEffect.hueProperty());
		list.put("Saturation", floodEffect.saturationProperty());
		return list;
	}

	public void setSaved(final boolean b) {
		execute(new SignaledRunnable() {
			
			@Override
			public boolean willSignal() {
				return false;
			}
			
			@Override
			public void run() {
				if (b){
					getChildren().add(savedNode);
				}else {
					getChildren().remove(savedNode);
				}
			}
		});
		
	}
}
