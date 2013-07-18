package cls.island.control.action;

import cls.island.control.GameState;

public abstract class RevertableAction implements Action {

	@Override
	public final boolean isRevartable() {
		return true;
	}
}
