package cls.island.view.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import cls.island.control.Config;
import cls.island.control.GameController;
import cls.island.control.MainController;
import cls.island.model.GameModel;
import cls.island.model.LooseCondition;
import cls.island.model.player.Player;
import cls.island.utils.Animations;
import cls.island.utils.ButtonFactory;
import cls.island.utils.LocCalculator.Loc;
import cls.island.view.component.MessagePanel;
import cls.island.view.component.actionsleft.ActionsLeftView;
import cls.island.view.component.island.Island;
import cls.island.view.component.island.IslandView;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.piece.PieceView;
import cls.island.view.component.player.base.PlayerBaseView;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.TreasuryCardView;
import cls.island.view.component.treasury.pile.TreasuryPile;
import cls.island.view.component.treasury.pile.TreasuryPile.PileType;
import cls.island.view.component.waterlevel.WaterLevelView;
import cls.island.view.screen.popup.FloodCardDrawPopUp;

public class IslandScreen extends AbstractScreen {

	private Rectangle background;
	private List<TreasuryCard> cards = new ArrayList<>();
	private TreasuryPile treasuryBase;
	private MessagePanel msgPanel = new MessagePanel();
	private GameController gameController;
	private Button moveButton;
	private Button nextTurnButton;
	private Button shoreUpButton;
	private Button tradeButton;
	private List<TreasuryCardView> floodCards = new ArrayList<>();
	private final GameModel model;
	private IslandView islandViewToDelete;
	private WaterLevelView waterLevelView;
	private Button collectTreasureButton;

	public IslandScreen(final MainController mainController, final Config config, GameModel model) {
		super(mainController, config);
		this.model = model;
		background = new Rectangle(config.getDefaultRes().getWidth(), config.getDefaultRes().getHeight(), Color.DARKGRAY);
		ImageView background2 = new ImageView(new Image("images/other/background2.png", 880, 980, false, true));

		getChildren().add(0, background);
		getChildren().add(1, background2);
		background2.relocate(300, 0);

		for (Island island : model.getIslands()) {
			getChildren().add(island.getComponent());
			Loc location = locCalculator.gridToCoords(island.getGrid());
			island.getComponent().relocate(location);
			islandViewToDelete = island.getComponent();
		}
		
		waterLevelView = model.getWaterLevel().getComponent();
		waterLevelView.relocate(1030,30);
		getChildren().add(waterLevelView);
		

		for (Player player : model.getPlayers()) {
			getChildren().add(player.getPiece().getComponent());
			Piece piece = player.getPiece();
			Island island = piece.getIsland();
			c_movePiece(piece.getComponent(),island.getComponent(),island.getPiecePosition(piece));
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
				gameController.mouseClicked(event);
			}
		});

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Buttons
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		moveButton = ButtonFactory.actionButton("Move");
		shoreUpButton = ButtonFactory.actionButton("Shore \nUp");
		tradeButton = ButtonFactory.actionButton("Trade");
		collectTreasureButton = ButtonFactory.actionButton("Collect \n Treasure");
		nextTurnButton = ButtonFactory.actionButton("Next \n Turn");
		ActionsLeftView actionsLeft = model.getActionsLeft().getComponent();
		getChildren().add(actionsLeft);
		actionsLeft.relocate(400, 810);
		moveButton.relocate(1200, 300);
		shoreUpButton.relocate(1310, 300);
		tradeButton.relocate(1200, 410);
		collectTreasureButton.relocate(1310,410);
		nextTurnButton.relocate(1200, 520);
		getChildren().add(moveButton);
		getChildren().add(shoreUpButton);
		getChildren().add(collectTreasureButton);
		getChildren().add(tradeButton);
		getChildren().add(nextTurnButton);
		moveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				gameController.buttonPressed(GameController.ButtonAction.MOVE);
			}
		});
		shoreUpButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				gameController.buttonPressed(GameController.ButtonAction.SHORE_UP);
			}
		});
		collectTreasureButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				gameController.buttonPressed(GameController.ButtonAction.COLLECT_TREASURE);
				
			}
		});
		tradeButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				gameController.buttonPressed(GameController.ButtonAction.TRADE);
			}
		});
		nextTurnButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				gameController.buttonPressed(GameController.ButtonAction.NEXT_TURN);
			}
		});
		getChildren().add(msgPanel);
		msgPanel.relocate(600, 1000);
//		addControls();
	}

	public List<TreasuryCard> c_getTreasuryCards() {
		return cards;
	}

	public List<TreasuryCardView> c_getFloodCards() {
		return floodCards;
	}

	public void c_movePiece(PieceView pieceView, final IslandView islandView, int index) {
		Loc pieceLoc = islandView.getLoc().add(locCalculator.pieceLocationOnIslandTile(index));
		EventHandler<ActionEvent> onFinish = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Fix It !! BAD CODE *** calls the model.. when it
				// shouldn' t
				SortedMap<Integer, Piece> pieceZorder = islandView.getParentModel().getPiecesAndPositions();
				for (int order : pieceZorder.keySet()) {
					pieceZorder.get(order).getComponent().toFront();
				}
			}
		};
		Animations.moveComponentToLocation(pieceView, pieceLoc, onFinish, null);
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
		if (Platform.isFxApplicationThread())
			throw new RuntimeException(
					"the method should run outside fx-tread in order to be blocking");
		
		popUpwaitCondition = lock.newCondition();
		lock.lock();
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), new KeyValue(msgPanel.layoutYProperty(), 800)));
		timeline.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				msgPanel.showMessage(message);
				popUpwaitCondition.signal();
			}
		});
		timeline.play();
		try {
			popUpwaitCondition.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void c_hideMessagePanel() {
		if (Platform.isFxApplicationThread())
			throw new RuntimeException(
					"the method should run outside fx-tread in order to be blocking");
		
		popUpwaitCondition = lock.newCondition();
		lock.lock();
		Timeline tmln = new Timeline(new KeyFrame(Duration.millis(200), new KeyValue(msgPanel.layoutYProperty(), 1000)));
		tmln.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				popUpwaitCondition.signal();
				
			}
		});
		tmln.play();
		try {
			popUpwaitCondition.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void c_WaterCardDrawnPopUp() {
		c_showPopup(new FloodCardDrawPopUp());
	}

	public void setGameController(GameController gameController) {
		this.gameController = gameController;
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
					text.setText(newValue+"");
				}
			});
			counter = counter +50;
			hbox.getChildren().add(text);
			getChildren().add(hbox);
			hbox.relocate(50, counter);
		}
	}

	public void c_showLooseGamePopUp(LooseCondition checkLooseCondition) {
		c_showPopup(new FloodCardDrawPopUp());
	}
	
	

}
