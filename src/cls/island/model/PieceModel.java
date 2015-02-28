package cls.island.model;

public class PieceModel {
	
	String playerId;
	
	Island island;
	
	public PieceModel(String playerId) {
		this.playerId = playerId;
	}

	public String getPlayerId() {
		return playerId;
	}

	
	public Island getIsland() {
		return island;
	}

	public void setIsland(Island island) {
		this.island = island;
	}
	
	

}
