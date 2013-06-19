package cls.island.view.component.treasurebag;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import cls.island.view.component.treasury.card.Type;
import cls.island.view.component.treasury.card.Type.Ability;

public class TreasureBag {

	List<Type> acquiredTreasures = new ArrayList<>();
	private TreasuryBagView view;

	public TreasureBag(Image earth, Image chalice, Image fire, Image wind) {
		this(earth, chalice, fire, wind, new Type[] {});
	}

	public TreasureBag(Image earth, Image chalice, Image fire, Image wind, Type... treasures) {
		for (Type treasure : treasures) {
			addTreasure(treasure);
		}
		view = new TreasuryBagView(this, earth, chalice, fire, wind);
	}

	public List<Type> getAcquiredTreaures() {
		return new ArrayList<>(acquiredTreasures);
	}

	/**
	 * adds a treasure that has been acquired in the treasureBag.
	 * 
	 * @param treasure
	 */
	public final void addTreasure(Type treasure) {
		if (treasure.getAbility() != Ability.TREASURE)
			throw new IllegalArgumentException(treasure + " is not a treasure");
		if (acquiredTreasures.contains(treasure))
			throw new IllegalArgumentException(treasure + " already collected");
		acquiredTreasures.add(treasure);
	}

	/**
	 * convinient method. Checks if the specified treasure is already collected.
	 * 
	 * @param treasure
	 *            the treasure to check if it's collected
	 * @return true if the treasure is collected, false otherwise
	 */
	public boolean isTreasureCollected(Type treasure) {
		if (treasure.getAbility() != Ability.TREASURE)
			throw new IllegalArgumentException(treasure + " is not a treasure");
		return acquiredTreasures.contains(treasure);
	}

	public TreasuryBagView getComponent() {
		return view;
	}

}
