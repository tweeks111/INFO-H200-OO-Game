package Model.tiles;

import Model.observers.OpenDoor;
import Model.observers.OpenDoorObserver;

public class Door extends Tile implements OpenDoorObserver{
	
	private boolean opened = false;
	
	//CONSTRUCTEURS  -> par défaut la porte est fermée
	
	public Door(int x, int y) {
		super(x, y, 2);
		
	}
	
	public Door(int x, int y, boolean opened) {
		super(x, y, 2);
		this.opened=opened;
		this.colorTile=3;
	}
	
	//VERIFICATION
	
	@Override
	public boolean isObstacle() {
		return !opened;
	}

///////////////////////////////////////////////////
	
	@Override
	public void openDoor(OpenDoor od) {
		this.opened=true;
		this.colorTile=3;
	}

}
