package fish;

import engine.Diet;
import engine.HappinessStatus;
import engine.Qualify;
import engine.Tank;
import engine.Vector3D;
import graphics.Visual;

import java.util.LinkedList;
import java.util.Random;

import saito.objloader.OBJModel;

public class DwarfPuffer extends Fish{
	boolean puffed;
	OBJModel inactiveModel;
	
	public DwarfPuffer(Visual window, String nickname){
		this.name = "Dwarf Puffer";
		this.nickname = nickname;
		this.timeWell = System.currentTimeMillis();
		this.ease = 4;
		this.health = this.ease;
		this.status = HappinessStatus.HAPPY;
		this.maxHappyFull = this.ease*24*60*5;
		this.happiness = this.maxHappyFull;
		this.fullness = this.maxHappyFull;
		this.size = 2.5;
		this.minPH = 6.5;
		this.maxPH = 8.3;
		this.minTemp = 24;
		this.maxTemp = 28;
		this.minHard = 5;
		this.maxHard = 20;
		this.peacefulness = 2;
		this.tankSize = 19;
		this.diet = new LinkedList <Diet>();
		this.region = "middle";
		this.ammonia = 1;
		this.nitrite = 1;
		this.nitrate = 20;
		this.ratio = 2;
		this.schooling = 0;
		this.properties = new LinkedList <Qualify>();
		this.model = new OBJModel(window, "graphics/dwarfpuffer.obj", Visual.POLYGON);
		this.sprite = "graphics/dwarfpuffer.png";
		this.model.scale(28);
		this.model.translateToCenter();
		this.position = new Vector3D(0, 0, 0);
		this.velocity = new Vector3D(0, 0, 0);
		this.acceleration = new Vector3D(0, 0, 0);
		this.orientation = new Vector3D(0, 0, 0);
		this.dimensions = new Vector3D(65, 29, 16);
		this.puffed = false;
		this.inactiveModel = new OBJModel(window, "graphics/dwarfpufferpuffed.obj", Visual.POLYGON);
		this.inactiveModel.scale(28);
		this.inactiveModel.translateToCenter();

		diet.add(Diet.FROZEN);
		diet.add(Diet.LIVE);
		properties.add(Qualify.PREDATOR);
		properties.add(Qualify.AGGRESSIVE);
		properties.add(Qualify.PLANTS);
	}
	
	public void puff(Visual window){
		OBJModel temp = inactiveModel;
		this.inactiveModel = this.model;
		this.model = temp;
		this.dimensions = new Vector3D(60, 37, 27);
		this.puffed = true;
	}
	
	public void unpuff(Visual window){
		OBJModel temp = inactiveModel;
		this.inactiveModel = this.model;
		this.model = temp;
		this.dimensions = new Vector3D(65, 29, 16);
		this.puffed = false;
	}
	
	public void updatePosition(Visual visual){
		this.position.x = centermost((int)(-.4*Visual.zoomPercentage*Visual.fieldX+this.dimensions.x/2.0), this.position.x+this.velocity.x, (int)(.4*Visual.zoomPercentage*Visual.fieldX-this.dimensions.x/2.0));
		this.position.y = centermost((int)(-.5*Visual.zoomPercentage*Visual.fieldY*visual.tank.waterLevel+this.dimensions.y/2.0), this.position.y+this.velocity.y, (int)(.5*Visual.zoomPercentage*Visual.fieldY*visual.tank.waterLevel-this.dimensions.y/2.0));
		this.position.z = centermost((int)(-.25*Visual.zoomPercentage*Visual.fieldZ+this.dimensions.x/2.0), this.position.z+this.velocity.z, (int)(.25*Visual.zoomPercentage*Visual.fieldZ-this.dimensions.x/2.0));
		
		Random random = new Random();		
		if(random.nextInt(100) < 1){
			if(puffed) unpuff(visual);
			else puff(visual);
		}
		
		this.updateVelocity(visual.tank);
	}
	
	public void updateVelocity(Tank tank){
		if(puffed) this.velocity = this.velocity.multiplyScalar(.9999f);
		else{
			this.velocity.x = centermost(-1, this.velocity.x + this.acceleration.x, 1);
			this.velocity.y = centermost(-1, this.velocity.x + this.acceleration.y, 1);
			this.velocity.z = centermost(-1, this.velocity.x + this.acceleration.z, 1);
			this.velocity = this.velocity.addVector(hungerContribution(tank));
		}
		this.updateOrientationRelativeToVelocity(this.velocity);
		this.updateAcceleration();
	}
	
}
