package cls.island.view.screen.popup;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import cls.island.utils.TimelineSingle;
import cls.island.view.screen.AbstractScreen;

/**
 * Provides the open / close mechanism with animations, adds a gray semi-opaque
 * background around the actual pop-up and a pseudo-modality.
 *   
 * @param <T> the type of the return result of the internal popup
 */
public class PopUpWrapper<T> extends AbstractScreen {

	private final PopUpInternal<T> internal;
	private final AbstractScreen parent;
	
	/**
	 * Constructs a wrapper for the internal popUp.
	 * @param internal the pop-up. 
	 * @param parent the parent screen this screnn will be added too.
	 */
	public PopUpWrapper(PopUpInternal<T> internal, AbstractScreen parent) {
		super(parent.getMainController(), parent.getConfig());
		this.parent = parent;
		this.internal = internal;
		Rectangle rectangle = new Rectangle(config.getDefaultRes().getWidth(), config
				.getDefaultRes().getHeight(), Color.BLACK);
		rectangle.setOpacity(0.5);
		getChildren().add(rectangle);
		rectangle.addEventHandler(Event.ANY, new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				event.consume();
			}
		});

			
		
		if (internal != null) {
			internal.registerToPopUp(this);

			internal.relocate((this.getBoundsInLocal().getWidth() - internal.getBoundsInLocal()
					.getWidth()) / 2, (this.getBoundsInLocal().getHeight() - internal
					.getBoundsInLocal().getHeight()) / 2);
			getChildren().add(internal);
		}

	}
	
	/**
	 * shows the pop-up
	 */
	public void show() {
		setOpacity(0);
		TimelineSingle timelineSingle = new TimelineSingle();
		timelineSingle.getKeyFrames().add(
				new KeyFrame(Duration.millis(300), new KeyValue(PopUpWrapper.this.opacityProperty(),
						1)));
		timelineSingle.play();

	}

	public void close() {
		parent.closePopup(this);
	}

	public T getResult() {
		return internal.getRusults();
	}

}
