package cls.island.view.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import cls.island.control.Config;
import cls.island.control.GameController;
import cls.island.control.MainController;
import cls.island.control.Options;
import cls.island.model.GameModel;
import cls.island.model.Island;
import cls.island.model.TreasuryCardModel;
import cls.island.utils.ButtonFactory;
import cls.island.utils.LocCalculator.Grid;
import cls.island.utils.LocCalculator.Loc;
import cls.island.view.components.Card;
import cls.island.view.components.CardBase;
import cls.island.view.components.IslandTile;
import cls.island.view.components.MessagePanel;
import cls.island.view.components.Piece;
import cls.island.view.components.PlayerCardHolder;
import cls.island.view.screen.popup.FloodCardDraw;
import cls.island.view.screen.popup.PopUpScreen;

public class IslandScreen extends AbstractScreen {
	private static int yoohooCounter = 0;

	private Loc[][] islandTileCoords;
	private List<IslandTile> islandTiles = new ArrayList<>();
	private Rectangle background;
	private List<Piece> pieces = new ArrayList<>();
	private List<PlayerCardHolder> playerCardHolders = new ArrayList<>();
	private List<Card> treasuryCards = new ArrayList<>();
	private CardBase treasuryBase;
	private MessagePanel msgPanel = new MessagePanel();
	private GameController gameController;
	private Button nextTurn;

	private CardBase floodBase;

	private List<Card> floodCards = new ArrayList<>();

	public IslandScreen(final MainController mainController, final Config config, GameModel model) {
		super(mainController, config);
		setupCoords(mainController.getOptions());
		background = new Rectangle(config.getDefaultRes().getWidth(), config.getDefaultRes()
				.getHeight(), Color.DARKGRAY);
		getChildren().add(background);
		getChildren().add(msgPanel);
		msgPanel.relocate(600, 1000);

		gererateBoard(config, mainController.getOptions(), model);

		// ~~~~~~~~~~~~~~~~~~~~~~ pieces ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
		for (cls.island.model.PieceModel player : model.getPieces()){
			pieces.add(new Piece(config.getPieceWhite(), player));
		}
		
		getChildren().addAll(pieces);

		pieces.get(1).relocate(30, 0);

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ playerCardHolders
		// ~~~~~~~~~~~~~~~~~~~~~~~~~ //
		int playerCardHolderPosition = 0;
		for (Options.PlayerType playerType : mainController.getOptions().getPlayers()) {
			PlayerCardHolder playerCardHolder = new PlayerCardHolder(playerType, config);
			playerCardHolders.add(playerCardHolder);
			getChildren().add(playerCardHolder);
			playerCardHolder.relocate(locCalculator
					.playerBasePositionToLoc(playerCardHolderPosition));
			playerCardHolderPosition++;

		}

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ treasury base -treasuryCards
		// ~~~~~~~~~~~~~~~~~~~~~~~~~ //
		treasuryBase = new CardBase("Treasury Cards", Color.GREEN);
		treasuryBase.relocate(1150, 30);
		getChildren().add(1, treasuryBase);
		for (TreasuryCardModel trModel : model.getTreasuryCards()) {
			Card card = new Card(config.getIslandCard(), config.getIslandBackCard(), trModel);
			treasuryCards.add(card);
			getChildren().add(card);
			treasuryBase.addCardtoPile(card);
		}
		
		addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				gameController.mouseClicked(event);
			}
		});
		
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~` Buttons ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		nextTurn = ButtonFactory.genButton("Next \n Turn");
		nextTurn.relocate(750, 820);
		getChildren().add(nextTurn);
		nextTurn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				gameController.nextTurnPressed();
				
			}
		});
	}

	private void layoutIslandTiles(Options options) {
		for (IslandTile tile : islandTiles){
			Grid grid = tile.model.getGrid();
			tile.moveToGrid(grid.row, grid.col);
		}
		
	}

	private void gererateBoard(Config config, Options options, GameModel model) {
		for (String name : config.getIslandTilesImages().keySet()) {
			IslandTile island = new IslandTile(config.getIslandTilesImages().get(name),name, model.getIsland(name));
			islandTiles.add(island);
			getChildren().add(island);
		}
		layoutIslandTiles(options);
	}

	private void setupCoords(Options options) {
		islandTileCoords = new Loc[6][6];
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 6; col++) {
				islandTileCoords[row][col] = locCalculator.gridToCoords(row, col);
			}
		}
	}

	public List<IslandTile> c_getIslandTiles() {
		return islandTiles;
	}

	public List<Piece> c_getPieces() {
		return pieces;
	}

	public List<Card> c_getTreasuryCards() {
		return treasuryCards;
	}

	public List<Card> c_getFloodCards() {
		return floodCards;
	}

	public void c_movePieceToTile(final Piece piece, final IslandTile toIsland) {
		if (piece.model.getIsland() != null) {
			IslandTile islandTileFrom = getIslandTileFromModel(piece.model.getIsland());
			islandTileFrom.removePiece(piece);
			islandTileFrom.model.removePiece(piece.model);
			
		}
		EventHandler<ActionEvent> onFinish = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				SortedMap<Integer, Piece> pieceZorder = toIsland.getPiecesAndPositions();
				for (int order : pieceZorder.keySet()) {
					pieceZorder.get(order).toFront();
				}
			}
		};
		toIsland.addPiece(piece, onFinish);
		toIsland.model.addPiece(piece.model);
		piece.toFront();
	}

	private IslandTile getIslandTileFromModel(Island model) {
		for (IslandTile islandTile : islandTiles){
			if (islandTile.model == model){
				return islandTile;
			}
		}
		return null;
	}

	public void c_moveTreasuryCardFromPileToPlayer(Card card, PlayerCardHolder playerHolder) {
		if (!treasuryBase.containsInPile(card)) {
			throw new IllegalArgumentException("treasuryBase does not have the card " + card);
		}
		treasuryBase.removeCardFromPile(card);
		playerHolder.addCard(card);
	}

	public void c_moveTreasuryCardFromPlayerToDiscardPile(Card card, PlayerCardHolder playerHolder) {
		if (!playerHolder.contains(card)) {
			throw new IllegalArgumentException("PlayerCardHolder does not have the card " + card);
		}
		treasuryBase.addCardtoDiscardPile(card);
		playerHolder.removeCard(card);
	}

	@Override
	public void c_setIslandComponentsSelectable(boolean selectable) {
		for (IslandTile tile : islandTiles) {
			tile.setSelectable(selectable);
		}
	}

	public void c_showMessagePanel(final String message) {
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), new KeyValue(
				msgPanel.layoutYProperty(), 800)));
		timeline.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				msgPanel.showMessage(message);
			}
		});
		timeline.play();
	}

	public void c_hideMessagePanel() {
		new Timeline(new KeyFrame(Duration.millis(200), new KeyValue(msgPanel.layoutYProperty(),
				1000))).play();
	}

	public void c_popUp() {
		EventHandler<ActionEvent> onClose = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				IslandScreen.this.getChildren().remove(event.getSource());
			}
		};
		PopUpScreen popUp = new PopUpScreen(new FloodCardDraw(mainController, config),
				IslandScreen.this, onClose);
		getChildren().add(popUp);
		popUp.show();
	}

	public void setGameController(GameController gameController) {
		this.gameController = gameController;
		
	}

}
