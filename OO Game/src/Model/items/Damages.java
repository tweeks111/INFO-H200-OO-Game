package Model.items;

import java.util.ArrayList;

import Model.observers.Removable;
import Model.observers.RemovableObserver;

public class Damages extends Item implements Runnable,Removable{

	private boolean displayed;
	private ArrayList<RemovableObserver> robs = new ArrayList<RemovableObserver>();
	
	//Cette classe sert uniquement � afficher l'endroit o� l'attaque est effectu�e.
	//Elle est mise dans Item car les items sont imprim�s sur la couche sup�rieure.
	
	public Damages(int x, int y) {
		super(x, y, 6);
		displayed=true;
	}

	@Override
	public void run() {
		while(displayed){
			try{
				Thread.sleep(200);
				removableNotifyObserver();
				displayed=false;
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
	}

	@Override
	public void removableAttach(RemovableObserver ro) {
		robs.add(ro);
	}

	@Override
	public  void removableNotifyObserver() {
		for(RemovableObserver ro : robs){
			ro.remove(this);
		}
	}
	
	
	
}
