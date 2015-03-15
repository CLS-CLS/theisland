
package cls.island.view.screen.popup;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import cls.island.control.Config;
import cls.island.utils.ButtonFactory;

public class WinGamePopUp extends PopUpInternal<Void> {
	
	public WinGamePopUp() {
		VBox vBox = new VBox();
		ImageView win = new ImageView(Config.getInstance().winImage);
		vBox.getChildren().add(win);
		Button exit = ButtonFactory.genButton("OK");
		exit.setOnAction((event) -> close());
		
		vBox.getChildren().add(exit);
		vBox.setMaxHeight(Double.MAX_VALUE);
		getChildren().add(vBox);
	}

	@Override
	public Void getRusults() {
		return null;
	}
}
