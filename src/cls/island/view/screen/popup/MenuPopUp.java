package cls.island.view.screen.popup;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import cls.island.control.Config;
import cls.island.control.GameController;
import cls.island.utils.ButtonFactory;

public class MenuPopUp extends PopUpInternal<Void> {

	public MenuPopUp(GameController gameController, Config config) {
		VBox layout = new VBox();
		Button quitButton = ButtonFactory.menuButton("Quit");
		quitButton.setOnAction((e) -> {
			gameController.backToMainScreen();
			this.close();
		});
		layout.getStyleClass().add("pile-sneek-pick-pane");
		layout.setPrefHeight(400);
		layout.setPrefWidth(400);
		layout.setAlignment(Pos.TOP_CENTER);
		layout.getChildren().add(quitButton);
		this.getChildren().add(layout);
		
	}

	@Override
	public Void getRusults() {
		return null;
	}

}
