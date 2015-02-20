package cls.island.view.screen.popup;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Provides the open / close mechanism with animations, adds a gray semi-opaque
 * background around the actual pop-up and a pseudo-modality.
 *   
 * @param <T> the type of the return result of the internal popup
 */
public class PopUpWrapper<T> extends Stage {

	private final PopUpInternal<T> internal;
	private final Window parent;
	
	/**
	 * Constructs a wrapper for the internal popUp.
	 * @param internal the pop-up. 
	 * @param parent the parent screen this screnn will be added too.
	 */
	public PopUpWrapper(PopUpInternal<T> internal, Window parent) {
		this.parent = parent;
		this.internal = internal;
		internal.registerToPopUp(this);
		Scene scene = new Scene(internal);
		setScene(scene);
		initModality(Modality.WINDOW_MODAL);
		initOwner(this.parent);
		showAndWait();		
	}
	

	public void close() {
		super.close();
	}

	public T getResult() {
		return internal.getRusults();
	}

}
