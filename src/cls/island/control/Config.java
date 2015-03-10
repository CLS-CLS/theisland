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
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.stage.Screen;
import cls.island.view.component.piece.PieceColor;
import cls.island.view.component.treasury.card.Type;

public class Config {
	private static Config INSTANCE = new Config();

	public enum PlayerType{
		DIVER, EXPLORER, PILOT, MESSENGER, RANDOM, ENGINEER, NAVIGATOR;
	}
	
	public enum Difficulty{
		EASY, NORMAL, EPIC, LEGENDARY
	}


	public static Config getInstance() {
		return INSTANCE;
	}

	private static final String STYLESHEET_PATH = "style.fxss";
	private static final String ISLAND_TILES_IMAGE_PATH = "images/tiles";
	private static final double DEFAULT_WIDTH = 1440D;
	private static final double DEFAULT_HEIGHT = 900D;
	private static final boolean FULL_SCREEN = false;

	private Rectangle2D defaultRes = new Rectangle2D(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	private Rectangle2D fullScreenRes;
	private double scaleFactor;

	public final Image background;
	public final Image cursorImg;
	public final Image shoreUpCursorImg;
	public final Image diverImage;
	public final Image explorerImage;
	public final Image randomPlayerImage;
	public final Image tickImage;
	public final Image pieceWhite;
	public final Image pieceYellow;
	public final Image pieceBrown;
	public final Image playerCardHolder;
	public final Image islandCard;
	public final Image islandBackCard;
	private Map<String, Image> islandTiles = new HashMap<>();
	public final Image waterRiseCard;
	public final Image treasuryCardUseImg;
	public final Image treasuryCardDiscardImg;
	public final Image sandBagsCard;
	public final Image fireCard;
	public final Image earthCard;
	public final Image windCard;
	public final Image chaliceCard;
	public final Image heliCard;
	public final Image waterLevelImage;
	public final Image waterLevelMarkerImage;
	public final Image pieceRed;
	public final Image pieceGreen;
	public Image earth;
	public Image chalice;
	public Image fire;
	public Image wind;
	public Image pieceBlue;
	private Image pieceRedLarge;
	private Image pieceGreenLarge;
	private Image pieceBlueLarge;
	private Image pieceWhiteLarge;
	public Image checkBoxImg;
	public Image checkBoxWithTick;
	public Image pilotImage;
	public Image messengerImage;
	public Image navigatorImage;
	public Image engineerImage;
	public Image deepOcean;
	public Image window;
	private Image pieceBlack;
	private AudioClip clickSound;
	private AudioClip undoSound;
	private AudioClip clickBtnSound;
	private AudioClip splashSound;
	private Media backgoundSound;
	private AudioClip fireballSound;

	

	private Config() {
		background = new Image("images/other/startScreen.png", false);
		diverImage = new Image("images/other/diver.png", 120, 120, false, true);
		explorerImage = new Image("images/other/explorer.png", 120, 120, false, true);
		pilotImage = new Image("images/other/pilot.png", 120, 120, false, true);
		messengerImage = new Image("images/other/messenger.png", 120, 120, false, true);
		engineerImage = new Image("images/other/engineer.png", 120, 120, false, true);
		navigatorImage = new Image("images/other/navigator.png", 120, 120, false, true);
		randomPlayerImage = new Image("images/other/randomPlayer.png", 160, 125, true, true);
		tickImage = new Image("images/other/tick.png", 40, 40, true, true);
		pieceWhite = new Image("images/other/pieceWhite.png", 46, 78, true, true);
		pieceGreen = new Image("images/other/pieceGreen.png", 46, 78, true, true);
		pieceRed = new Image("images/other/pieceRed.png", 46, 78, true, true);
		pieceBlue = new Image("images/other/pieceBlue.png", 46, 78, true, true);
		pieceYellow = new Image("images/other/pieceYellow.png", 46, 78, true, true);
		pieceBrown = new Image("images/other/pieceWhite.png", 46, 78, true, true);
		//TODO curently mapped to white image!! make it black
		pieceBlack = new Image("images/other/pieceBlack.png", 46, 78, true, true);
		pieceWhiteLarge = new Image("images/other/pieceWhite.png", 31 * 2, 52 * 2, true, true);
		pieceGreenLarge = new Image("images/other/pieceGreen.png", 31 * 2, 52 * 2, true, true);
		pieceRedLarge = new Image("images/other/pieceRed.png", 31 * 2, 52 * 2, true, true);
		pieceBlueLarge = new Image("images/other/pieceBlue.png", 31 * 2, 52 * 2, true, true);
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
		waterLevelImage = new Image("images/other/WaterLevelMarker.png", 86, 246, false, true);
		waterLevelMarkerImage = new Image("images/other/WaterMarker.png", 28, 16, false, true);
		loadIslandTileImages();
		fullScreenRes = Screen.getPrimary().getBounds();
		scaleFactor = Math.min(getFullScreenRes().getWidth() / getDefaultRes().getWidth(),
				getFullScreenRes().getHeight() / getDefaultRes().getHeight());

		shoreUpCursorImg = new Image("images/other/dig.png", 32, 32, false, true);
		cursorImg = new Image("images/other/mouse.png", 32, 32, false, true);
		earth = new Image("images/other/earth.png", 55, 70, false, true);
		chalice = new Image("images/other/chalice.png", 55, 70, false, true);
		fire = new Image("images/other/fire.png", 55, 70, false, true);
		wind = new Image("images/other/wind.png", 55, 70, false, true);

		checkBoxImg = new Image("images/other/checkbox.png", 50, 50, false, true);
		checkBoxWithTick = new Image("images/other/checkboxwithtick.png", 50, 50, false, true);

		deepOcean = new Image("images/other/deepocean.jpg", 400, 300, false, true);
		window = new Image("images/other/window.png", getDefaultRes().getWidth(), getDefaultRes()
				.getHeight(), false, true);

		clickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
		undoSound = new AudioClip(getClass().getResource("/sounds/undo.mp3").toString());
		splashSound = new AudioClip(getClass().getResource("/sounds/splash.mp3").toString());
		
		clickBtnSound = new AudioClip(getClass().getResource("/sounds/clickBtn.mp3").toString());
		backgoundSound = new Media(getClass().getResource("/sounds/WavesFinal.mp3").toString());
		fireballSound = new AudioClip(getClass().getResource("/sounds/Fireball.mp3").toString());
		fireballSound.setVolume(0.3);

	}

	public Image getTreasureImage(Type type) {
		switch (type) {
		case CRYSTAL_OF_FIRE:
			return fire;
		case EARTH_STONE:
			return earth;
		case STATUE_OF_WIND:
			return wind;
		case OCEAN_CHALICE:
			return chalice;
		}
		return null;
	}

	public Image getPlayerImage(PlayerType player) {
		switch (player) {
		case DIVER:
			return diverImage;
		case ENGINEER:
			return engineerImage;
		case EXPLORER:
			return explorerImage;
		case MESSENGER:
			return messengerImage;
		case PILOT:
			return pilotImage;
		case NAVIGATOR:
			return navigatorImage;
		default:
			return null;
		}

	}

	public Image getTreasureCard(Type type) {
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

	public Rectangle2D getFullScreenRes() {
//		 return fullScreenRes;
		// TODO
		return new Rectangle2D(0, 0, 1200, 800);
//		 return defaultRes;
	}

	public Rectangle2D getDefaultRes() {
		return defaultRes;
	}

	public double getScaleFactor() {
		return scaleFactor;
	}

	public String getStyleSheetPath() {
		return STYLESHEET_PATH;
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
			Image islandImg = new Image(ISLAND_TILES_IMAGE_PATH + "/"
					+ fileName, 120, 120, true, true);
			islandTiles.put(fileName.split("\\.")[0], islandImg);
		}
	}

	public Image getPieceImage(PieceColor color) {
		switch (color) {
		case RED:
			return pieceRed;
		case GREEN:
			return pieceGreen;
		case BLUE:
			return pieceBlue;
		case WHITE:
			return pieceWhite;
		case YELLOW:
			return pieceYellow;
		case BROWN:
			return pieceBrown;
		default:
			break;
		}
		return pieceRed;
	}

	public Image getPieceImageLarge(PieceColor color) {
		switch (color) {
		case RED:
			return pieceRedLarge;
		case GREEN:
			return pieceGreenLarge;
		case BLUE:
			return pieceBlueLarge;
		case WHITE:
			return pieceWhiteLarge;
		default:
			break;
		}
		return pieceRedLarge;
	}

	public AudioClip getClickSound() {
		return clickSound;
	}


	public AudioClip getUndoSound() {
		return undoSound;
	}

	public boolean isFullScreen() {
		return FULL_SCREEN;
	}

	public Media getBackgoundSound() {
		return backgoundSound;
	}

	public AudioClip getClickBtnSound() {
		return clickBtnSound;
	}

	public AudioClip getSplashSound() {
		return splashSound;
	}

	public AudioClip getFireballSound() {
		return fireballSound;
	}

}
