/**
 * 
 */
package gng;

import java.util.Random;

/**
 * @author iwona
 *
 */
public class InputSignal {

	double x;
	double y;
	Random generator;
	//In bright future set of all available in the picture points in file, not range like this
	double squareStartX;
	double squareStartY;
	double squareEndX;
	double squareEndY;
	
	public InputSignal(double squareStartX, double squareStartY, double squareEndX, double squareEndY) {
		this.squareStartX = squareStartX;
		this.squareStartY = squareStartY;
		this.squareEndX = squareEndX;
		this.squareEndY = squareEndY;
		generator = new Random();
	}
	
	public void changePosition(){
    	this.x = squareStartX + generator.nextDouble()*squareEndX - 1;
    	this.y = squareStartY + generator.nextDouble()*squareEndY - 1;
	}
}
