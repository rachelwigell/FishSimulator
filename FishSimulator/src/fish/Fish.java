/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish;

import java.util.LinkedList;
import java.util.Random;

import saito.objloader.OBJModel;
import engine.Diet;
import engine.HappinessStatus;
import engine.Qualify;
import engine.Tank;
import engine.Vector3D;
import graphics.Visual;

public abstract class Fish {
	public String name;
	public String nickname;
	public HappinessStatus status = HappinessStatus.HAPPY;
	public long timeWell; //if status == happy, this is the time at which it first became happy. else Long.MAX_VALUE
	public long fullness;
	public long maxHappyFull;
	public long happiness;
	public int health;
	public int ease;
	public double size;
	public double minPH;
	public double maxPH;
	public double minTemp;
	public double maxTemp;
	public double minHard;
	public double maxHard;
	public int peacefulness;
	public int tankSize;
	public LinkedList <Diet> diet;
	public String region;
	public double ammonia;
	public double nitrite;
	public double nitrate;
	public double ratio;
	public int schooling;
	public LinkedList <Qualify> properties;
	public OBJModel model;
	public String sprite;
	public Vector3D position;
	public Vector3D orientation;
	public Vector3D dimensions;
	public Vector3D velocity;
	public Vector3D acceleration;

	public int setHealth(){
		if(this.health < this.ease //if its health is diminished (ease == maxHealth)
				&& System.currentTimeMillis() - this.timeWell > 86400000){ //and it's been well for more than 1 day
			this.health++; //heal one unit
		}
		if(this.happiness < 0){ //if its happiness is low
			this.health--; //lose one unit health
			this.happiness = this.maxHappyFull; //reset happiness
		}
		return this.health;
	}

	public long changeHunger(){
		int hunger = (int) (this.size/2); //hunger changes relative to fish size
		this.fullness -= hunger;
		return hunger;
	}
	
	public long changeHappy(){
		if(status == HappinessStatus.HAPPY){
			this.happiness = this.maxHappyFull; //set to max
		}
		else{
			this.happiness--;
		}
		return this.happiness;
	}
	
	public void handleDeceased(Visual visual){
		if(this.health <= 0){
			visual.tank.fish.remove(this);
			visual.tank.waste += this.size;
			visual.fishChoice = -1;
			visual.fishChoices.removeItem(this.nickname +": " + this.name);
		}
	}
	
	//status indicates whether happiness is decreasing or not
	//if happy, happiness will be at max value
	//if not, happiness will decrease at a fixed rate
	//resulting in a health reduction when it hits zero
	//(fullness is constantly decreasing)
	public HappinessStatus happy(Tank tank){
		if(this.fullness < 0){
			this.timeWell = Long.MAX_VALUE;
			this.status = HappinessStatus.HUNGRY;
		}
		else if(this.ammonia > this.ammonia){
			this.timeWell = Long.MAX_VALUE;
			this.status = HappinessStatus.AMMONIA;
		}
		else if(this.nitrate > this.nitrate){
			this.timeWell = Long.MAX_VALUE;
			this.status = HappinessStatus.NITRATE;
		}
		else if(this.nitrite > this.nitrite){
			this.timeWell = Long.MAX_VALUE;
			this.status = HappinessStatus.NITRITE;
		}
		else if(tank.pH < this.minPH){
			this.timeWell = Long.MAX_VALUE;
			this.status = HappinessStatus.PHLOW;
		}
		else if(tank.pH > this.maxPH){
			this.timeWell = Long.MAX_VALUE;
			this.status = HappinessStatus.PHHIGH;
		}
		else if(tank.temp < this.minTemp){
			this.timeWell = Long.MAX_VALUE;
			this.status = HappinessStatus.TEMPLOW;
		}
		else if(tank.temp > this.maxTemp){
			this.timeWell = Long.MAX_VALUE;
			this.status = HappinessStatus.TEMPHIGH;
		}
		else if(tank.hardness < this.minHard){
			this.timeWell = Long.MAX_VALUE;
			this.status = HappinessStatus.HARDLOW;
		}
		else if(tank.hardness > this.maxHard){
			this.timeWell = Long.MAX_VALUE;
			this.status = HappinessStatus.HARDHIGH;
		}
		else{ //if none of the above, then  it's happy
			if(this.status != HappinessStatus.HAPPY){ //if it has just become happy,
				this.timeWell = System.currentTimeMillis(); //record the time
			}
			this.status = HappinessStatus.HAPPY;
		}
		return this.status;
	}
	
