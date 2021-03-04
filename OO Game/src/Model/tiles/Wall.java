package Model.tiles;

public class Wall extends RemovableTile{
	
	
	public Wall(int x, int y) {
		super(x, y, 1);
		setLife(8);
	}

	@Override
	public boolean isObstacle() {
		return true;
	}

	@Override
	public String getName() {
		return "Wall";
	}

}
