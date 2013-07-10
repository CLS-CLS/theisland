package cls.island.utils.concurrent;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class TestApplicatoin extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Rectangle rect = new Rectangle(10, 10);
		root.getChildren().add(rect);
		Scene scene = new Scene(root, 10, 10);
		primaryStage.setScene(scene);
		primaryStage.show();
//		ThreadUtil.Runlater(new Runnable() {
//
//			@Override
//			public void run() {
//				System.out.println(Thread.currentThread());
//			}
//		});
	}
	
	
	public static void show(){
		TestApplicatoin.launch();
	}

}
