package cls.island.view.screen;

import javafx.scene.image.ImageView;
import javafx.scene.transform.Scale;
import cls.island.control.Config;
import cls.island.control.MainController;

public class Root extends AbstractScreen {
	
	ImageView imageView;
	
	public Root(final MainController mainController, final Config config) {
		super(mainController, config);
		this.imageView =  new ImageView(config.getBackground());
		getChildren().add(imageView);
		getTransforms().add(
				new Scale(config.getScaleFactor(), config.getScaleFactor()));
		
	}
	public void clear() {
		getChildren().clear();
		getChildren().add(imageView);
	}
	
}
