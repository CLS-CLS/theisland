package cls.island.view.component.piece;

import javafx.scene.paint.Color;

public enum PieceColor {
	RED(Color.rgb(230, 49, 49)), 
	GREEN(Color.GREEN),
	BLUE(Color.rgb(20, 60, 220)),
	YELLOW(Color.rgb(220, 220, 19)),
	WHITE(Color.rgb(180, 180, 180)),
	BROWN(Color.BROWN),
	BLACK(Color.rgb(20, 20, 20));

	private final Color color;

	private PieceColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

}
