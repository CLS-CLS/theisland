package cls.island.control.state;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
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

	public UseOrDiscardCardState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel, GameState fromState) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.fromState = fromState;

	}

	@Override
	public GameState mouseClicked(MouseEvent event) {
		return null;
	}

	@Override
	public GameState buttonPressed(ButtonAction action) {
		Player player = gameModel.getCurrentTurnPlayer();
		switch (action) {
		case USE:
			return useCard(player);
		case DISCARD:
			return discardCard(player);
		}
		
		return null;
	}

	private GameState discardCard(Player player) {
		for (TreasuryCard playerCard : player.getTreasuryCards()) {
			playerCard.getComponent().disableUseDiscard();
		}
		gameModel.discardCard(player, selectedCard);
		islandScreen.c_discardPlayerCard(player.getBase().getComponent(),
				selectedCard.getComponent());
		islandScreen.c_hideMessagePanel();
		return fromState;
	}

	private GameState useCard(Player player) {
		for (TreasuryCard playerCard : player.getTreasuryCards()) {
			playerCard.getComponent().disableUseDiscard();
		}
		islandScreen.c_hideMessagePanel();
		switch (selectedCard.getType()) {
		case SANDBAGS:
			return new UseShoreUpCardState(gameController, islandScreen,
					gameModel, selectedCard, UseOrDiscardCardState.this);
		case HELICOPTER:
			return new UseHelicopterCardState(gameController,
					islandScreen, gameModel, selectedCard, UseOrDiscardCardState.this);
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
		for (TreasuryCard treasuryCard : gameModel.getCurrentTurnPlayer().getTreasuryCards()) {
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

}
