package engine;

/**
 *
 * @author Rachel
 */
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import fish.Fish;
import graphics.Visual;

//implement way for pH to rise
//implement way to control nitrates

public class Tank {
	public double cmFish; //cm
	public double plants; //cm
	public double pH; //unitless
	public double temp; //degrees C
	public double hardness; //ppm
	public double o2; //percent
	public double co2; //percent
	public double ammonia; //ppm
	public double nitrite; //ppm
	public double nitrate; //ppm
	public double nitrosomonas; //bacteria
	public double nitrobacter; //bacteria
	public int waste; //poops
	public int length; //cm
	public int width; //cm
	public int height; //cm
	public double volume; //L
	public int surfaceArea; //square cm
	public int time; //min
	public LinkedList <Fish> fish;
	public int aggressiveFish;
	public int susceptibleFish;
	public int predators;
	public int prey;
	public LinkedList<Poop> poops;
	public LinkedList<Food> food;

	public final double roomTemp = 22;
	public final double pi = 3.14159;
	public final double waterLevel = .75;
	public final double timeScale = .01; //higher = harder

	//constructors

	public Tank(TankSize size){
		switch(size){
		case SMALL:
			this.length = 30;
			this.width = 15;
			this.height = 20;
			this.volume = waterLevel*30*15*20/1000.0;
			this.surfaceArea = 30*15;
			break;
		case MEDIUM:
			this.length = 60;
			this.width = 30;
			this.height = 40;
			this.volume = waterLevel*60*30*40/1000.0;
			this.surfaceArea = 60*30;
			break;
		case LARGE:
			this.length = 90;
			this.width = 45;
			this.height = 60;
			this.volume = waterLevel*90*45*60/1000.0;
			this.surfaceArea = 90*45;
			break;
		}

		this.cmFish = 0;
		this.plants = 1;
		this.pH=7;
		this.temp = 24;
		this.hardness = 1;
		this.o2 = 11; //temp/2
		this.co2 = 11; //temp/2
		this.ammonia = 0;
		this.nitrite = 0;
		this.nitrate = 0;
		this.nitrosomonas = .01;
		this.nitrobacter = .01;
		this.waste=0;
		this.time = getTime();
		this.fish = new LinkedList<Fish>();
		this.aggressiveFish = 0;
		this.susceptibleFish = 0;
		this.predators = 0;
		this.prey = 0;
		this.poops = new LinkedList<Poop>();
		this.food = new LinkedList<Food>();
	}

	public Tank(double plants,
			int length,
			int width,
			int height,
			double pH,
			double temp,
			double hardness,
			double o2,
			double co2,
			double ammonia,
			double nitrite,
			double nitrate,
			double nitrosomonas,
			double nitrobacter,
			int waste,
			LinkedList<Poop> poops,
			LinkedList<Food> food,
			LinkedList <Fish> fish){
		this.plants = plants;
		this.length = length;
		this.width = width;
		this.height = height;
		this.volume = waterLevel*length*width*height/1000.0;
		this.surfaceArea = length*width;
		this.pH = pH;
		this.temp = temp;
		this.hardness = hardness;
		this.o2 = o2;
		this.co2 = co2;
		this.ammonia=ammonia;
		this.nitrite=nitrite;
		this.nitrate=nitrate;
		this.nitrosomonas=nitrosomonas;
		this.nitrobacter=nitrobacter;
		this.waste=waste;
		this.time = getTime();
		this.poops = poops;
		this.food = food;
		this.fish = fish;
		this.cmFish = this.calcTotalFish();
	}

	//helper functions

	double calcTotalFish(){
		double cmFish = 0;
		for(Fish f: this.fish){
			cmFish = cmFish + f.size;
		}
		return cmFish;
	}

	public int getTime(){
		Date date = new Date();
		return 60 * date.getHours() + date.getMinutes();
	}
	
	public void addFish(Fish aFish){
		this.fish.add(aFish);
		this.cmFish = this.cmFish + aFish.size;
		if(aFish.properties.contains(Qualify.AGGRESSIVE)){
			this.aggressiveFish++;
		}
		if(aFish.properties.contains(Qualify.SUSCEPTIBLE)){
			this.susceptibleFish++;
		}
		if(aFish.properties.contains(Qualify.PREDATOR)){
			this.predators++;
		}
		if(aFish.properties.contains(Qualify.PREY)){
			this.prey++;
		}
	}

	double changeFish(){
		double cmFish = this.calcTotalFish();
		return cmFish;
	}

	int changePlants(){
		int plants = 0;
		return plants;
	}

