package cls.island.utils.concurrent;

public abstract class NoSignalingRunnable extends ThreadBlockingRunnable {
	
	@Override
	public final boolean willSignal() {
		return false;
	}

	@Override
	public final void signal() {
		throw new UnsupportedOperationException();
	}

}
