package Model.observers;

public interface OpenDoor {
	void openDoorAttach(OpenDoorObserver odo);
	void openDoorNotifyObserver();
}
