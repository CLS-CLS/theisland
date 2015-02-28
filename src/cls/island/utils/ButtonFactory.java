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
}
