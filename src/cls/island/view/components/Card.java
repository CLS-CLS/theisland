package cls.island.view.components;

import cls.island.model.TreasuryCardModel;
import cls.island.model.TreasuryCardModel.ViewStatus;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card extends AbstractComponent{
	
	
	private ImageView faceUpView;

	private ImageView faceDownView;
	
	public final TreasuryCardModel model;
	
	public Card(Image islandCard, Image backImage, TreasuryCardModel model) {
		super(false);
		faceUpView = new ImageView(islandCard);
		faceDownView = new ImageView(backImage);
		getChildren().add(new ImageView(islandCard));
		this.model = model;
	}
	
	public void setFaceUp(boolean faceUp){
		if (faceUp){
			getChildren().add(faceUpView);
			getChildren().remove(faceDownView);
			model.setViewStatus(ViewStatus.FACE_UP);
		}else{
			getChildren().add(faceDownView);
			getChildren().remove(faceUpView);
			model.setViewStatus(ViewStatus.FACE_DOWN);
		}
	}
	
	

}
