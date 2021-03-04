package Model.characters;

import Model.tiles.Door;
import Model.tiles.Tile;
import Model.tiles.UnremovableTile;

public class Ghost extends Mob{
	
	public Ghost(int x, int y) {
		super(x, y, 2);
		super.setLife(3);
		super.setMoveSpeed(1);
		super.setDistanceView(3);
		super.setAttack(1);
		super.setGiveXP(1);
	}
	
	
	//DEPLACEMENT 
		
	public boolean canMove(int posX, int posY){
		
		boolean canMove = true;
		for(Tile tile : tiles){
		
			if(tile.isAtPosition(posX,posY) && (tile instanceof UnremovableTile || tile instanceof Door)){
				canMove = false;
			}
			if(canMove == false){
				break;
			}
		}
		
		for(Character character : characters){

				if(character.isAtPosition(posX, posY) &&  character.isObstacle()){
						canMove = false;
				}
				if(canMove == false){
					break;
				}
		}
		return canMove;
	}

	//GETTEURS
	
	@Override
	public String getName() {
		return "Ghost";
	}
	
}
