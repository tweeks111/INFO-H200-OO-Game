package Model.characters;

public class Spider extends Mob{
	
	public Spider(int x, int y) {
		super(x, y, 1);
		super.setLife(3);
		super.setMoveSpeed(4);
		super.setDistanceView(2);
		super.setAttack(1);
		super.setGiveXP(1);
	}

	@Override
	public String getName() {
		return "Spider";
	}

}
