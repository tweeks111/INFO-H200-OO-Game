package Model.observers;

public interface MapChange {
	void mapChangeAttach(MapChangeObserver mco);
	void mapChangeNotifyObserver();
}
