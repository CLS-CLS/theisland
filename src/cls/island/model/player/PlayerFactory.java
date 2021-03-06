package cls.island.model.player;

import cls.island.control.Config.PlayerType;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.player.base.PlayerBase;

public class PlayerFactory {

	public static Player createPlayer(PlayerType type, Piece piece, PlayerBase base) {
		if (type == null || piece == null || base == null)
			throw new IllegalArgumentException("parameters should not be null");
		switch (type) {
		case EXPLORER:
			return new ExplorerPlayer(piece, base);
		case ENGINEER:
			return new EngineerPlayer(piece, base);
		case DIVER:
			return new DiverPlayer(piece, base);
		case PILOT:
			return new PilotPlayer(piece, base);
		case MESSENGER:
			return new MessengerPlayer(piece, base);
		case NAVIGATOR:
			return new NavigatorPlayer(piece, base);
		default:
			return new Player(type, piece, base);
		}
	}

}
