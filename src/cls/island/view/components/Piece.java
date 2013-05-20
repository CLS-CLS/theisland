package cls.island.view.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Piece extends AbstractComponent {
	
	public final cls.island.model.PieceModel model;
	
	IslandTile islandTile;
	
	public Piece(Image image, cls.island.model.PieceModel model) {
		super(true);
		getChildren().add(new ImageView(image));
		this.model = model;
	}
		
	

}
