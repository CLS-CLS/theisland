package cls.island.utils.concurrent;

/**
 * initializes a {@link GameThread} and provides methods to schedule
 * tasks to run on that thread. 
 * @author cls 
 *
 */
public class ThreadUtil {
	private static final GameThread gameThread = new GameThread(true);
	
	static{
		gameThread.start();
	}
	
	/**
	 * Schedules a runnable to run in the {@link GameThread} thread.
	 * @param runnable
	 */
	public static void Runlater(Runnable runnable){
		gameThread.addTask(runnable);
	}
	
	private ThreadUtil(){}
	
}
