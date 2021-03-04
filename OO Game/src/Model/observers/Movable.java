package Model.observers;

public interface Movable {
	void movableAttach(MovableObserver mo);
	void movableNotifyObserver();
}
