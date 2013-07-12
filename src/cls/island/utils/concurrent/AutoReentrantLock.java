package cls.island.utils.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * a Reentrant lock implementation that uses the {@link AutoLockCondition}
 * @author lytsikas
 *
 */
public class AutoReentrantLock extends ReentrantLock {

	
	private static final long serialVersionUID = 1L;

	@Override
	public Condition newCondition() {
		Condition condition =  super.newCondition();
		return new AutoLockCondition(condition, this);
	}
	
	@Override
	public void lock() {
		if (isHeldByCurrentThread())return;
		super.lock();
//		System.out.println("lock : locked By thread " + Thread.currentThread());
	}
	
	@Override
	public void unlock() {
		super.unlock();
//		System.out.println("unlock : UNlocked by thread  " +  Thread.currentThread());
	}
	
	
}
