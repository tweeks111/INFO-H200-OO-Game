package Model.characters;

import java.util.ArrayList;
import java.util.Random;

import Model.items.*;
import Model.observers.*;
import Model.tiles.*;


public class Player extends Character implements Runnable,MapChange,MovableObserver,RemovableObserver,Refresh{

	//Listes d'oberservers
	private ArrayList<RefreshObserver> refObs = new ArrayList<RefreshObserver>();
	private ArrayList<MapChangeObserver> mapObs = new ArrayList<MapChangeObserver>();
	
	private Inventory inventory;
	
	//Constantes
	private final static int MAXLIFE =8;
	private final static int MAXMANA = 8;
	
	//Attributs
	private int maxMana = 5;
	private int maxLife = 5;
	private int mana;
	private int xp;
	private int level;
	private int actualWeapon = 0;
	private boolean isDrugged;
	private boolean isInvincible;
	
	public Player(int x, int y) {
		super(x, y, 0);
		this.level=1;
		this.xp=0;
		this.mana=5;
		setLife(maxLife);
		setAttack(1);
		isDrugged(false);
		isInvincible=false;
		inventory = new Inventory();
	}
	
    //DEPLACEMENT
	
	public void move(int x,int y){
		try{
		this.posX=this.posX+x;
		this.posY=this.posY+y;
		for(Tile tile:tiles){
			if(tile instanceof Door && this.getPosX()==tile.getPosX() && this.getPosY()==tile.getPosY()){
				mapChangeNotifyObserver();
			}
			if(tile instanceof Spikes && this.getPosX()==tile.getPosX() && this.getPosY()==tile.getPosY()){
				Spikes spikes = (Spikes)tile;
				isHurt(spikes.getAttack());
			}
			if(tile instanceof DrugTile&& this.getPosX()==tile.getPosX() && this.getPosY()==tile.getPosY()){
				if(!isDrugged){
					isDrugged=true;
					new Thread(this).start();
				}
			}

		}
		}catch(Exception ez ){
		}
	}
	
