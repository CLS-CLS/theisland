package cls.island.control.state;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.action.RevertableAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.model.player.Player;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.Type;
import cls.island.view.screen.IslandScreen;

public class UseOrDiscardCardState implements GameState {

	private final GameController gameController;
	private final IslandScreen islandScreen;
	private final GameModel gameModel;
	private final GameState fromState;
	private TreasuryCard selectedCard;
	private final Player playerToDiscardCards;

	/**
	 * Constructor to create a UseOrDiscardCardState for the cards of the currentTurn player
	 * @param gameController
	 * @param islandScreen
	 * @param gameModel
	 * @param fromState
	 */
	public UseOrDiscardCardState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel, GameState fromState) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.fromState = fromState;
		this.playerToDiscardCards = gameModel.getCurrentTurnPlayer();
	}

	/**
	 * 
	 * @param playertToDiscardCards the player who holds more cards than allowed
	 * @param gameController
	 * @param islandScreen
	 * @param gameModel
	 * @param fromState
	 */
	public UseOrDiscardCardState(Player playertToDiscardCards, GameController gameController,
			IslandScreen islandScreen, GameModel gameModel, GameState fromState) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.fromState = fromState;
		this.playerToDiscardCards = playertToDiscardCards;
	}

	@Override
	public GameState mouseClicked(MouseEvent event) {
		return null;
	}

	@Override
	public GameState buttonPressed(ButtonAction action) {
		switch (action) {
		case USE:
			return useCard();
		case DISCARD:
			return discardCard();
		}

		return null;
	}

	private GameState discardCard() {
		end();
		gameController.executeAction(new RevertableAction() {

			@Override
			public GameState revert() {
				gameModel.undiscardCard(playerToDiscardCards, selectedCard);
				islandScreen.c_moveTreasuryCardFromPileToPlayer(selectedCard.getComponent(),
						playerToDiscardCards.getBase().getComponent());
				return UseOrDiscardCardState.this;
			}

			@Override
			public void execute() {
				gameModel.discardCard(playerToDiscardCards, selectedCard);
				islandScreen.c_discardPlayerCard(playerToDiscardCards.getBase().getComponent(),
						selectedCard.getComponent());
			}
		});

		return fromState;
	}

	private GameState useCard() {
		end();
		switch (selectedCard.getType()) {
		case SANDBAGS:
			return new UseShoreUpCardState(gameController, islandScreen, gameModel, selectedCard,
					UseOrDiscardCardState.this);
		case HELICOPTER:
			return new UseHelicopterCardState(gameController, islandScreen, gameModel,
					selectedCard, UseOrDiscardCardState.this);
		}
		return null;
	}

	private class UseHandler implements EventHandler<ActionEvent> {

		private final TreasuryCard card;

		public UseHandler(TreasuryCard card) {
			this.card = card;
		}

		@Override
		public void handle(final ActionEvent event) {
			UseOrDiscardCardState.this.selectedCard = card;
			gameController.buttonPressed(ButtonAction.USE);
		}
	}

	private class DiscardHandler implements EventHandler<ActionEvent> {

		private final TreasuryCard card;

		public DiscardHandler(TreasuryCard card) {
			this.card = card;
		}

		@Override
		public void handle(final ActionEvent event) {
			UseOrDiscardCardState.this.selectedCard = card;
			gameController.buttonPressed(ButtonAction.DISCARD);

		}

	}

	@Override
	public GameState getFromState() {
		return fromState;
	}

	@Override
	public GameState start() {
		islandScreen.disableButtons();
		for (TreasuryCard treasuryCard : playerToDiscardCards.getTreasuryCards()) {
			if (treasuryCard.getType().getAbility() == Type.Ability.AID) {
				treasuryCard.getComponent().enableUseDiscard(new UseHandler(treasuryCard),
						new DiscardHandler(treasuryCard));
			} else {
				treasuryCard.getComponent()
						.enableUseDiscard(null, new DiscardHandler(treasuryCard));
			}
		}
		islandScreen.c_showMessagePanel("Select a card to Use or Discard");
		return null;
	}

	@Override
	public void end() {
		for (TreasuryCard playerCard : playerToDiscardCards.getTreasuryCards()) {
			playerCard.getComponent().disableUseDiscard();
		}
		islandScreen.c_hideMessagePanel();
	}

}
