package cls.island.model;

import java.util.ArrayList;
import java.util.List;

import cls.island.utils.LocCalculator.Grid;

public class Island {
	
	private Grid grid;
	
	List<PieceModel> pieces = new ArrayList<>();
	
	
	private String name;
	private boolean flooded;
	private boolean sunk;

	public Island(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isFlooded() {
		return flooded;
	}
	public void setFlooded(boolean flooded) {
		this.flooded = flooded;
	}
	public boolean isSunk() {
		return sunk;
	}
	public void setSunk(boolean sunk) {
		this.sunk = sunk;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Island other = (Island) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public Grid getGrid(){
		return grid;
	}
	
	public void setGrid(int row, int col){
		grid = new Grid(row, col);
	}
	
	public void setGrid(Grid grid){
		this.grid = grid;
	}
	
	public void addPiece(PieceModel piece){
		pieces.add(piece);
	}
	
	public void removePiece(PieceModel piece){
		pieces.remove(piece);
	}
	
	public List<PieceModel> getPieces(){
		return new ArrayList<>(pieces);
	}
	
	

}
