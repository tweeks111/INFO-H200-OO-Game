package Model.items;

public class Weapon extends Item {
	private int attack;
	
	public Weapon(int x, int y, int color, int attack) {
		super(x, y, color);
		this.attack=attack;
	}
	
	public int getAttack(){
		return attack;
	}
}
