/**
 * 
 */
package gng;

/**
 * @author iwona
 *
 */
import static java.lang.Math.*;
import java.io.Serializable;
import java.util.Random;
import java.util.*;
import gng.Edge;
import gng.Neuron;

public class GrowingNeuralGas implements Serializable {
    private static final long serialVersionUID = 2L;

    Random generator;
    //The number of signals processed so far
    private int signalsProcessed;
    //The number of iterations to proceed
    private int maxIterations;
    //The learning rate to update best matching neuron
    private double epsBest;
    //The learning rate to update neighbors of best matching neuron
    private double epsNeighbor;
    //The maximum age of edges
    private int edgeLifetime;
    //If the number of input signals so far is an integer multiple of lambda, insert a new neuron
    private int lambda;
    //Range of positions for the square - in the future it can be a set of available points in a file
    /*private double squareStartX;
    private double squareStartY;
    private double squareEndX;
    private double squareEndY;*/
    //Input signal
    InputSignal inputSignal;
    //Neurons in the neural network
    private ArrayList<Neuron> neurons = new ArrayList<>();
    //The workspace to find nearest neighbors - top2[0] is  the closest neuron, top2[1] is second closest
    private Neuron[] top2 = new Neuron[2];

    /**
     * Constructor.
     * @param d the dimensionality of signals.
     * @param epsBest the learning rate to update best matching neuron.
     * @param epsNeighbor the learning rate to update neighbors of best matching neuron.
     * @param edgeLifetime the maximum age of edges.
     * @param lambda if the number of input signals so far is an integer multiple
     *               of lambda, insert a new neuron.
     * @param alpha decrease error variables by multiplying them with alpha
     *              during inserting a new neuron.
     * @param beta decrease all error variables by multiply them with beta.
     */
    public GrowingNeuralGas(double epsBest, double epsNeighbor, int edgeLifetime, int lambda, int maxIterations, double squareStartX, double squareStartY, double squareEndX, double squareEndY) {
        this.epsBest = epsBest;
        this.epsNeighbor = epsNeighbor;
        this.edgeLifetime = edgeLifetime;
        this.lambda = lambda;
        this.maxIterations = maxIterations;
        this.signalsProcessed = 0;
	    /*this.squareStartX = squareStartX;
    	this.squareStartY = squareStartY;
    	this.squareEndX = squareEndX;
    	this.squareEndY = squareEndY;*/
    	
    	generator = new Random();
    	inputSignal = new InputSignal(squareStartX, squareStartY, squareEndX, squareEndY);
    }
    
    public void initialise(int firstNeuronsAmount, double initStartX, double initStartY, double initEndX, double initEndY) {
    	double x;
    	double y;
    	for(int i = 0; i < firstNeuronsAmount; i++) {
    		x = initStartX + generator.nextDouble()*(initEndX - initStartX);
        	y = initStartY + generator.nextDouble()*(initEndY - initStartY);
    		Neuron n = new Neuron(x, y, 0);
    		System.out.println("INITIALISE");
    		System.out.println(i + "_neuron_" + n.x + "_" + n.y);
    		neurons.add(n);
    	}
    	//For each neuron add 2 edges (2 is a sample used here)
    	/*for(int i = 0; i < firstNeuronsAmount; i++) {
    		//todo
    	}*/
    	//In the beginning we have 3 neurons and I connect them in triangle
    	for(int i = 0; i < firstNeuronsAmount; i++) {
    		for(int j = 0; j < firstNeuronsAmount; j++) {
        		if(i != j) {
        			neurons.get(i).addEdge(neurons.get(j), 0);
        		}
        	}
    		//only for print debugging, remove later:
    		for (Iterator<Edge> iter = neurons.get(i).edges.iterator(); iter.hasNext();) {
                Edge edge = iter.next();
                System.out.println(i + "_neuron_edgeNeighbor_" + edge.neighbor.x + "_edgeAge_" + edge.age);
            }
    	}
    }
    
    public void countDistances() {
    	System.out.println("COUNTDISTANCES");
    	double distance = 0;
    	for(int neuronIndex = 0; neuronIndex < neurons.size(); neuronIndex++) {
	    	//count distance 
    		//Euklides method :D 
    		double xPow = pow(neurons.get(neuronIndex).x - inputSignal.x, 2);
    		double yPow = pow(neurons.get(neuronIndex).y - inputSignal.y, 2);
    		distance = sqrt(xPow + yPow);
	    	//update neuron's distance property
	    	neurons.get(neuronIndex).distance = distance;//test
	    	System.out.println(neuronIndex + "_distance_neuron_" + neurons.get(neuronIndex).distance);
    	}
    }
    
