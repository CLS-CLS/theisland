package cls.island.utils.concurrent;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class TestGameThread {

	public void testConcurrent() {
		Logger logger = Logger.getLogger(GameThread.class.getName());
		logger.setLevel(Level.INFO);
		logger.addHandler(new Handler() {
			
			@Override
			public void publish(LogRecord record) {
				System.out.println(record.getMessage());
				
			}
			
			@Override
			public void flush() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void close() throws SecurityException {
				// TODO Auto-generated method stub
				
			}
		});
		
		new Thread("thread_1") {
			public void run() {
				System.out.println(Thread.currentThread() + " sout 1");
				ThreadUtil.Runlater(new Runnable() {

					@Override
					public void run() {
						System.out.println(Thread.currentThread() + " sout 2");

					}
				});
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				ThreadUtil.Runlater(new Runnable() {

					@Override
					public void run() {
						System.out.println(Thread.currentThread() + " sout 3");
						ThreadUtil.Runlater(new Runnable() {

							@Override
							public void run() {
								System.out.println(Thread.currentThread() + " sout 4");

							}
						});

					}
				});
				

			}
		}.start();

	}
	
	public static void main(String[] args) {
		new TestGameThread().testConcurrent();
	}

}
