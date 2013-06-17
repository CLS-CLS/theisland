package cls.island.utils;

import javafx.scene.control.Button;

public class ButtonFactory {
	
	public static Button genButton(String text){
		Button genButton = new Button(text);
		genButton.getStyleClass().add("gen-button");
		genButton.setMaxWidth(Double.MAX_VALUE);
		genButton.setMaxHeight(Double.MAX_VALUE);
		return genButton;
	}
	
	public static Button actionButton(String text){
		double size = 100D;
		Button genButton = new Button(text);
		genButton.getStyleClass().add("action-button");
		genButton.setMaxWidth(size);
		genButton.setMaxHeight(size);
		genButton.setMinWidth(size);
		genButton.setMinHeight(size);
		return genButton;
	}
}
