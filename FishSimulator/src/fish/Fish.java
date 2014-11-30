/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish;

import java.util.LinkedList;

import saito.objloader.OBJModel;
import engine.Coordinate;
import engine.Diet;
import engine.HappinessStatus;
import engine.Qualify;
import engine.Tank;
import graphics.Visual;

public abstract class Fish {
	public String name;
	public String nickname;
	public HappinessStatus status = HappinessStatus.HAPPY;
	public long timeWell; //status == happy
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
	public Coordinate position;
	public int index;

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
}
