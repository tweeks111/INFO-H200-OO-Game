package Model.tiles;

import java.util.ArrayList;
import java.util.Random;

import Model.items.Arrow;
import Model.items.InvincibilityPotion;
import Model.items.Item;
import Model.items.LifePotion;
import Model.items.ManaPotion;

public class Box extends RemovableTile{
	private ArrayList<Item> itemsOnMap;
	
	public Box(int x, int y) {
		super(x, y, 6);
		setLife(2);
	}
	
	public void isAttacked(int attack){
		setLife(getBlockLife()-attack);
		System.out.println("Life of "+this.getName()+" : "+getBlockLife());
		if(getBlockLife()<=0){
			removableNotifyObserver();
			dropObject();
		}
	}
	private void dropObject(){
		Random rand = new Random();
		int objectNumber = rand.nextInt(100);
		if(objectNumber < 25){
			drop(new LifePotion(posX,posY));
		}
		else if(objectNumber < 50){
			drop(new ManaPotion(posX,posY));
		}
		else if(objectNumber < 75){
			drop(new Arrow(posX,posY));
		}
		else if(objectNumber<100){
			drop(new InvincibilityPotion(posX,posY));
		}
	}
	private void drop(Item item){
		itemsOnMap.add(item);
	}
	
	public void setObjects(ArrayList<Item> items){
		this.itemsOnMap=items;
	}
	
	@Override
	public String getName() {
		return "Box";
	}

}
