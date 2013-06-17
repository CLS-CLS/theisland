package cls.island.view.component;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ValidEffectNode extends Parent {

	private ColorAdjust effect;
	Timeline timeline = new Timeline();

	public ValidEffectNode(double width, double height, Parent node) {
		effect = new ColorAdjust();
		effect.setSaturation(-0.3D);
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000), new KeyValue(effect.saturationProperty(), -0.8)));
		timeline.setAutoReverse(true);
		timeline.setCycleCount(Timeline.INDEFINITE);
		
		if (node == null){ 
			Rectangle rect = new Rectangle(width + 6, height + 6, Color.BLUE);
			getChildren().add(rect);
			rect.setEffect(effect);
		}else {
			node.setEffect(effect);
			getChildren().add(0, node);
		}

	}

	public void switchEffectOn() {
		timeline.playFromStart();
	}

	public void switchEffectOff() {
		this.setEffect(null);
		timeline.stop();
	}

}
