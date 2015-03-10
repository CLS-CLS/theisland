package cls.island.view.component.treasury.card;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import cls.island.view.component.AbstractView;

public class TreasuryCardView extends AbstractView<TreasuryCard> {
	private ImageView faceUpView;

	private ImageView faceDownView;

	public final TreasuryCard model;
	private final Image useImg;
	private final Image discardImg;
	private UseDiscardComponent useDiscardComponent;
	
	public TreasuryCardView(Image islandCard, Image backImage, TreasuryCard model, Image useImg,
			Image discardImg) {
		super(false, model);
		this.useImg = useImg;
		this.discardImg = discardImg;
		faceUpView = new ImageView(islandCard);
		faceDownView = new ImageView(backImage);
		getChildren().add(new ImageView(islandCard));
		this.model = model;
		useDiscardComponent = new UseDiscardComponent();
	}

	public void setFaceUp(final boolean faceUp) {
		if (faceUp && !getChildren().contains(faceUpView)) {
			getChildren().add(faceUpView);
			getChildren().remove(faceDownView);
		}
		if (!faceUp && !getChildren().contains(faceDownView)) {
			getChildren().add(faceDownView);
			getChildren().remove(faceUpView);
		}

	}

	public void enableUseDiscard(EventHandler<ActionEvent> inUse,
			EventHandler<ActionEvent> inDiscard) {
		setSelectable(false);
		int displayed = inUse != null ? 1 : 0;
		useDiscardComponent.useButton.setOpacity(displayed);
		useDiscardComponent.useButton.setOnAction(inUse);
		displayed = inDiscard != null ? 1 : 0;
		useDiscardComponent.discardButton.setOnAction(inDiscard);
		useDiscardComponent.discardButton.setOpacity(displayed);
		getChildren().add(useDiscardComponent);

	}

	/**
	 * Removes the buttons added in the card
	 * in order to select if the card will be 
	 * discarded or used.
	 */
	public void disableUseDiscard() {
		getChildren().remove(useDiscardComponent);
		setSelectable(true);
	}

	private class UseDiscardComponent extends Parent {

		Button useButton = new Button("", new ImageView(useImg));
		Button discardButton = new Button("", new ImageView(discardImg));

		public UseDiscardComponent() {
			getChildren().add(useButton);
			getChildren().add(discardButton);
			useButton.setTranslateX(-5);
			useButton.setTranslateY(5);
			discardButton.setTranslateX(30);
			discardButton.setTranslateY(5);
			discardButton.getStyleClass().add("treasury-use-discard-button");
			useButton.getStyleClass().add("treasury-use-discard-button");
		}

	}

}
