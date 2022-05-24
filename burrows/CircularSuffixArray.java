/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private class CircularSuffix implements Comparable<CircularSuffix> {
        private final String str;
        private final int start;

        private CircularSuffix(String s, int i) {
            str = s;
            start = i;
        }

        public int compareTo(CircularSuffix other) {
            if (this == other) return 0;
            int i = start, j = other.start;
            while (str.charAt(i) == other.str.charAt(j)) {
                i = (i + 1) % str.length();
                j = (j + 1) % other.str.length();
                if (i == start) break;
            }
            return str.charAt(i) - str.charAt(j);
        }
    }

    private final int n;
    private final CircularSuffix[] arr;
    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("null argument");
        n = s.length();
        arr = new CircularSuffix[n];
        for (int i = 0; i < n; i++) arr[i] = new CircularSuffix(s, i);
        Arrays.sort(arr);
    }

    // length of s
    public int length() {
        return n;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= n) throw new IllegalArgumentException("index out of bounds");
        return arr[i].start;
    }

    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(args[0]);
        CircularSuffixArray csa = new CircularSuffixArray(in.readString());
        for (int i = 0; i < csa.length(); i++)
            StdOut.print(csa.index(i) + " ");
    }
}
