package cls.island.utils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import javafx.application.Platform;
import cls.island.utils.concurrent.AutoReentrantLock;
import cls.island.utils.concurrent.SignaledRunnable;
import cls.island.view.component.ThreadBlock;

public class FxThreadBlock implements ThreadBlock {
	
	private volatile Lock lock = new AutoReentrantLock();
	protected volatile Condition wait = lock.newCondition();
	
	@Override
	public void execute(final SignaledRunnable runnable) {
		if (Platform.isFxApplicationThread()) {
			runnable.run();
			return;
		}
		System.out.println(Thread.currentThread().getName() + " acqurining lock in execute method " );
		lock.lock();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				runnable.run();
			}
		});
		try {
			if (runnable.willSignal()){
				System.out.println(Thread.currentThread().getName() + " Waiting " );
				wait.await();
			}else {
				lock.unlock();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
		

	@Override
	public void unpause() {
		wait.signal();
	}

}
