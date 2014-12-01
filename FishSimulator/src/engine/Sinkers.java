package engine;

import graphics.Visual;

public abstract class Sinkers {
	public Vector3D position;
	public Vector3D color;
	public Vector3D velocity;
	public Vector3D dimensions;
	public Vector3D restingPosition;
	
	public void updatePosition(){
		if(this.position.y < this.restingPosition.y){
			this.position = this.position.addVector(velocity);
		}
	}
}
