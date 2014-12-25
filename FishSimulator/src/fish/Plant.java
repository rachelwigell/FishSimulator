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

	public Plant createFromParameters(Visual visual, Vector3D absolutePosition, Vector3D orientation){
		this.absolutePosition = absolutePosition;
		this.orientation = orientation;
		this.position = this.absolutePosition.addVector(new Vector3D((int)(-.4*Visual.fieldX), (int)(-.5*Visual.fieldY)-(int)(Visual.zoomPercentage*Visual.fieldY*.5*(1-visual.tank.waterLevel)), (int)(Visual.fieldZ)-(int)(Visual.zoomPercentage*.25*Visual.fieldZ)));
		return this;
	}
	
	public Plant setChosenPosition(Visual visual, Vector3D start, Vector3D end){
		Vector3D normal = end.addVector(start.multiplyScalar(-1)).normalize();
		float y = (float) (.5*Visual.fieldY + Visual.zoomPercentage*.5*Visual.fieldY);
		float factor = (y-start.y)/normal.y;
		this.absolutePosition = start.addVector(normal.multiplyScalar(factor));
		this.position = this.absolutePosition.addVector(new Vector3D((int)(-.4*Visual.fieldX), (int)(-.5*Visual.fieldY)-(int)(Visual.zoomPercentage*Visual.fieldY*.5*(1-visual.tank.waterLevel)), (int)(Visual.fieldZ)-(int)(Visual.zoomPercentage*.25*Visual.fieldZ)));
		return this;
	}
	
	public void drawPlant(Visual visual){
		visual.noStroke();
		visual.pushMatrix();
		visual.translate((int)(.4*Visual.fieldX),
				(int)(.5*Visual.fieldY)+(int)(Visual.zoomPercentage*Visual.fieldY*.5*(1-visual.tank.waterLevel)),
				(int)(-Visual.fieldZ)+(int)(Visual.zoomPercentage*.25*Visual.fieldZ));
		visual.translate(this.position.x, this.position.y, this.position.z);
		visual.rotateY(this.orientation.y);
		this.model.draw();
		visual.popMatrix();
	}
}

