package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import View.Map;
import Model.characters.Player;
import Model.items.Item;
public class HUD extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static int width;
	private static int height;
	
	//Textures
	private BufferedImage hud;
	private BufferedImage heart;
	private BufferedImage sword;
	private BufferedImage bow;
	private BufferedImage arrow;
	private BufferedImage emptyHeart;
	private BufferedImage lifePotion;
	private BufferedImage manaPotion;
	private BufferedImage invincibilityPotion;
	
	//Liste de personnages sur la map
	private ArrayList<Model.characters.Character> characters;
	
	//CONSTRUCTEUR
	
	public HUD(){
		Content.loadHudImages();               
		hud = Content.hud[0][0];
		heart = Content.huditems[0][0];
		emptyHeart= Content.huditems[0][1];
		sword = Content.huditems[0][2];
		bow = Content.huditems[0][3];
		arrow = Content.huditems[1][1];
		lifePotion = Content.items[0][0];
		manaPotion = Content.items[0][1];
		invincibilityPotion = Content.items[3][2];
		width =Map.getTileSize()*Map.getMapSize();
		height =Map.getTileSize()*Map.getMapSize()/5;
		
		this.setPreferredSize(new Dimension(width,height));
	}
	
	//DESSIN DU HUD
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		drawHUD(g);
	}
	
	public void drawHUD(Graphics g){
		Player player = (Player) characters.get(0);
		g.drawImage(hud, 0, 0, width, height, null);
		
		String level = String.valueOf(player.getLevel());
		String xp = String.valueOf(player.getXP());
		ArrayList<Item> items = player.getItems();
		int life = player.getLife();
		int mana = player.getMana();
		Font font = new Font("Arial",Font.PLAIN,height*3/16);
		g.setFont(font);
		g.setColor(Color.WHITE);
		
		//Draw PLAYER INFOS
		g.drawString("Level : "+level, height*3/16,height*1/4);
		g.drawString("XP : "+xp+"/"+level+"00", height*3/16,height*7/16);
		
		//DRAW ITEMS
		try{
		for(Item item : items){

				int color = item.getColorTile();			
				
				if(color == 0){
					g.drawImage(lifePotion,width/2+height*5/16*items.indexOf(item), height*3/5,height*5/16, height*5/16, null);
				}else if(color == 1){
					g.drawImage(manaPotion,width/2+height*5/16*items.indexOf(item), height*3/5,height*5/16, height*5/16, null);
				}else if(color == 2){
					g.drawImage(invincibilityPotion,width/2+height*5/16*items.indexOf(item), height*3/5,height*5/16, height*5/16, null);
					
				}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		g.drawRect(width/3,height*1/8, height*10/16,height*5/16);
		g.drawImage(arrow, width/3,height*1/8, height*5/16,height*5/16, null);
		g.drawString(String.valueOf(player.countArrow()), width/3+height*6/16,height*3/8);
		
		//Draw INFOS GAME
		g.drawString("1", width/2+height*1/8, height*3/8);
		g.drawString("2", width/2+height*3/4, height*3/8);
		g.drawString("3", width/2+height*11/8, height*3/8);
		
		//Draw INVENTORY PLACES
		for(int i =0; i<3; i++){
			if(i==player.getActualWeapon()){
				g.setColor(Color.RED);
			}
			g.drawRect(width/2+height*5/16+height*i*5/8, height*1/8, height*5/16, height*5/16);
			if(i==0 && player.hasSword()) g.drawImage(sword, width/2+height*5/16, height*1/8, height*5/16, height*5/16,null);
			if(i==1 && player.hasBow()) g.drawImage(bow, width/2+height*5/16+height*5/8, height*1/8, height*5/16, height*5/16,null);
			g.setColor(Color.WHITE);
		}
		for(int i = 0;i<6;i++){
			g.drawRect(width/2+height*5/16*i, height*3/5,height*5/16, height*5/16);
		}
		
		//Draw HEART
		for(int i=0;i<player.getMaxLife();i++){
			g.drawImage(emptyHeart,  height*3/16+ height*i*1/4,  height*11/16,height*1/4,height*1/4,null);
		}
		for(int i =0; i<life; i++){
			g.drawImage(heart,  height*3/16+ height*i*1/4,  height*11/16,height*1/4,height*1/4,null);
		}
		
		//Draw MANA
		
		g.setColor(Color.GREEN);
		g.fillRect(height*3/16, height*9/16, height*1/4*mana, height*1/16);
		g.setColor(Color.WHITE);
		g.drawRect(height*3/16, height*9/16, height*1/4*player.getMaxMana(), height*1/16);
	
	}

	public void redraw(){
		this.repaint();
	}
	
	//SETTEURS
	
	public void setObjects(ArrayList<Model.characters.Character> characters){ 
		this.characters = characters;
	}

}