    public void findClosest(){
    	System.out.println("FINDCLOSEST");
    	//I assume we have min 3 neurons from the beginning, if not, it will break xd
    	//Put first two neurons in the list to top2
    	if(neurons.get(0).distance < neurons.get(1).distance) {
    		top2[0] = neurons.get(0);
    		top2[1] = neurons.get(1);
    	}else {
    		top2[0] = neurons.get(1);
    		top2[1] = neurons.get(0);
    	}
    	//compare the rest neurons' distances with initial top2
    	for(int neuronIndex = 2; neuronIndex < neurons.size(); neuronIndex++) {
    		if(neurons.get(neuronIndex).distance < top2[0].distance) {
    			top2[0] = neurons.get(neuronIndex);
        	}else if(neurons.get(neuronIndex).distance < top2[1].distance){
        		top2[1] = neurons.get(neuronIndex);
        	}
    	}
    	System.out.println("_top2[0]_" + top2[0].x + "_" + top2[0].y);
    	System.out.println("_top2[1]_" + top2[1].x + "_" + top2[1].y);
    }
    
    public void connectTop2() {
    	System.out.println("CONNECTTOP2");
    	// DO PRZETESTOWANIA CZY DOBRE WYNIKI NA TYM ETAPIE ZWRACA I DOBRZE WIAZALO WCZESNIEJ EDGAMI
    	// - sprawdzone
    	boolean connect = true;
    	for (Iterator<Edge> iter = top2[0].edges.iterator(); iter.hasNext();) {
            Edge edge = iter.next();
            if (edge.neighbor == top2[1]) {
                connect = false;
                break;
            }
        }
    	if(connect) {
    		System.out.println("Connect needed between top2");
    		top2[0].addEdge(top2[1], 0);
    		top2[1].addEdge(top2[0], 0);
    	}
    }
    
    public void moveNeuron(Neuron neuron, boolean isTheClosest) {
    	System.out.println("MOVENEURON");
    	//Count the linear function going between signal and neuron
    	double a = (neuron.y - inputSignal.y)/(neuron.x - inputSignal.x);
    	double b = neuron.y - (a*neuron.x);
    	double xMovement;
    	//Find new position for the neuron
    	if(isTheClosest) {
    		xMovement = abs(neuron.x - inputSignal.x)*epsBest;//moze wartoc bezwzgl z tego wez
    	} else {
    		xMovement = abs(neuron.x - inputSignal.x)*epsNeighbor;
    	}
    	if(neuron.x < inputSignal.x) {
    		neuron.x = neuron.x + xMovement;
    	}else {
    		neuron.x = neuron.x - xMovement;
    	}
    	neuron.y = a*neuron.x + b;    	
    }
    
    public void moveNeurons() {
    	System.out.println("MOVENEURONS_");
    	moveNeuron(top2[0], true);
    	//moveNeuron.updateError();
    	for (Iterator<Edge> iter = top2[0].edges.iterator(); iter.hasNext();) {
            Edge edge = iter.next();
            moveNeuron(edge.neighbor, false);
            //increase age of the edge that is updated
            edge.increaseAge();
            //edge.neighbor.updateError();
        }
    	 
    	 //only for println debugging, remove later:
    	 for(int i = 0; i < neurons.size(); i++) {
     		Neuron n = neurons.get(i);
     		System.out.println(i + "_moved_neuron_" + n.x + "_" + n.y);
     	}
    }
    
    public void updateErrors() {
    	System.out.println("UPDATEERRORS");
    	for(int i = 0; i < neurons.size(); i++) {
     		Neuron n = neurons.get(i);
     		n.updateError();
     		System.out.println(i + "_error_neuron_" + n.error);
     		//jesli error ma wzrastac tylko tym updateowanym, to w funkcji movee=neurons zrob to 
     	}
    }
    
    public void removeUnused() {
    	System.out.println("REMOVEUNUSED");
    	for(int i = 0; i < neurons.size(); i++) {
    		for (Iterator<Edge> iter = neurons.get(i).edges.iterator(); iter.hasNext();) {
    			//System.out.println(i + "_REmoved_part1_");
    			/*if(iter.next() == null) {
    				System.out.println("NULL");
    				continue;
    			}*/
                Edge edge = iter.next();
                //System.out.println(i + "_REmoved_part2_" + edge.neighbor);
                if(edge.age < (this.signalsProcessed - this.edgeLifetime)) {
                	//remove egde
                	//neurons.get(i).removeEdge(edge.neighbor);
                	//edge.neighbor.removeEdge(neurons.get(i));
                	
                	
                	//jak tworzysz now¹ edge potem w trakcie, to pamietaj, zeby jej age inicjalizowac
                	//na this.signalsProcessed, nie 0 KONIECZNIE!
                	//i wtedy ten if bedzie zawsze odsiewal nieupdateowane, a nie wszystkie swieze tez
                }
            }
    		if(neurons.get(i).edges.size() == 0) {
    			//remove neuron
    			//neurons.remove(i);
    		}
    	}
    }
    
