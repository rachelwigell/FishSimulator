package engine;

import graphics.Visual;

public class Food extends Sinkers{

	public Food(Visual visual){
		int x = visual.mouseX;
		int y = visual.mouseY;
		double percentAcross = (double) (x-visual.tankMinX) / (double) (visual.tankMaxX - visual.tankMinX)-.5;
		double percentUp = (double) (y-visual.tankMinY) / (double) (visual.tankMaxY - visual.tankMinY)-.5;
		double modelX = percentAcross * (.8*visual.zoomPercentage*visual.fieldX);
		double modelY = percentUp * (visual.tank.waterLevel*visual.zoomPercentage*visual.fieldY);
		
		this.position = new Vector3D((float) (modelX), (float) (modelY), (float) (-.25*visual.zoomPercentage*visual.fieldZ + 5));
		this.velocity = new Vector3D(0, 1, 0);
		this.dimensions = new Vector3D(5, 5, 5);
		this.color = new Vector3D(200, 200, 0);
		this.restingPosition = new Vector3D(0, (float) (.5*visual.zoomPercentage*visual.fieldY - .5*visual.zoomPercentage*visual.fieldY*(1-visual.tank.waterLevel)), 0);
	}
}
