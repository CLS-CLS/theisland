package cls.island.view.screen.popup;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import cls.island.control.Config;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.piece.PieceView;

public class SelectPieceToFlyPopup extends PopUpInternal<List<Piece>> {
	
	

	private final List<Piece> piecesToMove;
	private final List<Piece> result = new ArrayList<>();

	public SelectPieceToFlyPopup(List<Piece> piecesToMove) {
		this.piecesToMove = piecesToMove;
		Group mainGroup = new Group();
		VBox innerVbox = new VBox();
		VBox outerVbox = new VBox();
		HBox pieceGroup = new HBox();
		
				
		Rectangle rect = new Rectangle(300,300,Color.BLUE);
		
		mainGroup.getChildren().add(rect);
		mainGroup.getChildren().add(innerVbox);

		for (int i= 0; i <piecesToMove.size(); i++){
			VBox pieceWithCheckVBox = new VBox();
			pieceWithCheckVBox.setSpacing(5);
			pieceWithCheckVBox.setAlignment(Pos.CENTER);
			final Piece piece = piecesToMove.get(i);
			PieceView pieceViewClone = new PieceView(Config.getInstance().getPieceImageLarge(piece.getColor()), null);
			pieceWithCheckVBox.getChildren().add(pieceViewClone);
			final CustomCheckBox pieceCheckBox  = new CustomCheckBox();
			pieceCheckBox.selectedProperty.addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable,
						Boolean oldValue, Boolean newValue) {
					if (newValue){
						result.add(piece);
					}else if (!newValue){
						result.remove(piece);
					}
					
				}
			});
			
			pieceWithCheckVBox.getChildren().add(pieceCheckBox);
			pieceGroup.getChildren().add(pieceWithCheckVBox);
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
		outerVbox.setMaxHeight(Double.MAX_VALUE);
		getChildren().add(outerVbox);
	}

	@Override
	public List<Piece> getRusults() {
		return new ArrayList<>(piecesToMove);
	}
	
	private static class CustomCheckBox extends Control{
		
		static final Node checked = new ImageView(Config.getInstance().checkBoxWithTick);
		static final Node unchecked = new ImageView(Config.getInstance().checkBoxImg);
		
		private SimpleBooleanProperty selectedProperty = new SimpleBooleanProperty(true);
		
		public CustomCheckBox() {
			this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					setSelected(!isSelected());
				}
			});
			selectedProperty.addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable,
						Boolean oldValue, Boolean newValue) {
					if (newValue){
						getChildren().clear();
						getChildren().add(checked);
					}else if (!newValue){
						getChildren().clear();
						getChildren().add(unchecked);
					}
				}
			});
			
		}
		
		public void setSelected(boolean selected){
			selectedProperty.set(selected);
		}
		
		public  BooleanProperty selectedProperty(){
			return selectedProperty;
		}
		
		public boolean isSelected(){
			return selectedProperty.get();
		}
	}

}
