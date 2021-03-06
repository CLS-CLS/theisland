package cls.island.view.screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import cls.island.control.Config;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.model.GameModel;
import cls.island.model.LooseCondition;
import cls.island.model.player.NavigatorPlayer;
import cls.island.model.player.PilotPlayer;
import cls.island.model.player.Player;
import cls.island.utils.Animations;
import cls.island.utils.ButtonFactory;
import cls.island.utils.FxThreadBlock;
import cls.island.utils.LocCalculator;
import cls.island.utils.LocCalculator.Loc;
import cls.island.view.component.MessagePanel;
import cls.island.view.component.actionsleft.ActionsLeftView;
import cls.island.view.component.island.Island;
import cls.island.view.component.island.IslandView;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.piece.PieceView;
import cls.island.view.component.player.base.PlayerBaseView;
import cls.island.view.component.treasurebag.TreasuryBagView;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.TreasuryCardView;
import cls.island.view.component.treasury.pile.TreasuryPile;
import cls.island.view.component.treasury.pile.TreasuryPile.PileType;
import cls.island.view.component.waterlevel.WaterLevelView;
import cls.island.view.control.Action;
import cls.island.view.screen.popup.FloodCardDrawPopUp;
import cls.island.view.screen.popup.GameLostPopUp;
import cls.island.view.screen.popup.MenuPopUp;
import cls.island.view.screen.popup.PopUpInternal;
import cls.island.view.screen.popup.PopUpWrapper;
import cls.island.view.screen.popup.SelectPieceToFlyPopup;
import cls.island.view.screen.popup.TreasuryCardsSneakPeek;
import cls.island.view.screen.popup.WinGamePopUp;

public class IslandScreen extends Group {
	private static final int MAX_CAMERA_ANGLE = 45;
	Group controlSceneGroup = new Group();
	Group islandSceneGroup = new Group();
	private SubScene controlScene = new SubScene(controlSceneGroup, 1440, 900);
	private SubScene islandScene = new SubScene(islandSceneGroup, 1440, 900, true, SceneAntialiasing.BALANCED);

	protected static LocCalculator locCalculator = LocCalculator.getInstance();
	private boolean animationInProgress;

	private Rectangle background;
	private List<TreasuryCard> cards = new ArrayList<>();
	private TreasuryPile treasuryBase;
	private MessagePanel msgPanel = new MessagePanel();
	/**
	 * hold all the available buttons for easy iteration.
	 */
	private List<ButtonBase> buttons = new ArrayList<>();
	private ToggleGroup toggleGroup = new ToggleGroup();
	private ToggleButton moveButton;
	private Button nextTurnButton;
	private Button undoButton;
	private ToggleButton shoreUpButton;
	private ToggleButton tradeButton;
	private ToggleButton flyButton;
	private ToggleButton moveOtherButton;
	private Button menuButton;
	private List<TreasuryCardView> floodCards = new ArrayList<>();
	private IslandView islandViewToDelete;
	private WaterLevelView waterLevelView;
	private Button collectTreasureButton;
	private Config config;
	private Stage stage;
	private double startingY;
	private Rotate xRotate;
	private boolean moveCamera;
	private ImageView background2;
	private PopUpInternal<Void> menuPopUp;

