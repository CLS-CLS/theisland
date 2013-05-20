package cls.island.view.components;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import cls.island.utils.LocCalculator;
import cls.island.utils.LocCalculator.Loc;
import cls.island.view.screen.IslandComponent;

public class AbstractComponent extends Parent implements IslandComponent {
	private static final double HOVER_OVER_OPACITY = 0.2;
	private static final double HOVER_OVER_ANIM_DURATION = 200;

	private boolean selectable = true;

	protected static LocCalculator locCalculator = LocCalculator.getInstance();

	private Rectangle mouseEnteredRect;

	private Timeline onEnteredAnimation = new Timeline();
	private Timeline onExitedAnimation = new Timeline();

	private static final Effect defaultEffect() {
		Light.Distant light = new Light.Distant();
		light.setAzimuth(-45);
		light.setElevation(45);
		Lighting l = new Lighting();
		l.setLight(light);
		l.setSurfaceScale(3.5);
		l.setDiffuseConstant(2);
		return l;
	}

	public AbstractComponent(boolean enableDefaultEffect) {
		if (enableDefaultEffect) {
			setEffect(defaultEffect());
		}
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
					mouseEnteredRect = new Rectangle(AbstractComponent.this.getLayoutBounds().getWidth(),
							AbstractComponent.this.getLayoutBounds().getHeight(), Color.BLACK);
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
					mouseEnteredRect = new Rectangle(AbstractComponent.this.getLayoutBounds().getWidth(),
							AbstractComponent.this.getLayoutBounds().getHeight(), Color.BLACK);
					mouseEnteredRect.setOpacity(0);
					getChildren().add(mouseEnteredRect);
				}
				double delta = mouseEnteredRect.getOpacity();
				Duration duration = Duration.millis(delta * 400);
				if (duration.toMillis() > 0) {
					onExitedAnimation = new Timeline(new KeyFrame(duration, new KeyValue(mouseEnteredRect.opacityProperty(), 0)));
					onExitedAnimation.playFromStart();
				}
			}
		});

	}

	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		onEnteredAnimation.stop();
		onEnteredAnimation.stop();
		if (mouseEnteredRect!=null){
			mouseEnteredRect.setOpacity(0);
		}
		this.selectable = selectable;
	}
}
