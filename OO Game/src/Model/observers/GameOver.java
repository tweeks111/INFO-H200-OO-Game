package Model.observers;

public interface GameOver {
	void gameOverAttachObserver(GameOverObserver goo);
	void gameOverNotifyObserver();
}
