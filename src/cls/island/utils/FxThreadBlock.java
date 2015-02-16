package cls.island.utils;

import javafx.application.Platform;
import cls.island.utils.concurrent.SignaledRunnable;
import cls.island.view.component.ThreadBlock;

import com.sun.javafx.tk.Toolkit;

public class FxThreadBlock implements ThreadBlock {

	@Override
	public void execute(final SignaledRunnable runnable) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				runnable.run();
			}
		});
		if (runnable.willSignal()) {
			System.out.println(Thread.currentThread().getName() + " Waiting ");
			Toolkit.getToolkit().enterNestedEventLoop(this);
		}
	}

	@Override
	public void unpause() {
		Toolkit.getToolkit().exitNestedEventLoop(this, null);
	}

}
