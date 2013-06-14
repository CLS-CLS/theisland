package cls.island.control;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

	@Override
	public void start(final Stage stage) throws Exception {
		MainController mainController = new MainController(stage);
		mainController.init();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	

}
