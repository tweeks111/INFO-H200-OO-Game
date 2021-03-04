package View;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.*;

public class Content {
	//LAUNCHER
	public static BufferedImage[][] background;
	//HUD
	public static BufferedImage[][] hud;
	public static BufferedImage[][] huditems;
	//MAP
	public static BufferedImage[][] items;
	public static BufferedImage[][] tiles;
	public static BufferedImage[][] player;
	public static BufferedImage[][] mobs;
	//GAMEOVER
	public static BufferedImage[][] gameOver;
	public static void loadBackground(){
		background=load("/LauncherBackground.png",800,500);
	}

	public static void loadHudImages(){
		hud = load("/HUD/HUD.png",420,105);
		huditems = load("/HUD/huditems.png",60,60);
		items = load("/Map/items.png",30,30);
		
	}
	
	public static void loadMapImages(){
		tiles = load("/Map/textures.png",30,30);
		player = load("/Map/player.png",30,30);
		mobs = load("/Map/mobs.png",30,30);
		items = load("/Map/items.png",30,30);
	}
	public static void loadGameOver(){
		gameOver=load("/gameover.png",350,200);
	}
	public static BufferedImage[][] load(String s, int w, int h){
		BufferedImage[][] subImage;
		try{
			BufferedImage imageSheet = ImageIO.read(Content.class.getResourceAsStream(s));
			int width = imageSheet.getWidth()/w;
			int height = imageSheet.getHeight()/h;
			subImage = new BufferedImage[height][width];
			for(int i =0; i<height;i++){
				for(int j = 0; j<width;j++){
					subImage[i][j] = imageSheet.getSubimage(j*w, i*h, w, h);
				}
			}
			return subImage;
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Erreur chargement image");
			System.exit(0);
		}
		return null;
	}
}
