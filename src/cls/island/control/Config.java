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

	public Config() {
		loadImages();
		loadIslandTileImages();
		fullScreenRes = Screen.getPrimary().getBounds();
		scaleFactor = Math.min(getFullScreenRes().getWidth() / getDefaultRes().getWidth(),
				getFullScreenRes().getHeight() / getDefaultRes().getHeight());

		loadCursorImg();
	}

	private void loadCursorImg() {
		cursorImg = new Image("mouse.png", 32, 32, false, true);
	}

	private void loadImages() {
		background = new Image("startScreen.png", false);
		diverImage = new Image("card.png", 160, 125, true, false);
		explorerImage = new Image("card.png", 160, 125, true, false);
		randomPlayerImage = new Image("randomPlayer.png", 160, 125, true, true);
		tickImage = new Image("tick.png", 40, 40, true, true);

		pieceWhite = new Image("pieceWhite.png", 31, 52, true, true);
		playerCardHolder = new Image("playerCardHolder.png", 283, 220, false, true);
		islandCard = new Image("islandCard.png", 70, 100, false, true);
		islandBackCard = new Image("islandCard.png", 70,100,false,true);

	}

	public Image getIslandBackCard() {
		return islandBackCard;
	}

	public Image getIslandCard() {
		return islandCard;
	}

	public Image getPlayerCardHolder() {
		return playerCardHolder;
	}

	public Rectangle2D getFullScreenRes() {
//		return fullScreenRes;
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
			dirStream = Files.newDirectoryStream(FileSystems.getDefault()
					.getPath(isladTileImageDir));
		} catch (IOException e) {
			throw new RuntimeException("Error initializing the island images", e);
		}
		for (Path imgPath : dirStream) {
			String fileName = imgPath.getFileName().toString();
			islandTiles.put(fileName.split("\\.")[0], new Image(ISLAND_TILES_IMAGE_PATH + "/"
					+ fileName, 120, 120, true, true));
		}
	}

}
