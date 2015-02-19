package cls.island.utils;

import com.sun.javafx.tk.Toolkit;

@SuppressWarnings("restriction")
public class FxThreadBlock  {

	public void execute(final Runnable runnable) {
		runnable.run();
		Toolkit.getToolkit().enterNestedEventLoop(this);
	}

	public void unpause() {
		Toolkit.getToolkit().exitNestedEventLoop(this, null);
	}

}
