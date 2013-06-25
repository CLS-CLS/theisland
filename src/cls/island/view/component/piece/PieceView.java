package cls.island.view.component.piece;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import cls.island.control.Config;
import cls.island.view.component.AbstractView;
import cls.island.view.component.OnOffEffectNode;

public class PieceView extends AbstractView<Piece> {
	
	public PieceView(Image image, Piece model) {
		super(false, model);
		getChildren().add(new ImageView(image));
		setSelectable(false);
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
