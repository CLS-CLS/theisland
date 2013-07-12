package cls.island.utils.concurrent;

import cls.island.view.component.ThreadBlock;

public abstract class ThreadBlockingRunnable implements SignaledRunnable{

	private ThreadBlock threadBlock;
	
	@Override
	public boolean willSignal() {
		return true;
	}
	
	public void register(ThreadBlock threadBlock){
		this.threadBlock = threadBlock;
	}
	
	@Override
	public void signal() {
		threadBlock.unpause();
	}

}
