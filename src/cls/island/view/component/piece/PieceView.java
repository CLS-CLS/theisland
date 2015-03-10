package cls.island.view.component.piece;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import cls.island.control.Config;
import cls.island.view.component.AbstractView;
import cls.island.view.component.OnOffEffectNode;

public class PieceView extends AbstractView<Piece> {
	
	private final Image image;


	public PieceView(Image image, Piece model) {
		super(false, model);
		Sphere sphere = new Sphere(20);
		sphere.getTransforms().add(new Translate(20, 40, -10));
		getChildren().add(sphere);
		PhongMaterial mat = new PhongMaterial(Color.GREY);
		mat.setSpecularColor(Color.DARKGRAY);
		sphere.setMaterial(mat);
		this.image = image;
//		getChildren().add(new ImageView(image));
		setSelectable(false);
	}
	

	public Image getImage() {
		return image;
	}
	
	
	@Override
	protected OnOffEffectNode createValidToClick() {
		return new PieceValidToClickEffect();
	}
	

	class PieceValidToClickEffect extends OnOffEffectNode{
		ImageView image;
		private Timeline timeline = new Timeline();
		
		public PieceValidToClickEffect() {
			image = new ImageView(Config.getInstance().pieceWhite);
			getChildren().add(image);
			setOpacity(0);
			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000), new KeyValue(this.opacityProperty(), 0.5)));
			timeline.setAutoReverse(true);
			timeline.setCycleCount(Timeline.INDEFINITE);
		}
		
		@Override
		public RelativePosition getRelativePosition() {
			return RelativePosition.TOP;
		}

		@Override
		public void switchEffectOn() {
			timeline.playFromStart();
			
		}

		@Override
		public void switchEffectOff() {
			timeline.stop();
			
		}
		
	}
	
}
