package fish;

import engine.Diet;
import engine.HappinessStatus;
import engine.Qualify;
import engine.Vector3D;
import graphics.Visual;

import java.util.LinkedList;

import saito.objloader.OBJModel;

public class WhiteCloudMountainMinnow extends Fish {
	public WhiteCloudMountainMinnow(Visual window, String nickname){
		this.name = "White Cloud Mountain Minnow";
		this.nickname = nickname;
		this.timeWell = System.currentTimeMillis();
		this.ease = 5;
		this.health = this.ease;
		this.status = HappinessStatus.HAPPY;
		this.maxHappyFull = this.ease*24*60*5;
		this.happiness = this.maxHappyFull;
		this.fullness = this.maxHappyFull;
		this.size = 4;
		this.minPH = 6;
		this.maxPH = 8.5;
		this.minTemp = 7;
		this.maxTemp = 24;
		this.minHard = 5;
		this.maxHard = 25;
		this.peacefulness = 4;
		this.tankSize = 19;
		this.diet = new LinkedList <Diet>();
		this.region = "all";
		this.ammonia = 1000000;
		this.nitrite = 1000000;
		this.nitrate = 1000000;
		this.ratio = 0;
		this.schooling = 0;
		this.properties = new LinkedList <Qualify>();
		this.model = new OBJModel(window, "graphics/whitecloudmountainminnow.obj", Visual.POLYGON);
		this.sprite = "graphics/whitecloudmountainminnow.png";
		this.model.scale(20);
		this.model.translateToCenter();
		this.position = new Vector3D(0, 0, 0);
		this.velocity = new Vector3D(0, 0, 0);
		this.acceleration = new Vector3D(0, 0, 0);
		this.orientation = new Vector3D(0, 0, 0);
		this.dimensions = new Vector3D(78, 26, 6);

		diet.add(Diet.FLAKE);
		diet.add(Diet.FROZEN);
		diet.add(Diet.LIVE);
		properties.add(Qualify.AGGRESSIVE);
		properties.add(Qualify.PLANTS);
	}
}
