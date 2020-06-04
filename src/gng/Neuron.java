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
    public double counter;

    public Neuron(double x, double y, double counter) {
        this.x = x;
        this.y = y;
        this.counter = counter;
        this.edges = new LinkedList<>();
    }

    public void addEdge(Neuron neighbor, int age) {
        edges.add(new Edge(neighbor, age));
    }
    
    /*
    //Updates the reference vector by w += eps * (x - w).
     // @param x a signal.
     // @param eps the learning rate.
     ///
    public void update(double[] x, double eps) {
        //for (int i = 0; i < x.length; i++) {
        //    w[i] += eps * (x[i] - w[i]);
        //}
    }

    //Adds an edge. 
    public void addEdge(Neuron neighbor) {
        addEdge(neighbor, 0);
    }

    // Adds an edge. 
    public void addEdge(Neuron neighbor, int age) {
        edges.add(new Edge(neighbor, age));
    }

   // Removes an edge. 
    public void removeEdge(Neuron neighbor) {
        for (Iterator<Edge> iter = edges.iterator(); iter.hasNext();) {
            Edge edge = iter.next();
            if (edge.neighbor == neighbor) {
                iter.remove();
                return;
            }
        }
    }

    // Sets the age of edge.
    public void setEdgeAge(Neuron neighbor, int age) {
        for (Iterator<Edge> iter = edges.iterator(); iter.hasNext();) {
            Edge edge = iter.next();
            if (edge.neighbor == neighbor) {
                edge.age = age;
                return;
            }
        }
    }

   //Increments the age of all edges emanating from the neuron. 
    public void age() {
        for (Edge edge : edges) {
            edge.age++;
        }
    }

    
    //Computes the distance between the neuron and a signal.
     
    public void distance(double[] x) {
        //distance = MathEx.distance(w, x);
    }

    @Override
    public int compareTo(Neuron o) {
        return Double.compare(distance, o.distance);
    }
    */
}
