package cls.island.utils;

import java.util.List;
import java.util.Random;

import javafx.scene.Node;
import cls.island.model.GameModel;
import cls.island.model.player.Player;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.screen.IslandComponent;

public class ViewUtils {
	
	private static final Random rndGen = new Random();
	
	public static IslandComponent findIslandComponent(Node target) {
		IslandComponent islandComponent = null;
		if (target instanceof IslandComponent) {
			islandComponent = (IslandComponent) target;
		} else if (target.getParent() != null) {
			islandComponent = findIslandComponent(target.getParent());
		}
		return islandComponent;
	}
	
	public static int getRandomInt(int fromInclusive, int toExclusinve){
		return rndGen.nextInt(toExclusinve - fromInclusive) + fromInclusive;
	}
	
	public static Player findPlayerHoldingCard(GameModel gameModel, TreasuryCard treasureCard) {
		Player result = null;
		List<Player> players = gameModel.getPlayers();
		for (Player player : players) {
			if (player.getTreasuryCards().contains(treasureCard)) {
				result = player;
				break;
			}
		}
		return result;
	}
	
	
	
	
}
