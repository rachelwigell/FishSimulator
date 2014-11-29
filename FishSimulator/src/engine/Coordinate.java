package engine;

public class Coordinate {
	public float x;
	public float y;
	public float z;
	
	public Coordinate(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float distance(Coordinate to){
		return (float) Math.sqrt(squareDistance(to));
	}
	
	public float squareDistance(Coordinate to){
		return (float) (Math.pow(this.x - to.x, 2) + Math.pow(this.y - to.y, 2) + Math.pow(this.z - to.z, 2));
	}
}