	//COMBAT
	public synchronized void zoneAttack(){
		//Attaque de zone qui fait des degats sur le carré entourrant le joueur
		try{
		if(mana>0){
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
					character.isHurt(getAttack());
				}
			}
			mana--;
		}
		}catch(Exception e){
		}
	}
	
	public void useWeapon(){
		
		if(actualWeapon==0 && hasSword()){
			setAttack(inventory.getWeaponAttack(actualWeapon));
			attack();
		}
		if(actualWeapon==1 && hasBow() && hasArrow()){
			boolean canShoot=true;
			for(Tile tile:tiles){
				if(tile.isAtPosition(posX+getDirection(0), posY+getDirection(1)) && !(tile instanceof DrugTile || tile instanceof Spikes)){
					canShoot=false;
					break;
				}
			}
			if(canShoot){
				for(Character character : characters){
					if(character.isAtPosition(posX+getDirection(0), posY+getDirection(1))){
						canShoot=false;
						break;
					}
				}
			}
			if(canShoot){
				shoot();
			}
			
		}
		if(actualWeapon==2){
			
		}
	}
	
	private void shoot(){
		Arrow arrow = inventory.dropArrow();
		arrow.movableAttach(this);
		arrow.removableAttach(this);
		arrow.setObjects(tiles, characters);
		arrow.setPosition(posX,posY);
		arrow.setDirection(getDirection(0),getDirection(1));
		itemsOnMap.add(arrow);
		Thread arrowThread = new Thread(arrow);
		arrowThread.start();
	}
	
	//UPDATE
	public synchronized void isHurt(int attack){
		if(isInvincible){
			System.out.println("Player is invincible !!");
		}
		else{
		
		setLife(getLife()-attack);
		System.out.println("Life of  "+this.getName()+" : "+getLife());
		if(getLife() <=0){
			isDead = true;
			gameOverNotifyObserver();
			removableNotifyObserver();
			System.out.println(this.getName()+" is Dead");
		}
		}
}
	private void updateLevel(){
		if(xp>=100*level){
		
			level++;
			if(level%5==0 && maxMana<MAXMANA && maxLife<MAXLIFE){
				maxMana++;
				maxLife++;
			}
		}

	}
	
	public void updateMana(){
		if(mana<maxMana){
			if(inventory.verifyPotions(1)) mana++;
		}
	}
	
	public void heal(){
		if(getLife()==maxLife && isDrugged){
			if(inventory.verifyPotions(0)){
				isDrugged=false;
			}
		}
		else if(getLife()<maxLife){
			if(inventory.verifyPotions(0)){
				setLife(getLife()+1);
				isDrugged=false;
			}
		}
	}
	
	public void tryToBeInvincible(){
		if(inventory.verifyPotions(2)){
			isInvincible=true;
			new Thread(this).start();
		}
	}
	
	public void addXP(int x){
		Random rand = new Random();
		int addXP = rand.nextInt(10*x)+10*x;
		xp+=addXP;
		updateLevel();
	}
	
	//VERIFICATIONS
	
	public boolean hasBow(){
		return inventory.verifyWeapons(1);
	}
	
	public boolean hasArrow(){
		boolean hasArrow=false;
		if(inventory.countArrow()>0) hasArrow= true;
		return hasArrow;
	}
	
	public boolean hasSword(){
		return inventory.verifyWeapons(0);
	}
	
	public int countArrow(){
		return inventory.countArrow();
	}
	
	public void checkInventory(){
		inventory.checkItemsOnMap(posX, posY);
	}
	
	//SETTEURS
	
	public void isDrugged(boolean isDrugged){
		this.isDrugged=isDrugged;
	} 
	
	public void setActualWeapon(int actualWeapon){
		this.actualWeapon=actualWeapon;
	}
	
	public void setObjects(ArrayList<Model.characters.Character> characters,ArrayList<Tile> tiles, ArrayList<Item> itemsOnMap){
		this.characters = characters;
		this.tiles =  tiles;
		this.itemsOnMap = itemsOnMap;
		inventory.setObjects(itemsOnMap);
	}
	
	//GETTEURS
	
	public int getLevel(){
		return level;
	}
	
	public int getXP(){
		return xp;
	}
	
	public int getMaxLife(){
		return maxLife;
	}
	
	public int getMana(){
		return mana;
	}
	
	public int getMaxMana(){
		return maxMana;
	}
	
	public int getActualWeapon(){
		return actualWeapon;
	}
	
	public boolean getIsDrugged(){
		return isDrugged;
	}
	
	public boolean getIsInvincible(){
		return isInvincible;
	}
	
	public ArrayList<Item> getItems(){
		return inventory.getItems();
	}

	
	@Override
	public String getName() {
		return "Player";
	}

/////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void moveObs() {
		refreshNotifyObserver();
	}

	@Override
	public synchronized void remove(Removable r) {
		itemsOnMap.remove(r);
		refreshNotifyObserver();
	}

	////OBSERVABLE	
	
	@Override
	public void refreshAttach(RefreshObserver ro) {
		refObs.add(ro);
	}

	@Override
	public void refreshNotifyObserver() {
		for(RefreshObserver ro : refObs){
			ro.refresh();
		}
		
	}

	@Override
	public void mapChangeAttach(MapChangeObserver mco) {
		mapObs.add(mco);
		
	}

	@Override
	public void mapChangeNotifyObserver() {
		for(MapChangeObserver mco : mapObs){
			mco.mapChange();
		}
	}

	////THREAD
	
	@Override
	public void run() {
		while(isInvincible){
			try{
				isDrugged=false;
				Thread.sleep(10000);
				isInvincible=false;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		while(isDrugged){
			try{
				Thread.sleep(6000);
				isDrugged=false;
			}catch(Exception e){
				e.printStackTrace();
			}

		}

	}

	


}
