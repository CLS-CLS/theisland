package cls.island.view.screen;

import javafx.scene.Group;
import cls.island.control.Config;
import cls.island.control.MainController;
import cls.island.utils.LocCalculator;

public abstract class AbstractScreen extends Group {
	
	protected static LocCalculator locCalculator = LocCalculator.getInstance();
	
	final protected MainController mainController;
	final protected Config config;

	public AbstractScreen(MainController mainController, Config config) {
		this.mainController = mainController;
		this.config = config;
	}

	public static LocCalculator getLocCalculator() {
		return locCalculator;
	}

	public MainController getMainController() {
		return mainController;
	}

	public Config getConfig() {
		return config;
	}
	
	public abstract void c_setIslandComponentsSelectable(boolean selectable);
	
	

}
