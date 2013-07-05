package cls.island.view.screen.popup;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import cls.island.model.LooseCondition;

public class GameLostPopUp extends PopUpInternal<Object> {

	public GameLostPopUp(LooseCondition condition) {
		Rectangle rect = new Rectangle(300, 200, Color.RED);
		getChildren().add(rect);
		Label label = new Label();
		switch (condition) {
		case FOOLS_LANDING_LOST:
			label.setText("Fool's Landing lost");
			break;
		case MAX_WATER_LEVEL_REACHED:
			label.setText("Max water level reached");
			break;
		case PLAYER_SUNK :
			label.setText("A player was sunk");
			break;
		case TREASURE_SUNK:
			label.setText("Treasure was sunk");
			break;
			
		default:
			break;
		}
		label.getStyleClass().add("standard-label");
		getChildren().add(label);
		
	}
	

	@Override
	public Object getRusults() {
		return null;
	}

}
