package application;

public interface Subject {
	
	public void registerObserver(Observer observer);

	public void notifyObserver();

	public void unRegisterObserver(Observer observer);

	public Boolean getUpdate();
}
