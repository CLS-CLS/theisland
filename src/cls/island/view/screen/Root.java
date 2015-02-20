package cls.island.view.screen;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Scale;
import cls.island.control.Config;

public class Root extends Group {
	
	ImageView imageView;
	
	public Root(final Config config) {
		
		this.imageView =  new ImageView(config.background);
		getChildren().add(imageView);
		getTransforms().add(
				new Scale(config.getScaleFactor(), config.getScaleFactor()));
		
	}
	public void clear() {
		getChildren().clear();
		getChildren().add(imageView);
	}
	
}
