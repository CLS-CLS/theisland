package cls.island.control;

public interface Action {
	
	public void execute();
	
	public GameState revert();

}
