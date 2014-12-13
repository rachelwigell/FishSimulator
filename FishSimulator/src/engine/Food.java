package engine;

import graphics.Visual;

import java.util.Random;

public class Food extends Waste{

	public Food(Visual visual, Vector3D start, Vector3D end){
		Vector3D normal = end.addVector(start.multiplyScalar(-1)).normalize();
		Random random = new Random();
		float percent = random.nextFloat();
		float z = (float) (-visual.fieldZ + 30 + percent*(.5*visual.zoomPercentage*visual.fieldZ)-30);
		float factor = (z-start.z)/normal.z;
		this.absolutePosition = start.addVector(normal.multiplyScalar(factor));
		this.position = this.absolutePosition.addVector(new Vector3D((int)(-.4*visual.fieldX), (int)(-.5*visual.fieldY)-(int)(visual.zoomPercentage*visual.fieldY*.5*(1-visual.tank.waterLevel)), (int)(visual.fieldZ)-(int)(visual.zoomPercentage*.25*visual.fieldZ)));
		this.speedChangeLocation = new Vector3D(0, (int)(.5*visual.fieldY + .5*visual.zoomPercentage*visual.fieldY - visual.zoomPercentage*visual.fieldY*visual.tank.waterLevel), 0);
		if(this.absolutePosition.y < speedChangeLocation.y){
			this.velocity = new Vector3D(0, 8, 0);
		}
		else{
			this.velocity = new Vector3D(0, 1, 0);
		}
		this.dimensions = new Vector3D(5, 5, 5);
		this.color = new Vector3D(200, 200, 0);
		this.restingPosition = new Vector3D(0, (float) (.5*visual.fieldY + .5*visual.zoomPercentage*visual.fieldY), 0);
	}
	
	public Food(Visual visual, Vector3D start, Vector3D end, boolean left){
		Vector3D normal = end.addVector(start.multiplyScalar(-1)).normalize();
		float x;
		if(left) x = (float) (.4*visual.fieldX - visual.zoomPercentage*.4*visual.fieldX + 30);
		else x = (float) (.4*visual.fieldX + visual.zoomPercentage*.4*visual.fieldX) - 30;
		float factor = (x-start.x)/normal.x;
		this.absolutePosition = start.addVector(normal.multiplyScalar(factor));
		this.position = this.absolutePosition.addVector(new Vector3D((int)(-.4*visual.fieldX), (int)(-.5*visual.fieldY)-(int)(visual.zoomPercentage*visual.fieldY*.5*(1-visual.tank.waterLevel)), (int)(visual.fieldZ)-(int)(visual.zoomPercentage*.25*visual.fieldZ)));
		this.speedChangeLocation = new Vector3D(0, (int)(.5*visual.fieldY + .5*visual.zoomPercentage*visual.fieldY - visual.zoomPercentage*visual.fieldY*visual.tank.waterLevel), 0);
		if(this.absolutePosition.y < speedChangeLocation.y){
			this.velocity = new Vector3D(0, 8, 0);
		}
		else{
			this.velocity = new Vector3D(0, 1, 0);
		}
		this.dimensions = new Vector3D(5, 5, 5);
		this.color = new Vector3D(200, 200, 0);
		this.restingPosition = new Vector3D(0, (float) (.5*visual.fieldY + .5*visual.zoomPercentage*visual.fieldY), 0);
	}
	
	public Food(Visual visual, Vector3D position){
		this.position = position;
		this.absolutePosition = this.position.addVector(new Vector3D((int)(.4*visual.fieldX), (int)(.5*visual.fieldY)+(int)(visual.zoomPercentage*visual.fieldY*.5*(1-visual.tank.waterLevel)), (int)(-visual.fieldZ)+(int)(visual.zoomPercentage*.25*visual.fieldZ)));
		this.velocity = new Vector3D(0, 1, 0);
		this.dimensions = new Vector3D(5, 5, 5);
		this.color = new Vector3D(200, 200, 0);
		this.restingPosition = new Vector3D(0, (float) (.5*visual.fieldY + .5*visual.zoomPercentage*visual.fieldY), 0);
	}
	
	public void removeFromTank(Tank t){
		t.food.remove(this);
	}
}
