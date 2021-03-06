package engine;

import fish.Fish;
import graphics.Visual;

public class Poop extends Waste{
	
	public Poop(Visual visual, Fish origin){
		this.position = new Vector3D((float) (origin.position.x + Math.cos(origin.orientation.y) * origin.dimensions.x/2.0),
				(float) (origin.position.y + Math.sin(origin.orientation.z)*origin.dimensions.x/2.0),
				(float) (origin.position.z - Math.sin(origin.orientation.y) * origin.dimensions.x/2.0));
		this.absolutePosition = this.position.addVector(new Vector3D((int)(.4*Visual.fieldX), (int)(.5*Visual.fieldY)+(int)(Visual.zoomPercentage*Visual.fieldY*.5*(1-visual.tank.waterLevel)), (int)(-Visual.fieldZ)+(int)(Visual.zoomPercentage*.25*Visual.fieldZ)));
		this.velocity = new Vector3D(0, 1, 0);
		this.dimensions = new Vector3D((float) origin.size, (float) origin.size, (float) origin.size);
		this.color = new Vector3D(150, 100, 0);
		this.restingPosition = new Vector3D(0, (float) (.5*Visual.fieldY + .5*Visual.zoomPercentage*Visual.fieldY), 0);
	}
	
	public Poop(Visual visual, Vector3D position, Vector3D size){
		this.position = position;
		this.absolutePosition = this.position.addVector(new Vector3D((int)(.4*Visual.fieldX), (int)(.5*Visual.fieldY)+(int)(Visual.zoomPercentage*Visual.fieldY*.5*(1-visual.tank.waterLevel)), (int)(-Visual.fieldZ)+(int)(Visual.zoomPercentage*.25*Visual.fieldZ)));
		this.velocity = new Vector3D(0, 1, 0);
		this.dimensions = size;
		this.color = new Vector3D(150, 100, 0);
		this.restingPosition = new Vector3D(0, (float) (.5*Visual.fieldY + .5*Visual.zoomPercentage*Visual.fieldY), 0);

	}
	
	public void removeFromTank(Tank t){
		t.poops.remove(this);
		t.waste--;
	}
}
