package cls.island.view.screen.popup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import cls.island.control.Config;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.piece.PieceView;

public class SelectPieceToFlyPopup extends PopUpInternal<List<Piece>> {

	private final List<Piece> result;

	public SelectPieceToFlyPopup(List<Piece> piecesToMove) {
		result = new ArrayList<>(piecesToMove);
		Group mainGroup = new Group();
		VBox innerVbox = new VBox();
		innerVbox.setSpacing(10);
		VBox outerVbox = new VBox();
		HBox pieceGroup = new HBox();

		Rectangle rect = new Rectangle(400, 250, new LinearGradient(0D, 0D, 400D, 300D, false, CycleMethod.REPEAT,
				Arrays.asList(new Stop(0, Color.GREY), new Stop(0.5, Color.GREEN),new Stop(1, Color.GREY))));

		 mainGroup.getChildren().add(rect);
		mainGroup.getChildren().add(innerVbox);
		for (int i = 0; i < piecesToMove.size(); i++) {
			Group pieceWithCheckBoxWrapper = new Group();
			Group sceneGroup = new Group();
			SubScene ss = new SubScene(sceneGroup, 60, 150, true, SceneAntialiasing.BALANCED);
			PerspectiveCamera camera = new PerspectiveCamera(true);
			AmbientLight al = new AmbientLight(Color.WHITE);
			al.setTranslateX(100);
			PointLight pl = new PointLight(Color.ANTIQUEWHITE);
			pl.getTransforms().add(new Translate(100, 0, -100));
			PointLight pl2 = new PointLight(Color.ALICEBLUE);
			pl2.getTransforms().add(new Translate(-100, -50, -100));
			sceneGroup.getChildren().addAll(al, pl, pl2);
			camera.getTransforms().add(new Translate(26, 4, -280));
			ss.setCamera(camera);
			camera.setNearClip(0.1);
			camera.setFarClip(10000);
			pieceWithCheckBoxWrapper.getChildren().add(ss);

			final Piece piece = piecesToMove.get(i);
			PieceView pieceViewClone = new PieceView(piece.getColor().getColor(), null);
			Translate rotate = new Translate();
			pieceViewClone.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
			camera.getTransforms().addAll(rotate);
			sceneGroup.getChildren().add(pieceViewClone);
			final CustomCheckBox pieceCheckBox = new CustomCheckBox();
			pieceCheckBox.selectedProperty.addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						result.add(piece);
					} else if (!newValue) {
						result.remove(piece);
					}

				}
			});

			pieceWithCheckBoxWrapper.getChildren().add(pieceCheckBox);
			pieceCheckBox.relocate(0, 80);
			pieceGroup.getChildren().add(pieceWithCheckBoxWrapper);

		}

		Label label = new Label("Select the players to board \nin the helicopter");
		label.getStyleClass().add("white-text-button");
		innerVbox.getChildren().add(label);
		innerVbox.getChildren().add(pieceGroup);
		outerVbox.getChildren().add(mainGroup);
		Button exit = new Button("OK");
		exit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				SelectPieceToFlyPopup.this.close();
			}
		});
		outerVbox.getChildren().add(exit);
		getChildren().add(outerVbox);
	}

	@Override
	public List<Piece> getRusults() {
		return result;
	}

	private static class CustomCheckBox extends Group {

		final Node checked = new ImageView(Config.getInstance().checkBoxWithTick);
		final Node unchecked = new ImageView(Config.getInstance().checkBoxImg);

		private SimpleBooleanProperty selectedProperty = new SimpleBooleanProperty(true);

		public CustomCheckBox() {
			getChildren().add(checked);
			this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					setSelected(!isSelected());
				}
			});
			selectedProperty.addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						getChildren().clear();
						getChildren().add(checked);
					} else if (!newValue) {
						getChildren().clear();
						getChildren().add(unchecked);
					}
				}
			});

		}

		public void setSelected(boolean selected) {
			selectedProperty.set(selected);
		}

		public boolean isSelected() {
			return selectedProperty.get();
		}
	}
}
