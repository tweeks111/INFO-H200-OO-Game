package Model.tiles;

public class UnremovableTile extends Tile{

	public UnremovableTile(int x, int y) {
		super(x, y, 0);
	}

	@Override
	public boolean isObstacle(){
		return true;
	}

}
