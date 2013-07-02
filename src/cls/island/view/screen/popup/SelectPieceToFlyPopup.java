package cls.island.view.screen.popup;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import cls.island.view.component.piece.Piece;

public class SelectPieceToFlyPopup extends PopUpInternal<List<Piece>> {
	
	

	private final List<Piece> piecesToMove;

	public SelectPieceToFlyPopup(List<Piece> piecesToMove) {
		this.piecesToMove = piecesToMove;
		VBox vBox = new VBox();
		Rectangle rect = new Rectangle(500,500,Color.BLUE);
		vBox.getChildren().add(rect);
		Button exit = new Button("OK");
		exit.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				SelectPieceToFlyPopup.this.close();
				
			}
		});
		vBox.getChildren().add(exit);
		vBox.setMaxHeight(Double.MAX_VALUE);
		getChildren().add(vBox);
	}

	@Override
	public List<Piece> getRusults() {
		return new ArrayList<>(piecesToMove);
	}

}
