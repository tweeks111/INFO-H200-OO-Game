package Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import com.sun.glass.events.KeyEvent;

import View.GameOverPanel;
import View.Window;
import Model.characters.*;
import Model.characters.Character;
import Model.items.*;
import Model.observers.*;
import Model.tiles.*;

public class Game implements RemovableObserver,MapChangeObserver,MovableObserver,RefreshObserver,OpenDoor,GameOverObserver{
	
	//Observer contenant les portes, la notification déclenche leur ouverture
	ArrayList<OpenDoorObserver> odoObs = new ArrayList<OpenDoorObserver>();
	
	//Variables
	private static int mapSize = 11;
	private int mobNumber;
	private int levelCount;
	private int bestScore;
	private int secondBestScore;
	String[] scores;
	//Listes d'objets 
	private ArrayList<Character> characters = new ArrayList<Character>();
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	private ArrayList<Item> itemsOnMap = new ArrayList<Item>();
	
	Player player;
	
	private Window window;
	
	
	public Game(Window window,int mapSize){
		Game.mapSize=mapSize;
		
		//Création du personnage
		player = new Player(mapSize/2,mapSize-2);
		player.refreshAttach(this);
		player.mapChangeAttach(this);
		player.removableAttach(this);
		player.gameOverAttachObserver(this);
		characters.add(0,player);
		player.setObjects(characters, tiles, itemsOnMap);
		
		//Map Building
		levelCount=1;
		firstMapBuilding();
		
		this.window=window;
		window.setGameObjects(characters, tiles,itemsOnMap);
		
	}
	
	//GENERATION DE LA MAP
	
	public void firstMapBuilding(){
		for(int i=0;i<mapSize;i++){
			tiles.add(new UnremovableTile(0,i));
			tiles.add(new UnremovableTile(i, mapSize-1));
			tiles.add(new UnremovableTile(mapSize-1, i));
		}
		
		for(int i =0;i<mapSize/2;i++){
			tiles.add(new UnremovableTile(i,0));   
		}
		for(int i =mapSize/2+1;i<mapSize;i++){
			tiles.add(new UnremovableTile(i,0));   
		}

		Door door = new Door(mapSize/2,0,true);
		tiles.add(door);
		
		Sword sword = new Sword(mapSize/2+2,mapSize/2);
		itemsOnMap.add(sword);
		Bow bow = new Bow(mapSize/2-2,mapSize/2);
		itemsOnMap.add(bow);
		itemsOnMap.add(new Arrow(1,mapSize/2-1));
		itemsOnMap.add(new Arrow(1,mapSize/2));
		itemsOnMap.add(new Arrow(1,mapSize/2+1));
		itemsOnMap.add(new LifePotion(mapSize-2,mapSize/2-1));
		itemsOnMap.add(new InvincibilityPotion(mapSize-2,mapSize/2));
		itemsOnMap.add(new ManaPotion(mapSize-2,mapSize/2+1));
	}
	
