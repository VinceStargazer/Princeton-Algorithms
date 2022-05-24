/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.List;

public class SAP {

    private final Digraph net;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        net = new Digraph(G);
    }

    // length of the shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v == w) return 0;
        return ancestorSolver(List.of(v), List.of(w))[0];
    }

    // a common ancestor of v and w that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v == w) return v;
        return ancestorSolver(List.of(v), List.of(w))[1];
    }

    // length of the shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return ancestorSolver(v, w)[0];
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return ancestorSolver(v, w)[1];
    }

    private int[] ancestorSolver(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);
        if (!v.iterator().hasNext() || !w.iterator().hasNext())
            return new int[]{-1, -1};

        BreadthFirstDirectedPaths bdfp1 = new BreadthFirstDirectedPaths(net, v);
        BreadthFirstDirectedPaths bdfp2 = new BreadthFirstDirectedPaths(net, w);
        int path = Integer.MAX_VALUE, node = -1;
        for (int i = 0; i < net.V(); i++)
            if (bdfp1.hasPathTo(i) && bdfp2.hasPathTo(i)) {
                int len = bdfp1.distTo(i) + bdfp2.distTo(i);
                if (len < path) {
                    path = len;
                    node = i;
                }
            }

        if (path == Integer.MAX_VALUE) path = -1;
        return new int[]{path, node};
    }

    private void validate(Iterable<Integer> v) {
        if (v == null) throw new IllegalArgumentException("null input");
        for (Integer i : v) {
            if (i == null) throw new IllegalArgumentException("null element");
            if (i < 0 || i >= net.V())
                throw new IllegalArgumentException("index out of bounds");
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
        List<Integer> a = List.of(13, 23, 24), b = List.of(6, 16, 17);
        int len = sap.length(a, b), ancestor = sap.ancestor(a, b);
        StdOut.printf("shortest length = %d, common ancestor = %d\n", len, ancestor);
    }
}
