package cls.island.utils.concurrent;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Convenient class that automatically acquires the asks lock
 * when an opration that needs the lock in order to be executed
 * is invoked.
 * @author cls
 *
 */
public class AutoLockCondition implements Condition{
	
	private Condition condition;
	private final Lock parentLock;
	
	public AutoLockCondition(Condition condition, Lock parentLock) {
		this.condition  = condition;
		this.parentLock = parentLock;
	}
	
	
	@Override
	public void await() throws InterruptedException {
		condition.await();
	}

	
	@Override
	public void signal() {
		parentLock.lock();
		condition.signal();
		parentLock.unlock();
	}

	@Override
	public void signalAll() {
		throw new UnsupportedOperationException();
	}
	@Override
	public void awaitUninterruptibly() {
		condition.awaitUninterruptibly();
		
	}

	@Override
	public long awaitNanos(long nanosTimeout) throws InterruptedException {
		parentLock.lock();
		return condition.awaitNanos(nanosTimeout);
	}

	@Override
	public boolean await(long time, TimeUnit unit) throws InterruptedException {
		parentLock.lock();
		return this.condition.await(time, unit);
	}

	@Override
	public boolean awaitUntil(Date deadline) throws InterruptedException {
		parentLock.lock();
		return condition.awaitUntil(deadline);
	}


}
