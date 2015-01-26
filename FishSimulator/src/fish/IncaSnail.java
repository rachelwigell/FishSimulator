package fish;

import engine.Diet;
import engine.HappinessStatus;
import engine.Qualify;
import engine.Tank;
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
		this.model = new OBJModel(window, "graphics/incasnail.obj", Visual.POLYGON);
		this.sprite = "graphics/incasnail.png";
		this.model.scale(8);
		this.model.translateToCenter();
		this.velocity = new Vector3D(0, 0, 0);
		this.acceleration = new Vector3D(0, 0, 0);
		this.orientation = new Vector3D(0, 0, 0);
		this.dimensions = new Vector3D(70, 49, 46);
		this.position = new Vector3D(0, (int)(.5*Visual.zoomPercentage*Visual.fieldY*window.tank.waterLevel-this.dimensions.y/2.0), 0);
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
			this.acceleration.x += random.nextFloat()*.0625-.03125;
			this.acceleration.y = 0;
			this.acceleration.z += random.nextFloat()*.0625-.03125;
			break;
		case LEFT:
		case RIGHT:
			this.acceleration.x = 0;
			this.acceleration.y += random.nextFloat()*.0625-.03125;
			this.acceleration.z += random.nextFloat()*.0625-.03125;
			break;
		case BACK:
			this.acceleration.x += random.nextFloat()*.0625-.03125;
			this.acceleration.y += random.nextFloat()*.0625-.03125;
			this.acceleration.z = 0;
			break;
		}
	}

	public Vector3D hungerContribution(Tank tank){
		Vector3D nearestFood = tank.nearestFood(this.position);
		if(nearestFood == null) return new Vector3D(0,0,0);
		double percent = Math.max((.5-((double) Math.max(this.fullness, 0)/(double) this.maxHappyFull))*6, 0);
		Vector3D normal = nearestFood.addVector(this.position.multiplyScalar(-1)).normalize();
		return normal.multiplyScalar((float) percent);
	}
	
	public void updateVelocity(Tank tank){
		Vector3D hungerContribution = hungerContribution(tank);
		switch(location){
		case FLOOR:
			this.velocity.x = centermost(-.5f, this.velocity.x + this.acceleration.x, .5f);
			this.velocity.y = 0;
			this.velocity.z = centermost(-.5f, this.velocity.z + this.acceleration.z, .5f);
			hungerContribution.y = 0;
			break;
		case LEFT:
			this.velocity.x = 0;
			this.velocity.y = centermost(-.5f, this.velocity.y + this.acceleration.y, .5f);
			this.velocity.z = centermost(-.5f, this.velocity.z + this.acceleration.z, .5f);
			hungerContribution.x = 0;
			break;
		case RIGHT:
			this.velocity.x = 0;
			this.velocity.y = centermost(-.5f, this.velocity.y + this.acceleration.y, .5f);
			this.velocity.z = centermost(-.5f, this.velocity.z + this.acceleration.z, .5f);
			hungerContribution.x = 0;
			break;
		case BACK:
			this.velocity.x = centermost(-.5f, this.velocity.x + this.acceleration.x, .5f);
			this.velocity.y = centermost(-.5f, this.velocity.y + this.acceleration.y, .5f);
			this.velocity.z = 0;
			hungerContribution.z = 0;
			break;
		}
		this.velocity = this.velocity.addVector(hungerContribution);
		this.updateOrientationRelativeToVelocity(this.velocity);
		this.updateAcceleration();
		this.transitionWalls(tank);
	}

	public void updateOrientationRelativeToVelocity(Vector3D velocity){
		switch(this.location){
		case FLOOR:
			double angle = Math.asin(Math.abs(velocity.z)/velocity.magnitude());
			this.orientation.z = 0;
			this.orientation.x = 0;
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
			angle = Math.asin(Math.abs(velocity.z)/velocity.magnitude());
			this.orientation.z = (float) (-Visual.PI/2.0);
			this.orientation.x = 0;
			if(velocity.y > 0 && velocity.z > 0) this.orientation.y = (float) angle;
			else if(velocity.y < 0 && velocity.z > 0) this.orientation.y = (float) (Visual.PI - angle);
			else if(velocity.y < 0 && velocity.z < 0) this.orientation.y = (float) (Visual.PI + angle);
			else if(velocity.y > 0 && velocity.z < 0) this.orientation.y = (float) -angle;
			else if(velocity.z == 0 && velocity.y > 0) this.orientation.y = 0;
			else if(velocity.y == 0 && velocity.z > 0) this.orientation.y = (float) (Visual.PI/2.0);
			else if(velocity.z == 0 && velocity.y < 0) this.orientation.y = (float) Visual.PI;
			else if(velocity.y == 0 && velocity.z < 0) this.orientation.y = (float) (3*Visual.PI/2.0);
			break;
		case LEFT:
			angle = Math.asin(Math.abs(velocity.z)/velocity.magnitude());
			this.orientation.z = (float) (Visual.PI/2.0);
			this.orientation.x = 0;
			if(velocity.y < 0 && velocity.z > 0) this.orientation.y = (float) angle;
			else if(velocity.y > 0 && velocity.z > 0) this.orientation.y = (float) (Visual.PI - angle);
			else if(velocity.y > 0 && velocity.z < 0) this.orientation.y = (float) (Visual.PI + angle);
			else if(velocity.y < 0 && velocity.z < 0) this.orientation.y = (float) -angle;
			else if(velocity.z == 0 && velocity.y < 0) this.orientation.y = 0;
			else if(velocity.y == 0 && velocity.z > 0) this.orientation.y = (float) (Visual.PI/2.0);
			else if(velocity.z == 0 && velocity.y > 0) this.orientation.y = (float) Visual.PI;
			else if(velocity.y == 0 && velocity.z < 0) this.orientation.y = (float) (3*Visual.PI/2.0);
			break;
		case BACK:
			angle = Math.asin(Math.abs(velocity.y)/velocity.magnitude());
			this.orientation.z = (float) (Visual.PI/2.0);
			this.orientation.x = (float) (-Visual.PI/2.0);
			if(velocity.y < 0 && velocity.x < 0) this.orientation.y = (float) angle;
			else if(velocity.y > 0 && velocity.x < 0) this.orientation.y = (float) (Visual.PI - angle);
			else if(velocity.y > 0 && velocity.x > 0) this.orientation.y = (float) (Visual.PI + angle);
			else if(velocity.y < 0 && velocity.x > 0) this.orientation.y = (float) -angle;
			else if(velocity.x == 0 && velocity.y < 0) this.orientation.y = 0;
			else if(velocity.y == 0 && velocity.x < 0) this.orientation.y = (float) (Visual.PI/2.0);
			else if(velocity.x == 0 && velocity.y > 0) this.orientation.y = (float) Visual.PI;
			else if(velocity.y == 0 && velocity.x > 0) this.orientation.y = (float) (3*Visual.PI/2.0);
			break;
		}
	}

	public void transitionWalls(Tank tank){
		switch(this.location){
		case FLOOR:
			if(this.position.x <= (int)(-.4*Visual.zoomPercentage*Visual.fieldX+this.dimensions.x/2.0)
			&& this.velocity.x < 0){
				this.position.x = (int)(-.4*Visual.zoomPercentage*Visual.fieldX+dimensions.y/2.0);
				this.velocity.y = -.5f;
				this.location = Wall.LEFT;
			}
			else if(this.position.x >= (int)(.4*Visual.zoomPercentage*Visual.fieldX-this.dimensions.x/2.0)
					&& this.velocity.x > 0){
				this.position.x = (int)(.4*Visual.zoomPercentage*Visual.fieldX-this.dimensions.y/2.0);
				this.velocity.y = -.5f;
				this.location = Wall.RIGHT;
			}
			else if(this.position.z <= (int)(-.25*Visual.zoomPercentage*Visual.fieldZ+this.dimensions.x/2.0)
					&& this.velocity.z < 0){
				this.position.z = (int)(-.25*Visual.zoomPercentage*Visual.fieldZ+this.dimensions.y/2.0);
				this.velocity.y = -.5f;
				this.location = Wall.BACK;
			}
			break;
		case RIGHT:
			if(this.position.y >= (int)(.5*Visual.zoomPercentage*Visual.fieldY*tank.waterLevel-this.dimensions.x/2.0)
			&& this.velocity.y > 0){
				this.position.y = (int)(.5*Visual.zoomPercentage*Visual.fieldY*tank.waterLevel-this.dimensions.y/2.0);
				this.velocity.x = -.5f;
				this.location = Wall.FLOOR;
			}
			else if(this.position.z <= (int)(-.25*Visual.zoomPercentage*Visual.fieldZ+this.dimensions.x/2.0)
					&& this.velocity.z < 0){
				this.position.z = (int)(-.25*Visual.zoomPercentage*Visual.fieldZ+this.dimensions.y/2.0);
				this.velocity.x = -.5f;
				this.location = Wall.BACK;
			}
			break;
		case LEFT:
			if(this.position.y >= (int)(.5*Visual.zoomPercentage*Visual.fieldY*tank.waterLevel-this.dimensions.x/2.0)
			&& this.velocity.y > 0){
				this.position.y = (int)(.5*Visual.zoomPercentage*Visual.fieldY*tank.waterLevel-this.dimensions.y/2.0);
				this.velocity.x = .5f;
				this.location = Wall.FLOOR;
			}
			else if(this.position.z <= (int)(-.25*Visual.zoomPercentage*Visual.fieldZ+this.dimensions.x/2.0)
					&& this.velocity.z < 0){
				this.position.z = (int)(-.25*Visual.zoomPercentage*Visual.fieldZ+this.dimensions.y/2.0);
				this.velocity.x = .5f;
				this.location = Wall.BACK;
			}
			break;
		case BACK:
			if(this.position.x <= (int)(-.4*Visual.zoomPercentage*Visual.fieldX+this.dimensions.x/2.0)
			&& this.velocity.x < 0){
				this.position.x = (int)(-.4*Visual.zoomPercentage*Visual.fieldX+dimensions.y/2.0);
				this.velocity.z = .5f;
				this.location = Wall.LEFT;
			}
			else if(this.position.x >= (int)(.4*Visual.zoomPercentage*Visual.fieldX-this.dimensions.x/2.0)
					&& this.velocity.x > 0){
				this.position.x = (int)(.4*Visual.zoomPercentage*Visual.fieldX-this.dimensions.y/2.0);
				this.velocity.z = .5f;
				this.location = Wall.RIGHT;
			}
			else if(this.position.y >= (int)(.5*Visual.zoomPercentage*Visual.fieldY*tank.waterLevel-this.dimensions.x/2.0)
					&& this.velocity.y > 0){
				this.position.y = (int)(.5*Visual.zoomPercentage*Visual.fieldY*tank.waterLevel-this.dimensions.y/2.0);
				this.velocity.z = .5f;
				this.location = Wall.FLOOR;
			}
			break;
		}
	}

	public void drawFish(Visual visual){
		visual.noStroke();
		visual.pushMatrix();
		visual.translate((int)(.4*Visual.fieldX),
				(int)(.5*Visual.fieldY)+(int)(Visual.zoomPercentage*Visual.fieldY*.5*(1-visual.tank.waterLevel)),
				(int)(-Visual.fieldZ)+(int)(Visual.zoomPercentage*.25*Visual.fieldZ));
		visual.translate(this.position.x, this.position.y, this.position.z);
		visual.rotateZ(this.orientation.z);
		visual.rotateX(this.orientation.x);
		visual.rotateY(this.orientation.y);
		this.model.draw();
		visual.popMatrix();
		this.updatePosition(visual);
	}
	
	public void skipAhead(Visual visual){
		Random random = new Random();
		switch(random.nextInt(4)){
		case 0:
			this.location = Wall.FLOOR;
			this.position.x = (int)(-.4*Visual.zoomPercentage*Visual.fieldX+this.dimensions.x/2.0) + random.nextFloat() * (int)(.8*Visual.zoomPercentage*Visual.fieldX-this.dimensions.x/2.0);
			this.position.y = (int)(.5*Visual.zoomPercentage*Visual.fieldY*visual.tank.waterLevel-this.dimensions.y/2.0);
			this.position.z = (int)(-.25*Visual.zoomPercentage*Visual.fieldZ+this.dimensions.x/2.0) + random.nextFloat() * (int)(.5*Visual.zoomPercentage*Visual.fieldZ-this.dimensions.x/2.0);
			break;
		case 1:
			this.location = Wall.RIGHT;
			this.position.x = (int)(.4*Visual.zoomPercentage*Visual.fieldX-dimensions.y/2.0);
			this.position.y = (int)(-.5*Visual.zoomPercentage*Visual.fieldY*visual.tank.waterLevel+this.dimensions.y/2.0) + random.nextFloat() * (int)(Visual.zoomPercentage*Visual.fieldY*visual.tank.waterLevel-this.dimensions.y/2.0);
			this.position.z = (int)(-.25*Visual.zoomPercentage*Visual.fieldZ+this.dimensions.x/2.0) + random.nextFloat() * (int)(.5*Visual.zoomPercentage*Visual.fieldZ-this.dimensions.x/2.0);
			break;
		case 2:
			this.location = Wall.LEFT;
			this.position.x = (int)(-.4*Visual.zoomPercentage*Visual.fieldX+dimensions.y/2.0);
			this.position.y = (int)(-.5*Visual.zoomPercentage*Visual.fieldY*visual.tank.waterLevel+this.dimensions.y/2.0) + random.nextFloat() * (int)(Visual.zoomPercentage*Visual.fieldY*visual.tank.waterLevel-this.dimensions.y/2.0);
			this.position.z = (int)(-.25*Visual.zoomPercentage*Visual.fieldZ+this.dimensions.x/2.0) + random.nextFloat() * (int)(.5*Visual.zoomPercentage*Visual.fieldZ-this.dimensions.x/2.0);
			break;
		case 3:
			this.location = Wall.BACK;
			this.position.x = (int)(-.4*Visual.zoomPercentage*Visual.fieldX+this.dimensions.x/2.0) + random.nextFloat() * (int)(.8*Visual.zoomPercentage*Visual.fieldX-this.dimensions.x/2.0);
			this.position.y = (int)(-.5*Visual.zoomPercentage*Visual.fieldY*visual.tank.waterLevel+this.dimensions.y/2.0) + random.nextFloat() * (int)(Visual.zoomPercentage*Visual.fieldY*visual.tank.waterLevel-this.dimensions.y/2.0);
			this.position.z = (int)(-.25*Visual.zoomPercentage*Visual.fieldZ+this.dimensions.x/2.0);
			break;
		}
	}
	
	public void updatePosition(Visual visual){
		this.position.x = centermost((int)(-.4*Visual.zoomPercentage*Visual.fieldX), this.position.x+this.velocity.x, (int)(.4*Visual.zoomPercentage*Visual.fieldX));
		this.position.y = centermost((int)(-.5*Visual.zoomPercentage*Visual.fieldY*visual.tank.waterLevel), this.position.y+this.velocity.y, (int)(.5*Visual.zoomPercentage*Visual.fieldY*visual.tank.waterLevel));
		this.position.z = centermost((int)(-.25*Visual.zoomPercentage*Visual.fieldZ), this.position.z+this.velocity.z, (int)(.25*Visual.zoomPercentage*Visual.fieldZ));
		this.updateVelocity(visual.tank);
	}
}
