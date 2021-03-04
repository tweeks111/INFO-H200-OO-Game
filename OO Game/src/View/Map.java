package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

import Model.items.Arrow;
import Model.items.Item;
import Model.tiles.Tile;

public class Map extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static int tileSize;
	private static int mapSize;
	
	private ArrayList<Model.characters.Character> characters;
	private ArrayList<Tile> tiles;
	private ArrayList<Item> items;
	
	private int[][] randGround;
	
	//////TEXTURES//////
	
	//PLAYER
	private BufferedImage playerDown;
	private BufferedImage playerUp;
	private BufferedImage playerLeft;
	private BufferedImage playerRight;
	
	//MOBS
	private BufferedImage spiderUp;
	private BufferedImage spiderDown;
	private BufferedImage spiderLeft;
	private BufferedImage spiderRight;
	private BufferedImage ghostLeft;
	private BufferedImage ghostRight;
	private BufferedImage mageUp;
	private BufferedImage mageDown;
	private BufferedImage mageLeft;
	private BufferedImage mageRight;
	
	//ITEMS
	private BufferedImage lifePotion;
	private BufferedImage manaPotion;
	private BufferedImage invincibilityPotion;
	private BufferedImage bow;
	private BufferedImage sword;
	private BufferedImage arrow;private BufferedImage arrowLeft;
	private BufferedImage arrowRight;private BufferedImage arrowUp;
	private BufferedImage arrowDown;
	private BufferedImage damages;
	//TILES
	private BufferedImage wall;
	private BufferedImage box;
	private BufferedImage doorOpened;
	private BufferedImage doorClosed;
	private BufferedImage unremovableWall;
	private BufferedImage[] ground;
	private BufferedImage spikes;
	private BufferedImage lava;
	private BufferedImage drug;
	
	/////CONSTRUCTEUR/////
	
	public Map(int mapSize){
		
		Map.mapSize=mapSize;
		setTileSize(mapSize);    //=> La taille des cases dépend de la taille de la map
								 //   afin de garder les proportions de la fenêtre
		
		//Chargement des textures
		Content.loadMapImages();
		
			//PLAYER
		playerDown = Content.player[0][0];
		playerUp = Content.player[1][1];
		playerLeft = Content.player[0][1];
		playerRight = Content.player[1][0];
			//MOBS
		spiderDown=Content.mobs[0][0];
		spiderUp=Content.mobs[1][0];
		spiderLeft=Content.mobs[0][1];
		spiderRight=Content.mobs[1][1];
		ghostLeft =Content.mobs[0][2];
		ghostRight = Content.mobs[1][2];
		mageUp=Content.mobs[2][0];
		mageDown=Content.mobs[3][1];
		mageLeft=Content.mobs[2][1];
		mageRight=Content.mobs[3][0];
			//TILES :
		doorClosed = Content.tiles[1][1];
		doorOpened = Content.tiles[1][2];
		wall = Content.tiles[0][0];
		box = Content.tiles[2][0];
		unremovableWall = Content.tiles[1][0];
		spikes = Content.tiles[1][4];
		drug=Content.items[3][1];
			//ITEMS
		lifePotion = Content.items[0][0];
		manaPotion = Content.items[0][1];
		invincibilityPotion = Content.items[3][2];
		bow=Content.items[0][2];
		sword=Content.items[2][0];
	    arrow=Content.items[1][0];
	    arrowLeft=Content.items[1][1];
	    arrowRight=Content.items[2][1];
	    arrowUp=Content.items[1][2];
	    arrowDown=Content.items[2][2];
		damages=Content.items[3][0];
		//Génération d'une texture aléatoire pour le sol :
		ground = new BufferedImage[4];
		for(int i=0;i<4;i++){
			ground[i]=Content.tiles[0][i+1];
		}
		Random rand = new Random();
		randGround = new int[mapSize][mapSize];
		for(int i = 0; i<mapSize; i++){		
			for(int j = 0; j<mapSize; j++){
				randGround[i][j] = rand.nextInt(4);
			}
		}
		
		
		this.setPreferredSize(new Dimension(tileSize*mapSize,tileSize*mapSize));
		this.setFocusable(true);
		this.requestFocusInWindow();
	}
	
	//DESSIN DE L'INTERFACE
	
	public void paintComponent(Graphics g) { 
		/*
		 * Pour visualiser le jeu avec les formes basiques et sans les textures il faut 
		 * mettre uniquement drawTable(g)
		 */
			//drawTable(g);
			drawBackground(g);
			drawTiles(g);
			drawItems(g);
			drawCharacters(g);
			
	}
	
	public void drawBackground(Graphics g){
		for(int i = 0; i<mapSize; i++){		
			for(int j = 0; j<mapSize; j++){			
				g.drawImage(ground[randGround[i][j]],i*tileSize,j*tileSize,tileSize,tileSize,null);
			}
		}
		
	}
	
	public void drawTiles(Graphics g){
		try{
		for(Tile tile : this.tiles){ 
			int x = tile.getPosX();
			int y = tile.getPosY();
			int color = tile.getColorTile();			
			
			if(color == 0){
				g.drawImage(unremovableWall, x*tileSize, y*tileSize, tileSize, tileSize, null); 
			}else if(color == 1){
				g.drawImage(wall, x*tileSize, y*tileSize, tileSize, tileSize, null); 
			}else if(color == 2){
				g.drawImage(doorClosed, x*tileSize, y*tileSize, tileSize, tileSize, null); 
			}else if(color == 3){
				g.drawImage(doorOpened, x*tileSize, y*tileSize, tileSize, tileSize, null); 
			}else if(color ==4){
				g.drawImage(spikes, x*tileSize, y*tileSize, tileSize, tileSize, null); 
			}else if(color==5){
				g.drawImage(lava, x*tileSize, y*tileSize, tileSize, tileSize, null); 
			}else if(color==6){
				g.drawImage(box, x*tileSize, y*tileSize, tileSize, tileSize, null); 
			}else if(color==7){
				g.drawImage(drug, x*tileSize, y*tileSize, tileSize, tileSize, null); 
			}
		
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	public void drawCharacters(Graphics g){
		try{
		for(Model.characters.Character character : this.characters){ 
			int x = character.getPosX();
			int y = character.getPosY();
			int color = character.getColorTile();
			int directionX = character.getDirection(0);
			int directionY = character.getDirection(1);
			
			if(color == 0){
				if(directionY==1) g.drawImage(playerDown,x*tileSize,y*tileSize,tileSize,tileSize,null);
				if(directionY==-1 )g.drawImage(playerUp,x*tileSize,y*tileSize,tileSize,tileSize,null);
				if(directionX==-1) g.drawImage(playerLeft,x*tileSize,y*tileSize,tileSize,tileSize,null);
				if(directionX==1) g.drawImage(playerRight,x*tileSize,y*tileSize,tileSize,tileSize,null);
			}else if(color == 1){
				if(directionY==1) g.drawImage(spiderDown,x*tileSize,y*tileSize,tileSize,tileSize,null);
				if(directionY==-1 )g.drawImage(spiderUp,x*tileSize,y*tileSize,tileSize,tileSize,null);
				if(directionX==-1) g.drawImage(spiderLeft,x*tileSize,y*tileSize,tileSize,tileSize,null);
				if(directionX==1) g.drawImage(spiderRight,x*tileSize,y*tileSize,tileSize,tileSize,null);
			}
			else if(color == 2){
				if(directionX==-1) g.drawImage(ghostLeft,x*tileSize,y*tileSize,tileSize,tileSize,null);
				if(directionX==1) g.drawImage(ghostRight,x*tileSize,y*tileSize,tileSize,tileSize,null);
				else g.drawImage(ghostLeft,x*tileSize,y*tileSize,tileSize,tileSize,null);
			}
			else if(color==3){
				if(directionX==-1) g.drawImage(mageLeft,x*tileSize,y*tileSize,tileSize,tileSize,null);
				if(directionX==1) g.drawImage(mageRight,x*tileSize,y*tileSize,tileSize,tileSize,null);
				if(directionY==1) g.drawImage(mageDown,x*tileSize,y*tileSize,tileSize,tileSize,null);
				if(directionY==-1 )g.drawImage(mageUp,x*tileSize,y*tileSize,tileSize,tileSize,null);
				
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void drawItems(Graphics g){
		try{
		for(Item item : this.items){ 
			int x = item.getPosX();
			int y = item.getPosY();
			int directionX = 0;
			int directionY = 0;
			int color = item.getColorTile();
			if(item instanceof Arrow){
				Arrow arrow = (Arrow) item;
				directionX=arrow.getDirection(0);
				directionY=arrow.getDirection(1);
			}
			
			if(color == 0){
				g.drawImage(lifePotion, x*tileSize, y*tileSize, tileSize, tileSize, null); 
			}else if(color == 1){
				g.drawImage(manaPotion, x*tileSize, y*tileSize, tileSize, tileSize, null); 
			}else if(color==2){
				g.drawImage(invincibilityPotion, x*tileSize,  y*tileSize,tileSize,tileSize, null);
			}
			else if(color==3){
				g.drawImage(bow, x*tileSize,  y*tileSize, tileSize, tileSize, null);
			}else if(color==4){
				g.drawImage(sword, x*tileSize,  y*tileSize,tileSize,tileSize, null);
			}
			else if(color==5){
				if(directionX==1) g.drawImage(arrowRight, x*tileSize,  y*tileSize, tileSize, tileSize, null);
				if(directionX==-1 )g.drawImage(arrowLeft, x*tileSize,  y*tileSize, tileSize, tileSize, null);
				if(directionY==1 )g.drawImage(arrowDown, x*tileSize,  y*tileSize, tileSize, tileSize, null);
				if(directionY==-1 )g.drawImage(arrowUp, x*tileSize,  y*tileSize, tileSize, tileSize, null);
				if(directionX==0 && directionY==0) g.drawImage(arrow, x*tileSize,  y*tileSize, tileSize, tileSize, null);
			}
			else if(color==6){
				g.drawImage(damages, x*tileSize,  y*tileSize,tileSize,tileSize, null);
			}
			
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void drawTable(Graphics g){
		 try{
		for(int i = 0; i<mapSize; i++){		
				for(int j = 0; j<mapSize; j++){
					int x = i;
					int y = j;
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(x*tileSize, y*tileSize, tileSize, tileSize); 

				}
		}
		
		for(Tile tile : this.tiles){ 
			int x = tile.getPosX();
			int y = tile.getPosY();
			int color = tile.getColorTile();			
			
			if(color == 0){
				g.setColor(Color.DARK_GRAY);
			}else if(color == 1){
				g.setColor(Color.GRAY);
			}else if(color == 2){
				g.setColor(Color.YELLOW);
			}else if(color == 3){
				g.setColor(Color.ORANGE);
			}
			
			g.fillRect(x*tileSize, y*tileSize, tileSize, tileSize); 
		}
		for(Item item : this.items){ 
			int x = item.getPosX();
			int y = item.getPosY();
			int color = item.getColorTile();			
			
			if(color == 0){
				g.setColor(Color.PINK);
			}else if(color == 1){
				g.setColor(Color.BLUE);
			}else if(color == 2){
				g.setColor(Color.WHITE);
			}
			
			g.fillOval(x*tileSize+6, y*tileSize+6, tileSize-12, tileSize-12);
		}
		for(Model.characters.Character character : this.characters){ 
			int x = character.getPosX();
			int y = character.getPosY();
			int color = character.getColorTile();
			
			if(color == 0){
				g.setColor(Color.RED);
			}else if(color == 1){
				g.setColor(Color.GREEN);
			}else if(color == 2){
				g.setColor(Color.WHITE);
			}

			g.fillOval(x*tileSize, y*tileSize, tileSize, tileSize);  

		}
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		
	}
	
	
	public void redraw(){
		this.repaint();
	}
	
	//SETTEURS
	
	public void setTileSize(int mapSize){
		if(mapSize==11) Map.tileSize=55;
		if(mapSize==15) Map.tileSize=41;
		if(mapSize==17) Map.tileSize=32;
		if(mapSize==19) Map.tileSize=32;
		if(mapSize==21) Map.tileSize=29;
		if(mapSize==31) Map.tileSize=20;
	}
	
	public void setObjects(ArrayList<Model.characters.Character> characters, ArrayList<Tile> tiles, ArrayList<Item> items){ //crée liste avec les objets
		this.characters = characters;
		this.tiles = tiles;
		this.items= items;
	}
	
	//GETTEURS
	
	public static int getMapSize(){
		return mapSize;
	}
	public static int getTileSize(){
		return tileSize;
	}
	
}
