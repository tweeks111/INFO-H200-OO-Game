package View;

import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import Model.items.Item;
import Model.tiles.Tile;

public class Window{

		private Map map;
		private HUD hud;
		private JFrame window;
		
	public Window(int mapSize){
		map= new Map(mapSize);
		hud= new HUD();
	    window = new JFrame("Buried - "+mapSize+"x"+mapSize);
	    window.setResizable(false);
	    window.setLayout(new BorderLayout());
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.getContentPane().add(this.map,BorderLayout.SOUTH);
	    window.getContentPane().add(this.hud,BorderLayout.NORTH);
	    window.setVisible(true);
	    window.pack();

	    
	}
	
	public void update(){
		this.map.redraw();
		this.hud.redraw();
	}
	
	public void setGameObjects(ArrayList<Model.characters.Character> characters, ArrayList<Tile> tiles, ArrayList<Item> items){
		this.map.setObjects(characters, tiles, items);
		this.map.redraw();
		this.hud.setObjects(characters);
		this.hud.redraw();
	}
	public void setKeyListener(KeyListener keyboard){
	    this.map.addKeyListener(keyboard);
	}
	public void dispose(){
		window.dispose();
	}
}
