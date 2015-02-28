package cls.island.view.screen;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.Reflection;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import cls.island.control.Config;
import cls.island.control.MainController;
import cls.island.control.Options;
import cls.island.utils.ButtonFactory;
import cls.island.utils.TimelineSingle;

public class SelectPlayerScreen extends AbstractScreen {
	protected static final int MAX_RANDOM_PLAYERS = 4;

	List<Combo> players = new ArrayList<>();
	ComboRandom randomNode;

	public SelectPlayerScreen(final MainController controller, Config config) {
		super(controller, config);
		Group root = new Group();
		Group main = new Group();
		this.getChildren().add(root);
		this.getChildren().add(main);
		randomNode = new ComboRandom(new ImageView(config.getRandomPlayerImage()),
				Options.PlayerType.RANDOM.name());

		final Combo diverNode = new Combo(new ImageView(config.getDiverImage()),
				randomNode.rndCombo, Options.PlayerType.DIVER.name());
		final Combo explorerNode = new Combo(new ImageView(config.getExplorerImage()),
				randomNode.rndCombo, Options.PlayerType.EXPLORER.name());
		final Combo pilotNode = new Combo(new ImageView(config.getDiverImage()),
				randomNode.rndCombo, Options.PlayerType.PILOT.name());
		final Combo lol2Node = new Combo(new ImageView(config.getDiverImage()),
				randomNode.rndCombo, Options.PlayerType.FORGOTTEN.name());

		randomNode.rndCombo.selected.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
					Boolean newValue) {
				if (newValue) {
					diverNode.setSelected(false);
					explorerNode.setSelected(false);
					pilotNode.setSelected(false);
					lol2Node.setSelected(false);
				}
			}
		});

		players.add(diverNode);
		players.add(explorerNode);
		players.add(pilotNode);
		players.add(lol2Node);

		diverNode.relocate(200, 180);
		explorerNode.relocate(400, 180);
		pilotNode.relocate(600, 180);
		lol2Node.relocate(800, 180);
		randomNode.relocate(1100, 180);
		Label text = new Label("Select the players to land... \n"
				+ "         ...on the forbidden island");
		text.getStyleClass().add("select-player-msg");
		text.relocate(50, 50);

		Light.Distant light = new Light.Distant();
		light.setAzimuth(-45);
		light.setElevation(45);
		Lighting l = new Lighting();
		l.setLight(light);
		l.setSurfaceScale(3.5);
		l.setDiffuseConstant(2);
		Reflection refl = new Reflection();
		refl.setTopOffset(10);
		refl.setInput(l);
		main.setEffect(refl);

		VBox buttons = new VBox();
		buttons.relocate(700, 750);
		buttons.getStyleClass().add("gen-vBox");
		buttons.setFillWidth(true);

		Button back = ButtonFactory.genButton("Back");
		back.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				controller.goToMainScreen(SelectPlayerScreen.this);
			}
		});

		Button goToIsland = ButtonFactory.genButton("Go To Island");
		goToIsland.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				controller.goToIslandScreen();
			}
		});

		buttons.getChildren().add(goToIsland);
		buttons.getChildren().add(back);

		root.getChildren().add(text);
		main.getChildren().add(diverNode);
		main.getChildren().add(explorerNode);
		main.getChildren().add(pilotNode);
		main.getChildren().add(lol2Node);
		main.getChildren().add(randomNode);
		root.getChildren().add(buttons);

	}

	private class Combo extends VBox {
		private ImageView imageView;
		private Label selectedNode = new Label("", new ImageView(config.getTickImage()));
		private BooleanProperty selected = new SimpleBooleanProperty(false);
		private Effect unselectedEffect = new ColorAdjust(0, -1, 0, 0);
		private Combo random;
		private String description;

		public Combo(final ImageView imageView, final Combo randomCombo, String description) {
			this.imageView = imageView;
			this.description = description;
			imageView.getStyleClass().add("clickable");
			this.random = randomCombo;
			selectedNode.setOpacity(0);
			setSpacing(10);
			setAlignment(Pos.CENTER);

			imageView.setEffect(unselectedEffect);
			imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					setSelected(!selected.getValue());
				}
			});
			Label text = new Label(description);
			text.getStyleClass().add("white-text-button");

			getChildren().add(selectedNode);
			getChildren().add(text);
			getChildren().add(imageView);

		}

		public void setSelected(boolean selected) {
			this.selected.set(selected);
			if (selected) {
				selectedNode.setOpacity(1);
				imageView.setEffect(null);
				if (random != null) {
					random.setSelected(false);
				}
			} else {
				selectedNode.setOpacity(0);
				imageView.setEffect(unselectedEffect);
			}
		}

		public boolean isSelected() {
			return selected.getValue();
		}

		public String getDescription() {
			return description;
		}
	}

	private class ComboRandom extends HBox {

		private Combo rndCombo;
		private int randomPlayers = 2;

		public ComboRandom(ImageView imageView, String description) {
			super(5);
			setAlignment(Pos.BOTTOM_LEFT);
			final Label playersNumber = new Label("" + randomPlayers);
			playersNumber.getStyleClass().add("select-player-msg");
			playersNumber.getStyleClass().add("large-size-text");

			playersNumber.setMinHeight(USE_PREF_SIZE);
			playersNumber.setMinWidth(USE_PREF_SIZE);

			final HBox buttons = new HBox();
			buttons.setFillHeight(true);
			buttons.getStyleClass().add("gen-hBox");
			buttons.setEffect(null);

			Button plus = ButtonFactory.genButton("+");
			Button minus = ButtonFactory.genButton("-");

			plus.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (randomPlayers < MAX_RANDOM_PLAYERS) {
						randomPlayers++;
						playersNumber.setText("" + randomPlayers);
					}
				}
			});
			minus.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (randomPlayers > 1) {
						randomPlayers--;
						playersNumber.setText("" + randomPlayers);
					}
				}
			});

			final VBox textWithbuttons = new VBox();
			textWithbuttons.setOpacity(0);

			rndCombo = new Combo(imageView, null, description) {
				private void animate(double opacity) {
					TimelineSingle opacityTimeline = new TimelineSingle();
					opacityTimeline.getKeyFrames().add(
							new KeyFrame(Duration.millis(200), new KeyValue(textWithbuttons
									.opacityProperty(), opacity)));
					opacityTimeline.play();
				}

				@Override
				public void setSelected(boolean selected) {
					super.setSelected(selected);
					if (selected) {
						animate(1);
					} else {
						animate(0);
					}
				}
			};

			Label playersLabel = new Label("Players");
			playersLabel.getStyleClass().add("white-text-button");

			textWithbuttons.getStyleClass().add("gen-vBox");
			textWithbuttons.setAlignment(Pos.BOTTOM_CENTER);
			buttons.getChildren().add(minus);
			buttons.getChildren().add(plus);
			textWithbuttons.getChildren().add(playersLabel);

			textWithbuttons.getChildren().add(playersNumber);
			textWithbuttons.getChildren().add(buttons);
			getChildren().add(rndCombo);
			getChildren().add(textWithbuttons);

		}
	}

	public ArrayList<Options.PlayerType> getSelectedPlayers() {
		ArrayList<Options.PlayerType> enumPlayers = new ArrayList<>();
		for (Combo player : players) {
			if (player.isSelected()) {
				enumPlayers.add(Options.PlayerType.valueOf(player.getDescription()));
			}
		}
		if (randomNode.rndCombo.isSelected()) {
			for (int i = 0; i < randomNode.randomPlayers; i++) {
				enumPlayers.add(Options.PlayerType.valueOf(randomNode.rndCombo.getDescription()));
			}
		}
		return enumPlayers;
	}

	@Override
	public void c_setIslandComponentsSelectable(boolean selectable) {
		throw new UnsupportedOperationException();
		
	}
	
	
}
