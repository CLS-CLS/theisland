package cls.island.utils.concurrent;

public interface SignaledRunnable {
	
	public void run();
	public boolean willSignal();
	public void signal();

}
