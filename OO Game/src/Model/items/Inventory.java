package Model.items;

import java.util.ArrayList;

import Model.characters.Player;

public class Inventory {
	
	Player player;
	//Listes d'items dans l'inventaire
	private ArrayList<Item> items;
	private ArrayList<Weapon> weapons;
	private ArrayList<Arrow> arrows;
	
	//Liste d'item sur la map
	private ArrayList<Item> itemsOnMap;
	
	//Constantes
	private static final int itemCapacity =6;
	private static final int arrowCapacity=20;

	//CONSTRUCTEUR
	
	public Inventory(){
		items= new ArrayList<Item>();
		weapons=new ArrayList<Weapon>();
		arrows=new ArrayList<Arrow>();
		
	}
	
	//////

	public Arrow dropArrow(){
		Arrow arrow = arrows.get(0);
		arrows.remove(arrow);
		return arrow;
	}
	
	public int countArrow(){
		return arrows.size();
	}
	
	//VERIFICATION
	
	public void checkItemsOnMap(int posX, int posY){
		if(itemsOnMap.size()>0){
			//Ramassage des flèches
			if(arrows.size()<arrowCapacity){
				for(Item item : itemsOnMap){
					if(item instanceof Arrow && item.isAtPosition(posX, posY)){
						Arrow arrow = (Arrow) item;
						arrows.add(arrow);
						itemsOnMap.remove(item);
						break;
					}
				}
			}
			//Ramassage des armes
			if(weapons.size()<3){
				for(Item item : itemsOnMap){
					if(item instanceof Weapon && item.isAtPosition(posX, posY)){
						Weapon weapon = (Weapon) item;
						if(weapon instanceof Sword && !verifyWeapons(0)) weapons.add(weapon);
						if(weapon instanceof Bow && !verifyWeapons(1)) weapons.add(weapon);
						itemsOnMap.remove(item);
						break;
					}
				}
			}
			//Ramassage des potions
			if(items.size()<itemCapacity){
				for(Item item : itemsOnMap){
					if(item.isAtPosition(posX, posY) && !(item instanceof Weapon) && !(item instanceof Arrow) && !(item instanceof Damages)){
						items.add(item);
						itemsOnMap.remove(item);
						System.out.println("Player has : "+items.size()+" objects in his inventory");
						break;
					}
				}
			}

		}
	}
	
	public boolean verifyPotions(int potion){
		//Vérifie si la potion désirée se trouve dans l'inventaire
		boolean potionInInventory=false;
		
		if(potion==0){   //Potion de vie
			for(Item item:items){
				if(item instanceof LifePotion){
					potionInInventory= true;
					items.remove(item);
					break;
				}
			}
		}
		
		if(potion==1){    //Potion de mana
			for(Item item:items){
				if(item instanceof ManaPotion){
					potionInInventory= true;
					items.remove(item);
					break;
				}
			}
		}
		if(potion==2){    //Potion d'invincibilité
			for(Item item:items){
				if(item instanceof InvincibilityPotion){
					potionInInventory= true;
					items.remove(item);
					break;
				}
			}
		}
		return potionInInventory;
	}
	
	public boolean verifyWeapons(int weaponNum){
		//Vérifie si l'arme demandée est bien dans l'inventaire
		
		boolean weaponInInventory=false;
		if(weaponNum==0){
			for(Weapon weapon : weapons){
				if(weapon instanceof Sword){
					weaponInInventory=true;
					break;
				}
			}
		}
		if(weaponNum==1){
			for(Weapon weapon : weapons){
				if(weapon instanceof Bow){
					weaponInInventory=true;
					break;
				}
			}
		}
		return weaponInInventory;
	}
	
	
	//GETTEURS
	
	public int getWeaponAttack(int actualWeapon){
		return weapons.get(actualWeapon).getAttack();
	}
	
	public ArrayList<Item> getItems(){
		return items;
	}
	
	//SETTEURS
	
	public void setObjects(ArrayList<Item> itemsOnMap){
		this.itemsOnMap = itemsOnMap;
	}

}
