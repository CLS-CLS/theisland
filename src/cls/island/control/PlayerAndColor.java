package cls.island.control;

import cls.island.control.Config.PlayerType;
import cls.island.view.component.piece.PieceColor;

public  class PlayerAndColor {
	PlayerType player;
	PieceColor color;
	
	public PlayerAndColor(PlayerType player, PieceColor color) {
		super();
		this.player = player;
		this.color = color;
	}
	public PlayerType getPlayer() {
		return player;
	}
	public PieceColor getColor() {
		return color;
	}
}