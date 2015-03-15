package popups;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;
import cls.island.view.screen.popup.PopUpInternal;
import cls.island.view.screen.popup.PopUpWrapper;
import cls.island.view.screen.popup.WinGamePopUp;

public class WingGamePopUp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root, 800, 600, true, SceneAntialiasing.BALANCED);
		primaryStage.setScene(scene);

		
		primaryStage.show();
		PopUpInternal<Void> internal = new WinGamePopUp();
		new PopUpWrapper(internal, primaryStage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
