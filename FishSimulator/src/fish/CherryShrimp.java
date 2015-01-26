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

public class CherryShrimp extends Fish{

	public CherryShrimp(Visual window, String nickname){
		this.name = "Cherry Shrimp";
		this.nickname = nickname;
		this.timeWell = System.currentTimeMillis();
		this.ease = 5;
		this.health = this.ease;
		this.status = HappinessStatus.HAPPY;
		this.maxHappyFull = this.ease*24*60*5;
		this.happiness = this.maxHappyFull;
		this.fullness = this.maxHappyFull;
		this.size = 3;
		this.minPH = 7;
		this.maxPH = 8;
		this.minTemp = 15;
		this.maxTemp = 28;
		this.minHard = 3;
		this.maxHard = 15;
		this.peacefulness = 5;
		this.tankSize = 5;
		this.diet = new LinkedList <Diet>();
		this.region = "floor";
		this.ammonia = 1;
		this.nitrite = 1;
		this.nitrate = 25;
		this.ratio = 0;
		this.schooling = 0;
		this.properties = new LinkedList <Qualify>();
		this.model = new OBJModel(window, "graphics/cherryshrimp.obj", Visual.POLYGON);
		this.sprite = "graphics/cherryshrimp.png";
		this.model.scale(25);
		this.model.translateToCenter();
		this.velocity = new Vector3D(0, 0, 0);
		this.acceleration = new Vector3D(0, 0, 0);
		this.orientation = new Vector3D(0, 0, 0);
		this.dimensions = new Vector3D(84, 13, 35);
		this.position = new Vector3D(0, (int)(.5*Visual.zoomPercentage*Visual.fieldY*window.tank.waterLevel-this.dimensions.y/2.0), 0);

		diet.add(Diet.ALGAE);
		diet.add(Diet.FLAKE);
		properties.add(Qualify.PREY);
		properties.add(Qualify.SUSCEPTIBLE);
	}

	public void updateAcceleration(){
		Random random = new Random();
		this.acceleration.x += random.nextFloat()*.125-.0625;
		this.acceleration.y = 0;
		this.acceleration.z += random.nextFloat()*.125-.0625;
	}

	public void updateVelocity(Tank tank){
		this.velocity.x = centermost(-.5f, this.velocity.x + this.acceleration.x, .5f);
		this.velocity.y = 0;
		this.velocity.z = centermost(-.5f, this.velocity.z + this.acceleration.z, .5f);
		Vector3D hungerContribution = hungerContribution(tank);
		hungerContribution.y = 0;
		this.velocity = this.velocity.addVector(hungerContribution);
		this.updateOrientationRelativeToVelocity(this.velocity);
		this.updateAcceleration();
	}

	public void updateOrientationRelativeToVelocity(Vector3D velocity){
		double angle = Math.asin(Math.abs(velocity.z)/velocity.magnitude());
		this.orientation.z = 0;
		if(velocity.x > 0 && velocity.z < 0) this.orientation.y = (float) angle;
		else if(velocity.x < 0 && velocity.z < 0) this.orientation.y = (float) (Visual.PI - angle);
		else if(velocity.x < 0 && velocity.z > 0) this.orientation.y = (float) (Visual.PI + angle);
		else if(velocity.x > 0 && velocity.z > 0) this.orientation.y = (float) -angle;
		else if(velocity.z == 0 && velocity.x > 0) this.orientation.y = 0;
		else if(velocity.x == 0 && velocity.z < 0) this.orientation.y = (float) (Visual.PI/2.0);
		else if(velocity.z == 0 && velocity.x < 0) this.orientation.y = (float) Visual.PI;
		else if(velocity.x == 0 && velocity.z > 0) this.orientation.y = (float) (3*Visual.PI/2.0);
	}
	
	public void skipAhead(Visual visual){
		Random random = new Random();
		this.position.x = (int)(-.4*Visual.zoomPercentage*Visual.fieldX+this.dimensions.x/2.0) + 5 + random.nextFloat() * (int)(.8*Visual.zoomPercentage*Visual.fieldX-this.dimensions.x/2.0-10);
		this.position.y = (int)(.5*Visual.zoomPercentage*Visual.fieldY*visual.tank.waterLevel+this.dimensions.y/2.0);
		this.position.z = (int)(-.25*Visual.zoomPercentage*Visual.fieldZ+this.dimensions.x/2.0) + 5 + random.nextFloat() * (int)(.5*Visual.zoomPercentage*Visual.fieldZ-this.dimensions.x/2.0-10);
	}
}
