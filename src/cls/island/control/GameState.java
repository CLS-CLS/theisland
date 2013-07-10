package cls.island.control;

import javafx.scene.input.MouseEvent;
import cls.island.control.GameController.ButtonAction;

public interface GameState {
	
	public GameState mouseClicked(MouseEvent event);
	
	public GameState buttonPressed(ButtonAction action);
	
	public GameState getFromState();
	
	public GameState start();
	
}
