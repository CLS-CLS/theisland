package cls.island.control;

import java.util.Arrays;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import cls.island.control.Options.PlayerType;
import cls.island.model.GameModel;
import cls.island.utils.Animations;
import cls.island.utils.TimeLineManager;
import cls.island.view.component.piece.PieceColor;
import cls.island.view.screen.IslandScreen;
import cls.island.view.screen.OptionScreen;
import cls.island.view.screen.Root;
import cls.island.view.screen.SelectPlayerScreen;
import cls.island.view.screen.StartScreen;

public class MainController {

	protected volatile boolean animationInProgress = false;
	protected Stage stage;
	protected StartScreen startScreen;
	protected SelectPlayerScreen selectPlayerScreen;
	protected Scene scene;
	protected Config config;
	protected Root root;
	protected Options options;
	protected Node optionScreen;
	protected IslandScreen islandScreen;
	protected GameController gameController;
	protected GameModel gameModel;

	public MainController(Stage stage) {
		this.stage = stage;
	}

	public void goToStartPreGame() {
		Animations.transtition(startScreen, selectPlayerScreen, root);
	}

	public void goToOptions() {
		Animations.transtition(startScreen, optionScreen, root);
	}

	public void goToIslandScreen() {
		options.setPlayers(selectPlayerScreen.getSelectedPlayers());
//		Animations.transtition(selectPlayerScreen, islandScreen, root);
		gameController.startNewGame();

	}

	public void goToMainScreen(Node from) {
		Animations.transtition(from, startScreen, root);
	}

	public void init() {
		config = Config.getInstance();
		root = new Root(MainController.this, config);
		options = new Options();
		selectPlayerScreen = new SelectPlayerScreen(MainController.this, config);
		startScreen = new StartScreen(MainController.this, config);
		optionScreen = new OptionScreen(MainController.this, config);

		initScene();
		initStage();

		// When animation is in progress the mouse events are disabled.
		TimeLineManager.getInstance().registerNotifyStart(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				animationInProgress = true;
			}
		});

		// When animation is over the mouse events are enabled.
		TimeLineManager.getInstance().registerNotifyFinish(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				animationInProgress = false;
			}
		});

		// goToMainScreen(null);
		options.setPlayers(Arrays.asList(new PlayerAndColor[] {
				new PlayerAndColor(PlayerType.ENGINEER, PieceColor.RED),
				new PlayerAndColor(PlayerType.EXPLORER, PieceColor.GREEN), 
				new PlayerAndColor(PlayerType.PILOT, PieceColor.BLUE)}));
		gameModel = new GameModel(options, config);
		gameModel.newGame();
		gameController = new GameController(MainController.this, gameModel);
		islandScreen = new IslandScreen(MainController.this, gameController, config, gameModel);
		gameController.setIslandScreen(islandScreen);
		root.clear();
		root.getChildren().add(islandScreen);
		gameController.startNewGame();

	}

	private void initScene() {
		scene = new Scene(root);
		scene.setCursor(new ImageCursor(config.cursorImg));
		scene.getStylesheets().add(config.getStyleSheetPath());
	}

	private void initStage() {
		stage.fullScreenProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
					Boolean newValue) {
				if (!newValue) {
					stage.setIconified(true);
				}
			}
		});
		stage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
					Boolean newValue) {
				if (!newValue) {
					stage.setFullScreen(true);
				}
			}
		});

		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		// stage.setFullScreen(true);
		stage.show();
	}

	public Options getOptions() {
		return options;
	}

	public void setCursorImage(Image cursorImg) {
		scene.setCursor(new ImageCursor(cursorImg));
		
	}

}
