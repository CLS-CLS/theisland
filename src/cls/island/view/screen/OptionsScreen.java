package cls.island.view.screen;

import java.util.ArrayList;

import cls.island.control.PlayerAndColor;

public interface OptionsScreen {

	ArrayList<PlayerAndColor> getSelectedPlayers();

	int getFloodStartingLevel();

	void prepareNewScreen();

}
