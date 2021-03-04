package Model.items;

import java.util.ArrayList;

import Model.characters.Character;
import Model.observers.Movable;
import Model.observers.MovableObserver;
import Model.observers.Removable;
import Model.observers.RemovableObserver;
import Model.tiles.*;
public class Arrow extends Item implements Movable, Removable, Runnable{
	
	//Listes d'observers
	private ArrayList<RemovableObserver> remObs = new ArrayList<RemovableObserver>();
	private ArrayList<MovableObserver> mObs = new ArrayList<MovableObserver>();
	
	//Listes d'objets
	protected ArrayList<Model.characters.Character> characters;
	protected ArrayList<Tile> tiles;
	
	//Attributs
	private int attack;
	private int dirX;
	private int dirY;
	
	//CONSTRUCTEUR
	
	public Arrow(int x, int y) {
		super(x, y, 5);
		this.attack=1;
	}

	//DEPLACEMENT
	
	public void move(){
	posX+=dirX;
	posY+=dirY;
	
	}
	
	//GETTEURS
	
	public int getDirection(int i){
		int direction = 0;
		if(i==0) direction = dirX;
		if(i==1) direction = dirY;
		return direction;
	}


	//SETTEURS
	
	public void setObjects(ArrayList<Tile> tiles,ArrayList<Model.characters.Character> characters){
		this.characters=characters;
		this.tiles=tiles;
	}
	
	public void setDirection(int dirx,int diry){
		this.dirX=dirx;
		this.dirY=diry;
	}
	
	//THREAD
	
	@Override
	public void run() {
		try{
			boolean canMove=true;
			while(canMove){
				/* 
				 * La flèche va avancer jusqu'à ce qu'elle recontre un mur ou un personnages
				 * Elle se plante dans les cases indestructibles et est à nouveau ramassable
				 * Mais elle fais perdre de la vie aux cases destructibles et aux personnages
				 */
				for(Tile tile : tiles){
					if(tile.isAtPosition(posX+dirX, posY+dirY) && !(tile instanceof Spikes || tile instanceof DrugTile)){
						if(tile instanceof RemovableTile){
							RemovableTile rmtile = (RemovableTile) tile;
							rmtile.isAttacked(attack);
							removableNotifyObserver();
						}
						canMove=false;
						break;
					}
				}
				for(Character character : characters){
					if(character.isAtPosition(posX+dirX, posY+dirY)){
						character.isHurt(attack);
						canMove=false;
						removableNotifyObserver();
						break;
					}
				}
				if(canMove){
					move();
					movableNotifyObserver();
					Thread.sleep(150);
				}
			}
			
		}catch(Exception e){ e.printStackTrace();}
	}
	
/////////////////////////////////////////////////////////////////
	
	@Override
	public void removableAttach(RemovableObserver ro) {
		remObs.add(ro);
	}

	@Override
	public void removableNotifyObserver() {
		for(RemovableObserver ro : remObs){
			ro.remove(this);
		}	

	}
	@Override
	public void movableAttach(MovableObserver mo) {
		mObs.add(mo);
	}

	@Override
	public void movableNotifyObserver() {
		for(MovableObserver mo : mObs){
			mo.moveObs();
		}	
	}

}
