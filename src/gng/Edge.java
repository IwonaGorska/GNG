/**
 * 
 */
package gng;

/**
 * @author iwona
 *
 */

import java.io.Serializable;

public class Edge implements Serializable {
    private static final long serialVersionUID = 2L;
    /**
     * The neighbor neuron.
     */
    public final Neuron neighbor;
    /**
     * The age of this edges.
     */
    public int age;

    /**
     * Constructor.
     */
    public Edge(Neuron neighbor) {
        this(neighbor, 0);
    }

    /**
     * Constructor.
     */
    public Edge(Neuron neighbor, int age) {
        this.neighbor = neighbor;
        this.age = age;
    }
    
    public void increaseAge() {
    	this.age += 1;
    }
}
