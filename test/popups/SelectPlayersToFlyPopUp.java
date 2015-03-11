package popups;

import java.util.ArrayList;
import java.util.List;

import cls.island.view.component.piece.Piece;
import cls.island.view.component.piece.PieceColor;
import cls.island.view.screen.popup.PopUpInternal;
import cls.island.view.screen.popup.PopUpWrapper;
import cls.island.view.screen.popup.SelectPieceToFlyPopup;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;

public class SelectPlayersToFlyPopUp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root, 800, 600, true, SceneAntialiasing.BALANCED);
		primaryStage.setScene(scene);

		List<Piece> piecesToMove = new ArrayList<Piece>();
		Piece p1 = new Piece("1", PieceColor.RED);
		Piece p2 = new Piece("2", PieceColor.GREEN);
		piecesToMove.add(p1);
		piecesToMove.add(p2);
		primaryStage.show();
		PopUpInternal<List<Piece>> internal = new SelectPieceToFlyPopup(piecesToMove);
		PopUpWrapper<SelectPieceToFlyPopup> out = new PopUpWrapper(internal, primaryStage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
