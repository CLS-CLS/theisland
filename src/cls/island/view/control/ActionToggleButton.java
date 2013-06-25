package cls.island.view.control;

import cls.island.control.GameController.ButtonAction;
import javafx.scene.control.ToggleButton;

public class ActionToggleButton extends ToggleButton implements Action {

	private ButtonAction buttonAction;

	public ActionToggleButton(String text, ButtonAction action) {
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
