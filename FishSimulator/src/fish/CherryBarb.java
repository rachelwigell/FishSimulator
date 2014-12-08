package fish;

import engine.Diet;
import engine.HappinessStatus;
import engine.Qualify;
import engine.Vector3D;
import graphics.Visual;

import java.util.LinkedList;

import saito.objloader.OBJModel;

public class CherryBarb extends Fish{
	public CherryBarb(Visual window, String nickname){
		this.name = "Cherry Barb";
		this.nickname = nickname;
		this.timeWell = System.currentTimeMillis();
		this.ease = 5;
		this.health = this.ease;
		this.status = HappinessStatus.HAPPY;
		this.maxHappyFull = this.ease*24*60*5;
		this.happiness = this.maxHappyFull;
		this.fullness = this.maxHappyFull;
		this.size = 5;
		this.minPH = 5.5;
		this.maxPH = 8;
		this.minTemp = 22;
		this.maxTemp = 28;
		this.minHard = 5;
		this.maxHard = 25;
		this.peacefulness = 5;
		this.tankSize = 19;
		this.diet = new LinkedList <Diet>();
		this.region = "all";
		this.ammonia = 1000000;
		this.nitrite = 1000000;
		this.nitrate = 1000000;
		this.ratio = 2;
		this.schooling = 6;
		this.properties = new LinkedList <Qualify>();
		this.model = new OBJModel(window, "cherrybarb.obj", Visual.POLYGON);
		this.sprite = "cherrybarb.png";
		this.model.scale(12);
		this.model.translateToCenter();
		this.position = new Vector3D(0, 0, 0);
		this.velocity = new Vector3D(0, 0, 0);
		this.acceleration = new Vector3D(0, 0, 0);
		this.orientation = new Vector3D(0, 0, 0);
		this.dimensions = new Vector3D(59, 24, 9);

		diet.add(Diet.FLAKE);
		diet.add(Diet.FROZEN);
		diet.add(Diet.LIVE);
		properties.add(Qualify.SUSCEPTIBLE);
		properties.add(Qualify.PLANTS);
	}

	public CherryBarb(Visual window,
			String nickname,
			HappinessStatus status,
			long timeWell,
			long fullness,
			long happiness,
			int health){
		this.name = "CherryBarb";
		this.nickname = nickname;
		this.timeWell = timeWell;
		this.ease = 5;
		this.health = health;
		this.status = status;
		this.maxHappyFull = this.ease*24*60*5;
		this.happiness = happiness;
		this.fullness = fullness;
		this.size = 5;
		this.minPH = 5.5;
		this.maxPH = 8;
		this.minTemp = 22;
		this.maxTemp = 28;
		this.minHard = 5;
		this.maxHard = 25;
		this.peacefulness = 5;
		this.tankSize = 19;
		this.diet = new LinkedList <Diet>();
		this.region = "all";
		this.ammonia = 1000000;
		this.nitrite = 1000000;
		this.nitrate = 1000000;
		this.ratio = 2;
		this.schooling = 0;
		this.properties = new LinkedList <Qualify>();
		this.model = new OBJModel(window, "cherrybarb.obj", Visual.POLYGON);
		this.sprite = "cherrybarb.png";
		this.model.scale(12);
		this.model.translateToCenter();
		this.position = new Vector3D(0, 0, 0);
		this.orientation = new Vector3D(0, 0, 0);
		this.dimensions = new Vector3D(59, 24, 9);
		this.velocity = new Vector3D(0, 0, 0);
		this.acceleration = new Vector3D(0, 0, 0);

		diet.add(Diet.FLAKE);
		diet.add(Diet.FROZEN);
		diet.add(Diet.LIVE);
		properties.add(Qualify.SUSCEPTIBLE);
		properties.add(Qualify.PLANTS);
	}
}