	double changePH(){
		double pH = .001*this.pH*(14-this.pH)*(-.01*this.co2 - .1*this.ammonia + .16*this.hardness)/(Math.pow(10, Math.abs(this.pH-7.0))*this.volume);
		return pH;
	}

	double changeTemp(){
		double temp = .001*this.surfaceArea*(roomTemp - this.temp)/this.volume; //no heater feature yet
		return temp;  
	}

	double changeHard(){
		double hardness = -.1*this.co2*this.hardness/this.volume + .003*this.surfaceArea/this.hardness; //assuming stones on bottom of tank
		return hardness;
	}

	double changeO2(){
		double photosynthesis = (.5 + .5*Math.sin(pi/720.0*this.time-pi/2.0))*this.plants*this.co2; //check this
		double respiration = (this.cmFish+this.plants)*this.o2; // and this
		double o2 = .2*(photosynthesis-respiration+this.surfaceArea)/this.volume+(this.o2+this.co2)*((100-this.temp)-(this.o2+this.co2))/this.volume;
		return o2;
	}

	double changeCO2(){
		double photosynthesis = (.5+.5*Math.sin(pi/720.0*this.time-pi/2.0))*this.plants*this.co2;
		double respiration = (this.cmFish+this.plants)*this.o2;
		double co2 = .15*(respiration-photosynthesis+this.surfaceArea)/this.volume+(this.co2+this.o2)*((100-this.temp)-(this.co2+this.o2))/this.volume;
		return co2;
	}

	double changeAmmonia(){
		double ammonia = (.04*this.temp*this.pH*(this.waste + this.food.size() + .5*this.cmFish) - .2*this.nitrosomonas*this.ammonia)/this.volume;
		return ammonia;
	}

	double changeNitrite(){
		double nitrite = (.2*this.ammonia*this.nitrosomonas - .2*this.nitrite*this.nitrobacter)/this.volume;
		return nitrite;
	}

	double changeNitrate(){
		double nitrate = (.2*this.nitrite*this.nitrobacter-10*this.plants*this.nitrate)/this.volume;
		return nitrate;
	}

	double changeNitrosomonas(){
		double nitrosomonas = .05*this.ammonia*this.nitrosomonas-.4*this.nitrosomonas;
		return nitrosomonas;
	}

	double changeNitrobacter(){
		double nitrobacter = .05*this.nitrite*this.nitrobacter-.4*this.nitrobacter;
		return nitrobacter;
	}
	
	public void addFood(Food food){
		this.food.add(food);
	}

	public int changeWaste(Visual visual){
		int waste = 0;
		Random random = new Random();
		for(Fish f: this.fish){
			int threshold = (int) (((double) Math.max(f.fullness, 0)) / ((double) f.maxHappyFull) * f.size/2.0);
			int rand = random.nextInt(500);
			if(rand < threshold){
				waste++;
				addPoop(visual, f);
			}
		}
		return waste;
	}

	double totalHunger(){
		double total = 0;
		for(Fish f: this.fish){
			total += f.fullness;
		}
		return total;
	}

	//change, make random
	//chance that a fish will find food proportionate to amount of food in tank and hunger level
	double eat(Fish aFish){
		double total = totalHunger();
		double eaten;
		if(total > this.food.size()){
			double prop = this.food.size()/total;
			eaten = aFish.fullness * prop;
		}
		else{
			eaten = aFish.fullness;
		}
		//		aFish.hunger = Math.max(aFish.hunger - eaten, 0);
//		this.food = Math.max(this.food.size() - eaten, 0);
		return aFish.fullness;
	}

	public void cleaning(){
		this.waste = 0;
//		this.food = 0;
	}

	public void feeding(int food){
//		this.food = this.food + food;
	}

	public Tank waterChange(double percent){
		percent = .01*percent;
		this.pH = Math.log10(percent*Math.pow(10, 7) + (1-percent)*Math.pow(10, this.pH));
		this.temp = percent*24 + (1-percent)*this.temp;
		this.hardness = percent*1 + (1-percent)*this.hardness;
		this.o2 = percent*11 + (1-percent)*this.o2;
		this.co2 = percent*11 + (1-percent)*this.co2;
		this.ammonia = percent*0 + (1-percent)*this.ammonia;
		this.nitrite = percent*0 + (1-percent)*this.nitrite;
		this.nitrate = percent*0 + (1-percent)*this.nitrate;
		this.nitrosomonas = percent*.01 + (1-percent)*this.nitrosomonas;
		this.nitrobacter = percent*.01 + (1-percent)*this.nitrobacter;
		return this;
	}
	
	public void addPoop(Visual visual, Fish f){
		this.poops.add(new Poop(visual, f));
	}

