package cls.island.control.action;

import cls.island.control.GameState;

public interface Action {
	
	public boolean isRevartable();
	
	public void execute();
	
	public GameState revert();

}
