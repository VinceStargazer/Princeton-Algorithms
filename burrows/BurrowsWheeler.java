/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }

        for (int i = 0; i < csa.length(); i++) {
            int idx = csa.index(i);
            BinaryStdOut.write(s.charAt(idx == 0 ? s.length() - 1 : idx - 1));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int n = s.length();
        char[] arr = s.toCharArray();
        int[] next = new int[n];
        Arrays.sort(arr);
        for (int i = 0; i < n; ) {
            int j = i + 1;
            while (j < n && arr[j] == arr[j - 1]) j++;
            for (int k = 0; k < n; k++) {
                if (s.charAt(k) == arr[i]) {
                    next[i++] = k;
                    if (i == j) break;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(arr[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
