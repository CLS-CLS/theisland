package cls.island.view.component;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ValidEffectNode extends OnOffEffectNode{

	protected ColorAdjust effect;
	protected Timeline timeline = new Timeline();

	public ValidEffectNode(double width, double height, Parent effectNode) {
		effect = new ColorAdjust();
		effect.setSaturation(-0.3D);
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000), new KeyValue(effect.saturationProperty(), -0.8)));
		timeline.setAutoReverse(true);
		timeline.setCycleCount(Timeline.INDEFINITE);
		
		if (effectNode == null){ 
			Rectangle rect = new Rectangle(width + 6, height + 6, Color.VIOLET);
			getChildren().add(rect);
			rect.setEffect(effect);
		}else {
			effectNode.setEffect(effect);
			getChildren().add(0, effectNode);
		}
	}

	public void switchEffectOn() {
		relocate(-3, -3);
		timeline.playFromStart();
	}

	public void switchEffectOff() {
		this.setEffect(null);
		timeline.stop();
	}

	@Override
	public RelativePosition getRelativePosition() {
		return RelativePosition.BOTTOM;
	}

}
