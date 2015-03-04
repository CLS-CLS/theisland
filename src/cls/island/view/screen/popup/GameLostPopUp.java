package cls.island.view.screen.popup;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Translate;
import cls.island.control.Config;
import cls.island.model.LooseCondition;
import cls.island.utils.ButtonFactory;
import cls.island.view.component.treasury.card.Type;

public class GameLostPopUp extends PopUpInternal<Object> {

	public GameLostPopUp(LooseCondition condition, Object[] infos) {
		ImageView background = new ImageView(Config.getInstance().deepOcean);
		getChildren().add(background);
		Label label = new Label();
		label.getStyleClass().add("white-text-button");

		switch (condition) {
		case FOOLS_LANDING_LOST:
			label.setText("Fool's Landing lost");

			break;
		case MAX_WATER_LEVEL_REACHED:
			label.setText("Max water level reached");
			break;
		case PLAYER_SUNK:
			label.setText(infos[0] + " Player was drawn after the \nisland sunk");
			break;
		case TREASURE_SUNK:
			label.setText("Treasure was lost");
			label.getTransforms().add(new Translate(100, 0));
			ImageView lostImage = new ImageView(Config.getInstance().getTreasureImage(
					Type.valueOf(infos[0].toString())));
			lostImage.getTransforms().add(new Translate(150, 50));
			getChildren().add(lostImage);
			break;

		default:
			break;
		}
		getChildren().add(label);
		Button okButton = ButtonFactory.genButton("OK");
		okButton.getTransforms().add(new Translate(0, 300));
		getChildren().add(okButton);
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				GameLostPopUp.this.close();
				
			}
		});

	}

	@Override
	public Object getRusults() {
		return null;
	}

}
