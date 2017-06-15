package application;


import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WatchedDirectory implements Subject{
	
	private List<Observer> observersList;
	private boolean stateChange;
	
	public WatchedDirectory(String path){
		this.observersList = new ArrayList<Observer>();
		stateChange = false;
		Path myDir = Paths.get(path); 
		
		try {
			WatchService watcher = myDir.getFileSystem().newWatchService();
			myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, 
					StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

			WatchKey watckKey = watcher.take();

			List<WatchEvent<?>> events = watckKey.pollEvents();
			for (WatchEvent event : events) {
				if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
					stateChange = true;
					notifyObserver();
					
				}
				if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
					stateChange = true;
					notifyObserver();
				}
				if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
					stateChange = true;
					notifyObserver();
				}
			}

		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
	}
	
	public void registerObserver(Observer observer){
		observersList.add(observer);
	}

	public void notifyObserver(){
		if (stateChange) {
			for (Observer observer : observersList) {
				observer.update();
			}
		}
	}

	public void unRegisterObserver(Observer observer){
		observersList.remove(observer);
	}

	public Boolean getUpdate(){
		Boolean changedState = null;
		// should have logic to send the
		// state change to querying observer
		if (stateChange) {
			changedState = true;
		}
		return changedState;
	}
}
