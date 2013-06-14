package cls.island.view.component.waterlevel;

import javafx.beans.property.ReadOnlyFloatProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.scene.image.Image;

public class WaterLevel {
	
	private ReadOnlyIntegerWrapper waterLevel = new ReadOnlyIntegerWrapper();
	
	WaterLevelView waterLevelView;
	
	public WaterLevel(int waterLevel, Image backgroundimg, Image markerImg) {
		this.waterLevel.set(waterLevel);
		waterLevelView = new WaterLevelView(this, backgroundimg, markerImg);
	}
	
	public int getWaterLevel() {
		return waterLevel.getValue();
	}
	
	protected ReadOnlyIntegerProperty waterLevelProperty(){
		return waterLevel.getReadOnlyProperty();
	}
	
	public void setWaterLevel(int level){
		waterLevel.set(level);
	}
	
	public WaterLevelView getComponent(){
		return waterLevelView;
	}
	

}
