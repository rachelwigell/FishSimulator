package engine;

import saito.objloader.OBJModel;
import fish.Fish;
import graphics.Visual;

public class DeadFish extends Waste{
	public Vector3D orientation;
	public OBJModel model;
	public double size;
	public String species;

	public DeadFish(Visual visual, Fish origin){
		this.orientation = new Vector3D(Visual.PI, origin.orientation.y, 0);
		this.size = origin.size;
		this.velocity = new Vector3D(0, -1, 0);
		this.dimensions = origin.dimensions;
		this.position = origin.position;
		this.absolutePosition = this.position.addVector(new Vector3D((int)(.4*Visual.fieldX), (int)(.5*Visual.fieldY)+(int)(Visual.zoomPercentage*Visual.fieldY*.5*(1-visual.tank.waterLevel)), (int)(-Visual.fieldZ)+(int)(Visual.zoomPercentage*.25*Visual.fieldZ)));
		this.restingPosition = new Vector3D(0, (int)(.5*Visual.fieldY + .5*Visual.zoomPercentage*Visual.fieldY - Visual.zoomPercentage*Visual.fieldY*visual.tank.waterLevel), 0);
		this.model = origin.model;
		this.species = origin.name;
	}

	public DeadFish(Visual visual, Vector3D position, Vector3D orientation, String species){
		this.position = position;
		this.absolutePosition = this.position.addVector(new Vector3D((int)(.4*Visual.fieldX), (int)(.5*Visual.fieldY)+(int)(Visual.zoomPercentage*Visual.fieldY*.5*(1-visual.tank.waterLevel)), (int)(-Visual.fieldZ)+(int)(Visual.zoomPercentage*.25*Visual.fieldZ)));
		this.velocity = new Vector3D(0, -1, 0);
		this.orientation = orientation;
		this.restingPosition = new Vector3D(0, (int)(.5*Visual.fieldY + .5*Visual.zoomPercentage*Visual.fieldY - Visual.zoomPercentage*Visual.fieldY*visual.tank.waterLevel), 0);
		this.species = species;
		for(Fish f: visual.getFishSpeciesList()){
			if(f.name.equals(species)){
				this.model = f.model;
				this.dimensions = f.dimensions;
				this.size = f.size;
			}
		}
	}

	public void removeFromTank(Tank t){
		t.deadFish.remove(this);
		t.waste -= this.size;
	}
}
