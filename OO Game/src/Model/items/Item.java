package Model.items;

import Model.GameObject;

public abstract class Item extends GameObject{

	public Item(int x, int y, int color) {
		super(x, y, color);
	}

	@Override
	public boolean isObstacle() {
		return false;
	}

}
