package cls.island.view.screen.popup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import cls.island.control.Config;
import cls.island.view.component.treasury.card.TreasuryCard;
import cls.island.view.component.treasury.card.TreasuryCard.ViewStatus;
import cls.island.view.component.treasury.card.TreasuryCardView;
import cls.island.view.component.treasury.card.Type;
import cls.island.view.component.treasury.pile.TreasuryPile;

public class TreasuryCardsSneakPeek extends PopUpInternal<Void> {

	public TreasuryCardsSneakPeek(TreasuryPile treasuryPile, TreasuryPile.PileType pileType) {
		List<TreasuryCard> treasuryCards = treasuryPile.getTreasuryCards(pileType);
		Map<Type, Integer> cardsPerType = new HashMap<>();
		Arrays.stream(Type.values()).forEach((t) -> cardsPerType.put(t, 0));
		treasuryCards.stream().map((c) -> c.getType()).forEach((t) -> cardsPerType.put(t, cardsPerType.get(t) + 1));
		Group skin = createSkin(cardsPerType);
		this.getChildren().addAll(skin);
	}
	
	private Group createSkin(Map<Type, Integer> mapsPerType){
		Group root = new Group();
		List<Group> subGroups = createSubGroups(mapsPerType);
		FlowPane layout = new FlowPane();
		BorderPane bp = new BorderPane(layout);
		BorderPane.setMargin(layout, new Insets(30, 30, 30, 30));
		layout.setVgap(20);
		layout.setHgap(30);
		
		switch (subGroups.size()) {
		case 4:
			layout.setPrefWrapLength(320);
			break;
		default :
			layout.setPrefWrapLength(450);
		}
		
		layout.getChildren().addAll(subGroups);
		bp.getStyleClass().add("pile-sneek-pick-pane");
		root.getChildren().add(bp);
		return root;
	}

	private List<Group> createSubGroups(Map<Type, Integer> mapsPerType) {
		Config config = Config.getInstance();
		List<Group> list = new ArrayList<>();
		for (Type type : Type.values()){
			Integer value = mapsPerType.get(type);
			if (value == null || value == 0){
				continue;
			}
			Group root = new Group();
			HBox hBox = new HBox();
			root.getChildren().add(hBox);
			TreasuryCardView trCardView = new TreasuryCard(config.getTreasureCard(type),
					config.islandBackCard, new TreasuryCard.Model(type, ViewStatus.FACE_UP),
					config.treasuryCardUseImg, config.treasuryCardDiscardImg).getComponent();
			Text text = createText(value);
			hBox.getChildren().addAll(trCardView, text);
			hBox.setAlignment(Pos.CENTER_LEFT);
			list.add(root);
		}
		return list;
	}

	private Text createText(int value) {
		Text text = new Text(" \u00D7"+ value);
		text.getStyleClass().add("pile-sneek-pick-text");
		return text;
	}



	@Override
	public Void getRusults() {
		return null;
	}

}
