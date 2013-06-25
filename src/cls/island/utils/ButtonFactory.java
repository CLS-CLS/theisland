package cls.island.utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.view.control.ActionButton;
import cls.island.view.control.ActionToggleButton;

public class ButtonFactory {
	
	public static Button genButton(String text){
		Button genButton = new Button(text);
		genButton.getStyleClass().add("gen-button");
		genButton.setMaxWidth(Double.MAX_VALUE);
		genButton.setMaxHeight(Double.MAX_VALUE);
		return genButton;
	}
	
	public static ActionButton actionButton(String text, final ButtonAction action, final GameController controller){
		double size = 100D;
		ActionButton genButton = new ActionButton(text, action);
		genButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				controller.buttonPressed(action);
				
			}
		});
		genButton.getStyleClass().add("action-button");
		
		genButton.setMaxWidth(size);
		genButton.setMaxHeight(size);
		genButton.setMinWidth(size);
		genButton.setMinHeight(size);
		return genButton;
	}
	
	public static ActionToggleButton actionToggleButton(String text, final ButtonAction action, final GameController controller){
		double size = 100D;
		ActionToggleButton genButton = new ActionToggleButton(text, action);
		genButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				controller.buttonPressed(action);
			}
		});
		genButton.getStyleClass().add("action-toggle-button");
		genButton.setMaxWidth(size);
		genButton.setMaxHeight(size);
		genButton.setMinWidth(size);
		genButton.setMinHeight(size);
		return genButton;
		
	}
}