	public void mapBuilding(){
		
		if(levelCount%5!= 0){
		for(int i =0;i<mapSize/2;i++){
			tiles.add(new UnremovableTile(i,0));   
			tiles.add(new UnremovableTile(0,i));
			tiles.add(new UnremovableTile(i, mapSize-1));
			tiles.add(new UnremovableTile(mapSize-1, i));
		}
		
		for(int i =mapSize/2+1;i<mapSize;i++){
			tiles.add(new UnremovableTile(i,0));   
			tiles.add(new UnremovableTile(0,i));
			tiles.add(new UnremovableTile(i, mapSize-1));
			tiles.add(new UnremovableTile(mapSize-1, i));
		}
		
		//DOORS
		Door door1 = new Door(0,(mapSize-1)/2);
		openDoorAttach(door1);
		
		tiles.add(door1);
		Door door2 = new Door((mapSize-1)/2,0);
		openDoorAttach(door2);
		
		tiles.add(door2);
		Door door3 = new Door(mapSize-1,(mapSize-1)/2);
		openDoorAttach(door3);
		
		tiles.add(door3);
		Door door4 = new Door((mapSize-1)/2,mapSize-1);
		openDoorAttach(door4);
		
		tiles.add(door4);
		
		//WALL
		int maxBreakableBlocks = ((mapSize-3)*(mapSize-3))/3;
		Random rand = new Random();
		int numberOfBreakableBlocks =rand.nextInt(maxBreakableBlocks/2 +1)+maxBreakableBlocks/2;
		System.out.println(String.valueOf(maxBreakableBlocks));
		System.out.println(String.valueOf(numberOfBreakableBlocks));
		int[][] matrix = new int[mapSize-2][mapSize-2];
		int breakableBlocks=0;
		while(breakableBlocks<numberOfBreakableBlocks){                                                   //=> j'ai modifié le <= en <
			int i = rand.nextInt(mapSize-2);
			int j = rand.nextInt(mapSize-2);
			if(matrix[i][j]!= 1 && i!=(mapSize-2)/2 && j!=(mapSize-2)/2){
				matrix[i][j]=1;
				breakableBlocks++;
			}
		}
		for(int i=0;i<matrix.length; i++){
			for(int j=0; j<matrix[i].length;j++){
				if(matrix[i][j]==1){
					Wall wall = new Wall(i+1,j+1);
					wall.removableAttach(this);
					tiles.add(wall);
				}
			}
		}
		//BOXES
		int numberOfBoxes = rand.nextInt(3)+1;
		int boxes=0;
		while(boxes<numberOfBoxes){
			int i = rand.nextInt(mapSize-2);
			int j = rand.nextInt(mapSize-2);
			if(matrix[i][j]!= 1 && matrix[i][j]!=2 && i!=(mapSize-2)/2 && j!=(mapSize-2)/2){
				matrix[i][j]=2;
				boxes++;
			}
		}
		for(int i=0;i<matrix.length; i++){
			for(int j=0; j<matrix[i].length;j++){
				if(matrix[i][j]==2){
					Box box = new Box(i+1,j+1);
					box.removableAttach(this);
					box.setObjects(itemsOnMap);
					tiles.add(box);
				}
			}
		}
		int randomNumber = rand.nextInt(50);
		int numberOfDrugTile =0;
		int drugTiles=0;
		if(randomNumber>=40) numberOfDrugTile=4;
		else if(randomNumber>=30) numberOfDrugTile=3;
		else if(randomNumber>=20) numberOfDrugTile=2;
		else if(randomNumber>=10) numberOfDrugTile=1;
		while(drugTiles<numberOfDrugTile){
			int i = rand.nextInt(mapSize-2);
			int j = rand.nextInt(mapSize-2);
			if(matrix[i][j]!= 1 && matrix[i][j]!=2 && matrix[i][j]!=3){
				matrix[i][j]=3;
				drugTiles++;
			}
		}
		
		for(int i=0;i<matrix.length; i++){
			for(int j=0; j<matrix[i].length;j++){
					if(matrix[i][j]==3){
						DrugTile drugTile = new DrugTile(i+1,j+1);
						tiles.add(drugTile);
					}
			}
		}
		
	
		
		
		}
		
		
		
		if(levelCount%5==0){
			for(int i =0;i<mapSize/2;i++){
				tiles.add(new UnremovableTile(i,0));   
				tiles.add(new UnremovableTile(0,i));
				tiles.add(new UnremovableTile(i, mapSize-1));
				tiles.add(new UnremovableTile(mapSize-1, i));
			}
			
			for(int i =mapSize/2+1;i<mapSize;i++){
				tiles.add(new UnremovableTile(i,0));   
				tiles.add(new UnremovableTile(0,i));
				tiles.add(new UnremovableTile(i, mapSize-1));
				tiles.add(new UnremovableTile(mapSize-1, i));
			}
			
			Door door1 = new Door(0,(mapSize-1)/2);
			openDoorAttach(door1);
			
			tiles.add(door1);
			Door door2 = new Door((mapSize-1)/2,0);
			openDoorAttach(door2);
			tiles.add(door2);
			Door door3 = new Door(mapSize-1,(mapSize-1)/2);
			openDoorAttach(door3);
			tiles.add(door3);
			Door door4 = new Door((mapSize-1)/2,mapSize-1);
			openDoorAttach(door4);
			tiles.add(door4);
			
			for(int i =2;i<mapSize/2;i++){
				Spikes spikesUp = new Spikes(i,1);
				tiles.add(spikesUp);
				
				Spikes spikesDown=new Spikes(i,mapSize-2);
				tiles.add(spikesDown);
				
				Spikes spikesLeft = new Spikes(1,i);
				tiles.add(spikesLeft);
				
				Spikes spikesRight= new Spikes(mapSize-2,i);
				tiles.add(spikesRight);
			}
			for(int i =mapSize/2+1;i<mapSize-2;i++){
				Spikes spikesUp = new Spikes(i,1);
				tiles.add(spikesUp);
				
				Spikes spikesDown=new Spikes(i,mapSize-2);
				tiles.add(spikesDown);
				
				Spikes spikesLeft = new Spikes(1,i);
				tiles.add(spikesLeft);
				
				Spikes spikesRight= new Spikes(mapSize-2,i);
				tiles.add(spikesRight);
			}
				Spikes spikesSide1 = new Spikes(1,1);
				tiles.add(spikesSide1);
				Spikes spikesSide2 = new Spikes(1,mapSize-2);
				tiles.add(spikesSide2);
				Spikes spikesSide3 = new Spikes(mapSize-2,1);
				tiles.add(spikesSide3);
				Spikes spikesSide4 = new Spikes(mapSize-2,mapSize-2);
				tiles.add(spikesSide4);
		
				Random rand= new Random();
				int randomNumber = rand.nextInt(60);
				int numberOfDrugTile =0;
				int drugTiles=0;
				if(randomNumber>=55) numberOfDrugTile=6;
				else if(randomNumber>=45) numberOfDrugTile=5;
				else if(randomNumber>=35) numberOfDrugTile=4;
				else if(randomNumber>=25) numberOfDrugTile=3;
				else if(randomNumber>=15) numberOfDrugTile=2;
				else if(randomNumber>=5) numberOfDrugTile=1;
				
				int[][] matrix = new int[mapSize-2][mapSize-2];
				while(drugTiles<numberOfDrugTile){                                                   //=> j'ai modifié le <= en <
					int i = rand.nextInt(mapSize-4);
					int j = rand.nextInt(mapSize-4);
					if(matrix[i][j]!= 1){
						matrix[i][j]=1;
						drugTiles++;
					}
				}
				for(int i=0;i<matrix.length; i++){
					for(int j=0; j<matrix[i].length;j++){
						if(matrix[i][j]==1 && matrix[i][j]!=2 && matrix[i][j]!=3 && i!=(mapSize-2)/2 && j!=(mapSize-2)/2){
							DrugTile drugTile = new DrugTile(i+2,j+2);
							tiles.add(drugTile);
						}
					}
				}
		
		}
		
	}
	
