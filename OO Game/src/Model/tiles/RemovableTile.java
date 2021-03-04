package Model.tiles;

import java.util.ArrayList;

import Model.observers.Removable;
import Model.observers.RemovableObserver;

public abstract class RemovableTile extends Tile implements Removable{
	private ArrayList<RemovableObserver> observers = new ArrayList<RemovableObserver>();
	
	private int blockLife;
	public RemovableTile(int x, int y, int color) {
		super(x, y, color);
	}
	
	
	public void isAttacked(int attack){
		blockLife-= attack;
		System.out.println("Life of "+this.getName()+" : "+blockLife);
		if(blockLife<=0){
			removableNotifyObserver();
		}
	}
	
	public void setLife(int life){
		this.blockLife=life;
	}
	
	public int getBlockLife(){
		return blockLife;
	}
	
	public abstract String getName();
//////////////////////////////////////////////////////////////////////////
	@Override
		public void removableAttach(RemovableObserver ro) {
		observers.add(ro);
	}

	@Override
	public void removableNotifyObserver() {
		for(RemovableObserver ro : observers){
			ro.remove(this);
		}
	}
	

	
	@Override
	public boolean isObstacle() {
		return true;
	}

}
