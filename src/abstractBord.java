import java.awt.Graphics;
import java.util.ArrayList;

/**
 * @author Patty
 *
 */
public abstract class Racer {
	protected String ID;	// racer ID
	private double x;			// x position
	private double y;			// y position
	public boolean over = false;
	public double dir;
	public int seeDistance;
	public boolean ghost;
	public Tri[] dirTris;
	
	/** default constructor
	 *  Sets ID to blank
	 */
	public Racer() {
		ID = "";
	}
	
	/** Constructor
	 * @param rID	racerID
	 * @param rX	x position
	 * @param rY	y position
	 */
	public Racer(String rID, double rX, double rY) {
		ID = rID;
		x = rX;
		y = rY;
	}
	/** accessor for ID
	 * @return	ID
	 */
	public String getID() {
		return ID;
	}
	
	public double xDist(Racer second) {
		double dist = second.getX() - getX();
		
		return dist;
	}
	
	public double yDist(Racer second) {
		double dist = second.getY() - getY();

		return dist;
	}
	
	public double distance(Racer second) {
		double xComponent = xDist(second);
		double yComponent = yDist(second);
		return Math.sqrt(Math.pow(xComponent, 2) + Math.pow(yComponent, 2));
	}
	
	/** accessor for x
	 * @return	current x value
	 */
	public double getX() {
		return x;
	}
	
	/** accessor for y
	 * @return	current y value
	 */
	public double getY() {
		return y;
	}

	/** mutator for x
	 * @param	newX	new value for x
	 */
	public void setX(double newX) {
		x = newX;
	}

	/** mutator for y
	 * @param 	newY	new value for y
	 */
	public void setY(double newY) {
		y = newY;
	}

	/** abstract method for Racer's move
	 */
	public abstract void move();
	
	/** abstract method for drawing Racer
	 *  @param	g	Graphics context
	 */
	public abstract void draw( Graphics g );
	
	public ArrayList<Racer> sees() {
		ArrayList<Racer> canSee = new ArrayList<>();
		for (Racer racer : Race.getRacers()) {
			if (this == racer) 
				continue;
			if (distance(racer) < seeDistance)
				canSee.add(racer);
		}
		return canSee;
	}
	
//	public double angleTo(Racer second) {
//		return Math.atan(xTo(second) / yTo(second));
//	}
	
	public double angleTo(Racer second) {
		int sign;
		double angle = second.dir - dir;
		sign = angle < 0 ? -1 : 1;
		angle = Math.abs(angle);
		if (angle > Math.PI) {
			angle -= angle - Math.PI;
		}
		angle =  angle * sign;
		return angle;
	}
	
	
	/**
	 * Racer Field
	 */
	public boolean isWinner = false;
	
}
