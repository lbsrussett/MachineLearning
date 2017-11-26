package proj4;

public class Ant {
	
	private int antID;
	private boolean isCarrying = false;
	private Point point;
	
	public Ant(int antID) {
		this.antID = antID;
	}
	public boolean carrying() {
		return isCarrying;
	}
	public void pickUp(Point p) {
		isCarrying = true;
		point = p;
	}
	public void dropOff(Point p) {
		isCarrying = false;
		point = null;
	}
	public int getID() {
		return antID;
	}
}
