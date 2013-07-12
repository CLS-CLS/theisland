package cls.island.utils.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Thread where all the non visual operations of the game  will run on
 * 
 */
class GameThread extends Thread {

	private Logger logger = Logger.getLogger(GameThread.class.getName());

	private static final String THREAD_ID = "Game_thread";

	private ReentrantLock lock = new ReentrantLock();
	private Condition waitCondition = lock.newCondition();
	private BlockingQueue<Runnable> taskList = new LinkedBlockingQueue<>(50);

	public GameThread(boolean isDaemon) {
		super(THREAD_ID);
		setDaemon(isDaemon);
	}
	
	/**
	 * Adds a task that will be executed when all the previous tasks have finished, or
	 * immediately if no other task exists.
	 * @param runnable the task to run
	 */
	protected void addTask(Runnable runnable) {
		try {
			lock.lock();
			taskList.put(runnable);
			waitCondition.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public final void run() {
		while (isRunning()) {
			Runnable runnable = taskList.poll();
			if (runnable == null) {
				try {
					lock.lock();
					logger.log(Level.FINE, "Waiting for Task ...");
					waitCondition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
					logger.log(Level.FINE, "Trying to unlock ");
					try {
						lock.unlock();
					} catch (IllegalMonitorStateException ex) {
						e.printStackTrace();
					}
				}
			} else {
				logger.log(Level.FINE, "Running Task " + runnable);
				runnable.run();
			}
		}
	}

	public boolean isRunning() {
		return true;
	}

}