    public void createNewNeuron() {
    	System.out.println("CREATENEWNEURON");
    	//find neuron with the biggest accumulated error
    	Neuron n = neurons.get(0);
    	for(int i = 1; i < neurons.size(); i++) {
    		if(neurons.get(i).error > n.error) {
    			n = neurons.get(i);
    		}
    	}
    	System.out.println("neuron_highest_error1_" + n.x + "_" + n.y + "Error: " + n.error);
    	
    	//for this neuron find it’s neighbor with the highest accumulated error
    	Neuron nn = n.edges.get(0).neighbor;
    	for(int i = 1; i < n.edges.size(); i++) {
    		if(n.edges.get(i).neighbor.error > n.error) {
    			nn = n.edges.get(i).neighbor;
    		}
    	}
    	System.out.println("neuron_highest_error2_" + nn.x + "_" + nn.y + "Error: " + nn.error);
    	
    	
    	//wartoœæ error pozostaje bez zmian dla nich?
    	//logi dorob wszedzie
    	
    	//TERAZ SPRAWDZ CZY TEN NEIGHBOOR TO NA PEWNO REFERENCJA DO NEURONA Z LISTY NEURONS OGOLNEJ A NIE KOPIA
    	//create new neuron in the middle of them
    	double x = (n.x + nn.x)/2;
    	double y = (n.y + nn.y)/2;
		Neuron newNeuron = new Neuron(x, y, 0);
		System.out.println("_NEW_neuron_" + newNeuron.x + "_" + newNeuron.y);
		neurons.add(newNeuron);
		
		//usun obie edges - w obu kierunkach
		n.removeEdge(nn);
		nn.removeEdge(n);
		//stworz nowe edges - 4, w obu kierunkach po obu stronach
		n.addEdge(newNeuron, this.signalsProcessed);
		nn.addEdge(newNeuron, this.signalsProcessed);
		newNeuron.addEdge(n, this.signalsProcessed);
		newNeuron.addEdge(nn, this.signalsProcessed);
		
		//only for print debugging, remove later:
		for (Iterator<Edge> iter = n.edges.iterator(); iter.hasNext();) {
            Edge edge = iter.next();
            System.out.println("AFTER_CREATION_n_neuron_edgeNeighbor_" + edge.neighbor.x + "_edgeAge_" + edge.age);
        }
		for (Iterator<Edge> iter = nn.edges.iterator(); iter.hasNext();) {
            Edge edge = iter.next();
            System.out.println("AFTER_CREATION_nn_neuron_edgeNeighbor_" + edge.neighbor.x + "_edgeAge_" + edge.age);
        }
		for (Iterator<Edge> iter = newNeuron.edges.iterator(); iter.hasNext();) {
            Edge edge = iter.next();
            System.out.println("AFTER_CREATION_newNeuron_neuron_edgeNeighbor_" + edge.neighbor.x + "_edgeAge_" + edge.age);
        }
    }
    
    public void grow() {
    	//Set the shape - for now, square with diagonal (0,0) - (100, 100)
    	//in bright future it could be a file with points position that build a picture
    	
    	for(int currentIteration = 0; currentIteration < maxIterations; currentIteration++) {
    		inputSignal.changePosition();
    		System.out.println(currentIteration + "_inputSignal_" + inputSignal.x + "_" + inputSignal.y);
    		countDistances();
    		//find the closest two neurons
    		findClosest();
    		//connect top2 neurons if edge doesn't exist for them yet
    		connectTop2();
    		//move top2[0] and its neighbors towards input signal, age of the edges is updated also here
    		moveNeurons();
    		updateErrors();
    		//age of the edges is updated also here
    		removeUnused();
    		
    		this.signalsProcessed++;
    		
    		//nie miesci mi sie w glowie, ze tu nie mozna zrobic if(!(this.signalsProcessed % this.lambda))...
    		int timeForNew = this.signalsProcessed % this.lambda;
    		if(timeForNew == 0) {
    			createNewNeuron();
    			//nowy neuron zrob
    		}
    		
    		//sprawdz poprawnosc dzialania jak juz bd wiecej neuronow niz 3, sprobuj dac this. przed funkcjami tu w wywolaniach
    		
    		//change errors, counters, edges etc. if needed, add new neuron 
    		
    	}
    }
    
}
