package cls.island.view.component.actionsleft;

import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import cls.island.view.component.AbstractView;

public class ActionsLeftView extends AbstractView<ActionsLeft>{

	public ActionsLeftView(ActionsLeft model, IntegerProperty actionLeft) {
		super(true, model);
		HBox hbox = new HBox();
		getChildren().add(hbox);
		Label label = new Label("Actions Left:");
		label.getStyleClass().add("standard-label");
		hbox.getChildren().add(label);
		final Text text = new Text();
		text.getStyleClass().add("standard-label");
		hbox.getChildren().add(text);
		actionLeft.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				text.setText(""+newValue);
			}
			
		});
	}
	
	

}
