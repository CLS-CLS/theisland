package cls.island.view.component.piece;

import javafx.scene.paint.Color;

public enum PieceColor {
	RED(Color.RED), 
	GREEN(Color.GREEN),
	BLUE(Color.BLUE),
	YELLOW (Color.YELLOW), WHITE(Color.WHITE), BROWN(Color.BROWN);
		
	private final Color color;

	private PieceColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	
	
	
}
