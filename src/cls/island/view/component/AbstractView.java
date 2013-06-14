package cls.island.view.component;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import cls.island.utils.LocCalculator;
import cls.island.utils.LocCalculator.Loc;
import cls.island.utils.SignaledRunnable;
import cls.island.utils.concurrent.AutoReentrantLock;
import cls.island.view.screen.IslandComponent;

public class AbstractView<T> extends Parent implements IslandComponent {
	private static final double HOVER_OVER_OPACITY = 0.2;
	private static final double HOVER_OVER_ANIM_DURATION = 200;

	private boolean selectable = true;
	private final Lock lock = new AutoReentrantLock();
	protected static LocCalculator locCalculator = LocCalculator.getInstance();
	private Rectangle mouseEnteredRect;
	private Timeline onEnteredAnimation = new Timeline();
	private Timeline onExitedAnimation = new Timeline();
	private T model;
	protected volatile Condition wait = lock.newCondition();

	private static final Effect defaultEffect() {
		Light.Distant light = new Light.Distant();
		light.setAzimuth(-45);
		light.setElevation(60);
		Lighting l = new Lighting();
		l.setLight(light);
		l.setSurfaceScale(5.5);
		l.setDiffuseConstant(1.5);
		DropShadow dr = new DropShadow();
//		dr.setInput(l);
		return dr;
	}

	public AbstractView(boolean enableDefaultEffect, T model) {
		if (enableDefaultEffect) {
			setEffect(defaultEffect());
		}
		this.model = model;
		initSelectableHandlers();
	}

	@Override
	public Loc getLoc() {
		return new Loc(getLayoutX(), getLayoutY());
	}

	@Override
	public void relocate(Loc loc) {
		super.relocate(loc.x, loc.y);
	}

	private void initSelectableHandlers() {

		
		this.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (!isSelectable())
					return;
				onExitedAnimation.stop();
				if (mouseEnteredRect == null){
					mouseEnteredRect = new Rectangle(AbstractView.this.getLayoutBounds().getWidth(),
							AbstractView.this.getLayoutBounds().getHeight(), Color.BLACK);
					mouseEnteredRect.setOpacity(0);
					getChildren().add(mouseEnteredRect);
				}
				double delta = HOVER_OVER_OPACITY - mouseEnteredRect.getOpacity();
				Duration duration = Duration.millis(delta * HOVER_OVER_ANIM_DURATION/ HOVER_OVER_OPACITY);
				if (duration.toMillis() > 0) {
					onEnteredAnimation = new Timeline(new KeyFrame(duration, new KeyValue(mouseEnteredRect.opacityProperty(), HOVER_OVER_OPACITY)));
					onEnteredAnimation.playFromStart();
				}
			}
		});
		this.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (!isSelectable())
					return;
				onEnteredAnimation.stop();
				if (mouseEnteredRect == null){
					mouseEnteredRect = new Rectangle(AbstractView.this.getLayoutBounds().getWidth(),
							AbstractView.this.getLayoutBounds().getHeight(), Color.BLACK);
					mouseEnteredRect.setOpacity(0);
					getChildren().add(mouseEnteredRect);
				}
				double delta = mouseEnteredRect.getOpacity();
				Duration duration = Duration.millis(Math.max(0,delta * 400));
				onExitedAnimation = new Timeline(new KeyFrame(duration, new KeyValue(mouseEnteredRect.opacityProperty(), 0)));
				onExitedAnimation.playFromStart();
				
			}
		});

	}

	public boolean isSelectable() {
		return selectable;
	}
	
	/**
	 * Temporarily cancels the selected effect. This method is provided because is some
	 * cases the mouseExited event is not throw and the component has the selected effect even
	 * if the mouse is not over it. An example of such case is when a treasury card is clicked and
	 * immediately is moved to pile by the computer. 
	 */
	public void cancelSelectedEffect(){
		mouseEnteredRect.opacityProperty().setValue(0);
	}

	public void setSelectable(boolean selectable) {
		onEnteredAnimation.stop();
		if (mouseEnteredRect!=null){
			mouseEnteredRect.setOpacity(0);
		}
		this.selectable = selectable;
	}
	
	public T getParentModel(){
		return model;
	}
	
	public void execute(final SignaledRunnable runnable){
		if (Platform.isFxApplicationThread()){
			runnable.run();
			return;
		}
		lock.lock();
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				runnable.run();
				if (!runnable.willSignal()){
					wait.signal();
				}
			}
		});
		try {
			wait.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
