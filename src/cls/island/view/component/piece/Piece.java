package cls.island.view.component.piece;

import cls.island.view.component.island.Island;
import javafx.scene.image.Image;

public class Piece {
	
	private PieceView pieceView;
	private Island island;
	private String playerId;
	private final PieceColor color;
	
	public Piece(String playerId, PieceColor color) {
		this.color = color;
		pieceView = new PieceView(color.getColor(), this);
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

	public PieceView getComponent() {
		return pieceView;
	}

	public PieceColor getColor() {
		return color;
	}

}
