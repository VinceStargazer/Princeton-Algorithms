/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordNet {

    private final Map<String, List<Integer>> map = new HashMap<>();
    private final List<String> synonyms = new ArrayList<>();
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        In in1 = new In(synsets), in2 = new In(hypernyms);
        while (in1.hasNextLine()) {
            String[] entry = in1.readLine().split(",");
            int id = Integer.parseInt(entry[0]);
            synonyms.add(entry[1]);
            for (String s : entry[1].split(" ")) {
                map.putIfAbsent(s, new ArrayList<>());
                map.get(s).add(id);
            }
        }

        Digraph net = new Digraph(synonyms.size());
        while (in2.hasNextLine()) {
            String[] vertices = in2.readLine().split(",");
            int source = Integer.parseInt(vertices[0]);
            for (int i = 1; i < vertices.length; i++)
                net.addEdge(source, Integer.parseInt(vertices[i]));
        }

        validateDAG(net);
        validateRoot(net);
        sap = new SAP(net);
    }

    // throw exception if the input does not correspond to a DAG
    private void validateDAG(Digraph net) {
        int n = net.V();
        boolean[] visited = new boolean[n];
        boolean[] recStack = new boolean[n];
        for (int i = 0; i < n; i++)
            if (isCycle(net, i, visited, recStack))
                throw new IllegalArgumentException("cycle detected");
    }

    private boolean isCycle(Digraph net, int i, boolean[] visited, boolean[] recStack) {
        if (recStack[i]) return true;
        if (visited[i]) return false;
        visited[i] = true;
        recStack[i] = true;
        for (int next : net.adj(i))
            if (isCycle(net, next, visited, recStack))
                return true;

        recStack[i] = false;
        return false;
    }

    // throw exception if the DAG does not share a common root
    private void validateRoot(Digraph net) {
        int count = 0;
        for (int i = 0; i < net.V(); i++)
            if (!net.adj(i).iterator().hasNext()) {
                count++;
                if (count > 1) throw new IllegalArgumentException("multiple roots");
            }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("null argument");
        return map.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("absent noun");
        if (nounA.equals(nounB)) return 0;
        return sap.length(map.get(nounA), map.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in the shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("absent noun");
        return synonyms.get(sap.ancestor(map.get(nounA), map.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        System.out.println(wordNet.distance("d", "l"));
        System.out.println(wordNet.sap("d", "l"));
    }
}
