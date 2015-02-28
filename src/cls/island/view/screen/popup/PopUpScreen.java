package cls.island.view.screen.popup;

import cls.island.utils.TimelineSingle;
import cls.island.view.screen.AbstractScreen;
import cls.island.view.screen.IslandScreen;
import cls.island.view.screen.PopUpInternal;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class PopUpScreen extends AbstractScreen {

	private EventHandler<ActionEvent> onClose;
	PopUpInternal internal;
	AbstractScreen parent;

	public PopUpScreen(PopUpInternal internal, AbstractScreen parent,
			EventHandler<ActionEvent> onClose) {
		super(parent.getMainController(), parent.getConfig());
		this.parent = parent;
		this.onClose = onClose;
		this.internal = internal;
		Rectangle rectangle = new Rectangle(config.getDefaultRes().getWidth(), config
				.getDefaultRes().getHeight(), Color.BLACK);
		rectangle.setOpacity(0.5);
		getChildren().add(rectangle);
		if (internal != null) {
			internal.registerToPopUp(this);

			internal.relocate((this.getBoundsInLocal().getWidth() - internal.getBoundsInLocal()
					.getWidth()) / 2, (this.getBoundsInLocal().getHeight() - internal
					.getBoundsInLocal().getHeight()) / 2);
			getChildren().add(internal);
		}
		double width = 700;
		double height = 500;
		if (internal != null) {
			width = internal.getLayoutBounds().getWidth() + 30;
			height = internal.getLayoutBounds().getHeight() + 30;
		}
		ImageView whiteBorder = new ImageView(
				new Image("whiteBorder.png", width, height, false, true));
		getChildren().add(whiteBorder);
		whiteBorder.relocate(internal.getLayoutX() - 15, internal.getLayoutY() - 15);
	}

	public void show() {
		parent.c_setIslandComponentsSelectable(false);
		setOpacity(0);
		TimelineSingle timelineSingle = new TimelineSingle();
		timelineSingle.getKeyFrames().add(
				new KeyFrame(Duration.millis(300), new KeyValue(PopUpScreen.this.opacityProperty(),
						1)));
		timelineSingle.play();

	}

	public void close() {
		parent.c_setIslandComponentsSelectable(true);
		if (onClose != null) {
			onClose.handle(new ActionEvent(this, null));
		}
	}

	@Override
	public void c_setIslandComponentsSelectable(boolean selectable) {
		throw new UnsupportedOperationException();
	}
}