	public void mobRandomSpawn(Player player){
			int lev = player.getLevel();
			
			if(levelCount%5 !=0){
			mobNumber = 2+lev;
			
			for(int i=1;i<=mobNumber;i++){
				Random rand = new Random();
				boolean canSpawn= false;
			
				while(canSpawn!=true){ 
					int x = rand.nextInt(mapSize-2) +1;
					int y = rand.nextInt(mapSize-2) +1;
					for(Tile tile:tiles){
						if(tile.isAtPosition(x, y)){
							canSpawn = false;
							break;
	
						}else{
							canSpawn= true;
						}
					}
					if(canSpawn==true){
						for(Character character:characters){
							if(character.isAtPosition(x, y)){
								canSpawn = false;
								break;
							}else{
								canSpawn=true;
							}
						}
					}
					if (canSpawn == true){
						int randomMob = rand.nextInt(2);
						if(randomMob==0){
							Spider spider = new Spider(x,y);
							characters.add(i,spider);
							spider.removableAttach(this);
							spider.movableAttach(this);
							spider.setObjects(characters, tiles, itemsOnMap);
							new Thread(spider).start();
							
							
						}
						else if(randomMob==1){
							Ghost ghost = new Ghost(x,y);
							characters.add(i,ghost);
							ghost.removableAttach(this);
							ghost.movableAttach(this);
							ghost.setObjects(characters,tiles, itemsOnMap);
							new Thread(ghost).start();
							
						}
					}
				}
			}
			}
			if(levelCount%5==0){
				mobNumber=1;
				if(levelCount>=15){
					mobNumber=levelCount/5;
				}
				for(int i=0;i<mobNumber;i++){
					Random rand = new Random();
					int random1 = rand.nextInt(mapSize-10)+5;
					int random2 = rand.nextInt(mapSize-10)+5;
					Mage mage = new Mage(random1,random2,levelCount/5);
					characters.add(mage);
					mage.removableAttach(this);
					mage.movableAttach(this);
					mage.setObjects(characters, tiles, itemsOnMap);
					new Thread(mage).start();
				}
			
			}

			
	}
	
	//DEPLACEMENT
	
	public void movePlayer(int x, int y){
		
		if(player.getIsDrugged()==true){
			x*=-1;
			y*=-1;
		}
		
		if(player.getDirection(0)==x && player.getDirection(1)==y){
			int nextX = player.getPosX()+x;
			int nextY = player.getPosY()+y;
		
			if(player.canMove(nextX,nextY)){
				player.move(x,y);
				notifyView();
			}
		}
		else if(player.getDirection(0)!=x || player.getDirection(1)!=(y)){
			player.changeDirection(x, y);
			notifyView();
		}
	}
	
	public void moveToOpposite(Player player){
		int posX= player.getPosX();
		int posY= player.getPosY();
		

		if(posX==0) player.setPosition(mapSize-2,posY);
		if(posX==mapSize-1) player.setPosition(1, posY);
		if(posY==0) player.setPosition(posX, mapSize-2);
		if(posY==mapSize-1) player.setPosition(posX, 1);
	}

