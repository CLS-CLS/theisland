package cls.island.control.action;

import cls.island.control.GameState;

/**
 * An action that may be reverted (undone). Provides methods to undo the action
 * and to check if the action can be reverted.  
 * @author cls
 *
 */
public interface Action {
	
	/**
	 * Checks if the action is revertable. This method should be called first, to 
	 * confirm that the action can be reverted before the {@link #revert()} method is called.
	 * @return
	 */
	public boolean isRevartable();
	
	/**
	 * The code executed by the action.
	 */
	public void execute();
	
	/**
	 * provides the means to revert the system to the 
	 * state that it was before the action was executed.
	 * @return
	 */
	public GameState revert();

}
