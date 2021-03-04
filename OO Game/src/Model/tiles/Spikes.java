package Model.tiles;

public class Spikes extends Tile{
	
	//ATTRIBUT
	
	private int attack;
	
	//CONSTRUCTEUR
	
	public Spikes(int x, int y) {
		super(x, y, 4);
		this.attack=1;
	}
	
	
	//VERIFICATION
	
	@Override
	public boolean isObstacle() {
		return false;
	}
	
	//GETTEURS
	
	public int getAttack(){
		return attack;
	}
	
	
///////////////////////////////////////////////////////	

}
