package cls.island.control.action;

/**
 * An action that can be reverted
 * @author cls
 *
 */
public abstract class RevertableAction implements Action {

	@Override
	public final boolean isRevartable() {
		return true;
	}
}
