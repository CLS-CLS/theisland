package cls.island.view.screen;

import java.util.concurrent.locks.Condition;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import cls.island.control.Config;
import cls.island.control.MainController;
import cls.island.utils.FxThreadBlock;
import cls.island.utils.LocCalculator;
import cls.island.utils.SignaledRunnable;
import cls.island.utils.concurrent.AutoReentrantLock;
import cls.island.view.component.ThreadBlock;
import cls.island.view.screen.popup.PopUpInternal;
import cls.island.view.screen.popup.PopUpWrapper;

public abstract class AbstractScreen extends Group implements ThreadBlock {

	protected static LocCalculator locCalculator = LocCalculator.getInstance();
	protected final AutoReentrantLock lock = new AutoReentrantLock();

	final protected MainController mainController;
	final protected Config config;
	private volatile boolean animationInProgress;
	private boolean animationInProgressOriginal;
	private volatile ThreadBlock threadBlock = new FxThreadBlock();

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
		final PopUpWrapper<T> popUp = new PopUpWrapper<>(popUpInternal, AbstractScreen.this);
		threadBlock.execute(new SignaledRunnable() {

			@Override
			public boolean willSignal() {
				return true;
			}

			@Override
			public void run() {
				getChildren().add(popUp);
				popUp.show();
				animationInProgressOriginal = animationInProgress;
				
			}
		});
		c_setAnimationInProgress(false);
		return popUp.getResult();
		
		
	}

	/**
	 * closes the pop-up and resumes the paused thread which requested the 
	 * pop-up to open.
	 * @param popUp
	 */
	public void closePopup(final PopUpWrapper<?> popUp) {
		condition().signal();
		getChildren().remove(popUp);
		c_setAnimationInProgress(animationInProgressOriginal);
		// popUpwaitCondition.signal();

	}

	@Override
	public void execute(SignaledRunnable runnable) {
		threadBlock.execute(runnable);

	}

	@Override
	public Condition condition() {
		return threadBlock.condition();
	}

}
