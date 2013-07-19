package cls.island.control.action;

import cls.island.control.GameState;

/**
 * An action that cannot be undone
 * @author cls
 *
 */
public abstract class UnrevertableAction implements Action {

	@Override
	public final boolean isRevartable() {
		return false;
	}
	
	@Override
	public final GameState revert() {
		throw new UnsupportedOperationException();
	}

}
