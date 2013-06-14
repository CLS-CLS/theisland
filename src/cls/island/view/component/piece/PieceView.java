package cls.island.view.component.piece;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import cls.island.view.component.AbstractView;

public class PieceView extends AbstractView<Piece> {
	
	public PieceView(Image image, Piece model) {
		super(false, model);
		getChildren().add(new ImageView(image));
	}

}
