package cls.island.view.screen.popup;

import javafx.scene.Group;


public abstract class PopUpInternal<T> extends Group {
	
	private PopUpWrapper popupScreen = null;
	
	
	public final void close(){
		popupScreen.close();
	}
	
	public void registerToPopUp(PopUpWrapper popUpWrapper) {
		this.popupScreen = popUpWrapper;
	}
	
	public abstract T getRusults();


}
