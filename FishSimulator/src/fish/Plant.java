package fish;

import saito.objloader.OBJModel;
import engine.Vector3D;
import graphics.Visual;

public abstract class Plant {
	public String name;
	public Vector3D position;
	public Vector3D absolutePosition;
	public Vector3D dimensions;
	public Vector3D orientation;
	public OBJModel model;
	public String sprite;

	public Plant createFromParameters(Visual visual, Vector3D absolutePosition, Vector3D orintation){
		this.absolutePosition = absolutePosition;
		this.orientation = orintation;
		this.position = this.absolutePosition.addVector(new Vector3D((int)(-.4*visual.fieldX), (int)(-.5*visual.fieldY)-(int)(visual.zoomPercentage*visual.fieldY*.5*(1-visual.tank.waterLevel)), (int)(visual.fieldZ)-(int)(visual.zoomPercentage*.25*visual.fieldZ)));
		return this;
	}
	
	public Plant setChosenPosition(Visual visual, Vector3D start, Vector3D end){
		Vector3D normal = end.addVector(start.multiplyScalar(-1)).normalize();
		float y = (float) (.5*visual.fieldY + visual.zoomPercentage*.5*visual.fieldY);
		float factor = (y-start.y)/normal.y;
		this.absolutePosition = start.addVector(normal.multiplyScalar(factor));
		this.position = this.absolutePosition.addVector(new Vector3D((int)(-.4*visual.fieldX), (int)(-.5*visual.fieldY)-(int)(visual.zoomPercentage*visual.fieldY*.5*(1-visual.tank.waterLevel)), (int)(visual.fieldZ)-(int)(visual.zoomPercentage*.25*visual.fieldZ)));
		return this;
	}
}

