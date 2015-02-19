package cls.island.control.action;

import cls.island.control.GameState;

/**
 * A revert-able action
 * @author cls
 *
 */
public interface RevertableAction extends Action {

	/**
	 * provides the means to revert the system to the 
	 * state that it was before the action was executed.
	 * @return
	 */
	public GameState revert();
}
