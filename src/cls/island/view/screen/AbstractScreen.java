package cls.island.view.screen;

import java.util.concurrent.locks.Condition;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import cls.island.control.Config;
import cls.island.control.MainController;
import cls.island.utils.LocCalculator;
import cls.island.utils.SignaledRunnable;
import cls.island.utils.concurrent.AutoReentrantLock;
import cls.island.view.screen.popup.PopUpInternal;
import cls.island.view.screen.popup.PopUpWrapper;

public abstract class AbstractScreen extends Group {

	protected static LocCalculator locCalculator = LocCalculator.getInstance();
	protected final AutoReentrantLock lock = new AutoReentrantLock();

	final protected MainController mainController;
	final protected Config config;
	private volatile boolean animationInProgress;

	protected Condition popUpwaitCondition;

	public AbstractScreen(MainController mainController, Config config) {
		this.mainController = mainController;
		this.config = config;
		addDisableButtonPressFilter();
	}

	public synchronized void setAnimationInProgress(boolean inProgress) {
		this.animationInProgress = inProgress;
	}

	private void addDisableButtonPressFilter() {
		this.addEventFilter(MouseEvent.ANY, new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				if (animationInProgress) {
					event.consume();
				}
			}
		});

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

	public <T> T c_showPopup(PopUpInternal<T> popUpInternal) {
		if (Platform.isFxApplicationThread())
			throw new RuntimeException(
					"the method should run outside fx-tread in order to be blocking");
		
		popUpwaitCondition = lock.newCondition();
		lock.lock();
		final PopUpWrapper<T> popUp = new PopUpWrapper<>(popUpInternal, this);
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				getChildren().add(popUp);
				popUp.show();
			}
		});
		try {
			popUpwaitCondition.await();
			return popUp.getResult();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void closePopup(final PopUpWrapper<?> popUp) {
		getChildren().remove(popUp);
		popUpwaitCondition.signal();

	}
	
	
	

}
