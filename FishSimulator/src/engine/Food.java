package engine;

import graphics.Visual;

import java.util.Random;

public class Food extends Waste{

	public Food(Visual visual, Vector3D start, Vector3D end){
		Vector3D normal = end.addVector(start.multiplyScalar(-1)).normalize();
		Random random = new Random();
		float percent = random.nextFloat();
		float z = (float) (-Visual.fieldZ + 30 + percent*(.5*Visual.zoomPercentage*Visual.fieldZ)-30);
		float factor = (z-start.z)/normal.z;
		this.absolutePosition = start.addVector(normal.multiplyScalar(factor));
		this.position = this.absolutePosition.addVector(new Vector3D((int)(-.4*Visual.fieldX), (int)(-.5*Visual.fieldY)-(int)(Visual.zoomPercentage*Visual.fieldY*.5*(1-visual.tank.waterLevel)), (int)(Visual.fieldZ)-(int)(Visual.zoomPercentage*.25*Visual.fieldZ)));
		this.speedChangeLocation = new Vector3D(0, (int)(.5*Visual.fieldY + .5*Visual.zoomPercentage*Visual.fieldY - Visual.zoomPercentage*Visual.fieldY*visual.tank.waterLevel), 0);
		if(this.absolutePosition.y < speedChangeLocation.y){
			this.velocity = new Vector3D(0, 8, 0);
		}
		else{
			this.velocity = new Vector3D(0, 1, 0);
		}
		this.dimensions = new Vector3D(5, 5, 5);
		this.color = new Vector3D(200, 200, 0);
		this.restingPosition = new Vector3D(0, (float) (.5*Visual.fieldY + .5*Visual.zoomPercentage*Visual.fieldY), 0);
	}
	
	public Food(Visual visual, Vector3D start, Vector3D end, boolean left){
		Vector3D normal = end.addVector(start.multiplyScalar(-1)).normalize();
		float x;
		if(left) x = (float) (.4*Visual.fieldX - Visual.zoomPercentage*.4*Visual.fieldX);
		else x = (float) (.4*Visual.fieldX + Visual.zoomPercentage*.4*Visual.fieldX);
		float factor = (x-start.x)/normal.x;
		this.absolutePosition = start.addVector(normal.multiplyScalar(factor));
		this.position = this.absolutePosition.addVector(new Vector3D((int)(-.4*Visual.fieldX), (int)(-.5*Visual.fieldY)-(int)(Visual.zoomPercentage*Visual.fieldY*.5*(1-visual.tank.waterLevel)), (int)(Visual.fieldZ)-(int)(Visual.zoomPercentage*.25*Visual.fieldZ)));
		this.speedChangeLocation = new Vector3D(0, (int)(.5*Visual.fieldY + .5*Visual.zoomPercentage*Visual.fieldY - Visual.zoomPercentage*Visual.fieldY*visual.tank.waterLevel), 0);
		if(this.absolutePosition.y < speedChangeLocation.y){
			this.velocity = new Vector3D(0, 8, 0);
		}
		else{
			this.velocity = new Vector3D(0, 1, 0);
		}
		this.dimensions = new Vector3D(5, 5, 5);
		this.color = new Vector3D(200, 200, 0);
		this.restingPosition = new Vector3D(0, (float) (.5*Visual.fieldY + .5*Visual.zoomPercentage*Visual.fieldY), 0);
	}
	
	public Food(Visual visual, Vector3D position){
		this.position = position;
		this.absolutePosition = this.position.addVector(new Vector3D((int)(.4*Visual.fieldX), (int)(.5*Visual.fieldY)+(int)(Visual.zoomPercentage*Visual.fieldY*.5*(1-visual.tank.waterLevel)), (int)(-Visual.fieldZ)+(int)(Visual.zoomPercentage*.25*Visual.fieldZ)));
		this.velocity = new Vector3D(0, 1, 0);
		this.dimensions = new Vector3D(5, 5, 5);
		this.color = new Vector3D(200, 200, 0);
		this.restingPosition = new Vector3D(0, (float) (.5*Visual.fieldY + .5*Visual.zoomPercentage*Visual.fieldY), 0);
	}
	
	public void removeFromTank(Tank t){
		t.food.remove(this);
	}
}
