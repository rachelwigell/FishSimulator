package engine;


public abstract class Waste {
	public Vector3D position;
	public Vector3D absolutePosition;
	public Vector3D color;
	public Vector3D velocity;
	public Vector3D dimensions;
	public Vector3D restingPosition;
	public Vector3D speedChangeLocation;
	
	public void updatePosition(){
		if((this.velocity.y > 0 && this.absolutePosition.y < this.restingPosition.y)
				|| (this.velocity.y < 0 && this.absolutePosition.y > this.restingPosition.y)){
			this.absolutePosition = this.absolutePosition.addVector(velocity);
			this.position = this.position.addVector(velocity);
			if(this.velocity.y > 1){
				if(this.absolutePosition.y > this.speedChangeLocation.y){
					this.velocity.y = 1;
				}
			}
		}
	}
	
	public void removeFromTank(Tank t){}
}
