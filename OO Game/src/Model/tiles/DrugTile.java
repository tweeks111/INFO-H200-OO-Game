package Model.tiles;

public class DrugTile extends Tile{

	public DrugTile(int x, int y) {
		super(x, y, 7);
		
	}

	@Override
	public boolean isObstacle() {
		return false;
	}
	
}
