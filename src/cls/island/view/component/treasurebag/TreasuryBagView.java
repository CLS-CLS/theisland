package cls.island.view.component.treasurebag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import cls.island.view.component.AbstractView;
import cls.island.view.component.treasury.card.Type;
import cls.island.view.component.treasury.card.Type.Ability;

public class TreasuryBagView extends AbstractView<TreasureBag>{

	private final ImageView earth;
	private final ImageView chalice;
	private final ImageView fire;
	private final ImageView wind;
	private Map<Type, ImageView> mapping = new HashMap<>();
	public static final int saturationUnaquiredEffectValue = 0;
	
	public TreasuryBagView(TreasureBag model, Image earth, Image chalice, Image fire, Image wind) {
		super(true, model);
		HBox container = new HBox();
		this.earth = new ImageView(earth);
		this.chalice = new ImageView(chalice);
		this.fire = new ImageView(fire);
		this.wind = new ImageView(wind);
		container.getChildren().add(this.earth);
		container.getChildren().add(this.chalice);
		container.getChildren().add(this.fire);
		container.getChildren().add(this.wind);
		mapping.put(Type.CRYSTAL_OF_FIRE, this.fire);
		mapping.put(Type.EARTH_STONE, this.earth);
		mapping.put(Type.OCEAN_CHALICE, this.chalice);
		mapping.put(Type.STATUE_OF_WIND, this.wind);
	
		
		List<Type> unaquiredTreasures = new ArrayList<>(Type.getTypesWithAbility(Ability.TREASURE));
		unaquiredTreasures.removeAll(model.getAcquiredTreaures());
		for (Type treasureType : unaquiredTreasures){
			mapping.get(treasureType).setEffect(createUnacqiredEffect());
		}
		getChildren().add(container);
		
	}
	
	private Effect createUnacqiredEffect(){
		return  new ColorAdjust(0,-1,0,0);
	}
	
	public void removeEffect(final Type type){
		mapping.get(type).setEffect(null);
	}
	
	public void addEffect(final Type type){
		mapping.get(type).setEffect(createUnacqiredEffect());
	}
	
	
	
	

}
