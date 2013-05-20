package cls.island.utils;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class TimeLineManager {

	private static final TimeLineManager INSTANCE = new TimeLineManager();
	
	private TimeLineManager(){}
	
	private volatile int index  = -1;
	
	List<TimelineSingle> timelines = new ArrayList<>();
	private EventHandler<ActionEvent> handlerStart = new EmptyActionHandler();
	private EventHandler<ActionEvent> handlerFinish = new EmptyActionHandler();

	public static TimeLineManager getInstance() {
		return INSTANCE;
	}

	protected void notifyFinished(TimelineSingle timeLineExt) {
		if (!isPlaying()){
			handlerFinish.handle(null);
			synchronized (timelines) {
				timelines.clear();
				index = -1;
			}
		}else {
			playNext();
		}
		
	}

	private void playNext() {
		synchronized (timelines) {
			index++;
		}
		timelines.get(index).timeline.play();
	}

	protected boolean isPlaying() {
		synchronized (timelines) {
			return timelines.size() > index +1;
		}
	}

	protected void play(TimelineSingle timeLineExt) {
		synchronized (timelines) {
			timelines.add(timeLineExt);
		}
		if (index ==-1){
			handlerStart.handle(null);
			playNext();
		}
	}
	

	protected void playAsync(TimelineSingle timelineSingle) {
		handlerStart.handle(null);
		timelineSingle.timeline.play();
		
	}

	public void registerNotifyStart(EventHandler<ActionEvent> handler) {
		this.handlerStart = handler;
	}

	public void registerNotifyFinish(EventHandler<ActionEvent> handler) {
		this.handlerFinish = handler;
	}
	
	
	private class EmptyActionHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {		
		}
		
	}


	

//	Collection<TimeLineExt> timelines;
//
//	private EventHandler<ActionEvent> handlerFinish;
//
//	private EventHandler<ActionEvent> handlerStart;
//
//	private static final TimeLineManager INSTANCE = new TimeLineManager();
//
//	public static TimeLineManager getInstance() {
//		return INSTANCE;
//	}
//
//	private TimeLineManager() {
//		timelines = Collections
//				.synchronizedCollection(new ArrayList<TimeLineExt>());
//	}
//
//	protected synchronized void play(TimeLineExt timeLineExt) {
//
//		timelines.add(timeLineExt);
//		if (timelines.size() == 1) {
//			if (handlerStart != null) {
//				handlerStart.handle(new ActionEvent());
//			}
//			System.out.println("Start Playing timeline "  + timeLineExt);
//			timeLineExt.timeline.play();
//		}
//	}
//
//	protected synchronized void notifyFinished(TimeLineExt ext) {
//		System.out.println("Finished " + ext);
//		Iterator<TimeLineExt> iter = timelines.iterator();
//		if (ext!=iter.next())throw new RuntimeException("WTF");
//		iter.remove();
//		
//		if (iter.hasNext()) {
//			playNext(iter.next());
//		} else {
//			if (handlerStart != null) {
//				this.handlerFinish.handle(new ActionEvent());
//			}
//		}
//
//	}
//
//	private synchronized void playNext(TimeLineExt timeLineExt) {
//		System.out.println("Playing next = " + timeLineExt);
//		timeLineExt.timeline.play();
//	}
//
//	public synchronized boolean isPlaying() {
//		return timelines.size() > 0;
//	}
//
//	public void registerNotifyStart(EventHandler<ActionEvent> handler) {
//		this.handlerStart = handler;
//	}
//
//	public void registerNotifyFinish(EventHandler<ActionEvent> handler) {
//		this.handlerFinish = handler;
//	}


}
