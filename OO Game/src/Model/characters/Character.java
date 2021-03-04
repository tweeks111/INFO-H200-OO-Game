package Model.characters;
import java.util.ArrayList;

import Model.GameObject;
import Model.items.Damages;
import Model.items.Item;
import Model.observers.*;
import Model.tiles.RemovableTile;
import Model.tiles.Spikes;
import Model.tiles.Tile;

public abstract class Character extends GameObject implements Removable,GameOver,RemovableObserver{
	
	protected Player player;
	//Listes d'observers
	private ArrayList<RemovableObserver> observers = new ArrayList<RemovableObserver>();
	private ArrayList<GameOverObserver> goObs = new ArrayList<GameOverObserver>();
	
	//Listes d'objets
	protected ArrayList<Model.characters.Character> characters;
	protected ArrayList<Tile> tiles;
	protected ArrayList<Item> itemsOnMap;
	
	//Attributs
	private int[] direction = new int[2];
	
	private int life;
	private int attack;
	protected boolean isDead;
	
	//CONSTRUCTEUR
	
	public Character(int x, int y, int color){
		super(x,y,color);
		this.changeDirection(0,1);
	}
	
	//COMBAT
	
	public void attack(){
		try{
		Damages dmg = new Damages(posX+direction[0],posY+direction[1]);
		dmg.removableAttach(this);
		itemsOnMap.add(dmg);
		Thread thread = new Thread(dmg);
		thread.start();

		for(Character character : characters){
			if(character.isAtPosition(posX+direction[0], posY+direction[1])){
				System.out.println(getName()+" attack : "+getAttack());
				character.isHurt(attack);
			}
		}
		for(Tile tile : tiles){
			if(tile instanceof RemovableTile && tile.isAtPosition(posX+direction[0], posY+direction[1])){
				RemovableTile breakableTile = (RemovableTile) tile;
				System.out.println(getName()+" attack : "+getAttack());
				breakableTile.isAttacked(attack);
			}
		}
		}catch(Exception e){}
	}
	
	public void zoneAttack(){
		for(int i=-1;i<=1;i++){
			for(int j =-1;j<=1;j++){
				if(!(i==0&&j==0)){
					Damages dmg = new Damages(posX+i,posY+j);
					dmg.removableAttach(this);
					itemsOnMap.add(dmg);
					Thread thread = new Thread(dmg);
					thread.start();
				}
			}
		}
		for(Character character:characters){
			if(character instanceof Mob && character.isAtDistance(posX, posY, 1)){
				character.isHurt(attack);
			}
		}
	}
	
	public synchronized void isHurt(int attack){
			
			this.life -=attack;
			System.out.println("Life of  "+this.getName()+" : "+life);
			if(this.life <=0){
				isDead = true;
				removableNotifyObserver();
				System.out.println(this.getName()+" is Dead");
			
			}
	}
	
	//DEPLACEMENT
	
	public void move(int x,int y){
		this.posX=this.posX+x;
		this.posY=this.posY+y;
		for(Tile tile:tiles){
			if(tile instanceof Spikes && this.getPosX()==tile.getPosX() && this.getPosY()==tile.getPosY()){
				Spikes spikes = (Spikes) tile;
				this.isHurt(spikes.getAttack());
			}
		}
	}
	
	//VERIFICATION
	
	public boolean canMove(int posX, int posY){     // /!\ canMove(posX,posY) et pas canMove(x,y)
		
		boolean canMove = true;
		for(Tile tile : tiles){
			if(tile.isAtPosition(posX, posY) && tile.isObstacle()){
				canMove = false;
			}
			if(canMove == false){
				break;
			}
		}
		for(Character character : characters){
			if(character.isAtPosition(posX, posY) && character.isObstacle()){
				canMove = false;
			}
			if(canMove == false){
				break;
			}
		}
		return canMove;
	}
	
	public boolean isAtDistance(int posDestX, int posDestY, int distance){
		boolean isAtDistance = false;
			for(int x =posDestX-distance;x<=posDestX+distance;x++){
				for(int y =posDestY-distance;y<=posDestY+distance;y++){
					if(this.isAtPosition(x, y)){
						isAtDistance = true;
						break;
					}
				}
				if(isAtDistance) break;
			}
		return isAtDistance;
	}
	
	@Override
	public boolean isObstacle() {
		return true;
	}
	
	public boolean isDead(){
		return isDead;
	}
	
	//GETTEURS
	
	public int getDirection(int pos){
		return direction[pos];
	}

	public int getLife(){
		return life;
	}
	
	public int getAttack(){
		return attack;
	}

	public abstract String getName();
	
	//SETTEURS
	
	public void changeDirection(int x,int y){
		direction[0] = x;
		direction[1] = y;
	}
	
	public void setObjects(ArrayList<Model.characters.Character> characters,ArrayList<Tile> tiles, ArrayList<Item> itemsOnMap){
		this.player = (Player) characters.get(0);
		this.characters = characters;
		this.tiles =  tiles;
		this.itemsOnMap = itemsOnMap;
	}
	
	public void setLife(int life){
		this.life=life;
		if(life==0){
			isDead=true;
		}
	}
	
	public void setAttack(int attack){
		this.attack=attack;
	}
	
//////////////////////////////////////////////////////////
	
	public synchronized void remove(Removable r){
		itemsOnMap.remove(r);
	}
	
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
	public void gameOverAttachObserver(GameOverObserver goo) {
		goObs.add(goo);
		
	}

	@Override
	public void gameOverNotifyObserver() {
		for(GameOverObserver goo : goObs){
			goo.gameOver();
		}
		
	}
}
