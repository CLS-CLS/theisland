package cls.island.model;


public class LevelDesign {
	public static final int COLUMNS = 6 ;
	public static final int ROWS = 6 ;
	

	private final String[] level1 = new String[] {"0 0 X X 0 0" , "0 X X X X 0" , "X X X X X X" , "X X X X X X",  "0 X X X X 0" , "0 0 X X 0 0", "0 0 0 0 0 0"};

	private String[][] levels = { level1 };

	public LevelDesign() {
	}

	boolean[][] getLevelAsArray(int level) {
		String[] levelStr = levels[level];
		boolean[][] arrayLevel = new boolean[ROWS][COLUMNS];
		for (int row = 0; row < ROWS; row++) {
			String cols[] = levelStr[row].split(" ");
			for (int col = 0; col < COLUMNS; col++) {
				arrayLevel[row][col] = cols[col].equals("X");
			}
		}
		return arrayLevel;
	}

}
