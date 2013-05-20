package cls.island.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.image.Image;
import cls.island.control.Config;
import cls.island.view.components.IslandTile;

public class IslandComponentFactory {
	
	public static List<IslandTile> initializeTiles(Config config){
		List<IslandTile> islandTiles = new ArrayList<>();
		Map<String, Image> islandTilesImages = config.getIslandTilesImages();
		for (String name : islandTilesImages.keySet()){
//			islandTiles.add(new IslandTile(islandTilesImages.get(name), name));
//			islandTiles.add(new IslandTile(islandTilesImages.get(name), name));
		}
		return islandTiles;
	}

}
