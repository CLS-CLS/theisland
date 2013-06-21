package cls.island.view.component;

import javafx.scene.Parent;

public abstract class OnOffEffectNode extends Parent {
	
	/**
	 * the position the node is going to be added on the view.
	 * RelativePosition.BOTTOM when the effect should be placed behind all other node childs
	 * RelativePosition.BOTTOM to place the effect node in top of all the other childs.
	 *
	 */
	public enum RelativePosition{
		BOTTOM, TOP;
	}
	
	public abstract RelativePosition getRelativePosition();
	
	public abstract void switchEffectOn();
	
	public abstract void switchEffectOff();

}
