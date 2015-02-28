package cls.island.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cls.island.control.Options;
import cls.island.control.Options.PlayerType;
import cls.island.model.IslandGrid.Direction;
import cls.island.model.TreasuryCardModel.Type;
import cls.island.model.TreasuryCardModel.Type.Ability;

public class GameModel {

	private static final int SANDBAG_CARD_QUANTITY = 2;
	private static final int HELICOPTER_CARD_QUANTITY = 3;
	private static final int WATER_RISE_CARD_QUANTITY = 3;
	private static final int TREASURY_CARD_QUANTITY = 5;
	

	private enum CardNames {
		BreakersBridge, BronzeGate, CaveOfEmbers, CaveOfShadows, CliffsOfAbandon, CopperGate, 
		CoralPalace, CrimsonForest, DunesOfDeception, FoolsLanding, GoldGate, HowlingGarden,
		IronGate, LostLagoon, MistyMarsh, Observatory, PhantomRock, SilverGate, 
		TempleOfTheMoon, TempleOfTheSun, TidalPalace, TwilightHollow, Watchtower, WhisperingGarden;
	}
	
	private List<TreasuryCardModel> treasuryCards = new ArrayList<>();
	private List<PieceModel> pieces = new ArrayList<>();
	private List<Island> islands = new ArrayList<>();
	private IslandGrid<Island> islandGrid = new IslandGrid<>(6,6);
	

	public GameModel(Options options) {
		initIslands();	
		initPlayers(options.getPlayers());
		initTreasuryCards();
	}
	
	
	private void initTreasuryCards() {
		for (TreasuryCardModel.Type type : TreasuryCardModel.Type.values()){
			int repeats = 0;
			if (type.getAbility() == Ability.TREASURE){
				repeats = TREASURY_CARD_QUANTITY;
			}else if (type == Type.WATER_RISE){
				repeats =WATER_RISE_CARD_QUANTITY;
			}else if (type == Type.HELICOPTER){
				repeats = HELICOPTER_CARD_QUANTITY;
			}else if (type == Type.SANDBAGS){
				repeats= SANDBAG_CARD_QUANTITY;
			}
			for (int i=0;i<repeats;i++){
				treasuryCards.add(new TreasuryCardModel(type));
			}
		}
	}


	private void initPlayers(List<PlayerType> list) {
		for (PlayerType player : list){
			pieces .add(new PieceModel(player.name()));
		}
	}


	//TODO maybe the islands should be passed in Constructor from external controller
	private void initIslands() {
		for (CardNames names  :CardNames.values()){
				islands.add(new Island(names.name()));
		}
	}
	
	public void randomizeBoard(){
		setUpRandomTiles();
	}
	
		
	private void setUpRandomTiles() {
		Collections.shuffle(islands);
		int row = 0;
		int colStart = 2;
		int colEnd = 3;
		int col = colStart;
		for (int i = 0; i < 12; i++) {
			if (col > colEnd) {
				colStart--;
				colEnd++;
				row++;
				col = colStart;
			}
			islands.get(i).setGrid(row, col);
			islandGrid.addElement(islands.get(i), row, col);
			col++;
		}
		col = colStart;
		row++;
		for (int i = 12; i < 24; i++) {
			if (col > colEnd) {
				colStart++;
				colEnd--;
				row++;
				col = colStart;
			}
			islands.get(i).setGrid(row, col);
			islandGrid.addElement(islands.get(i), row, col);
			col++;
		}
		
	}


	public Island getIsland(String name) {
		for (Island island : islands){
			if (island.getName().equals(name)){
				return island;
			}
		}
		throw new IllegalArgumentException("Islans with name " + name + " does not Exists");
	}
	
	
	public List<TreasuryCardModel> getTreasuryCards() {
		return new ArrayList<TreasuryCardModel>(treasuryCards);
	}


	public List<PieceModel> getPieces(){
		return new ArrayList<PieceModel>(pieces);
	}
	
	public boolean isAdjacentIslands(Island from, Island to){
		Direction[] directions  = new Direction[]{Direction.LEFT, Direction.UP, Direction.RIGHT, Direction.DOWN};
		return islandGrid.isAdjacent(from, to, directions);
	}

}
