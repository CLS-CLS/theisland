package cls.island.control.state;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.action.RevertableAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.model.player.Player;
import cls.island.utils.ViewUtils;
import cls.island.view.component.island.Island;
import cls.island.view.component.island.IslandView;
import cls.island.view.component.piece.Piece;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.screen.IslandComponent;
import cls.island.view.screen.IslandScreen;

public class UseHelicopterCardStepTwoState implements GameState {

	private final GameState fromState;
	private final Island takeOffIsland;
	private final TreasuryCard card;
	private final IslandScreen islandScreen;
	private final GameController gameController;
	private final GameModel gameModel;

	public UseHelicopterCardStepTwoState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel, TreasuryCard card, Island takeOffIsland, GameState fromState) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.card = card;
		this.takeOffIsland = takeOffIsland;
		this.fromState = fromState;
	}

	@Override
	public GameState mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY) {
			return goToState(fromState);
		}

		// various checks --start
		if (event.getButton() != MouseButton.PRIMARY) {
			return null;
		}
		IslandComponent islandComponent = ViewUtils.findIslandComponent((Node) event.getTarget());
		if (islandComponent == null)
			return null;
		if (!(islandComponent instanceof IslandView))
			return null;

		Island island = ((IslandView) islandComponent).getParentModel();
		if (island.equals(takeOffIsland))
			return null;
		if (island.isSunk())
			return null;
		// various checks -- end

		if (takeOffIsland.getPieces().size() == 1) {
			return flyToIsland(island, takeOffIsland.getPieces().get(0));
		} else {
			List<Piece> result = islandScreen
					.c_showSelectPieceToFlyPopUp(takeOffIsland.getPieces());
			if (result.size() == 0) {
				return goToState(fromState);
			} else {
				return flyToIsland(island, result.toArray(new Piece[result.size()]));
			}
		}
	}
	
	/**
	 * possible ways to come here 
	 * 1) normal->trade->useDiscard->heli->heli2
	 * 2) normal->drawCard->useDiscard->heli->heli2
	 * 3) normal->heli->heli2
	 * @return
	 */
	private GameState aPreviousState() {
		GameState fromFromState = fromState.getFromState();
		if (fromFromState instanceof UseOrDiscardCardState) {
			if (fromFromState.getFromState() instanceof TradeCardState) {
				assert fromFromState.getFromState().getFromState() instanceof NormalState : "was "
						+ fromFromState.getFromState().getFromState().getClass();
				return fromFromState.getFromState().getFromState(); //if from trade ->use-discard -> heli->heli2 ==> goto normal
			}
			return fromFromState.getFromState(); //if drawCard->useDiscard->heli->heli2 ==>go to drawCard
		} else {
			return fromFromState; //if normal->heli->heli2 ==> go to normal
		}
	}
	
	/**
	 * possible ways to come here 
	 * 1) normal->trade->useDiscard->heli->heli2 / go to useDiscard
	 * 2) normal->drawCard->useDiscard->heli->heli2 / not applicable
	 * 3) normal->heli->heli2 / go to normal
	 * @return
	 */
	private GameState findRevertToState() {
		//happens to be ok for all possibilities!
		return fromState.getFromState();
	}

	private GameState flyToIsland(final Island island, final Piece... piece) {
		gameController.executeAction(new RevertableAction() {

			Player cardHolder = ViewUtils.findPlayerHoldingCard(gameModel, card);
			GameState revertToState = findRevertToState();

			@Override
			public GameState revert() {
				gameModel.undiscardCard(cardHolder, card);
				islandScreen.c_moveTreasuryCardFromPileToPlayer(card.getComponent(), cardHolder
						.getBase().getComponent());
				for (Piece currentPiece : piece) {
					int index = gameController.getPlayerWithPiece(currentPiece).setToIsland(
							takeOffIsland);
					islandScreen.c_movePiece(currentPiece.getComponent(),
							takeOffIsland.getComponent(), index);
				}
				return goToState(revertToState);
			}

			

			@Override
			public void execute() {
				for (Piece currentPiece : piece) {
					int index = gameController.getPlayerWithPiece(currentPiece).setToIsland(island);
					islandScreen.c_movePiece(currentPiece.getComponent(), island.getComponent(),
							index);
				}

				gameModel.discardCard(cardHolder, card);
				islandScreen.c_discardPlayerCard(cardHolder.getBase().getComponent(),
						card.getComponent());
			}
		});

		return goToState(aPreviousState());
	}

	private GameState goToState(GameState state) {
		islandScreen.c_hideMessagePanel();
		takeOffIsland.getComponent().setValidToCkickEffect(false);
		card.getComponent().setValidToCkickEffect(false);
		return state;
	}

	@Override
	public GameState buttonPressed(ButtonAction action) {
		throw new UnsupportedOperationException();
	}

	@Override
	public GameState getFromState() {
		throw new UnsupportedOperationException();
	}

	@Override
	public GameState start() {
		islandScreen.c_showMessagePanel("Select Island to Fly to\nRight Click to cancel");
		card.getComponent().setValidToCkickEffect(true);
		takeOffIsland.getComponent().setValidToCkickEffect(true);
		return null;
	}

	@Override
	public void end() {
	}

}
