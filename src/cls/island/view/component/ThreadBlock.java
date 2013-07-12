package cls.island.view.component;

import cls.island.utils.SignaledRunnable;

/**
 * Implemented by classes that have blocking methods. 
 *
 */
public interface ThreadBlock {

	/**
	 * Executes the runnable in another thread while the current thread is paused.
	 * The current thread should be resumed, either immediately if the call to 
	 * {@link SignaledRunnable#willSignal()} returns false, or manually, at some point in the future which will
	 * defined by the runnable, if the aformation call returns true. When the runnable is ready to unpause the 
	 * waiting thread, it should use the {@link #unpause()} method
	 * 
	 * @param runnable
	 */
	public void execute(final SignaledRunnable runnable);

	/**
	 * Wakes up a the waiting thread.
	 */
	public void unpause();

}
