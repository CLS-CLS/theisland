package cls.island.view.screen;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import cls.island.control.MainController;
import cls.island.utils.ButtonFactory;

public class OptionScreen extends Group {

	private MainController mainController;

	public OptionScreen(final MainController controller) {
		this.mainController = controller;
		createOptionButtonGroup();
	}

	private void createOptionButtonGroup() {
		Button button = ButtonFactory.genButton("Back");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mainController.goToMainScreen(OptionScreen.this);
			}
		});
		button.setMaxWidth(Double.MAX_VALUE);
		VBox optionBtns = new VBox();

		optionBtns.getChildren().add(button);

		optionBtns.setLayoutX(700);
		optionBtns.setLayoutY(750);
		getChildren().add(optionBtns);
	}



}
