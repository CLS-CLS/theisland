package cls.island.view.screen;

import cls.island.control.Config;
import cls.island.control.MainController;
import cls.island.view.screen.popup.PopUpScreen;

public abstract class PopUpInternal extends AbstractScreen {
	
	public PopUpInternal(MainController mainController, Config config) {
		super(mainController, config);
	}

	private PopUpScreen popupScreen = null;
	
	public void registerToPopUp(PopUpScreen popUpScreen){
		this.popupScreen = popUpScreen;
	}
	
	public void close(){
		popupScreen.close();
	}

}
