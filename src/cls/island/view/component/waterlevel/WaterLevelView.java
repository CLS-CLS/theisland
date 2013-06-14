package cls.island.view.component.waterlevel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import cls.island.utils.LocCalculator;
import cls.island.utils.LocCalculator.Loc;
import cls.island.view.component.AbstractView;

public class WaterLevelView extends AbstractView<WaterLevel>{
	
	LocCalculator locCalculator = LocCalculator.getInstance();
	private Node marker;
	
	public WaterLevelView(WaterLevel model, Image backgroundimg, Image markerImg ) {
		super(true, model);
		getChildren().add(new ImageView(backgroundimg));
		marker = new ImageView(markerImg);
		getChildren().add(marker);
		setMarker(model.getWaterLevel());
		model.waterLevelProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				animateMarker(newValue.floatValue());
			}
		});
	}
	
	/**
	 * sets the marker to the new value with an animations
	 * @param newWaterLevel
	 */
	public void animateMarker (float newWaterLevel){
		Loc loc = locCalculator.markerLocation(getParentModel().getWaterLevel());
		marker.relocate(loc.x, loc.y);
	}
	
	/**
	 * Sets the marker to the new value without animation.
	 * @param newWaterLevel
	 */
	public void setMarker(float newWaterLevel){
		Loc loc = locCalculator.markerLocation(getParentModel().getWaterLevel());
		marker.relocate(loc.x, loc.y);
	}


	@Override
	public void setSelectable(boolean selectable) {
		super.setSelectable(false);
	}
}
