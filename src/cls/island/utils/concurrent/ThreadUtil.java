package cls.island.utils.concurrent;

public class ThreadUtil {
	private static final GameThread gameThread = new GameThread(true);
	
	static{
		gameThread.start();
	}
	
	public static void Runlater(Runnable runnable){
		gameThread.addTask(runnable);
	}
	
	private ThreadUtil(){}
	
}
