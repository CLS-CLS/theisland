package cls.island.control;

import java.util.ArrayList;
import java.util.List;

public class Options {
	
	private static final int DEFAULT_FLOOD = 0;
	
	private int floodStartingLevel = DEFAULT_FLOOD;

	private List<PlayerAndColor> selectedPlayers = new ArrayList<>();
	/**
	 * @return a copy of the selected players in a list.
	 */
	public List<PlayerAndColor> getPlayers(){
		return new ArrayList<>(selectedPlayers);
	}
	/**
	 * creates a new list of the selected players from 
	 * the provided one
	 * @param enumPlayers
	 */
	public void setPlayers(List<PlayerAndColor> playerAndColor) {
		this.selectedPlayers = new ArrayList<>(playerAndColor);
		
	}

	public int getFloodStartingLevel() {
		return floodStartingLevel;
	}
	public void setFloodStartingLevel(int floodStartingLevel) {
		this.floodStartingLevel = floodStartingLevel;
	}
	
}
