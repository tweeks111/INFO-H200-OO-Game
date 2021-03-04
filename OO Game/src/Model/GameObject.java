package Model;

public abstract class GameObject {
	
	protected int posX; 
	protected int posY;
	protected int colorTile;
	
	public GameObject(int x,int y, int color){
		posX=x;
		posY=y;
		colorTile=color;
	}
	
	//SETTEURS
	
	public void setPosition(int x, int y){
		this.posX=x;
		this.posY=y;
	}
	
	//GETTEURS
	
	public int getPosX(){
		return this.posX;
	}
	
	public int getPosY(){
		return this.posY;
	}
	
	public int getColorTile(){
		return this.colorTile;
	}
	
	//VERIFICATION
	
	public boolean isAtPosition(int x, int y){
		return this.posX == x && this.posY == y;
	}
	
	public abstract boolean isObstacle();
	
}
