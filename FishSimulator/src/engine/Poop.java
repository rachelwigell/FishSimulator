package engine;

import fish.Fish;
import graphics.Visual;

public class Poop extends Sinkers{
	
	public Poop(Visual visual, Fish origin){
		this.position = new Vector3D((float) (origin.position.x + Math.cos(origin.orientation.y) * origin.dimensions.x/2.0),
				origin.position.y,
				(float) (origin.position.z - Math.sin(origin.orientation.y) * origin.dimensions.x/2.0));
		this.position = this.position.addVector(new Vector3D((int)(.4*visual.fieldX), (int)(.5*visual.fieldY)+(int)(visual.zoomPercentage*visual.fieldY*.5*(1-visual.tank.waterLevel)), (int)(-visual.fieldZ)+(int)(visual.zoomPercentage*.25*visual.fieldZ)));
		this.velocity = new Vector3D(0, 1, 0);
		this.dimensions = new Vector3D((float) origin.size, (float) origin.size, (float) origin.size);
		this.color = new Vector3D(150, 100, 0);
		this.restingPosition = new Vector3D(0, (float) (.5*visual.fieldY + .5*visual.zoomPercentage*visual.fieldY), 0);
	}
}
