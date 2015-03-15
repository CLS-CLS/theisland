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
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import cls.island.control.Config;
import cls.island.view.component.AbstractView;
import cls.island.view.component.OnOffEffectNode;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;

public class PieceView extends AbstractView<Piece> {

	PieceValidToClickEffect effect = new PieceValidToClickEffect();

	public PieceView(Color color, Piece model) {
		super(false, model);
		PhongMaterial mat = new PhongMaterial(color);
		mat.setSpecularColor(Color.GREY);
		TdsModelImporter importer = new TdsModelImporter();
		importer.read(this.getClass().getResource("/piece2.3DS").toString());
		MeshView pieceView = (MeshView) ((Group) importer.getImport()[0]).getChildren().get(0);
		pieceView.getTransforms().addAll(new Scale(3.5, 3.5, 3.5), new Translate(5, 10, -13.1));
		pieceView.setMaterial(mat);
		getChildren().add(pieceView);

		setSelectable(false);
	}

	@Override
	public OnOffEffectNode createValidToClick() {
		return effect;
	}

	class PieceValidToClickEffect extends OnOffEffectNode {
		private Timeline timeline = new Timeline();
		ImageView sunOuter = new ImageView(Config.getInstance().sunOuter);

		public PieceValidToClickEffect() {
			Scale scale = new Scale();
			sunOuter.getTransforms().addAll(new Translate(-15, 12, -2), scale);

			timeline.getKeyFrames().add(
					new KeyFrame(Duration.millis(1000), new KeyValue(scale.xProperty(), 0.7), new KeyValue(
							scale.yProperty(), 0.7), new KeyValue(sunOuter.translateYProperty(),
							sunOuter.getTranslateY() + 13), new KeyValue(sunOuter.translateXProperty(), sunOuter
							.getTranslateX() + 13)));
			timeline.setAutoReverse(true);
			timeline.setCycleCount(Timeline.INDEFINITE);
		}

		@Override
		public RelativePosition getRelativePosition() {
			return RelativePosition.TOP;
		}

		@Override
		public void switchEffectOn() {
			PieceView.this.getChildren().add(sunOuter);
			timeline.playFromStart();

		}

		@Override
		public void switchEffectOff() {
			PieceView.this.getChildren().remove(sunOuter);
			timeline.stop();

		}

	}

}
