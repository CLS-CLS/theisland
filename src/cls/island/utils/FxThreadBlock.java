package cls.island.utils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import javafx.application.Platform;
import cls.island.utils.concurrent.AutoReentrantLock;
import cls.island.view.component.ThreadBlock;

public class FxThreadBlock implements ThreadBlock {
	
	private final Lock lock = new AutoReentrantLock();
	protected volatile Condition wait = lock.newCondition();
	
	@Override
	public void execute(final SignaledRunnable runnable) {
		if (Platform.isFxApplicationThread()) {
			runnable.run();
			return;
		}
		lock.lock();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				runnable.run();
				if (!runnable.willSignal()) {
					wait.signal();
				}
			}
		});
		try {
			wait.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Condition condition(){
		return wait;
	}

}
