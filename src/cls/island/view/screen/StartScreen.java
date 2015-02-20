package cls.island.view.screen;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import cls.island.control.Config;
import cls.island.control.MainController;
import cls.island.utils.ButtonFactory;

public class StartScreen extends Group {

	private final double[] btnGroupLoc = new double[] { 700D, 750D };

	VBox mainBtns = new VBox();

	private MainController mainController;

	public StartScreen(MainController mainController) {
		this.mainController = mainController;
		mainBtns.setLayoutX(btnGroupLoc[0]);
		mainBtns.setLayoutY(btnGroupLoc[1]);
		mainBtns.getStyleClass().add("gen-vBox");
		createStartButton(mainBtns);
		createoptionsButton(mainBtns);
		createQuitButton(mainBtns);
		getChildren().add(mainBtns);
	}

	private void createStartButton(Pane buttonGroup) {
		Button button = ButtonFactory.genButton("Play");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mainController.goToStartPreGame();
			}
		});
		button.setMaxWidth(Double.MAX_VALUE);
		buttonGroup.getChildren().add(button);
	}

	private void createQuitButton(Pane buttonGroup) {
		Button button = ButtonFactory.genButton("Quit");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
		button.setMaxWidth(Double.MAX_VALUE);
		buttonGroup.getChildren().add(button);
	}

	private void createoptionsButton(Pane buttonGroup) {
		Button button = ButtonFactory.genButton("Options");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				goToOptions();
			}
		});
		button.setMaxWidth(Double.MAX_VALUE);
		buttonGroup.getChildren().add(button);
	}

	public void goToOptions() {
		mainController.goToOptions();
	}

}
