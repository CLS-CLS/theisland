package cls.island.view.screen;

import javafx.application.Platform;
import javafx.scene.Group;
import cls.island.control.Config;
import cls.island.control.MainController;
import cls.island.utils.FxThreadBlock;
import cls.island.utils.LocCalculator;
import cls.island.utils.concurrent.ThreadBlockingRunnable;
import cls.island.view.screen.popup.PopUpInternal;
import cls.island.view.screen.popup.PopUpWrapper;

public abstract class AbstractScreen extends Group {

	protected static LocCalculator locCalculator = LocCalculator.getInstance();
	final protected MainController mainController;
	final protected Config config;
	private volatile boolean animationInProgress;

	public AbstractScreen(MainController mainController, Config config) {
		this.mainController = mainController;
		this.config = config;
	}

	public void c_setAnimationInProgress(final boolean inProgress) {
		if (!Platform.isFxApplicationThread()) {
			throw new RuntimeException("should have been in Fx- Thread");
		}
		synchronized (AbstractScreen.this) {
			animationInProgress = inProgress;
		}
	}

	public boolean c_isAnimationInProgress() {
		return animationInProgress;
	}

	public static LocCalculator getLocCalculator() {
		return locCalculator;
	}

	public MainController getMainController() {
		return mainController;
	}

	public Config getConfig() {
		return config;
	}

	/**
	 * Shows the pop up on top of this screen. Makes the thread that 
	 * requested this pop-up to wait until the pop-up is closed.
	 * @param popUpInternal
	 * @return
	 */
	protected <T> T c_showPopup(final PopUpInternal<T> popUpInternal) {
		if (Platform.isFxApplicationThread())
			throw new RuntimeException(
					"the method should run outside fx-tread in order to be blocking");
		final PopUpWrapper<T> popUp = new PopUpWrapper<>(popUpInternal, mainController.getStage());
		return popUp.getResult();
	}


	public void execute(ThreadBlockingRunnable runnable) {
		FxThreadBlock localThreadBlock = new FxThreadBlock();
		runnable.register(localThreadBlock);
		localThreadBlock.execute(runnable);
	}
	
}
