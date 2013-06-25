package cls.island.view.control;

import javafx.scene.control.Button;
import cls.island.control.GameController.ButtonAction;

public class ActionButton extends Button implements Action {
	
	private ButtonAction buttonAction;

	
	public ActionButton(String text, ButtonAction action) {
		super(text);
		setButtonAction(action);
	}

	public ButtonAction getButtonAction() {
		return buttonAction;
	}

	public void setButtonAction(final ButtonAction buttonAction) {
		this.buttonAction = buttonAction;
	}
	
	

}