	public IslandScreen(final Stage stage, final GameController gameController, final Config config, GameModel model) {
		
		this.stage = stage;
		this.getChildren().addAll(controlScene, islandScene);
		this.config = config;
		this.menuPopUp = new MenuPopUp(gameController, config );
		
		PerspectiveCamera camera = new PerspectiveCamera(true);
		setUpLights(islandSceneGroup);
		Group cameraGroup = new Group();
		cameraGroup.getChildren().add(camera);
		cameraGroup.setTranslateX(720);
		cameraGroup.setTranslateY(450);
		camera.setTranslateZ(-1700);
		camera.setNearClip(100);
		camera.setFarClip(100000);
		xRotate = new Rotate(17, Rotate.X_AXIS);
		cameraGroup.getTransforms().add(xRotate);
		// cameraGroup.getTransforms().add(new Rotate(90, Rotate.Z_AXIS));
		islandScene.setCamera(camera);
		islandSceneGroup.getChildren().add(cameraGroup);
		
		background = new Rectangle(config.getDefaultRes().getWidth(), config.getDefaultRes().getHeight(), Color.DARKGRAY);
		background2 = new ImageView(new Image("images/other/background2.png", 880, 980, false, true));

		controlSceneGroup.getChildren().add(0, background);
		controlSceneGroup.getChildren().add(1, background2);
		background2.setTranslateX(300);
		background2.setTranslateY(0);

		for (Island island : model.getIslands()) {
			islandSceneGroup.getChildren().add(island.getComponent());
			Loc location = locCalculator.gridToCoords(island.getGrid());
			island.getComponent().translate(location);
			islandViewToDelete = island.getComponent();
			if (island.isFlooded()) {
				island.getComponent().setFlood();
				island.getComponent().activateSavedNode();
			}
			// TODO same for sinked.
		}

		waterLevelView = model.getWaterLevel().getComponent();
		waterLevelView.translate(1030, 30);
		controlSceneGroup.getChildren().add(waterLevelView);

		for (Player player : model.getPlayers()) {
			Piece piece = player.getPiece();
			islandSceneGroup.getChildren().add(piece.getComponent());
			Island island = piece.getIsland();
			c_movePiece(piece.getComponent(), island.getComponent(), island.getPiecePosition(piece));
			controlSceneGroup.getChildren().add(player.getBase().getComponent());
		}
		model.getCurrentTurnPlayer().getBase().getComponent().setActive(true);
		model.getCurrentTurnPlayer().getPiece().getComponent().createValidToClick().switchEffectOn();

		// ~~~~ ~~~~~~~~~~~~~ treasury base -cards ~~~~~~~~~~~~~~~~~~~~~~ //
		treasuryBase = model.getTreasuryPile();
		treasuryBase.getComponent().translate(1200, 5);
		controlSceneGroup.getChildren().add(2, treasuryBase.getComponent());
		for (TreasuryCard trCard : model.getTreasuryPile().getTreasuryCards(PileType.NORMAL)) {
			cards.add(trCard);
			controlSceneGroup.getChildren().add(trCard.getComponent());
		}
		treasuryBase.getComponent().rearrangePiles();

		addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
			if (!c_isAnimationInProgress())
				gameController.mouseClicked(e);
		});

		addEventHandler(MouseEvent.MOUSE_DRAGGED, this::moveCamera);
		
		addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
			moveCamera = (e.getTarget() == background2);
			startingY = e.getY();
		});

		TreasuryBagView treasureBagView = model.getTreasureBag().getComponent();
		controlSceneGroup.getChildren().add(treasureBagView);
		treasureBagView.translate(920, 810);

		moveButton = ButtonFactory.actionToggleButton("Move", ButtonAction.MOVE, gameController);
		moveButton.setToggleGroup(toggleGroup);
		buttons.add(moveButton);
		shoreUpButton = ButtonFactory.actionToggleButton("Shore \nUp", ButtonAction.SHORE_UP, gameController);
		shoreUpButton.setToggleGroup(toggleGroup);
		toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if (newValue == null) {
					toggleGroup.selectToggle(moveButton);
				}

			}
		});
		buttons.add(shoreUpButton);
		tradeButton = ButtonFactory.actionToggleButton("Trade", ButtonAction.TRADE, gameController);
		tradeButton.setToggleGroup(toggleGroup);
		buttons.add(tradeButton);
		collectTreasureButton = ButtonFactory.actionButton("Collect \n Treasure", ButtonAction.COLLECT_TREASURE,
				gameController);
		buttons.add(collectTreasureButton);
		nextTurnButton = ButtonFactory.actionButton("Next \n Turn", ButtonAction.NEXT_TURN, gameController);
		buttons.add(nextTurnButton);
		undoButton = ButtonFactory.actionButton("Undo", ButtonAction.UNDO, gameController);
		
		flyButton = ButtonFactory.actionToggleButton("Fly", ButtonAction.FLY, gameController);
		flyButton.setToggleGroup(toggleGroup);
		buttons.add(flyButton);
		
		menuButton = ButtonFactory.menuButton("Menu");
		menuButton.setOnAction((e)->c_showMenuPopUp());
		
		moveOtherButton = ButtonFactory.actionToggleButton("Move \nOther", ButtonAction.MOVE_OTHER, gameController);
		moveOtherButton.setToggleGroup(toggleGroup);
		buttons.add(moveOtherButton);
		
		ActionsLeftView actionsLeft = model.getActionsLeft().getComponent();
		controlSceneGroup.getChildren().add(actionsLeft);
		actionsLeft.translate(300, 710);
		moveButton.setTranslateX(1200);
		moveButton.setTranslateY(300);
		shoreUpButton.setTranslateX(1310);
		shoreUpButton.setTranslateY(300);
		tradeButton.setTranslateX(1200);
		tradeButton.setTranslateY(410);
		collectTreasureButton.setTranslateX(1310);
		collectTreasureButton.setTranslateY(410);
		nextTurnButton.setTranslateX(1200);
		nextTurnButton.setTranslateY(520);
		undoButton.setTranslateX(1200);
		undoButton.setTranslateY(630);
		undoButton.disableProperty().bind(BooleanBinding.booleanExpression(gameController.undoActionProperty()).not());
		flyButton.setTranslateX(1310);
		flyButton.setTranslateY(520);
		moveOtherButton.setTranslateX(1310);
		moveOtherButton.setTranslateY(520);
		
		menuButton.setTranslateX(1210);
		menuButton.setTranslateY(840);

		controlSceneGroup.getChildren().add(moveButton);
		controlSceneGroup.getChildren().add(shoreUpButton);
		controlSceneGroup.getChildren().add(collectTreasureButton);
		controlSceneGroup.getChildren().add(tradeButton);
		controlSceneGroup.getChildren().add(nextTurnButton);
		controlSceneGroup.getChildren().add(undoButton);
		controlSceneGroup.getChildren().add(menuButton);
		c_setUpButtonsForPlayer(model.getCurrentTurnPlayer());
		controlSceneGroup.getChildren().add(msgPanel);
		msgPanel.translate(500, 1000);
		// addControls();
	}


	public void moveCamera(MouseEvent me) {
		if (!moveCamera)
			return;
		double delta = me.getY() - startingY;
		startingY = me.getY();
		double deltaAngle = (delta / 500) * 30;
		double finalAngle = xRotate.getAngle() + deltaAngle;
		if (finalAngle > MAX_CAMERA_ANGLE) {
			finalAngle = MAX_CAMERA_ANGLE;
		}
		if (finalAngle < 0) {
			finalAngle = 0;
		}
		xRotate.setAngle(finalAngle);
	}

	private void setUpLights(Group islandSceneGroup) {
		PointLight pointLight = new PointLight();
		pointLight.getTransforms().add(new Translate(0, 0, -1500));
		PointLight pointLight2 = new PointLight();
		pointLight2.getTransforms().add(new Translate(1440, 700, -700));
		islandSceneGroup.getChildren().addAll(pointLight, pointLight2);
		AmbientLight al = new AmbientLight(Color.ANTIQUEWHITE);
		islandSceneGroup.getChildren().add(al);
		al.getTransforms().add(new Translate(500, 700, 0));

	}

	public List<TreasuryCard> c_getTreasuryCards() {
		return cards;
	}

	public List<TreasuryCardView> c_getFloodCards() {
		return floodCards;
	}

	public void c_movePiece(final PieceView pieceView, final IslandView islandView, final int index) {
		FxThreadBlock block = new FxThreadBlock();

		block.execute(() -> {
			Loc pieceLoc = islandView.getLoc().add(locCalculator.pieceLocationOnIslandTile(index));

			EventHandler<ActionEvent> onFinish = (event) -> {
				// TODO Fix It !! BAD CODE *** calls the model.. when it
				// shouldn' t
				SortedMap<Integer, Piece> pieceZorder = islandView.getParentModel().getPiecesAndPositions();
				for (int order : pieceZorder.keySet()) {
					pieceZorder.get(order).getComponent().toFront();
				}
			};
			Animations.moveComponentToLocation(pieceView, pieceLoc, onFinish, block);
		});
	}

	public void c_moveTreasuryCardFromPileToPlayer(TreasuryCardView treasuryCard, PlayerBaseView playerBaseView) {
		treasuryCard.setFaceUp(true);
		playerBaseView.moveToBase(treasuryCard);

	}

	public void c_discardPlayerCard(PlayerBaseView playerBaseView, TreasuryCardView treasuryCard) {
		treasuryBase.getComponent().moveCardtoPile(treasuryCard, PileType.DISCARD);
		playerBaseView.rearrangeCards();
	}

	public void c_showMessagePanel(final String message) {
		FxThreadBlock block = new FxThreadBlock();
		block.execute(() -> {
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), new KeyValue(msgPanel.translateYProperty(),
					800)));

			timeline.setOnFinished((event) -> {
				msgPanel.showMessage(message);
				block.unpause();
			});

			timeline.play();
		});

	}

	public void c_hideMessagePanel() {
		FxThreadBlock block = new FxThreadBlock();
		block.execute(() -> {
			Timeline tmln = new Timeline(new KeyFrame(Duration.millis(200),
					new KeyValue(msgPanel.translateYProperty(), 1000)));
			tmln.setOnFinished((e) -> block.unpause());
			tmln.play();
		});
	}
	public void c_WinGamePopUp(){
		c_showPopup(new WinGamePopUp());
	}
	public void c_WaterCardDrawnPopUp() {
		c_showPopup(new FloodCardDrawPopUp());
	}

	// Use for debug purposes
	public void addControls() {
		int counter = 500;
		for (String name : islandViewToDelete.exposeEffect().keySet()) {
			HBox hbox = new HBox();
			Label label = new Label(name);
			hbox.getChildren().add(label);
			ScrollBar scrollbar = new ScrollBar();
			scrollbar.maxProperty().set(1);
			scrollbar.minProperty().set(-1);
			scrollbar.valueProperty().set(0);
			scrollbar.orientationProperty().set(Orientation.HORIZONTAL);
			islandViewToDelete.exposeEffect().get(name).bind(scrollbar.valueProperty());
			hbox.getChildren().add(scrollbar);
			final Label text = new Label();
			SimpleDoubleProperty sdb = new SimpleDoubleProperty();
			sdb.bind(scrollbar.valueProperty());
			sdb.addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					text.setText(newValue + "");
				}
			});
			counter = counter + 50;
			hbox.getChildren().add(text);
			controlSceneGroup.getChildren().add(hbox);
			hbox.setTranslateX(50);
			hbox.setTranslateY(counter);
		}
	}

	public void c_showLooseGamePopUp(LooseCondition looseCondition, Object... infos) {
		c_showPopup(new GameLostPopUp(looseCondition, infos));
	}

	public void c_showTreasurePilePopUp(PileType type) {
		c_showPopup(new TreasuryCardsSneakPeek(treasuryBase, type));
	}
	
	public void c_showMenuPopUp(){
		c_showPopup(menuPopUp);
	}

	/**
	 * disables the action buttons with the specific actions. If no actions are
	 * provided all the buttons are disabled
	 * 
	 * @param actions
	 */
	public void disableButtons(ButtonAction... actions) {
		setButtonsDisabled(true, false, actions);
	}

	public List<Piece> c_showSelectPieceToFlyPopUp(List<Piece> piecesToMove) {
		return c_showPopup(new SelectPieceToFlyPopup(piecesToMove));

	}

	/**
	 * enables the action buttons with the specific actions. If no actions are
	 * provided all the buttons are enabled
	 * 
	 * @param actions
	 */
	public void enableButtons(ButtonAction... actions) {
		setButtonsDisabled(false, false, actions);
	}

	public void disableAllButtonsExcluding(ButtonAction... actions) {
		setButtonsDisabled(true, true, actions);
	}

	public void enableAllButtonsExluding(ButtonAction... actions) {
		setButtonsDisabled(false, true, actions);
	}

	private void setButtonsDisabled(boolean disabled, boolean isExcludeList, ButtonAction... buttonActions) {
		List<ButtonAction> actionList = Arrays.asList(buttonActions);
		for (ButtonBase button : buttons) {
			if (!(button instanceof Action))
				continue;
			Action actionButton = (Action) button;
			if (buttonActions.length == 0) {
				button.setDisable(disabled);
			} else {
				if (isExcludeList && !actionList.contains(actionButton.getButtonAction())) {
					button.setDisable(disabled);
				}
				if (!isExcludeList && actionList.contains(actionButton.getButtonAction())) {
					button.setDisable(disabled);
				}
			}

		}
	}

	public void c_setSelectedActionButton(final ButtonAction action) {

		switch (action) {
		case MOVE:
			moveButton.setSelected(true);
			break;
		case SHORE_UP:
			shoreUpButton.setSelected(true);
			break;
		case TRADE:
			tradeButton.setSelected(true);
			break;
		default:
			break;
		}
	}

	public void c_setCursorImage(ButtonAction action) {
		switch (action) {
		case SHORE_UP:
			c_setCursorImage(config.shoreUpCursorImg);
			break;
		case MOVE:
			c_setCursorImage(config.cursorImg);
		}
	}

	public void c_setCursorImage(Image cursorImg) {
		stage.getScene().setCursor(new ImageCursor(cursorImg));
	}

	public void c_setUpButtonsForPlayer(Player player) {
		controlSceneGroup.getChildren().remove(flyButton);
		controlSceneGroup.getChildren().remove(moveOtherButton);
		if (player instanceof PilotPlayer) {
			controlSceneGroup.getChildren().add(flyButton);
		} else if (player instanceof NavigatorPlayer) {
			controlSceneGroup.getChildren().add(moveOtherButton);
		}
	}

	/**
	 * Shows the pop up on top of this screen. Makes the thread that requested
	 * this pop-up to wait until the pop-up is closed.
	 * 
	 * @param popUpInternal
	 * @return
	 */
	protected <T> T c_showPopup(final PopUpInternal<T> popUpInternal) {
		final PopUpWrapper<T> popUp = new PopUpWrapper<>(popUpInternal, stage);
		return popUp.getResult();
	}

	public void c_setAnimationInProgress(final boolean inProgress) {
		animationInProgress = inProgress;
	}

	public boolean c_isAnimationInProgress() {
		return animationInProgress;
	}

}
