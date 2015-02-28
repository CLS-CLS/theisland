package cls.island.view.components;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class MessagePanel extends AbstractComponent {

	Label msg;

	public MessagePanel() {
		super(true);
		Rectangle rect = new Rectangle(400, 100);
		rect.getStyleClass().add("messagePanel");
		getChildren().add(rect);
		msg = new Label();
		msg.getStyleClass().add("white-text-button");
		getChildren().add(msg);
		msg.relocate(30,30);
		msg.setOpacity(0);
	}

	public void showMessage(String msgText) {
		msg.setText(msgText);
		new Timeline(new KeyFrame(Duration.millis(200), new KeyValue(msg.opacityProperty(), 1)))
				.play();
	}
	
	public void hideMessage(){
		msg.setOpacity(0);
	}
	
	@Override
	public boolean isSelectable() {
		return false;
	}

}
