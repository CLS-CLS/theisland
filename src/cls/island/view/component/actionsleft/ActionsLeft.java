package cls.island.view.component.actionsleft;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import cls.island.model.player.Player;

public class ActionsLeft {
	
	private IntegerProperty actionsLeft = new SimpleIntegerProperty();
	private ActionsLeftView actionsLeftView;
		
	public ActionsLeft() {
		actionsLeftView = new ActionsLeftView(this, actionsLeft);
	}
	
	public void setPlayer(Player player){
		bindPropertyToPlayer(player);
	}

	private void bindPropertyToPlayer(Player player) {
		actionsLeft.unbind();
		actionsLeft.bind(player.actionsLeftProperty());
	}
	
	public ActionsLeftView getComponent(){
		return actionsLeftView;
	}

}
