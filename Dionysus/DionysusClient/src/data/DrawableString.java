package data;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
/**
 * Reimagination of String to become drawable in my console application.
 * @author Julian G. West
 *
 */
public class DrawableString{
	private String string;
	private String temp;
	private boolean isDrawn;
	private int iteration;
	/**
	 * Defeault constructor, creates a displayable version of the string.
	 * @param string - to be displayed
	 */
	public DrawableString(String string){
		isDrawn = false;
		this.string = string;
		iteration = 0;
		temp = ""+string.charAt(0);
	}
	/**
	 * Constructor for possibly instant messages.
	 * @param string - to be displayed
	 * @param isInstant - true if to appear instantly
	 */
	public DrawableString(String string, boolean isInstant){
		isDrawn = false;
		this.string = string;
		if(isInstant = true)
			iteration = string.length();
		else
			iteration = 0;
		temp = ""+string.charAt(0);
	}
	/**
	 * Get the current point of iteration
	 * @return - the index of the string the drawing has iterated to.
	 */
	public int getIteration(){
		return iteration;
	}
	/**
	 * Return the body
	 * @return - string of the message
	 */
	public String getString(){
		return string;
	}
	/**
	 * Find out whether or not the object has comepleted an iteration.
	 * @return - true if iteration has finished
	 */
	public boolean isDrawn(){
		return isDrawn;
	}
	/**
	 * Change message.
	 * @param string - New message
	 */
	public void setString(String string){
		this.string = string;
	}
	/**
	 * Draw method to display the string bit by bit.
	 * @param g - graphics object from paintComponent
	 * @param location - x and y to draw the string at.
	 */
	public void draw(Graphics2D g, Point2D location){
		if(iteration < string.length()-1){
			if(iteration%2 == 1)
				g.drawString(temp, (int)location.getX(),(int)location.getY());
			else 
				g.drawString(temp + "|", (int)location.getX(),(int)location.getY());
			iteration++;
			temp += string.charAt(iteration);
		} else {
			isDrawn = true;
			g.drawString(string,(int)location.getX(),(int)location.getY());
		}
	}
}
