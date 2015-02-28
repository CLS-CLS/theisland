package cls.island.utils;

import javafx.scene.Node;
import cls.island.view.screen.IslandComponent;

public class ViewUtils {
	
	public static IslandComponent findIslandComponentParent(Node target) {
		IslandComponent islandComponent = null;
		if (target instanceof IslandComponent) {
			islandComponent = (IslandComponent) target;
		} else if (target.getParent() != null) {
			islandComponent = findIslandComponentParent(target.getParent());
		}
		return islandComponent;
	}

}
