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
import gng.HeapSelect;
import gng.Edge;
import gng.Neuron;

public class GrowingNeuralGas implements Serializable {
    private static final long serialVersionUID = 2L;

    Random generator;
    // The dimensionality of signals
    private final int d;
    //The number of signals processed so far
    private int t;
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
    //Decrease error variables by multiplying them with alpha during inserting a new neuron
    private double alpha;
    //Decrease all error variables by multiply them with beta
    private double beta;
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
    public GrowingNeuralGas(int d, double epsBest, double epsNeighbor, int edgeLifetime, int lambda, double alpha, double beta, int maxIterations, double squareStartX, double squareStartY, double squareEndX, double squareEndY) {
        this.d = d;
        this.epsBest = epsBest;
        this.epsNeighbor = epsNeighbor;
        this.edgeLifetime = edgeLifetime;
        this.lambda = lambda;
        this.alpha = alpha;
        this.beta = beta;
        this.maxIterations = maxIterations;
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
    		x = initStartX + generator.nextDouble()*initEndX - 1;
        	y = initStartY + generator.nextDouble()*initEndY - 1;
    		Neuron n = new Neuron(x, y, 0);
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
    	}
    }
    
    public void countDistances() {
    	double distance = 0;
    	for(int neuronIndex = 0; neuronIndex < neurons.size(); neuronIndex++) {
	    	//count distance 
    		//Euklides method :D 
    		double xPow = pow(neurons.get(neuronIndex).x - inputSignal.x, 2);
    		double yPow = pow(neurons.get(neuronIndex).y - inputSignal.y, 2);
    		distance = sqrt(xPow + yPow);
	    	//update neuron's distance property
	    	neurons.get(neuronIndex).distance = distance;//test
    	}
    }
    
    public void findClosest(){
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
    	// DO PRZETESTOWANIA CZY DOBRE WYNIKI NA TYM ETAPIE ZWRACA I DOBRZE WIAZALO WCZESNIEJ EDGAMI
    	boolean connect = true;
    	for (Iterator<Edge> iter = top2[0].edges.iterator(); iter.hasNext();) {
            Edge edge = iter.next();
            if (edge.neighbor == top2[1]) {
                connect = false;
                break;
            }
        }
    	if(connect) {
    		top2[0].addEdge(top2[1], 0);
    		top2[1].addEdge(top2[0], 0);
    	}
    }
    
    public void moveNeuron(Neuron neuron, boolean isTheClosest) {
    	//Count the linear function going between signal and neuron
    	double a = (neuron.y - inputSignal.y)/(neuron.x - inputSignal.x);
    	double b = neuron.y - (a*neuron.x);
    	double xMovement;
    	//Find new position for the neuron
    	if(isTheClosest) {
    		xMovement = (neuron.x - inputSignal.x)*epsBest;//moze wartoc bezwzgl z tego wez
    	} else {
    		xMovement = (neuron.x - inputSignal.x)*epsNeighbor;
    	}
    	if(neuron.x < inputSignal.x) {
    		neuron.x = neuron.x + xMovement;
    	}else {
    		neuron.x = neuron.x - xMovement;
    	}
    	neuron.y = a*neuron.x + b;    	
    }
    
    public void moveNeurons() {
    	moveNeuron(top2[0], true);
    	 for (Iterator<Edge> iter = top2[0].edges.iterator(); iter.hasNext();) {
            Edge edge = iter.next();
            moveNeuron(edge.neighbor, false);
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
    		//connect top2 neurons if edge doesn't exist for them yet, move top2[0] and its neighboors
    		connectTop2();
    		//towards input signal
    		moveNeurons();
    		//change errors, counters, edges etc. if needed, add new neuron if needed
    		//System.out.println(currentIteration + "_" + randomPoint.x + "_" + randomPoint.y);
    	}
    }

    /*
    public void update(double[] x) {
        t++;

        if (neurons.size() < 2) {
           // neurons.add(new Neuron(x.clone()));
            return;
        }

        // Find the nearest (s1) and second nearest (s2) neuron to x.
        neurons.stream().parallel().forEach(neuron -> neuron.distance(x));

        Arrays.fill(top2, null);
        HeapSelect<Neuron> heap = new HeapSelect<>(top2);
        for (Neuron neuron : neurons) {
            heap.add(neuron);
        }

        Neuron s1 = top2[1];
        Neuron s2 = top2[0];

        // update s1
        s1.update(x, epsBest);
        // update local counter of squared distance
        s1.counter += s1.distance * s1.distance;
        // Increase the edge of all edges emanating from s1./increase the age
        s1.age();

        boolean addEdge = true;
        for (Edge edge : s1.edges) {
            // Update s1's direct topological neighbors towards x.
            Neuron neighbor = edge.neighbor;
            neighbor.update(x, epsNeighbor);

            // Set the age to zero if s1 and s2 are already connected.
            if (neighbor == s2) {
                edge.age = 0;
                s2.setEdgeAge(s1, 0);
                addEdge = false;
            }
        }

        // Connect s1 and s2 if they are not neighbor yet.
        if (addEdge) {
            s1.addEdge(s2);
            s2.addEdge(s1);
            s2.update(x, epsNeighbor);
        }

        // Remove edges with an age larger than the threshold
        for (Iterator<Edge> iter = s1.edges.iterator(); iter.hasNext();) {
            Edge edge = iter.next();
            if (edge.age > edgeLifetime) {
                iter.remove();

                Neuron neighbor = edge.neighbor;
                neighbor.removeEdge(s1);
                // Remove a neuron if it has no emanating edges
                if (neighbor.edges.isEmpty()) {
                    neurons.removeIf(neuron -> neuron == neighbor);
                }
            }
        }

        // Add a new neuron if the number of input signals processed so far
        // is an integer multiple of lambda.
        if (t % lambda == 0) {
            // Determine the neuron with the maximum accumulated error.
            Neuron q = neurons.get(0);
            for (Neuron neuron : neurons) {
                if (neuron.counter > q.counter) {
                    q = neuron;
                }
            }

            // Find the neighbor of q with the largest error variable.
            Neuron f = q.edges.get(0).neighbor;
            for (Edge edge : q.edges) {
                if (edge.neighbor.counter > f.counter) {
                    f = edge.neighbor;
                }
            }

            // Decrease the error variables of q and f.
            q.counter *= alpha;
            f.counter *= alpha;

            // Insert a new neuron halfway between q and f.
            double[] w = new double[d];
           // for (int i = 0; i < d; i++) {
             //   w[i] += (q.w[i] + f.w[i]) / 2;
            //}

            //Neuron r = new Neuron(w, q.counter);
            //neurons.add(r);

            // Remove the connection (q, f) and add connections (q, r) and (r, f)
            q.removeEdge(f);
            f.removeEdge(q);
            //q.addEdge(r);
            //f.addEdge(r);
            //r.addEdge(q);
            //r.addEdge(f);
        }

        // Decrease all error variables.
        for (Neuron neuron : neurons) {
            neuron.counter *= beta;
        }
    }

*/

    /**
     * Returns the neurons in the network.
     * @return the neurons in the network.
     *//*
    public Neuron[] neurons() {
        return neurons.toArray(new Neuron[neurons.size()]);
    }*/

    /*public double[] quantize(double[] x) {
        neurons.stream().parallel().forEach(neuron -> neuron.distance(x));

        Neuron bmu = neurons.get(0);
        for (Neuron neuron : neurons) {
            if (neuron.distance < bmu.distance) {
                bmu = neuron;
            }
        }

        return bmu.w;
       
    }*/
    
}
