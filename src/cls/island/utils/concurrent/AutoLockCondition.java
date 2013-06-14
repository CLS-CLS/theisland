package cls.island.utils.concurrent;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

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
		parentLock.unlock();
		
	}

	
	@Override
	public void signal() {
		parentLock.lock();
//		System.out.println("Singal : locked by Thread " + Thread.currentThread());
		condition.signal();
		parentLock.unlock();
//		System.out.println("Signal : unlocked by Thread "  +Thread.currentThread());
		
	}

	@Override
	public void signalAll() {

	}
	@Override
	public void awaitUninterruptibly() {
		condition.awaitUninterruptibly();
		
	}

	@Override
	public long awaitNanos(long nanosTimeout) throws InterruptedException {
		return condition.awaitNanos(nanosTimeout);
	}

	@Override
	public boolean await(long time, TimeUnit unit) throws InterruptedException {
		return this.condition.await(time, unit);
	}

	@Override
	public boolean awaitUntil(Date deadline) throws InterruptedException {
		return condition.awaitUntil(deadline);
	}


}
