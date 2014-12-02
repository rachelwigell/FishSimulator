package engine;

import graphics.Visual;

import java.util.Random;

public class Food extends Sinkers{

	public Food(Visual visual, Vector3D start, Vector3D end){
		Vector3D normal = end.addVector(start.multiplyScalar(-1)).normalize();
		Random random = new Random();
		float percent = random.nextFloat();
		float z = (float) (percent*(.5*visual.zoomPercentage*visual.fieldZ-20) + -visual.fieldZ + 10);
		float factor = (z-start.z)/normal.z;
		this.position = start.addVector(normal.multiplyScalar(factor));
		this.velocity = new Vector3D(0, 1, 0);
		this.dimensions = new Vector3D(5, 5, 5);
		this.color = new Vector3D(200, 200, 0);
		this.restingPosition = new Vector3D(0, (float) (.5*visual.fieldY + .5*visual.zoomPercentage*visual.fieldY), 0);
	}
	
	public Food(Visual visual, Vector3D position){
		this.position = position;
		this.velocity = new Vector3D(0, 1, 0);
		this.dimensions = new Vector3D(5, 5, 5);
		this.color = new Vector3D(200, 200, 0);
		this.restingPosition = new Vector3D(0, (float) (.5*visual.fieldY + .5*visual.zoomPercentage*visual.fieldY), 0);
	}
}
