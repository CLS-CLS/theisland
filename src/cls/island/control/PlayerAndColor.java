package cls.island.control;

import cls.island.control.Config.PlayerType;
import cls.island.view.component.piece.PieceColor;

public  class PlayerAndColor {
	private final PlayerType playerType;
	private final PieceColor pieceColor;
	
	public PlayerAndColor(PlayerType player, PieceColor color) {
		super();
		this.playerType = player;
		this.pieceColor = color;
	}
	public PlayerType getPlayerType() {
		return playerType;
	}
	public PieceColor getPieceColor() {
		return pieceColor;
	}
}