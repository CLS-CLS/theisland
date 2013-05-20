package cls.island.control;

import java.util.Arrays;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import cls.island.control.Options.PlayerType;
import cls.island.model.GameModel;
import cls.island.utils.Animations;
import cls.island.utils.TimeLineManager;
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
		gameModel = new GameModel(options);
		gameModel.randomizeBoard();
		islandScreen = new IslandScreen(this,config,gameModel);
		gameController = new GameController(this, gameModel,islandScreen);
		islandScreen.setGameController (gameController);
		Animations.transtition(selectPlayerScreen, islandScreen, root);
		
	}

	public void goToMainScreen(Node from) {
		Animations.transtition(from, startScreen, root);
	}

	public void init() {
		config = new Config();
		root = new Root(this, config);
		options = new Options();
		selectPlayerScreen = new SelectPlayerScreen(this, config);
		startScreen = new StartScreen(this, config);
		optionScreen = new OptionScreen(this, config);

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

		root.addEventFilter(MouseEvent.ANY, new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				if (isAnimationInProgress()) {
					event.consume();
				}
			}
		});

//		goToMainScreen(null);
		options.setPlayers(Arrays.asList(new PlayerType[]{PlayerType.DIVER, PlayerType.EXPLORER}));
		gameModel = new GameModel(options);
		gameModel.randomizeBoard();
		root.clear();
		islandScreen = new IslandScreen(this, config, gameModel);
		gameController = new GameController(this,gameModel,islandScreen);
		islandScreen.setGameController(gameController);
		
		root.getChildren().add(islandScreen);
	}

	private void initScene() {
		scene = new Scene(root);
		scene.setCursor(new ImageCursor(config.getCursorImg()));
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
//		stage.setFullScreen(true);
		stage.show();
	}

	public boolean isAnimationInProgress() {
		return animationInProgress;
	}

	public Options getOptions() {
		return options;
	}
	

	

}
