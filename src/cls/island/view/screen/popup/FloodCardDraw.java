package cls.island.view.screen.popup;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import cls.island.control.Config;
import cls.island.control.MainController;
import cls.island.view.screen.PopUpInternal;

public class FloodCardDraw extends PopUpInternal {
	
	public FloodCardDraw(MainController controller, Config config) {
		super(controller, config);
		VBox vBox = new VBox();
		Rectangle rect = new Rectangle(500,500,Color.BLUE);
		vBox.getChildren().add(rect);
		Button exit = new Button("OK");
		exit.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				FloodCardDraw.this.close();
			}
		});
		vBox.getChildren().add(exit);
		vBox.setMaxHeight(Double.MAX_VALUE);
		getChildren().add(vBox);
	}

	@Override
	public void c_setIslandComponentsSelectable(boolean selectable) {
		throw new UnsupportedOperationException();
		
	}

}
