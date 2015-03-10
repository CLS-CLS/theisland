package cls.island.view.component.actionsleft;

import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import cls.island.view.component.AbstractView;

public class ActionsLeftView extends AbstractView<ActionsLeft>{

	public ActionsLeftView(ActionsLeft model, IntegerProperty actionLeft) {
		super(true, model);
		HBox hbox = new HBox();
		getChildren().add(hbox);
		Label label = new Label("Actions Left:");
		label.setFont(Font.font(40));
		hbox.getChildren().add(label);
		
		actionLeft.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				label.setText("Actions Left: " +newValue);
			}
			
		});
	}
	
	

}
