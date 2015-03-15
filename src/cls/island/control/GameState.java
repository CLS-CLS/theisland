package cls.island.control;

import javafx.scene.input.MouseEvent;
import cls.island.control.GameController.ButtonAction;

public interface GameState {
	
	/**
	 * handles the click of the mouse 
	 * @param event
	 * @return the next state to go if applicable, null if no change in state is required
	 */
	public GameState mouseClicked(MouseEvent event);
	
	/**
	 * handles the click on a button
	 * @param action the action of the button
	 * @return the next state to go if applicable, null if no change in state is required
	 */
	public GameState buttonPressed(ButtonAction action);
	
	/**
	 * return the state before this state.
	 * @return
	 */
	public GameState getFromState();
	
	/**
	 * initializes the state. 
	 * @return the next state to go if applicable, null if no change in state is required
	 */
	public GameState start();
	
	/**
	 * executes any final actions before this state is over.
	 */
	public void end();
	
}
