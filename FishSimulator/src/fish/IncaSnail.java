package fish;

import engine.Diet;
import engine.HappinessStatus;
import engine.Qualify;
import engine.Vector3D;
import engine.Wall;
import graphics.Visual;

import java.util.LinkedList;
import java.util.Random;

import saito.objloader.OBJModel;

public class IncaSnail extends Fish{
	Wall location;
	
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
		this.model.scale(8);
		this.model.translateToCenter();
		this.velocity = new Vector3D(0, 0, 0);
		this.acceleration = new Vector3D(0, 0, 0);
		this.orientation = new Vector3D(0, 0, 0);
		this.dimensions = new Vector3D(70, 49, 46);
		this.position = new Vector3D(0, (int)(.5*window.zoomPercentage*window.fieldY*window.tank.waterLevel+this.dimensions.y/2.0), 0);
		this.location = Wall.FLOOR;

		diet.add(Diet.ALGAE);
		properties.add(Qualify.PREY);
		properties.add(Qualify.SUSCEPTIBLE);
		properties.add(Qualify.PLANTS);
	}
	
	public void updateAcceleration(){
		Random random = new Random();
		switch(this.location){
		case FLOOR:
			this.acceleration.x += random.nextFloat()*.25-.125;
			this.acceleration.z += random.nextFloat()*.25-.125;
			break;
		case LEFT:
			this.acceleration.y += random.nextFloat()*.25-.125;
			this.acceleration.z += random.nextFloat()*.25-.125;
			break;
		case RIGHT:
			this.acceleration.y += random.nextFloat()*.25-.125;
			this.acceleration.z += random.nextFloat()*.25-.125;
			break;
		case BACK:
			this.acceleration.x += random.nextFloat()*.25-.125;
			this.acceleration.y += random.nextFloat()*.25-.125;
			break;
		}
	}
	
	public void updateOrientationRelativeToVelocity(Vector3D velocity){
		double angle = Math.asin(Math.abs(velocity.z)/velocity.magnitude());
		switch(this.location){
		case FLOOR:
			if(velocity.x < 0 && velocity.z > 0) this.orientation.y = (float) angle;
			else if(velocity.x > 0 && velocity.z > 0) this.orientation.y = (float) (Visual.PI - angle);
			else if(velocity.x > 0 && velocity.z < 0) this.orientation.y = (float) (Visual.PI + angle);
			else if(velocity.x < 0 && velocity.z < 0) this.orientation.y = (float) -angle;
			else if(velocity.z == 0 && velocity.x < 0) this.orientation.y = 0;
			else if(velocity.x == 0 && velocity.z > 0) this.orientation.y = (float) (Visual.PI/2.0);
			else if(velocity.z == 0 && velocity.x > 0) this.orientation.y = (float) Visual.PI;
			else if(velocity.x == 0 && velocity.z < 0) this.orientation.y = (float) (3*Visual.PI/2.0);
			break;
		case RIGHT:
			
			break;
		case LEFT:

			break;
		case BACK:
			
			break;
		}
	}
}
