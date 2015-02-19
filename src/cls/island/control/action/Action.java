package cls.island.control.action;


/**
 * An action that may be reverted (undone). Provides methods to undo the action
 * and to check if the action can be reverted.  
 * @author cls
 *
 */
public interface Action {
	
	/**
	 * The code executed by the action.
	 */
	public void execute();
	
	

}
