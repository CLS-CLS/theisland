package cls.island.view.screen.popup;

import javafx.scene.Group;


public abstract class PopUpInternal<T> extends Group {
	
	private PopUpWrapper<T> popupScreen = null;
	
	/**
	 * Handles the closing of the pop up
	 */
	public final void close(){
		popupScreen.close();
	}
	
	/**
	 * registers this internal pop up the its parent. Used by the wrapper. 
	 * @param popUpWrapper
	 */
	protected final void registerToPopUp(PopUpWrapper<T> popUpWrapper) {
		this.popupScreen = popUpWrapper;
	}
	
	/**
	 * Called after the pop up is closed by the wrapper.
	 * Override the method to provide the results.
	 * @return the selection result of  the pop up. 
	 */
	public abstract T getRusults();


}