	//INVENTAIRE
	
	public void checkInventory(){
		player.checkInventory();
	}

	//COMBATS
	
	public void attackOfPlayer(int key){
		
		//Attaque de zone
		if(key==KeyEvent.VK_X){
			System.out.println("Attaque de zone :"+String.valueOf(player.getAttack()));
			player.zoneAttack();
		}
		//Attaque normale
		if(key==KeyEvent.VK_SPACE){
			player.useWeapon();
		}		
		notifyView();
	}

	
	//USE OBJECTS
	
	public void useObject(int key){
		if(key==KeyEvent.VK_A){
			player.heal();
		}
		if(key==KeyEvent.VK_E){
			player.tryToBeInvincible();
		}
		if(key==KeyEvent.VK_F){
			player.updateMana();
		}
		if(key==KeyEvent.VK_1){
			player.setActualWeapon(0);
		}
		if(key==KeyEvent.VK_2){
			player.setActualWeapon(1);
		}
		if(key==KeyEvent.VK_3){
			player.setActualWeapon(2);
		}
		notifyView();
	}
	
	//GETTEURS
	
	public static int getTileNumber(){
		return mapSize;
	}
	public ArrayList<Character> getCharacters(){
		return characters;
	}
	public ArrayList<Tile> getTiles(){
		return tiles;
	}
	
////////////////////////////////////////////////////////////////////////////
	
	//REFRESH WINDOW
	
	private synchronized void notifyView(){
		window.update();
	}
	
	//OBSERVERS
	
	@Override
	public synchronized void moveObs() {
		notifyView();
	}
	
	@Override
	public synchronized void remove(Removable r) {
		if(r instanceof RemovableTile) tiles.remove(r);
		
		else if(r instanceof Mob){ 
			characters.remove(r);
			mobNumber--;
			System.out.println("Still "+String.valueOf(mobNumber)+" mobs");
			if(mobNumber==0){
				openDoorNotifyObserver();
			}
		}
		else if(r instanceof Player){
			System.out.println("Game Over");
			characters.remove(r);
		}
		notifyView();
	}

	@Override
	public void refresh() {
		notifyView();
	}

	@Override
	public void mapChange() {
		levelCount++;
		moveToOpposite(player);
		tiles.clear();
		itemsOnMap.clear();
		mapBuilding();
		mobRandomSpawn(player);
	}

	@Override
	public void gameOver() {
		int score = levelCount*player.getLevel();
		boolean newBestScore = false;
		load("Scores.txt");
		save("Scores.txt");
		if(score>bestScore){
			secondBestScore=bestScore;
			bestScore=score;
			newBestScore=true;
		}

		window.dispose();
		characters.clear();
		tiles.clear();
	
		GameOverPanel gopanel = new GameOverPanel(score,bestScore,secondBestScore,newBestScore);
		JFrame gameoverframe = new JFrame("Game Over");
		gameoverframe.setResizable(false);
		gameoverframe.setContentPane(gopanel);
		gameoverframe.setVisible(true);
		gameoverframe.pack();
		gameoverframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

	//OBSERVABLE
	
	@Override
	public void openDoorAttach(OpenDoorObserver odo) {
		odoObs.add(odo);
	}

	@Override
	public void openDoorNotifyObserver() {
		for(OpenDoorObserver odo : odoObs){
			odo.openDoor(this);
			notifyView();
		}
	}

	//PERSISTENCE
	
	public void load(String filename){
		String line;
		BufferedReader file;
		
		try{
			
			file = new BufferedReader(new FileReader(filename));
			while((line=file.readLine())!=null){
				scores = line.split(",");
			}
			file.close();
			bestScore= 0;
			secondBestScore=0;
			if(scores!=null){
			for(String score : scores){
				if(Integer.valueOf(score)>=bestScore){
					secondBestScore = bestScore;
					bestScore = Integer.valueOf(score);
					
				}
				else if(Integer.valueOf(score)>=secondBestScore){
					secondBestScore = Integer.valueOf(score);
				}
			}
			}
			System.out.println("Best score :"+bestScore);
			System.out.println("Second best score :"+secondBestScore);
		}catch(Exception e){e.printStackTrace();}
	}
	
	public void save(String filename){
		String line = "";
		if(scores!=null){
		for(String score:scores){
			line=line+String.valueOf(score)+",";
		}
		}
		line = line+ String.valueOf(levelCount*player.getLevel())+",";
		BufferedWriter file;
		try{
			file = new BufferedWriter(new FileWriter(filename));
			file.write(line);
			file.close();
			System.out.println("Score saved");
		}catch(Exception e){e.printStackTrace();
		}
	}


	
}
