package cls.island.control;

import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import cls.island.view.component.piece.PieceColor;
import cls.island.view.component.treasury.card.Type;

public class Config {

	private static final String STYLESHEET_PATH = "style.css";
	private static final String ISLAND_TILES_IMAGE_PATH = "images/tiles";
	private static final double DEFAULT_WIDTH = 1440D;
	private static final double DEFAULT_HEIGHT = 900D;

	private Rectangle2D defaultRes = new Rectangle2D(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	private Rectangle2D fullScreenRes;
	private double scaleFactor;

	private Image background;
	private Image cursorImg;
	private Image diverImage;
	private Image explorerImage;
	private Image randomPlayerImage;
	private Image tickImage;
	private Image pieceWhite;
	private Image playerCardHolder;
	private Image islandCard;
	private Image islandBackCard;
	private Map<String, Image> islandTiles = new HashMap<>();
	private Image waterRiseCard;
	private Image treasuryCardUseImg;
	private Image treasuryCardDiscardImg;
	private Image sandBagsCard;
	private Image fireCard;
	private Image earthCard;
	private Image windCard;
	private Image chaliceCard;
	private Image heliCard;
	private Image waterLevelImage;
	private Image waterLevelMarkerImage;
	private Image pieceRed;
	private Image pieceGreen;

	public Config() {
		loadImages();
		loadIslandTileImages();
		fullScreenRes = Screen.getPrimary().getBounds();
		scaleFactor = Math.min(getFullScreenRes().getWidth() / getDefaultRes().getWidth(), getFullScreenRes().getHeight()
				/ getDefaultRes().getHeight());

		loadCursorImg();
	}

	public Image getSandBagsCard() {
		return sandBagsCard;
	}

	private void loadCursorImg() {
		cursorImg = new Image("images/other/mouse.png", 32, 32, false, true);
	}

	private void loadImages() {
		background = new Image("images/other/startScreen.png", false);
		diverImage = new Image("images/other/card.png", 160, 125, true, false);
		explorerImage = new Image("images/other/card.png", 160, 125, true, false);
		randomPlayerImage = new Image("images/other/randomPlayer.png", 160, 125, true, true);
		tickImage = new Image("images/other/tick.png", 40, 40, true, true);
		pieceWhite = new Image("images/other/pieceWhite.png", 31, 52, true, true);
		pieceGreen = new Image("images/other/pieceGreen.png", 31, 52, true, true);
		pieceRed = new Image("images/other/pieceRed.png", 31, 52, true, true);
		playerCardHolder = new Image("images/other/playerCardHolder.png", 283, 220, false, true);
		islandCard = new Image("images/other/islandCard.png", 70, 100, false, true);
		islandBackCard = new Image("images/other/backcard.png", 70, 100, false, true);
		waterRiseCard = new Image("images/other/WaterRise.png", 70, 100, false, true);
		sandBagsCard = new Image("images/other/SandBags.png", 70, 100, false, true);
		fireCard = new Image("images/other/fireCard.png", 70, 100, false, true);
		earthCard = new Image("images/other/earthCard.png", 70, 100, false, true);
		windCard = new Image("images/other/windCard.png", 70, 100, false, true);
		chaliceCard = new Image("images/other/chaliceCard.png", 70, 100, false, true);
		heliCard = new Image("images/other/flycard.png", 70, 100, false, true);
		treasuryCardUseImg = new Image("images/other/gear.png", 25, 25, false, true);
		treasuryCardDiscardImg = new Image("images/other/cancel.png", 25, 25, false, true);
		waterLevelImage = new Image("images/other/WaterLevelMarker.png",86, 246,false,true);
		waterLevelMarkerImage =new Image("images/other/WaterMarker.png",28, 16,false,true);
	}

	public Image getHeliCard() {
		return heliCard;
	}

	public Image getTreasureIslandImgBack() {
		return islandBackCard;
	}

	public Image getTreasureIslandImgFront(Type type) {
		switch (type) {
		case WATER_RISE:
			return waterRiseCard;
		case SANDBAGS:
			return sandBagsCard;
		case HELICOPTER:
			return heliCard;
		case CRYSTAL_OF_FIRE:
			return fireCard;
		case EARTH_STONE:
			return earthCard;
		case STATUE_OF_WIND:
			return windCard;
		case OCEAN_CHALICE:
			return chaliceCard;
		default:
			return islandCard;
		}
	}

	public Image getPlayerCardHolder() {
		return playerCardHolder;
	}

	public Rectangle2D getFullScreenRes() {
		// return fullScreenRes;
		// TODO
		return new Rectangle2D(0, 0, 1366, 768);
//		 return defaultRes;
	}

	public Image getBackground() {
		return background;
	}

	public Rectangle2D getDefaultRes() {
		return defaultRes;
	}

	public double getScaleFactor() {
		return scaleFactor;
	}

	public Image getCursorImg() {
		return cursorImg;
	}

	public String getStyleSheetPath() {
		return STYLESHEET_PATH;
	}

	public Image getDiverImage() {
		return diverImage;
	}

	public Image getTickImage() {
		return tickImage;
	}

	public Image getExplorerImage() {
		return explorerImage;
	}

	public Image getRandomPlayerImage() {
		return randomPlayerImage;
	}

	public Image getPieceWhite() {
		return pieceWhite;
	}

	public Image getLolImagePlayer() {
		return explorerImage;
	}

	public Map<String, Image> getIslandTilesImages() {
		return new HashMap<>(islandTiles);
	}

	private void loadIslandTileImages() {
		DirectoryStream<Path> dirStream;
		URL base = this.getClass().getClassLoader().getResource(".");
		String isladTileImageDir = base.getPath().substring(1) + ISLAND_TILES_IMAGE_PATH;
		try {
			dirStream = Files.newDirectoryStream(FileSystems.getDefault().getPath(isladTileImageDir));
		} catch (IOException e) {
			throw new RuntimeException("Error initializing the island images", e);
		}
		for (Path imgPath : dirStream) {
			String fileName = imgPath.getFileName().toString();
			islandTiles.put(fileName.split("\\.")[0], new Image(ISLAND_TILES_IMAGE_PATH + "/" + fileName, 120, 120, true,
					true));
		}
	}

	public Image getTreasuryCardUseImg() {
		return treasuryCardUseImg;
	}

	public Image getTreasuryCardDiscardImg() {
		return treasuryCardDiscardImg;
	}

	public Image getWaterLevelImage() {
		return waterLevelImage;
	}

	public Image getWaterLevelMarkerImage() {
		return waterLevelMarkerImage;
	}

	public Image getPieceImage(PieceColor color) {
		switch (color) {
		case RED:
			return pieceRed;
		case GREEN:
			return pieceGreen;
		default:
			break;
		}
		return null;
	}

}
