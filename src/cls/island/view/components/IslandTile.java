package cls.island.view.components;

import java.util.SortedMap;
import java.util.TreeMap;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import cls.island.model.Island;
import cls.island.utils.Animations;
import cls.island.utils.LocCalculator.Loc;
import cls.island.utils.TimelineSingle;
import cls.island.utils.TimelineSingle.Process;

public class IslandTile extends AbstractComponent {
	
	public final Island model;
	
	
	public enum Position {
		UP, DOWN;
	}
	
	private String name;
	private DoubleProperty floodRate = new SimpleDoubleProperty();
	private Rectangle flood;
	private ImageView islandView;
	private Piece[] pieceSpots = new Piece[4];
	private double width;
	private double height;
	private Rectangle floodedBorder;

	public IslandTile(Image tileImage, String name, Island model) {
		super(true);
		this.model = model;
		this.name = name;
		floodRate.set(0D);
		width = tileImage.getRequestedWidth();
		height = tileImage.getRequestedHeight();
		islandView = new ImageView(tileImage);
		floodedBorder = new Rectangle(104, 104, Color.BLUE);
		floodedBorder.relocate(-2, -2);
		flood = new Rectangle(width, height, Color.BLUE);
		flood.opacityProperty().bind(floodRate);
		final ColorAdjust floodEffect = new ColorAdjust(0, 0, 0, 0);
		islandView.setEffect(floodEffect);

		floodRate.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue,
					Number newValue) {
				floodEffect.saturationProperty().set(-newValue.doubleValue() * 5);
			}
		});
		
		getChildren().add(islandView);
		getChildren().add(flood);
	}
	

	public void activateFlooBorder() {
		this.getChildren().add(floodedBorder);
	}

	
	public void deactivateFloodBorder() {
		TimelineSingle timelineSingle = new TimelineSingle(Process.ASYNC);
		timelineSingle.getKeyFrames()
				.add(new KeyFrame(Duration.millis(200), new KeyValue(floodedBorder
						.opacityProperty(), 0)));
		timelineSingle.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				IslandTile.this.getChildren().remove(floodedBorder);
				floodedBorder.setOpacity(1);
			}
		});
	}
	

	public void moveToGrid(int row, int col) {
		Loc location = locCalculator.gridToCoords(row, col);
		relocate(location.x, location.y);
	}

	
	/**
	 * adds the piece on this island tile. The IslandTile is the owner of the
	 * IslandTile-Piece bidirectional relationship , hence it sets this tile to
	 * the piece "islandTile" field.
	 * 
	 * @param piece
	 * @param onFinish
	 */
	public void addPiece(Piece piece, EventHandler<ActionEvent> onFinish) {
		Loc loc = null;
		for (int i = 0; i < pieceSpots.length; i++) {
			if (pieceSpots[i] == null) {
				pieceSpots[i] = piece;
				piece.model.setIsland(this.model);
				loc = locCalculator.pieceLocationOnIslandTile(i);
				break;
			}
		}
		Animations.moveComponentToLocation(piece, this.getLoc().add(loc), null, onFinish);
	}
	

	/**
	 * Returns the position of the piece on the tile.
	 * 
	 * @param piece
	 *            the piece for which we want to dind the position
	 * @return <code>Position.UP</code> if it is in the upper row.
	 *         <code>Position.DOWN</code> otherwise
	 * @throws IllegalArgumentException
	 *             if the tile does not contain the piece.
	 */
	public Position getPiecePosition(Piece piece) {
		Position pos = null;
		int index = getPieceIndex(piece);
		if (index < 2) {
			pos = Position.UP;
		}
		if (index >= 2) {
			pos = Position.DOWN;
		}
		return pos;
	}
	

	public void removePiece(Piece piece) {
		for (int i = 0; i < pieceSpots.length; i++) {
			if (pieceSpots[i] == piece) {
				pieceSpots[i] = null;
				return;
			}
		}
	}

	
	private int getPieceIndex(Piece piece) {
		int index = -1;
		for (int i = 0; i < pieceSpots.length; i++) {
			if (piece == pieceSpots[i]) {
				index = i;
				break;
			}
		}
		if (index == -1)
			throw new IllegalArgumentException("The piece " + piece + " was not found in the tile");
		return index;
	}

	
	public SortedMap<Integer, Piece> getPiecesAndPositions() {
		SortedMap<Integer, Piece> map = new TreeMap<>();

		for (int i = 0; i < pieceSpots.length; i++) {
			if (pieceSpots[i] != null) {
				map.put(Integer.valueOf(i), pieceSpots[i]);
			}
		}
		return map;
	}

	
	public String getName() {
		return name;
	}

	
	public DoubleProperty floodRateProperty() {
		return floodRate;
	}

	
	public void sink() {
		Animations.sinkTile(this);
	}

	
	public void flood() {
		Animations.floodTile(this, true);
	}

	
	public void unFlood() {
		Animations.floodTile(this, false);
	}


	public Island getModel() {
		return model;
	}
	
}
