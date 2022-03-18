/** Dog class
*   inherits from abstract Racer class
 * CS160-1
 * Mar 3, 2022
 * @author David Saifer & whoever wrote Program 3
 */


import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
//make all work on same timestep
//forces based on dist
public class Bord extends Racer
{
	static Random rand;
	Tri[] dirTris = new Tri[4];
	double speed = 7;
	
   /** Default Constructor: calls Racer default constructor
   */
   public Bord( )
   {
     super();
   }

   /** Constructor also initializes image
   *    @param rID   racer Id, passed to Racer constructor
   *    @param rX    x position, passed to Racer constructor
   *    @param rY    y position, passed to Racer constructor
   */
   public Bord( String rID, double rX, double rY , double dir, boolean ghost)
   {
     super( rID, rX, rY );
     seeDistance = 100;
     this.dir = dir;
     this.ghost = ghost;
//    for (int i = 0; i < 4; i++) {
// 		 dirTris[i] = new Tri();
// 	}
   }
   
   /**
 *  initializes random object to be used in rest of class
 */
private Random getRand( ) {
	      if (rand == null) {
	    	  rand = new Random( );
	      }
	      return rand;
	   }
	// should be fixed
	public void setDir(double radians) {
		dir += radians;
		dir = normalizeDir(dir);
		
	}
	
	public double normalizeDir (double dire) {
	while (dire < -Math.PI)  {
		dire += Math.PI * 2;
		}
	while (dire > Math.PI) {
		dire += -Math.PI * 2;
	}
	return dire;
	}
	
	public void turnClock(int angle) {
		setDir((angle / 360.) * Math.PI * 2);
	}
	
	public void turnCounter(int angle) {
		setDir((angle / 360.) * Math.PI * -2);
	}
	
	public double avgDir(ArrayList<Racer> arr) {
		double xComp = 0;
		double yComp = 0;
		for (Racer racer : arr) {
			xComp += Math.cos(racer.dir);
			yComp += Math.sin(racer.dir);
		}
		return Math.atan2(yComp, xComp);
	}
	
	public double avgPosDir(ArrayList<Racer> arr) {
		double xComp = 0;
		double yComp = 0;
		for (Racer racer : arr) {
			
			xComp += xDist(racer);
			
			yComp += yDist(racer) ;
		}
		return Math.atan2(yComp, xComp);
	}
	
	public double avgSepDir(ArrayList<Racer> arr) {
		// 1. get dir * dist for each go away from average
		double xComp = 0;
		double yComp = 0;
		for (Racer racer : arr) {
			if (xDist(racer) != 0.0) {
				xComp +=   xDist(racer);
				
				yComp +=   yDist(racer);
			}
		}

		
		return normalizeDir(Math.atan2(yComp, xComp) + Math.PI);
	}
	
   /** move:Takes X position of racer and adds 0 or 3 to it
    * Takes Y position of racer and adds the sin of its X value * 3 to it
   */
   public void move( )
   {
	//System.out.println(avgDir + " " + dir);
	
	alignment();
	cohesion();
	seperation();

    setX(getX() + Math.cos(dir) * speed);
    setY(getY() + Math.sin(dir) * speed);
   }
   //aligns down right
   public void alignment() {
	   int ammt = 2;
	   ArrayList<Racer> see = sees();
	   if (see.size() != 0) {
		   double avgDir = avgDir(see);
	
		   
		   dirTris[0] = new Tri(avgDir, Color.YELLOW);
		   toAngle(dir, avgDir, ammt);
	   } else {
		   dirTris[0] = null;
	   }
   }
   // go out from center
   public void seperation() {
	   int ammt = 7;
	   ArrayList<Racer> see = sees();
	   if (see.size() != 0) {
		   double avgDir = avgSepDir(see);
		   
		   dirTris[1] = new Tri(avgDir, Color.BLUE);
		   toAngle(dir, avgDir, ammt);
	   } else {
		   dirTris[1] = null;
	   }
   }
   
   //think works correct
   public void cohesion() {
	   int ammt = 3;
	   ArrayList<Racer> see = sees();
	   if (see.size() != 0) {
		   double avgDir = avgPosDir(see);
		   
		   toAngle(dir, avgDir, ammt);
		   dirTris[2] = new Tri(avgDir,Color.GREEN);
	   } else {
		   dirTris[2] = null;
	   }
   }
   
   
   public void toAngle(double from, double to, int amt) {

	   if (Math.min(Math.min(Math.abs(from - to), Math.abs(Math.PI + from - to)), Math.abs(-Math.PI + from - to)) < .1) {
		   return;
	   }
	   if (from < to) {
		   if (Math.abs(from - to) < Math.PI) {
			   turnClock(amt);
		   }
		   else {

			   turnCounter(amt);
		   }
	   }
	   else {
		   if (Math.abs(from - to) < Math.PI) {
			   turnCounter(amt);

		   }
		   else {

			   turnClock(amt);
		   }
	   }
   }

   /** draw: draws the Dog at (x - 30, y) coordinate
   *   @param g   Graphics context
   */
   public void draw( Graphics g )
   {
     int startY = (int) getY( );
     int startX = (int) getX( );

     g.drawOval(startX - 10, startY - 10, 20 ,20);
     
     dirTris[3] = new Tri(dir, Color.BLACK);
     for (Tri tris : dirTris) {
    	 if (tris != null)
    		 drawTri(g, tris);
     }
     
   }
   
   public void drawTri(Graphics g, Tri triangle) {
	// triangle points
	// {top, bot, right}
	   double angle = triangle.angle;
	   Color col = triangle.color;
	
	   
	   int[] a1 = {(int) (getX() + Math.cos(angle - .3 ) * 12), (int) (getX() + Math.cos(angle + .3 ) * 12), (int) (getX() + Math.cos(angle) * 30 )};
	   int[] a2 = {(int) (getY() + Math.sin(angle - .3 ) * 12), (int) (getY() + Math.sin(angle + .3 ) * 12), (int) (getY() + Math.sin(angle) * 30 )};
	   
	   g.setColor(col);
	   g.fillPolygon(a1, a2,3); 
	   
	   g.setColor(Color.BLACK);
	   g.drawPolygon(a1, a2,3);
   }
   
 

}