package cls.island.view.component.treasury.card;

import javafx.scene.image.Image;

public class TreasuryCard {
	private final Model model;
	private final TreasuryCardView treasuryCardView;

	public static enum ViewStatus{
		FACE_UP, FACE_DOWN;
	}
	
		
	public static class Model {
		private final Type type;
		private ViewStatus viewStatus; 
		
		public Model(Type type, ViewStatus viewStatus) {
			this.type = type;
			this.viewStatus = viewStatus;
		}

		public Type getType() {
			return type;
		}

		public ViewStatus getViewStatus() {
			return viewStatus;
		}
	}
	
	
	public TreasuryCard(Image faceUp, Image faceDown, Model model, Image use, Image cancel){
		this.model = model;
		treasuryCardView = new TreasuryCardView(faceUp, faceDown, this, use, cancel);
		
	}
	
	public Type getType(){
		return model.type;
	}
	
	public void setViewStatus(ViewStatus viewStatus) {
		this.model.viewStatus = viewStatus;
		treasuryCardView.setFaceUp(isFaceUp());
	}
	
	public boolean isFaceUp(){
		return model.getViewStatus() == ViewStatus.FACE_UP;
	}

	public TreasuryCardView getComponent() {
		return treasuryCardView;
	}

}
