package cls.island.view.screen;

import cls.island.utils.LocCalculator.Loc;

public interface IslandComponent {
	
	public Loc getLoc();
	
	public void translate(Loc loc);

}
