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
import javafx.scene.Group;
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
import javafx.util.Duration;
import cls.island.control.Config;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.MainController;
import cls.island.model.GameModel;
import cls.island.model.LooseCondition;
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
import cls.island.view.screen.popup.PopUpInternal;
import cls.island.view.screen.popup.PopUpWrapper;
import cls.island.view.screen.popup.SelectPieceToFlyPopup;

public class IslandScreen extends Group {
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
	private List<TreasuryCardView> floodCards = new ArrayList<>();
	private IslandView islandViewToDelete;
	private WaterLevelView waterLevelView;
	private Button collectTreasureButton;
	private MainController mainController;
	private Config config;

	public IslandScreen(final MainController mainController, final GameController gameController,
			final Config config, GameModel model) {
		this.mainController = mainController;
		this.config = config;
		background = new Rectangle(config.getDefaultRes().getWidth(), config.getDefaultRes()
				.getHeight(), Color.DARKGRAY);
		ImageView background2 = new ImageView(new Image("images/other/background2.png", 880, 980,
				false, true));

		getChildren().add(0, background);
		getChildren().add(1, background2);
		background2.relocate(300, 0);

		for (Island island : model.getIslands()) {
			getChildren().add(island.getComponent());
			Loc location = locCalculator.gridToCoords(island.getGrid());
			island.getComponent().relocate(location);
			islandViewToDelete = island.getComponent();
			if (island.isFlooded()) {
				island.getComponent().setFlood();
			}
			// TODO same for sinked.
		}

		waterLevelView = model.getWaterLevel().getComponent();
		waterLevelView.relocate(1030, 30);
		getChildren().add(waterLevelView);

		for (Player player : model.getPlayers()) {
			Piece piece = player.getPiece();
			getChildren().add(piece.getComponent());
			Island island = piece.getIsland();
			c_movePiece(piece.getComponent(), island.getComponent(), island.getPiecePosition(piece));
			getChildren().add(player.getBase().getComponent());
		}
		model.getCurrentTurnPlayer().getBase().getComponent().setActive(true);

		// ~~~~ ~~~~~~~~~~~~~ treasury base -cards ~~~~~~~~~~~~~~~~~~~~~~ //
		treasuryBase = model.getTreasuryPile();
		treasuryBase.getComponent().relocate(1200, 5);
		getChildren().add(2, treasuryBase.getComponent());
		for (TreasuryCard trCard : model.getTreasuryPile().getTreasuryCards(PileType.NORMAL)) {
			cards.add(trCard);
			getChildren().add(trCard.getComponent());
		}
		treasuryBase.getComponent().rearrangePiles();

		addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (!c_isAnimationInProgress()) {
					gameController.mouseClicked(event);
				}
			}
		});

		TreasuryBagView treasureBagView = model.getTreasureBag().getComponent();
		getChildren().add(treasureBagView);
		treasureBagView.relocate(620, 810);

		moveButton = ButtonFactory.actionToggleButton("Move", ButtonAction.MOVE, gameController);
		moveButton.setToggleGroup(toggleGroup);
		buttons.add(moveButton);
		shoreUpButton = ButtonFactory.actionToggleButton("Shore \nUp", ButtonAction.SHORE_UP,
				gameController);
		shoreUpButton.setToggleGroup(toggleGroup);
		toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue,
					Toggle newValue) {
				if (newValue == null) {
					toggleGroup.selectToggle(moveButton);
				}

			}
		});
		buttons.add(shoreUpButton);
		tradeButton = ButtonFactory.actionToggleButton("Trade", ButtonAction.TRADE, gameController);
		tradeButton.setToggleGroup(toggleGroup);
		buttons.add(tradeButton);
		collectTreasureButton = ButtonFactory.actionButton("Collect \n Treasure",
				ButtonAction.COLLECT_TREASURE, gameController);
		buttons.add(collectTreasureButton);
		nextTurnButton = ButtonFactory.actionButton("Next \n Turn", ButtonAction.NEXT_TURN,
				gameController);
		buttons.add(nextTurnButton);
		undoButton = ButtonFactory.actionButton("Undo", ButtonAction.UNDO, gameController);
		flyButton = ButtonFactory.actionToggleButton("Fly", ButtonAction.FLY, gameController);
		flyButton.setToggleGroup(toggleGroup);
		buttons.add(flyButton);
		ActionsLeftView actionsLeft = model.getActionsLeft().getComponent();
		getChildren().add(actionsLeft);
		actionsLeft.relocate(400, 810);
		moveButton.relocate(1200, 300);
		shoreUpButton.relocate(1310, 300);
		tradeButton.relocate(1200, 410);
		collectTreasureButton.relocate(1310, 410);
		nextTurnButton.relocate(1200, 520);
		undoButton.relocate(1200, 630);
		undoButton.disableProperty().bind(
				BooleanBinding.booleanExpression(gameController.undoActionProperty()).not());
		flyButton.relocate(1310, 520);

		getChildren().add(moveButton);
		getChildren().add(shoreUpButton);
		getChildren().add(collectTreasureButton);
		getChildren().add(tradeButton);
		getChildren().add(nextTurnButton);
		getChildren().add(undoButton);
		getChildren().add(flyButton);
		c_setUpButtonsForPlayer(model.getCurrentTurnPlayer());
		getChildren().add(msgPanel);
		msgPanel.relocate(600, 1000);
		// addControls();
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
			Animations.moveComponentToLocation(pieceView, pieceLoc, onFinish,
					block);
		});
	}

	public void c_moveTreasuryCardFromPileToPlayer(TreasuryCardView treasuryCard,
			PlayerBaseView playerBaseView) {
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
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200),
					new KeyValue(msgPanel.layoutYProperty(), 800)));
			
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
					new KeyValue(msgPanel.layoutYProperty(), 1000)));
			tmln.setOnFinished((e) -> block.unpause());
			tmln.play();
		});
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
				public void changed(ObservableValue<? extends Number> observable, Number oldValue,
						Number newValue) {
					text.setText(newValue + "");
				}
			});
			counter = counter + 50;
			hbox.getChildren().add(text);
			getChildren().add(hbox);
			hbox.relocate(50, counter);
		}
	}

	public void c_showLooseGamePopUp(LooseCondition looseCondition, Object... infos) {
		c_showPopup(new GameLostPopUp(looseCondition, infos));
	}

	/**
	 * disables the action buttons with the specific actions. If no
	 * actions are provided all the buttons are disabled
	 * @param actions
	 */
	public void disableButtons(ButtonAction... actions) {
		setButtonsDisabled(true, false, actions);
	}

	public List<Piece> c_showSelectPieceToFlyPopUp(List<Piece> piecesToMove) {
		return c_showPopup(new SelectPieceToFlyPopup(piecesToMove));

	}

	/**
	 * enables the action buttons with the specific actions. If no
	 * actions are provided all the buttons are enabled
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

	private void setButtonsDisabled(boolean disabled, boolean isExcludeList,
			ButtonAction... buttonActions) {
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
			mainController.setCursorImage(config.shoreUpCursorImg);
			break;
		case MOVE:
			mainController.setCursorImage(config.cursorImg);
		}
	}

	public void c_setUpButtonsForPlayer(Player player) {
		flyButton.setOpacity(0);
		if (player instanceof PilotPlayer) {
			flyButton.setOpacity(1);
		}
	}
	
	/**
	 * Shows the pop up on top of this screen. Makes the thread that 
	 * requested this pop-up to wait until the pop-up is closed.
	 * @param popUpInternal
	 * @return
	 */
	protected <T> T c_showPopup(final PopUpInternal<T> popUpInternal) {
		final PopUpWrapper<T> popUp = new PopUpWrapper<>(popUpInternal, mainController.getStage());
		return popUp.getResult();
	}
	
	public void c_setAnimationInProgress(final boolean inProgress) {
		animationInProgress = inProgress;
	}

	public boolean c_isAnimationInProgress() {
		return animationInProgress;
	}


}