	public String compileSaveText(){
		String saveText = this.name + "\n";
		saveText += this.nickname + "\n";
		saveText += this.status + "\n";
		saveText += this.timeWell + "\n";
		saveText += this.fullness + "\n";
		saveText += this.happiness + "\n";
		saveText += this.health + "\n";
		saveText += this.position.toCommaSeparated();
		saveText += this.orientation.toCommaSeparated();
		return saveText;
	}
	
	public void updateOrientationRelativeToVelocity(Vector3D velocity){		
		double angle = Math.asin(Math.abs(velocity.z)/velocity.magnitude());
		if(velocity.x < 0 && velocity.z > 0) this.orientation.y = (float) angle;
		else if(velocity.x > 0 && velocity.z > 0) this.orientation.y = (float) (Visual.PI - angle);
		else if(velocity.x > 0 && velocity.z < 0) this.orientation.y = (float) (Visual.PI + angle);
		else if(velocity.x < 0 && velocity.z < 0) this.orientation.y = (float) -angle;
		else if(velocity.z == 0 && velocity.x < 0) this.orientation.y = 0;
		else if(velocity.x == 0 && velocity.z > 0) this.orientation.y = (float) (Visual.PI/2.0);
		else if(velocity.z == 0 && velocity.x > 0) this.orientation.y = (float) Visual.PI;
		else if(velocity.x == 0 && velocity.z < 0) this.orientation.y = (float) (3*Visual.PI/2.0);
	}
	
	public void updateAcceleration(){
		Random random = new Random();
		this.acceleration.x += random.nextFloat()*.25-.125;
		this.acceleration.y += random.nextFloat()*.25-.125;
		this.acceleration.z += random.nextFloat()*.25-.125;
	}
	
	public void updateVelocity(){
		this.velocity.x = centermost(-1, this.velocity.x + this.acceleration.x, 1);
		this.velocity.y = centermost(-1, this.velocity.x + this.acceleration.y, 1);
		this.velocity.z = centermost(-1, this.velocity.x + this.acceleration.z, 1);
		this.updateOrientationRelativeToVelocity(this.velocity);
		this.updateAcceleration();
	}
	
	public void updatePosition(Visual visual){
		this.position.x = centermost((int)(-.4*visual.zoomPercentage*visual.fieldX+this.dimensions.x/2.0), this.position.x+this.velocity.x, (int)(.4*visual.zoomPercentage*visual.fieldX-this.dimensions.x/2.0));
		this.position.y = centermost((int)(-.5*visual.zoomPercentage*visual.zoomPercentage*visual.tank.waterLevel+this.dimensions.y/2.0), this.position.y+this.velocity.y, (int)(.5*visual.zoomPercentage*visual.zoomPercentage*visual.tank.waterLevel-this.dimensions.y/2.0));
		this.position.z = centermost((int)(-.25*visual.zoomPercentage*visual.fieldZ+this.dimensions.x/2.0), this.position.z+this.velocity.z, (int)(.25*visual.zoomPercentage*visual.fieldZ-this.dimensions.x/2.0));
		this.updateVelocity();
	}
	
	public float centermost(float one, float two, float three){
		if(two <= one && one <= three) return one;
		if(three <= one && one <= two) return one;
		if(one <= two && two <= three) return two;
		if(three <= two && two <= one) return two;
		if(two <= three && three <= one) return three;
		if(one <= three && three <= two) return three;
		return two;
	}
}
