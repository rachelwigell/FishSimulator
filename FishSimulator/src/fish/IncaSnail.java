package fish;

import engine.Diet;
import engine.HappinessStatus;
import engine.Qualify;
import engine.Vector3D;
import graphics.Visual;

import java.util.LinkedList;

import saito.objloader.OBJModel;

public class IncaSnail extends Fish{

	public IncaSnail(Visual window, String nickname){
		this.name = "Inca Snail";
		this.nickname = nickname;
		this.timeWell = System.currentTimeMillis();
		this.ease = 5;
		this.health = this.ease;
		this.status = HappinessStatus.HAPPY;
		this.maxHappyFull = this.ease*24*60*5;
		this.happiness = this.maxHappyFull;
		this.fullness = this.maxHappyFull;
		this.size = 5;
		this.minPH = 7;
		this.maxPH = 8.5;
		this.minTemp = 18;
		this.maxTemp = 27;
		this.minHard = 5;
		this.maxHard = 20;
		this.peacefulness = 5;
		this.tankSize = 19;
		this.diet = new LinkedList <Diet>();
		this.region = "surfaces";
		this.ammonia = 1000000;
		this.nitrite = 1000000;
		this.nitrate = 1000000;
		this.ratio = 0;
		this.schooling = 0;
		this.properties = new LinkedList <Qualify>();
		this.model = new OBJModel(window, "incasnail.obj", Visual.POLYGON);
		this.sprite = "incasnail.png";
		this.model.scale(20);
		this.model.translateToCenter();
		this.position = new Vector3D(0, 0, 0);
		this.velocity = new Vector3D(0, 0, 0);
		this.acceleration = new Vector3D(0, 0, 0);
		this.orientation = new Vector3D(0, 0, 0);
		this.dimensions = new Vector3D(75, 36, 41);

		diet.add(Diet.ALGAE);
		properties.add(Qualify.PREY);
		properties.add(Qualify.SUSCEPTIBLE);
		properties.add(Qualify.PLANTS);
	}
}
