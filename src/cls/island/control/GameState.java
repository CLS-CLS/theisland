package cls.island.control;

import javafx.scene.input.MouseEvent;
import cls.island.control.GameController.ButtonAction;

public interface GameState {
	
	public void mouseClicked(MouseEvent event);
	
	public void buttonPressed(ButtonAction action);
	
	public GameState getFromState();
	
	public void start();
	
	/**
	 * Creates a new gameState from the provided one. 
	 * @return the new gameState. The gameState return value should be
	 * the same class as the class that implements the method. 
	 */
	public GameState createGameState();

}
