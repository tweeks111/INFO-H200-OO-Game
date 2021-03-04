package Model.characters;
import java.util.ArrayList;
import java.util.Random;

import Model.items.Arrow;
import Model.items.Item;
import Model.items.LifePotion;
import Model.items.ManaPotion;
import Model.observers.*;
import Model.tiles.Tile;

public abstract class Mob extends Character implements Movable, Runnable{
	private ArrayList<MovableObserver> moveObservers = new ArrayList<MovableObserver>();
	
	private int giveXP;
	private int moveSpeed;
	private int distanceView;
	
	public Mob(int x, int y, int color) {
		super(x, y, color);

		
	}
	
	//DEPLACEMENT
	
	private synchronized void moveRandom(){

		Random rand = new Random();
		int i = rand.nextInt(5);
		if (i == 0 && canMove(posX-1,posY)) {
			this.changeDirection(-1,0);this.move(-1, 0);
		} else if (i == 1 && canMove(posX+1,posY)) {
			this.changeDirection(1,0);this.move(1, 0);
		} else if (i == 2 && canMove(posX,posY-1)) {
			this.changeDirection(0,-1);this.move(0,-1);
		} else if (i == 3 && canMove(posX,posY+1)) {
			this.changeDirection(0,1);this.move(0, 1);
		} else ;
	}
	
	//INVENTAIRE
	private void dropObject(){
		int x = verifyWhereDrop(0);
		int y = verifyWhereDrop(1);
		Random rand = new Random();
		int objectNumber = rand.nextInt(4);
		if(objectNumber == 0){
			drop(new LifePotion(posX+x,posY+y));
		}
		else if(objectNumber == 1){
			drop(new ManaPotion(posX+x,posY+y));
		}
		if(objectNumber == 2){
			drop(new Arrow(posX+x,posY+y ));
		}if(objectNumber == 3){
		
		}
	}
	private void drop(Item item){
		for(Tile tile : tiles){
			if(tile.isAtPosition(posX, posY)){
				if(!(tile.isAtPosition(posX, posY-1))){
					itemsOnMap.add(item);
					break;
				}
				else if(!(tile.isAtPosition(posX, posY+1))){
					itemsOnMap.add(item);
					break;
				}
				else if(!(tile.isAtPosition(posX-1, posY))){
					itemsOnMap.add(item);
					break;
				}
				else if(!(tile.isAtPosition(posX+1, posY))){
					itemsOnMap.add(item);
					break;
				}
				
			}
			else{
				itemsOnMap.add(item);
				break;
			}
			
		}
		
	}
	
	private int verifyWhereDrop(int i){
		int[] pos = {0,0};
		boolean canDropCenter = true;
		boolean canDropUp = true;
		boolean canDropDown = true;
		boolean canDropLeft=true;
		boolean canDropRight=true;
		for(Tile tile : tiles){
			if(tile.isAtPosition(posX, posY)){
				canDropCenter=false;
			}
			if(tile.isAtPosition(posX, posY-1)){
				canDropUp=false;
			}
			if(tile.isAtPosition(posX, posY+1)){
				canDropDown=false;
			}
			if(tile.isAtPosition(posX-1, posY)){
				canDropLeft=false;
			}
			if(tile.isAtPosition(posX+1, posY)){
				canDropRight=false;
			}
		}
		if(canDropCenter){
			pos[0]=0;
			pos[1]=0;
		}
		else if(canDropUp){
			pos[0]=0;
			pos[1]=-1;
		}
		else if(canDropDown){
			pos[0]=0;
			pos[1]=1;
		}
		else if(canDropLeft){
			pos[0]=-1;
			pos[1]=0;
		}
		else if(canDropRight){
			pos[0]=1;
			pos[1]=0;
		}
		return pos[i];
	}
	
	//COMBAT
 	private synchronized void moveToPlayer(){
		int posXPlayer = player.getPosX();
		int posYPlayer = player.getPosY();
		
		int vectX = posXPlayer - posX;
		int vectY = posYPlayer - posY;
		
		int dirX;
		int dirY;
		
		if(vectX<0) dirX = -1;
		else if(vectX>0) dirX=1;
		else dirX=0;
		if(vectY<0) dirY =-1;
		else if(vectY>0) dirY=1;
		else dirY=0;
		
		int nextX=posX+dirX;
		int nextY=posY+dirY;
		
		if(Math.abs(vectX) > Math.abs(vectY)){
			
					if(canMove(nextX,posY)) move(dirX,0);
					else if(dirY>0 && canMove(posX,nextY)) move(0,dirY);
					else if(dirY<0 && canMove(posX,nextY)) move(0,dirY);
					else moveRandom();
		}
		
		if(Math.abs(vectX) < Math.abs(vectY)){
			if(canMove(posX,nextY)) move(0,dirY);
			else if(dirX>0 && canMove(nextX,posY)) move(dirX,0);
			else if(dirX<0 && canMove(nextX,posY)) move(dirX,0);
			else moveRandom();
		}
		
		if(Math.abs(vectX) == Math.abs(vectY)){
			
			if(canMove(posX,nextY) && canMove(nextX,posY)){
				Random rand = new Random();
				int i = rand.nextInt(2);
				if(i==0) move(dirX,0);
				else move(0,dirY);
			}
			else if(!canMove(posX,nextY) && canMove(nextX,posY)) move(dirX,0);
			else if(canMove(posX,nextY) && !canMove(nextX,posY)) move(0,dirY);
			else moveRandom();
		}
	}
	
	public synchronized void isHurt(int attack){
		setLife(getLife()-attack);
		System.out.println("Life of "+this.getName()+" : "+getLife());
		if(this.getLife() <=0){
			isDead = true;
			dropObject();
			player.addXP(giveXP);
			removableNotifyObserver();
			System.out.println(this.getName()+" is dead");
		}
}
	//GETTEURS
	
	@Override
	public abstract String getName();
	
	public int getMoveSpeed(){
		return moveSpeed;
	}
	public void setMoveSpeed(int moveSpeed){
		this.moveSpeed=moveSpeed;
	}
	
	//SETTEURS
	
	public void setGiveXP(int giveXP){
		this.giveXP=giveXP;
	}
	
	public void setDistanceView(int dist){
		this.distanceView=dist;
	}
	////////////////////////////////////////////
	
	public void run() {
		while(!isDead()){
			try{
				if(player.isAtDistance(posX,posY,1) && (posX==player.getPosX() || posY==player.getPosY())){
					changeDirection(player.getPosX()-posX,player.getPosY()-posY);
					Thread.sleep(500);
					if(!(isDead)) attack();
					movableNotifyObserver();
					Thread.sleep(2000);
				}
				else if(player.isAtDistance(posX, posY, distanceView)){
					moveToPlayer();
					movableNotifyObserver();
					Thread.sleep(1000/moveSpeed);
				}
				else{
					moveRandom();
					movableNotifyObserver();
					Thread.sleep(2000/moveSpeed);
				}
				
				
				
			}catch(Exception e){e.printStackTrace();}
		}
		
	}
	
	@Override
	public void movableAttach(MovableObserver mo){
		moveObservers.add(mo);
	}

	@Override
	public void movableNotifyObserver() {
		for(MovableObserver mo : moveObservers){
			mo.moveObs();
		}	
	}


}

