package cls.island.control;

import java.util.ArrayList;
import java.util.List;

public class Options {
	
	public enum PlayerType{
		DIVER, EXPLORER, PILOT, FORGOTTEN, RANDOM;
	}

	private List<PlayerType> selectedPlayers = new ArrayList<>();
	
	
	public void addPlayer(PlayerType type){
		selectedPlayers.add(type);
	}
	
	public void removePlayer(PlayerType type){
		selectedPlayers.remove(type);
	}
	
	/**
	 * @return a copy of the selected players in a list.
	 */
	public List<PlayerType> getPlayers(){
		return new ArrayList<>(selectedPlayers);
	}
	/**
	 * creates a new list of the selected players from 
	 * the provided one
	 * @param enumPlayers
	 */
	public void setPlayers(List<PlayerType> enumPlayers) {
		this.selectedPlayers = new ArrayList<>(enumPlayers);
		
	}
	

}
