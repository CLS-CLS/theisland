package cls.island.control;

import java.util.ArrayList;
import java.util.List;

import cls.island.view.component.piece.PieceColor;

public class Options {
	
	public enum PlayerType{
		DIVER, EXPLORER, PILOT, MESSENGER, RANDOM, ENGINEER;
	}

	private static final int DEFAULT_FLOOD = 0;

	private List<PlayerAndColor> selectedPlayers = new ArrayList<>();
	
	
	public void addPlayer(PlayerType type, PieceColor color){
		selectedPlayers.add(new PlayerAndColor(type, color));
	}
	
	public void removePlayer(PlayerType type){
		selectedPlayers.remove(type);
	}
	
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
		return DEFAULT_FLOOD;
	}
	
}
