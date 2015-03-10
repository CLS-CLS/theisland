
package cls.island.view.screen.popup;

import cls.island.utils.ButtonFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FloodCardDrawPopUp extends PopUpInternal<Void> {
	
	public FloodCardDrawPopUp() {
		VBox vBox = new VBox();
		Rectangle rect = new Rectangle(500,500,Color.BLUE);
		vBox.getChildren().add(rect);
		Button exit = ButtonFactory.genButton("OK");
		exit.setOnAction((event) -> close());
		
		vBox.getChildren().add(exit);
		vBox.setMaxHeight(Double.MAX_VALUE);
		getChildren().add(vBox);
		
		Label label = new Label();
		label.getStyleClass().add("white-text-button");
		label.setText("     DANGER !!!!\n: Flood is eminent after this player' s turn is over!");
		getChildren().add(label);
	}

	@Override
	public Void getRusults() {
		return null;
	}
}
