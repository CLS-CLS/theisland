package cls.island.view.component;

import java.util.concurrent.locks.Condition;

import cls.island.utils.SignaledRunnable;

public interface ThreadBlock {

	public void execute(final SignaledRunnable runnable);

	Condition condition();

}
