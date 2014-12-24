package fish;

import engine.Vector3D;
import graphics.Visual;

import java.util.Random;

import processing.core.PVector;
import saito.objloader.OBJModel;

public class TallPlant extends Plant{
	
	public TallPlant(Visual visual){
		this.name = "Tall Plant";
		this.model = new OBJModel(visual, "plant.obj", Visual.POLYGON);
		this.sprite = "plant.png";
		this.model.translateToCenter();
		this.model.scale(50, 70, 50);
		this.dimensions = new Vector3D(100, 630, 100);
		this.model.translate(new PVector(0, 315, 0));
		Random random = new Random();
		this.orientation = new Vector3D(0, random.nextFloat()*visual.PI*2, 0);
	}
}
