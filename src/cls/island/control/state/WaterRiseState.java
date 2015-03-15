package cls.island.control.state;

import java.util.Arrays;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import cls.island.control.GameController;
import cls.island.control.GameController.ButtonAction;
import cls.island.control.GameState;
import cls.island.model.GameModel;
import cls.island.model.player.Player;
import cls.island.utils.ViewUtils;
import cls.island.view.component.island.Island;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.TreasuryCardView;
import cls.island.view.component.treasury.card.Type;
import cls.island.view.screen.IslandComponent;
import cls.island.view.screen.IslandScreen;

public class WaterRiseState implements GameState {

	private final GameController gameController;
	private final IslandScreen islandScreen;
	private final GameState fromState;
	private final GameModel gameModel;

	public WaterRiseState(GameController gameController, IslandScreen islandScreen,
			GameModel gameModel, GameState fromState) {
		this.gameController = gameController;
		this.islandScreen = islandScreen;
		this.gameModel = gameModel;
		this.fromState = fromState;
	}

	@Override
	public GameState mouseClicked(MouseEvent event) {
		// Cancel if Right- Click

		if (event.getButton() == MouseButton.SECONDARY) {
			return cancel();
		}
		// Do nothing for others clicks except primary
		if (event.getButton() != MouseButton.PRIMARY) {
			return null;
		}

		// check clicked on treasury card and that this card belongs to a playerType
		TreasuryCard selectedCard = null;
		IslandComponent component = ViewUtils.findIslandComponent((Node) event.getTarget());

		if (component != null && component instanceof TreasuryCardView) {
			selectedCard = ((TreasuryCardView) component).getParentModel();
		}
		if (selectedCard == null || Type.SANDBAGS != selectedCard.getType()) {
			return null;
		}

		boolean belongsToPlayer = false;
		for (Player player : gameModel.getPlayers()) {
			for (TreasuryCard card : player.getTreasuryCards()) {
				if (card == selectedCard) {
					belongsToPlayer = true;
					break;
				}
			}
		}
		if (!belongsToPlayer) {
			return null;
		}

		islandScreen.c_hideMessagePanel();
		disableClickableEffect(selectedCard.getComponent());
		return new UseShoreUpCardState(gameController, islandScreen, gameModel, selectedCard,
				WaterRiseState.this);
	}

	private void disableClickableEffect(TreasuryCardView... excludedCards) {
		List<TreasuryCardView> excludedCardsAsList = Arrays.asList(excludedCards);
		for (Player player : gameModel.getPlayers()) {
			for (TreasuryCard card : player.getTreasuryCards()) {
				if (card.getType() == Type.SANDBAGS
						&& !excludedCardsAsList.contains(card.getComponent())) {
					card.getComponent().setValidToCkickEffect(false);
				}
			}
		}
	}

	private GameState cancel() {
		disableClickableEffect();
		islandScreen.c_hideMessagePanel();
		return fromState;
	}

	@Override
	public GameState buttonPressed(ButtonAction action) {
		// TODO change to OK
		GameState returnState = null;
		if (action == ButtonAction.NEXT_TURN) {
			returnState = cancel();
		}
		return returnState;
	}

	@Override
	public GameState getFromState() {
		return fromState;
	}

	@Override
	public GameState start() {
		islandScreen.disableButtons();
		GameState returnState = null;
		if (!playerHasShoreUpCard() || !flooderIslandExists()) {
			returnState = fromState;
		} else {
			islandScreen
					.c_showMessagePanel("Save an Island with a Sandbag\nClick OK (or Right-Click) when ready");
			enableClickableEffect();
		}
		return returnState;
	}

	private void enableClickableEffect() {
		for (Player player : gameModel.getPlayers()) {
			for (TreasuryCard card : player.getTreasuryCards()) {
				if (card.getType() == Type.SANDBAGS) {
					card.getComponent().setValidToCkickEffect(true);
				}
			}
		}
	}

	private boolean flooderIslandExists() {
		boolean floodTileExist = false;
		for (Island island : gameModel.getIslands()) {
			if (island.isFlooded() && !island.isSunk()) {
				floodTileExist = true;
				break;
			}
		}
		return floodTileExist;
	}

	private boolean playerHasShoreUpCard() {
		boolean shoreUpCardsExist = false;
		for (Player player : gameModel.getPlayers()) {
			for (TreasuryCard card : player.getTreasuryCards()) {
				if (card.getType() == Type.SANDBAGS) {
					shoreUpCardsExist = true;
					break;
				}
			}
		}
		return shoreUpCardsExist;
	}

	@Override
	public void end() {
	}

}
