package cls.island.utils;

import java.util.concurrent.locks.Lock;

import cls.island.utils.concurrent.AutoReentrantLock;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class TimelineSync {
	
	private Timeline timeline;
	private Lock lock;
		
	public TimelineSync() {
		timeline = new Timeline();
		timeline.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				TimelineSync.this.lock.unlock();
				
			}
		});
		lock = new AutoReentrantLock();
	}
	
	

	public void play(){
		lock.lock();
		timeline.play();
	}
	
	public final void setOnFinished(final EventHandler<ActionEvent> eventHandler){
		EventHandler<ActionEvent> extendedEventHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				eventHandler.handle(event);
				lock.unlock();
			}
		};
		timeline.setOnFinished(extendedEventHandler);
	}

	public  ObservableList<KeyFrame> getKeyFrames() {
		return timeline.getKeyFrames();
	}
	
	public Lock getLock(){
		return lock;
	}

}
