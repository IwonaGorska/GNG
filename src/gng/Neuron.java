/**
 * 
 */
package gng;

/**
 * @author iwona
 *
 */

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
//import smile.math.MathEx;
//https://github.com/haifengl/smile/blob/master/math/src/main/java/smile/math/MathEx.java


public class Neuron implements Serializable {
    private static final long serialVersionUID = 2L;
    //Position
    public double x;
    public double y;
    //The direct connected neighbors.
    public final List<Edge> edges;
    //The distance between the neuron and an input signal.
    public double distance = Double.MAX_VALUE;
    //The local counter variable (e.g. the accumulated error, freshness, etc.)
    public double error;

    public Neuron(double x, double y, double error) {
        this.x = x;
        this.y = y;
        this.error = error;
        this.edges = new LinkedList<>();
    }

    public void addEdge(Neuron neighbor, int age) {
        edges.add(new Edge(neighbor, age));
    }
    
    public void removeEdge(Neuron neighbor) {
        for (Iterator<Edge> iter = edges.iterator(); iter.hasNext();) {
            Edge edge = iter.next();
            if (edge.neighbor == neighbor) {
                //iter.remove();
                edges.remove(edges.indexOf(edge));
                return;
            }
        }
    }
    
    public void updateError() {
        this.error += this.distance;
    }
    
}
