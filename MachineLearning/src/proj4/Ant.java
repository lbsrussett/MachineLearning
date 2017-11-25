package proj4;

public class Ant {
	
	private int antID;
	private boolean isCarrying = false;
	
	public Ant(int antID) {
		this.antID = antID;
	}
	public boolean carrying() {
		return isCarrying;
	}
	public void pickUp(Point p) {
		isCarrying = true;
	}
	public void dropOff(Point p) {
		isCarrying = false;
	}
	public int getID() {
		return antID;
	}
}
