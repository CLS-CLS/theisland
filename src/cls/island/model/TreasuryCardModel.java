package cls.island.model;

public class TreasuryCardModel {

	public static enum ViewStatus{
		FACE_UP, FACE_DOWN;
	}
	
	public static enum Type {
		
		WATER_RISE(Ability.DANGER), 
		EARTH_STONE(Ability.TREASURE), 
		STATUE_OF_WIND(Ability.TREASURE),
		CRYSTAL_OF_FIRE(Ability.TREASURE), 
		OCEAN_CHALICE(Ability.TREASURE), 
		HELICOPTER(Ability.AID),
		SANDBAGS(Ability.AID);
		
		enum Ability{
			TREASURE, DANGER, AID;
		}
		
		private final Ability ability;
		private Type(Ability ability) {
			this.ability = ability;
		}

		public Ability getAbility() {
			return ability;
		}
	}
	
	private final Type type;
	
	private ViewStatus viewStatus; 
	
	public TreasuryCardModel(Type type) {
		this.type = type;
		setViewStatus(ViewStatus.FACE_UP);
	}

	public Type getType() {
		return type;
	}

	public ViewStatus getViewStatus() {
		return viewStatus;
	}

	public void setViewStatus(ViewStatus viewStatus) {
		this.viewStatus = viewStatus;
	}
	
	public boolean isFaceUp(){
		return viewStatus == ViewStatus.FACE_UP;
	}
	
	
}