	/*String loneRecommendation(Fish aFish){
      if(aFish.tankSize > this.volume){
            return (aFish.name + " requires a " + aFish.tankSize + "L tank. Your tank is " + this.volume + "L.\n");
        }
      else{
          return "compatible";
        }
    }*/

	public String isCompatible(Fish one, Fish two){
		String compatible = "compatible";
		if(one.properties.contains(Qualify.PREDATOR) && two.properties.contains(Qualify.PREY)){
			compatible = (one.name + " might predate " + two.name + ".\n");
		}
		if(one.properties.contains(Qualify.PREY) && two.properties.contains(Qualify.PREDATOR)){
			compatible = (two.name + " might predate " + one.name + ".\n");
		}
		if(one.properties.contains(Qualify.AGGRESSIVE) && two.properties.contains(Qualify.SUSCEPTIBLE)){
			compatible = (one.name + " is aggressive and may pick on " + two.name + ".\n");
		}
		if(one.properties.contains(Qualify.SUSCEPTIBLE) && two.properties.contains(Qualify.AGGRESSIVE)){
			compatible = (two.name + " is aggressive and may pick on " + one.name + ".\n");
		}
		if(one.maxPH < two.minPH || one.minPH > two.maxPH){
			compatible = (one.name + " and " + two.name + "do not have overlapping pH ranges.\n");
		}
		if(one.maxTemp < two.minTemp || one.minTemp > two.maxTemp){
			compatible =(one.name + " and " + two.name + "do not have overlapping temperature ranges.\n");
		}
		if(one.maxHard < two.minHard || one.minHard > two.maxHard){
			compatible =(one.name + " and " + two.name + "do not have overlapping hardness ranges.\n");
		}
		return compatible;
	}

	public void progress(Visual visual){
		//per fish operations
		for(Fish f: this.fish){
			f.changeHunger(); //update fish's hunger level 
			f.happy(this); //update fish's happiness status
			f.changeHappy(); //update fish's happiness level
			f.setHealth(); //update fish's health
			f.handleDeceased(visual); //check if fish is dead and perform necessary operations if so
		}

		//tank operations
		double cmFish = this.changeFish();
		double pH = this.pH + timeScale * this.changePH();
		if(pH <=0) pH = .01;
		else if(pH >=14) pH = 13.99;
		double temp = this.temp + timeScale * this.changeTemp();
		double hardness = Math.max(this.hardness + timeScale * this.changeHard(), .01);
		double o2 = Math.max(this.o2 + timeScale * this.changeO2(), .01);
		double co2 = Math.max(this.co2 + timeScale * this.changeCO2(), .01);
		double ammonia = Math.min(Math.max(this.ammonia + timeScale * this.changeAmmonia(), 0), 1000000);
		double nitrite = Math.min(Math.max(this.nitrite + timeScale * this.changeNitrite(), 0), 1000000);
		double nitrate = Math.min(Math.max(this.nitrate + timeScale * this.changeNitrate(), 0), 1000000);
		double nitrosomonas = Math.min(Math.max(this.nitrosomonas + timeScale * this.changeNitrosomonas(), .01), 1000000);
		double nitrobacter = Math.min(Math.max(this.nitrobacter + timeScale * this.changeNitrobacter(), .01), 1000000);
		int waste = this.waste + this.changeWaste(visual);
		int time = this.getTime();

		//do assignment after so that all calculations are accurate
		this.cmFish = cmFish;
		this.pH = pH;
		this.temp = temp;
		this.hardness = hardness;
		this.o2 = o2;
		this.co2 = co2;
		this.ammonia = ammonia;
		this.nitrite = nitrite;
		this.nitrate = nitrate;
		this.nitrosomonas = nitrosomonas;
		this.nitrobacter = nitrobacter;
		this.waste = waste;
		this.time = time;
	}

	public void skipAhead(Visual visual, int minutes){
		for(int i = 0; i < minutes*12; i++){
			this.progress(visual);
		}
	}

	public boolean validateNickname(String nickname){
		for(Fish f: fish){
			if(f.nickname.equals(nickname)) return false;
		}
		return true;
	}
	
	public Vector3D nearestFood(Vector3D position){
		Vector3D closest = null;
		float distance = Float.MAX_VALUE;
		for(Food f: this.food){
			float fDistance = f.position.squareDistance(position);
			if(fDistance < distance){
				distance = fDistance;
				closest = f.position;
			}
		}
		return closest;
	}
	
	public void eat(Fish fish, Food food){
		if(fish.position.distance(food.position) < food.dimensions.x*4){
			fish.fullness = Math.min(fish.fullness+fish.ease*1800, fish.maxHappyFull);
			this.food.remove(food);
		}
	}
}
