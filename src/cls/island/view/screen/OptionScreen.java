package cls.island.view.screen;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import cls.island.control.Config;
import cls.island.control.MainController;
import cls.island.utils.ButtonFactory;

public class OptionScreen extends AbstractScreen {

	public OptionScreen(final MainController controller, Config config) {
		super(controller, config);
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

	@Override
	public void c_setIslandComponentsSelectable(boolean selectable) {
		throw new UnsupportedOperationException();
	}

}
