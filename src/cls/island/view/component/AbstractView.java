package cls.island.view.component;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import cls.island.utils.LocCalculator;
import cls.island.utils.LocCalculator.Loc;
import cls.island.view.screen.IslandComponent;


public class AbstractView<T> extends Parent implements IslandComponent {
	private static final double HOVER_OVER_OPACITY = 0.2;
	private static final double HOVER_OVER_ANIM_DURATION = 200;
	private boolean selectable = true;
	protected static LocCalculator locCalculator = LocCalculator.getInstance();
	private Rectangle mouseEnteredRect;
	private Timeline onEnteredAnimation = new Timeline();
	private Timeline onExitedAnimation = new Timeline();
	private T model;
	private volatile OnOffEffectNode validToClick;

	private static final Effect defaultEffect() {
//		Light.Distant light = new Light.Distant();
//		light.setAzimuth(-45);
//		light.setElevation(60);
//		Lighting l = new Lighting();
//		l.setLight(light);
//		l.setSurfaceScale(1.0);
//		l.setDiffuseConstant(1.5);
//		l.setSpecularConstant(0);
		DropShadow dr = new DropShadow();
		dr.setOffsetX(4f);
		dr.setOffsetY(4f);
//		dr.setInput(l);
		return dr;
	}
	
	/**
	 * Override to add custom valid to click effect.
	 * 
	 * @return
	 */
	protected OnOffEffectNode createValidToClick() {
		return new ValidEffectNode(getLayoutBounds().getWidth(), getLayoutBounds()
				.getHeight(), null);
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
		return new Loc(getTranslateX(), getTranslateY());
	}

	@Override
	public void translate(Loc loc) {
		translate(loc.x, loc.y);
	}
	
	public void translate(double x, double y){
		setTranslateX(x);
		setTranslateY(y);
	}
	
	public void setValidToCkickEffect(final boolean on) {

//		if (validToClick == null) {
//			validToClick = createValidToClick();
//		}
//		if (on && !getChildren().contains(validToClick)) {
//			if (validToClick.getRelativePosition() == RelativePosition.TOP) {
//				getChildren().add(validToClick);
//			} else {
//				getChildren().add(0, validToClick);
//			}
//			validToClick.switchEffectOn();
//		} else if (!on) {
//			getChildren().remove(validToClick);
//			validToClick.switchEffectOff();
//		}

	}

	private void initSelectableHandlers() {

		this.setOnMouseEntered(this::mouseEntered);
		
		this.setOnMouseExited(this::mouseExited);
	}
	
	
	public void mouseEntered(MouseEvent event){
		if (!isSelectable()) {
			return;
		}
		onExitedAnimation.stop();
		
		if (mouseEnteredRect == null) {
			mouseEnteredRect = new Rectangle(AbstractView.this.getLayoutBounds().getWidth(), AbstractView.this
					.getLayoutBounds().getHeight(), Color.BLACK);
			mouseEnteredRect.setOpacity(0);
			getChildren().add(mouseEnteredRect);
		}
		double delta = HOVER_OVER_OPACITY - mouseEnteredRect.getOpacity();
		Duration duration = Duration.millis(delta * HOVER_OVER_ANIM_DURATION / HOVER_OVER_OPACITY);
		if (duration.toMillis() > 0) {
			onEnteredAnimation = new Timeline(new KeyFrame(duration, new KeyValue(
					mouseEnteredRect.opacityProperty(), HOVER_OVER_OPACITY)));
			onEnteredAnimation.playFromStart();
		}
		
	}
	
	
	public void mouseExited(MouseEvent event){
		if (!isSelectable())
			return;
		onEnteredAnimation.stop();
		if (mouseEnteredRect == null) {
			mouseEnteredRect = new Rectangle(AbstractView.this.getLayoutBounds().getWidth(), AbstractView.this
					.getLayoutBounds().getHeight(), Color.BLACK);
			mouseEnteredRect.setOpacity(0);
			getChildren().add(mouseEnteredRect);
		}
		double delta = mouseEnteredRect.getOpacity();
		Duration duration = Duration.millis(Math.max(0, delta * 400));
		onExitedAnimation = new Timeline(new KeyFrame(duration, new KeyValue(mouseEnteredRect.opacityProperty(), 0)));
		onExitedAnimation.playFromStart();
	}
	
	public boolean isSelectable() {
		return selectable;
	}

	/**
	 * Temporarily cancels the selected effect. This method is provided because
	 * is some cases the mouseExited event is not throw and the component has
	 * the selected effect even if the mouse is not over it. An example of such
	 * case is when a treasury card is clicked and immediately is moved to pile
	 * by the computer.
	 */
	public void cancelSelectedEffect() {
		mouseEnteredRect.opacityProperty().setValue(0);
	}

	public void setSelectable(boolean selectable) {
		onEnteredAnimation.stop();
		if (mouseEnteredRect != null) {
			mouseEnteredRect.setOpacity(0);
		}
		this.selectable = selectable;
	}

	public T getParentModel() {
		return model;
	}

}
