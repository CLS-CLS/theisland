package cls.island.utils;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;


public class TimelineSingle{
	/**
	 * The process methods of the Animations.
	 * * ASYNC the timelineSingle is processed asynchronously, it will run independently
	 *  of the other timelineSingles.
	 * * SYNC The timeline is added in FIFO queue and it will be activated after all the previously
	 *  timelineSingles with process status <code>Process.SYNC</code> finish. This is the default
	 *  behavior if no process type is selected. 
	 * * OFF there timelineSigle will set the final values to the provided properties directly without
	 * performing any animation. This is like the duration time is set to 0. 
	 **/
	public enum Process {
		ASYNC, SYNC, OFF
	}
	
	private static TimeLineManager manager = TimeLineManager.getInstance();
	
	Timeline timeline = new Timeline();
	final Process sync;
	
	private EventHandler<ActionEvent> eventHandler;
	
	public TimelineSingle(Process sync){
		this.sync = sync;
		timeline.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				manager.notifyFinished(TimelineSingle.this);
			}
		});
	}
	
	public TimelineSingle() {
		this(Process.SYNC);
	}
	
	public TimelineSingle(EventHandler<ActionEvent> onFinish, Process sync) {
		if (onFinish == null) throw new IllegalArgumentException("Are you fucking idiot??");
		this.sync = sync;
		setOnFinished(onFinish);
	}
	
	public TimelineSingle(EventHandler<ActionEvent> onFinish) {
		this(onFinish, Process.SYNC);
	}

	public final void setOnFinished(final EventHandler<ActionEvent> eventHandler){
		this.eventHandler = eventHandler;
		EventHandler<ActionEvent> extendedEventHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				eventHandler.handle(event);
				manager.notifyFinished(TimelineSingle.this);
			}
		};
		timeline.setOnFinished(extendedEventHandler);
	}
	
	public final void play(){
		if (timeline.getStatus() == Status.STOPPED){
			if (sync == Process.SYNC){
				manager.play(this);
			}else if (sync == Process.ASYNC) {
				manager.playAsync(this);
			}else {
				playOff();
			}
		}
		if (timeline.getStatus() == Status.PAUSED){
			timeline.play();
		}
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void playOff() {
		for (KeyFrame kf : timeline.getKeyFrames()){
			for (KeyValue kv : kf.getValues()){
				((WritableValue)kv.getTarget()).setValue(kv.getEndValue());
			}
		}
		timeline.getOnFinished().handle(null);
	}
	
	
	public final BooleanProperty autoReverseProperty() {
		return timeline.autoReverseProperty();
	}


	public final ReadOnlyDoubleProperty currentRateProperty() {
		return timeline.currentRateProperty();
	}


	public final ReadOnlyObjectProperty<Duration> currentTimeProperty() {
		return timeline.currentTimeProperty();
	}


	public final IntegerProperty cycleCountProperty() {
		return timeline.cycleCountProperty();
	}


	public final ReadOnlyObjectProperty<Duration> cycleDurationProperty() {
		return timeline.cycleDurationProperty();
	}


	public final ObjectProperty<Duration> delayProperty() {
		return timeline.delayProperty();
	}


	public boolean equals(Object obj) {
		return timeline.equals(obj);
	}


	public final ObservableMap<String, Duration> getCuePoints() {
		return timeline.getCuePoints();
	}


	public final double getCurrentRate() {
		return timeline.getCurrentRate();
	}


	public final Duration getCurrentTime() {
		return timeline.getCurrentTime();
	}


	public final int getCycleCount() {
		return timeline.getCycleCount();
	}


	public final Duration getCycleDuration() {
		return timeline.getCycleDuration();
	}


	public final Duration getDelay() {
		return timeline.getDelay();
	}


	public final ObservableList<KeyFrame> getKeyFrames() {
		return timeline.getKeyFrames();
	}


	public final EventHandler<ActionEvent> getOnFinished() {
		return eventHandler;
	}


	public final double getRate() {
		return timeline.getRate();
	}


	public final Status getStatus() {
		return timeline.getStatus();
	}


	public final double getTargetFramerate() {
		return timeline.getTargetFramerate();
	}


	public final Duration getTotalDuration() {
		return timeline.getTotalDuration();
	}


	public int hashCode() {
		return timeline.hashCode();
	}


	public final boolean isAutoReverse() {
		return timeline.isAutoReverse();
	}


	public void jumpTo(Duration time) {
		timeline.jumpTo(time);
	}


	public void jumpTo(String cuePoint) {
		timeline.jumpTo(cuePoint);
	}


	public final ObjectProperty<EventHandler<ActionEvent>> onFinishedProperty() {
		return timeline.onFinishedProperty();
	}


	public void pause() {
		timeline.pause();
	}


	public final DoubleProperty rateProperty() {
		return timeline.rateProperty();
	}


	public final void setAutoReverse(boolean value) {
		timeline.setAutoReverse(value);
	}


	public final void setCycleCount(int value) {
		timeline.setCycleCount(value);
	}


	public final void setDelay(Duration value) {
		timeline.setDelay(value);
	}


	public final void setRate(double value) {
		timeline.setRate(value);
	}


	public final ReadOnlyObjectProperty<Status> statusProperty() {
		return timeline.statusProperty();
	}


	public void stop() {
		timeline.stop();
	}


	public String toString() {
		return timeline.toString();
	}


	public final ReadOnlyObjectProperty<Duration> totalDurationProperty() {
		return timeline.totalDurationProperty();
	}
	
	
	

}
