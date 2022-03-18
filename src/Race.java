import java.awt.*;
import java.util.Random;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Race extends JFrame {
	private static ArrayList<Racer> racerList; // racers stored in ArrayList
	private static Race app;
	private final int FIRST_RACER = 50;
	private int finishX; // location of finish line, dependent on window width
	private boolean raceIsOn = false;
	private RacePanel racePanel;
	BufferedImage image;
	private Random rand = new Random();
	public static int width;
	public static int height;
	int i = 0;
	
	public Race() {
		super("The Tortoise & The Hare!");
		Container c = getContentPane();
		racePanel = new RacePanel();
		c.add(racePanel, BorderLayout.CENTER);

		racerList = new ArrayList<Racer>();
		setSize(400, 400);
		setVisible(true);
	}

	private void prepareToRace() {
		int input;

		for (int i = 0; i < 100; i++) {
			
			racerList.add(new Bord("boi1", randomX(), randomY(), randRad(), false));
		}
	} // end prepareToRace
	
	public double randRad() {
		return rand.nextDouble() * Math.PI * 2 - Math.PI;
	}

	private class RacePanel extends JPanel {
		/**
		 * paint method
		 * 
		 * @param g
		 *           Graphics context draws the finish line; moves and draws
		 *           racers
		 */

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			if (raceIsOn) {

				Iterator<Racer> itr = racerList.iterator(); 
				while (itr.hasNext()) {
					Racer racer = itr.next(); 
					if (racer.ghost) {
						itr.remove(); 
					} 
				}
				
				
				addGhosts();
		
				for (Racer racer : racerList) {
					
					
					height = getHeight();
					width = getWidth();
					
					
					
					if (!racer.ghost) {
				
						while (racer.getX() < 0) racer.setX(racer.getX() + getWidth());
						while (racer.getY() < 0) racer.setY(racer.getY() + getHeight());
						
						racer.setX(racer.getX() % getWidth());
						racer.setY(racer.getY() % getHeight());
						
							racer.move();
						
						
						
					}
					racer.draw(g);
					
					
				}
				i++;
				System.out.println(i);
				//drawAvg(g);
				/** end of student code, part 2 */
				
			}
		}
	}
	
	public void drawAvg( Graphics g) {
		Tri[] dirTris = new Tri[4];
		for (int i = 0; i < 4; i++) {
			dirTris[i] = new Tri();
		}
		 
	     double startY = 0;
	     double startX = 0;
		 for (Racer racer : racerList) {
			 if (!racer.ghost) {
				 startX += racer.getX();
				 startY += racer.getY();
				 for (int i = 0; i < 4; i++) {
					 //System.out.println(racer.dirTris[0]);
//					 if (dirTris[i].color != null) {
//						 dirTris[i].angle += racer.dirTris[i].angle;
//						 dirTris[i].color = racer.dirTris[i].color;
//					 }
				 }
			 }
			 
		 }
		 for (int i = 0; i < 4; i++) { 
			 dirTris[i].angle /= racerList.size();
		 }
	      startY /= racerList.size();
	      startX /= racerList.size();
	      g.setColor(Color.RED);
		     g.fillOval((int) startX - 15, (int) startY - 15, 30 ,30);
		     g.setColor(Color.BLACK); 
	     g.drawOval((int) startX - 15, (int) startY - 15, 30 ,30);
	     
	    
	     for (Tri tri : dirTris) {
	    	 if (tri.color != null) {
	    	drawTri(g, tri, startX, startY);
	    	 }
	     }
	     
	   }
	   
	   public void drawTri(Graphics g, Tri triangle, double x, double y) {
		// triangle points
		// {top, bot, right}
		   double angle = triangle.angle;
		   Color col = triangle.color;
		
		   
		   int[] a1 = {(int) (x + Math.cos(angle - .3 ) * 12), (int) (x + Math.cos(angle + .3 ) * 12), (int) (x + Math.cos(angle) * 30 )};
		   int[] a2 = {(int) (y + Math.sin(angle - .3 ) * 12), (int) (y + Math.sin(angle + .3 ) * 12), (int) (y + Math.sin(angle) * 30 )};
		   
		   g.setColor(col);
		   g.fillPolygon(a1, a2,3); 
		   
		   g.setColor(Color.BLACK);
		   g.drawPolygon(a1, a2,3);
	   }
	
	public void addGhosts() {
		
		int[] signs = {1, -1};
		int siz = 0;
		for (Racer racer : racerList) {
				siz ++;
			
			
		} 

		for (int i = 0; i < siz; i++) {
			Racer racer = racerList.get(i);
			for (int sign : signs) {
				
				racerList.add(new Bord("Ghost", racer.getX() + getW() * sign, racer.getY() + getH() * -sign, racer.dir, true));
				racerList.add(new Bord("Ghost", racer.getX() + getW() * sign, racer.getY() + getH() * sign, racer.dir, true));
				racerList.add(new Bord("Ghost", racer.getX() + getW() * sign, racer.getY(), racer.dir, true));
				racerList.add(new Bord("Ghost", racer.getX(), racer.getY() + getH() * sign, racer.dir, true));
			
			}
			
			
		}
	}

	/**
	 * runRace method checks whether any racers have been added to racerList if
	 * no racers, exits with message otherwise, runs race, calls repaint to move
	 * & draw racers calls reportRaceResults to identify winners(s) calls reset
	 * to set up for next race
	 * @throws InterruptedException 
	 */
	public void runRace() throws InterruptedException  {
		if (racerList.size() == 0) {
			JOptionPane.showMessageDialog(this, "The race has no racers. exiting",
					"No Racers", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		raceIsOn = true;
		while (true) {
			// slow down here if you know how
			Thread.sleep(30);
			repaint();
		} // end while
	}
	
	public static int getW() {
		return width;
	}
	
	public static int getH() {
		return height;
	}

	/**
	 * gets racer selection from user
	 * 
	 * @return first character of user entry if user presses cancel, exits the
	 *         program
	 */
	private char getRacer() {
		String input = JOptionPane.showInputDialog(this, "Enter a racer:"
				+ "\nt for Tortoise, h for hare, m for me, d for dog" + "\nor s to start the race");
		if (input == null) {
			System.out.println("Exiting");
			System.exit(0);
		}
		if (input.length() == 0)
			return 'n';
		else
			return input.charAt(0);
	}


	/**
	 * reset: sets up for next race: sets raceIsOn flag to false clears the list
	 * of racers resets racer position to FIRST_RACER enables checkboxes and
	 * radio buttons
	 * @throws InterruptedException 
	 */
	private void reset() throws InterruptedException {
		char answer;
		String input = JOptionPane.showInputDialog(this, "Another race? (y, n)");
		if (input == null || input.length() == 0) {
			System.out.println("Exiting");
			System.exit(0);
		}

		answer = input.charAt(0);
		if (answer == 'y' || answer == 'Y') {
			raceIsOn = false;
			racerList.clear();
			app.prepareToRace();
			app.runRace();
		} else
			System.exit(0);
	}
	
	private int randomX() {
		return rand.nextInt(getWidth());
	}
	
	private int randomY() {
		return rand.nextInt(getHeight());
	}
	
	public static ArrayList<Racer> getRacers() {
		return racerList;
	}


	/**
	 * main instantiates the Race object app 
	 * calls runRace method
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		app = new Race();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.prepareToRace();
		app.runRace();
	}

}