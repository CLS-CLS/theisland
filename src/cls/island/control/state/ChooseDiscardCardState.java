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

public class ChooseDiscardCardState implements GameState {

	private final GameController gameController;
	private final IslandScreen islandScreen;
	private final GameModel gameModel;
	private final GameState fromState;

	public ChooseDiscardCardState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel, GameState fromState) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.fromState = fromState;
		
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		
	}

	@Override
	public void buttonPressed(ButtonAction action) {
		
	}

	private class UseHandler implements EventHandler<ActionEvent> {

		private final TreasuryCard card;

		public UseHandler(TreasuryCard card) {
			this.card = card;
		}

		@Override
		public void handle(final ActionEvent event) {
			new Thread(new Runnable(){
				public void run(){
					Player player = gameModel.getCurrentTurnPlayer();
					for (TreasuryCard playerCard : player.getTreasuryCards()) {
						playerCard.getComponent().disableUseDiscard();
					}
					islandScreen.c_hideMessagePanel();
					switch (card.getType()) {
					case SANDBAGS:
						gameController.setGameState(new UseShoreUpCardState(gameController, islandScreen,
								gameModel, card, ChooseDiscardCardState.this));
						break;
					case HELICOPTER:
						gameController.setGameState(new UseHelicopterCardState(gameController,
								islandScreen, gameModel, card, ChooseDiscardCardState.this));
						break;
					}
				
				}
			}).start();
		}
	}

	private class DiscardHandler implements EventHandler<ActionEvent> {

		private final TreasuryCard card;

		public DiscardHandler(TreasuryCard card) {
			this.card = card;
		}

		@Override
		public void handle(final ActionEvent event) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Player player = gameModel.getCurrentTurnPlayer();
					for (TreasuryCard playerCard : player.getTreasuryCards()) {
						playerCard.getComponent().disableUseDiscard();
					}
					gameModel.discardCard(player, card);
					islandScreen.c_discardPlayerCard(player.getBase().getComponent(), card.getComponent());
					islandScreen.c_hideMessagePanel();
					gameController.setGameState(fromState.createGameState());
					
				}
			}).start();
			
		}

	}

	@Override
	public GameState getFromState() {
		return fromState;
	}

	@Override
	public void start() {
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

	}

	@Override
	public GameState createGameState() {
		// TODO Auto-generated method stub
		return null;
	}

}
