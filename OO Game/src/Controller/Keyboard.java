package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Model.Game;

public class Keyboard implements KeyListener{

private Game game;

	public Keyboard(Game game){
		this.game = game;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		int key=event.getKeyCode(); 
		switch(key){
		//DEPLACEMENT
		case KeyEvent.VK_RIGHT : case KeyEvent.VK_D: 
			game.movePlayer(1, 0);
			game.checkInventory();
			break;
		case KeyEvent.VK_LEFT:case KeyEvent.VK_Q:
			game.movePlayer(-1, 0);
		    game.checkInventory();
			break;
		case KeyEvent.VK_DOWN:case KeyEvent.VK_S:
			game.movePlayer(0, 1);
			game.checkInventory();
			break;
		case KeyEvent.VK_UP:case KeyEvent.VK_Z:
			game.movePlayer(0, -1);
			game.checkInventory();
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent event) {
		
	}

	@Override
	public void keyReleased(KeyEvent event) {
		int key = event.getKeyCode();
		
		switch (key){
		

				
			//ATTAQUE
			case KeyEvent.VK_SPACE:
				game.attackOfPlayer(key);
				break;
			case KeyEvent.VK_X:
				game.attackOfPlayer(key);
				break;
				
			//USE OBJECTS
			case KeyEvent.VK_A: case KeyEvent.VK_F:case KeyEvent.VK_E:case KeyEvent.VK_1:case KeyEvent.VK_2:case KeyEvent.VK_3:
				game.useObject(key);
				break;

		}
	}
	
	
}
