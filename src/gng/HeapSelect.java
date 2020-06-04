/**
 * 
 */
package gng;

/**
 * @author iwona
 *
 */
public class HeapSelect<T extends Comparable<? super T>> {

    /**
     * The heap size.
     */
    private int k;
    /**
     * The number of objects that have been added into heap.
     */
    private int n;
    /**
     * True if the heap is fully sorted.
     */
    private boolean sorted;
    /**
     * The heap array.
     */
    private T[] heap;

    /**
     * Constructor.
     * @param k the size of heap.
     */
    @SuppressWarnings("unchecked")
    public HeapSelect(int k) {
        this((T[]) new Comparable[k]);
    }

    /**
     * Constructor.
     * @param heap the array to store smallest values to track.
     */
    public HeapSelect(T[] heap) {
        this.heap = heap;
        k = heap.length;
        n = 0;
        sorted = false;
    }

    /** Returns the number of objects that have been added into heap. */
    public int size() {
        return n;
    }

    /** Returns the array back the heap. */
    public T[] toArray() {
        return heap;
    }

    /** Returns the array back the heap. */
    public T[] toArray(T[] a) {
        System.arraycopy(heap, 0, a, 0, k);
        return a;
    }

    /**
     * Assimilate a new value from the stream.
     */
    public void add(T datum) {
        sorted = false;
        if (n < k) {
            heap[n++] = datum;
            if (n == k) {
                heapify(heap);
            }
        } else {
            n++;
            if (datum.compareTo(heap[0]) < 0) {
                heap[0] = datum;
                //Sort.siftDown(heap, 0, k-1);
            }
        }
    }

    /**
     * In case of avoiding creating new objects frequently, one may check and
     * update the peek object directly and call this method to sort the internal
     * array in heap order.
     */
    public void heapify() {
        if (n < k) {
            throw new IllegalStateException();
        }

        //Sort.siftDown(heap, 0, k-1);
    }

    /**
     * Returns the k-<i>th</i> smallest value seen so far.
     */
    public T peek() {
        return heap[0];
    }

    /**
     * Returns the i-<i>th</i> smallest value seen so far. i = 0 returns the smallest
     * value seen, i = 1 the second largest, ..., i = k-1 the last position
     * tracked. Also, i must be less than the number of previous assimilated.
     */
    public T get(int i) {
        if (i > Math.min(k, n) - 1) {
            throw new IllegalArgumentException("HeapSelect i is greater than the number of data received so far.");
        }

        if (i == k-1) {
            return heap[0];
        }
        
        if (!sorted) {
            sort(heap, Math.min(k,n));
            sorted = true;
        }

        return heap[k-1-i];
    }

    /**
     * Sort the smallest values.
     */
    public void sort() {
        if (!sorted) {
            sort(heap, Math.min(k,n));
            sorted = true;
        }
    }

    /**
     * Place the array in max-heap order. Note that the array is not fully sorted.
     */
    private static <T extends Comparable<? super T>> void heapify(T[] arr) {
        //int n = arr.length;
        //for (int i = n / 2 - 1; i >= 0; i--)
            //Sort.siftDown(arr, i, n - 1);
    }

    /**
     * Sorts the specified array into descending order. It is based on Shell
     * sort, which is very efficient because the array is almost sorted by
     * heapifying.
     */
    private static <T extends Comparable<? super T>> void sort(T[] a, int n) {
        int inc = 1;
        do {
            inc *= 3;
            inc++;
        } while (inc <= n);

        do {
            inc /= 3;
            for (int i = inc; i < n; i++) {
                T v = a[i];
                int j = i;
                while (a[j - inc].compareTo(v) < 0) {
                    a[j] = a[j - inc];
                    j -= inc;
                    if (j < inc) {
                        break;
                    }
                }
                a[j] = v;
            }
        } while (inc > 1);
    }
}
