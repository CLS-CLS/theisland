package cls.island.view.component.treasury.card;

import java.util.ArrayList;
import java.util.List;

public enum Type {
	EARTH_STONE(Ability.TREASURE), STATUE_OF_WIND(Ability.TREASURE), CRYSTAL_OF_FIRE(
			Ability.TREASURE),  OCEAN_CHALICE(Ability.TREASURE), HELICOPTER(Ability.AID), SANDBAGS(
			Ability.AID), WATER_RISE(Ability.DANGER);

	public enum Ability {
		TREASURE, DANGER, AID;
	}

	private final Ability ability;

	private Type(Ability ability) {
		this.ability = ability;
	}

	public Ability getAbility() {
		return ability;
	}
	
	public static List<Type> getTypesWithAbility(Ability ability){
		List<Type> specificTypes = new ArrayList<>();
		for (Type type : Type.values()){
			if (type.getAbility() == ability){
				specificTypes.add(type);
			}
		}
		return specificTypes;
	}
	
}