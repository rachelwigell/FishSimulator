package engine;

import graphics.Visual;

public abstract class Waste {
	public Vector3D position;
	public Vector3D absolutePosition;
	public Vector3D color;
	public Vector3D velocity;
	public Vector3D dimensions;
	public Vector3D restingPosition;
	
	public void updatePosition(){
		if(this.absolutePosition.y < this.restingPosition.y){
			this.absolutePosition = this.absolutePosition.addVector(velocity);
			this.position = this.position.addVector(velocity);
		}
	}
	
	public void removeFromTank(Tank t){}
}
