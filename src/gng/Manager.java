/**
 * 
 */
package gng;

/**
 * @author iwona
 *
 */
public class Manager {
	
	public Manager() {
	}
	
	public void collectDataAndRun() {
		//TODO - get this data from user, GUI
	    double epsBest = 0.5;
	    double epsNeighbor = 0.2;
	    int edgeLifetime = 50;
	    int lambda = 5;
	    int maxIterations = 100;
	    double squareStartX = 0;
    	double squareStartY = 0;
    	double squareEndX = 100;
    	double squareEndY = 100;
    	
    	int firstNeuronsAmount = 3;// MINIMUM 3! Do not change this value
    	//Range used to set the initial points somewhere close to each other
    	double initStartX = 40;
    	double initStartY = 40;
    	double initEndX = 42;
    	double initEndY = 42;
	    
	    GrowingNeuralGas growingNeuralGas = new GrowingNeuralGas(epsBest, epsNeighbor, edgeLifetime, lambda, maxIterations, squareStartX, squareStartY, squareEndX, squareEndY);
	    growingNeuralGas.initialise(firstNeuronsAmount, initStartX, initStartY, initEndX, initEndY);
	    growingNeuralGas.grow();
	}

	public static void main(String[] args) {
		Manager manager = new Manager();
    	manager.collectDataAndRun();
    }
}
