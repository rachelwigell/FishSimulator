package engine;

import saito.objloader.OBJModel;
import fish.Fish;
import graphics.Visual;

public class DeadFish extends Waste{
	public Vector3D orientation;
	public OBJModel model;
	public double size;

	public DeadFish(Visual visual, Fish origin){
		this.orientation = new Vector3D(Visual.PI, origin.orientation.y, 0);
		this.size = origin.size;
		this.velocity = new Vector3D(0, -1, 0);
		this.dimensions = origin.dimensions;
		this.position = origin.position;
		this.absolutePosition = this.position.addVector(new Vector3D((int)(.4*visual.fieldX), (int)(.5*visual.fieldY)+(int)(visual.zoomPercentage*visual.fieldY*.5*(1-visual.tank.waterLevel)), (int)(-visual.fieldZ)+(int)(visual.zoomPercentage*.25*visual.fieldZ)));
		this.restingPosition = new Vector3D(0, (int)(.5*visual.fieldY + .5*visual.zoomPercentage*visual.fieldY - visual.zoomPercentage*visual.fieldY*visual.tank.waterLevel), 0);
		this.model = origin.model;
	}
	
	public void removeFromTank(Tank t){
		t.deadFish.remove(this);
		t.waste -= this.size;
	}
}
