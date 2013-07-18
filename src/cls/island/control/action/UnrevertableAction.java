package cls.island.control.action;

import cls.island.control.GameState;

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
