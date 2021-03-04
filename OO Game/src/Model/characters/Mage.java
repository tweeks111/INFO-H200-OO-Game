package Model.characters;

public class Mage extends Mob{

	public Mage(int x, int y, int life) {
		super(x, y, 3);
		super.setLife(3+life);
		super.setMoveSpeed(3);
		super.setDistanceView(2);
		super.setAttack(1);
		super.setGiveXP(2);
	}

	public void attack(){
		if(player.getIsDrugged()==false){
			player.isDrugged(true);
			new Thread(player).start();
		}
		super.attack();
	}
	
	
	@Override
	public String getName() {
		return "Mage";
	}

}
