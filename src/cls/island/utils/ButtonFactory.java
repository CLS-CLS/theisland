package cls.island.utils;

import javafx.scene.control.Button;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.view.control.ActionButton;
import cls.island.view.control.ActionToggleButton;

public class ButtonFactory {

	private static final double SIZE = 100D;

	public static Button genButton(String text) {
		Button genButton = new Button(text);
		genButton.getStyleClass().add("gen-button");
		genButton.setMaxWidth(Double.MAX_VALUE);
		genButton.setMaxHeight(Double.MAX_VALUE);
		return genButton;
	}

	public static ActionButton actionButton(String text, final ButtonAction action, final GameController controller) {

		ActionButton genButton = new ActionButton(text, action);
		genButton.setOnAction((event) -> controller.buttonPressed(action));
		genButton.getStyleClass().add("action-button");

		genButton.setMaxWidth(SIZE);
		genButton.setMaxHeight(SIZE);
		genButton.setMinWidth(SIZE);
		genButton.setMinHeight(SIZE);
		return genButton;
	}

	public static ActionToggleButton actionToggleButton(String text, final ButtonAction action,
			final GameController controller) {
		double size = 100D;
		ActionToggleButton genButton = new ActionToggleButton(text, action);
		genButton.setOnAction((event) -> controller.buttonPressed(action));
		genButton.getStyleClass().add("action-toggle-button");
		genButton.setMaxWidth(size);
		genButton.setMaxHeight(size);
		genButton.setMinWidth(size);
		genButton.setMinHeight(size);
		return genButton;

	}

	public static Button menuButton(String text) {
		Button button = new Button(text);
		button.setMaxWidth(SIZE * 2);
		button.setMaxHeight(SIZE / 2);
		button.setMinWidth(SIZE * 2);
		button.setMinHeight(SIZE / 2);
		button.getStyleClass().add("action-button");
		button.getStyleClass().add("menu-button");
		return button;
	}
}
