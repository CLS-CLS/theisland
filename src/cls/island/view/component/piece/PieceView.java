package cls.island.view.component.piece;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import cls.island.control.Config;
import cls.island.view.component.AbstractView;
import cls.island.view.component.OnOffEffectNode;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;

public class PieceView extends AbstractView<Piece> {


	public PieceView(Color color, Piece model) {
		super(false, model);
		PhongMaterial mat = new PhongMaterial(color);
		mat.setSpecularColor(Color.GREY);
		TdsModelImporter importer = new TdsModelImporter();
		importer.read(this.getClass().getResource("/piece2.3DS").toString());
		MeshView pieceView = (MeshView) ((Group) importer.getImport()[0]).getChildren().get(0);
		pieceView.getTransforms().addAll(new Scale(3.5, 3.5, 3.5), new Translate(5, 10, -12.5));
		pieceView.setMaterial(mat);
		getChildren().add(pieceView);
		
		// getChildren().add(new ImageView(image));
		setSelectable(false);
	}


	@Override
	protected OnOffEffectNode createValidToClick() {
		return new PieceValidToClickEffect();
	}

	class PieceValidToClickEffect extends OnOffEffectNode {
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
