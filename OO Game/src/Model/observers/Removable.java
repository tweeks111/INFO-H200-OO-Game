package Model.observers;

public interface Removable {
	void removableAttach(RemovableObserver ro);
	void removableNotifyObserver();
}
