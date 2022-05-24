/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;
import java.util.List;

public class MoveToFront {
    private static final int R = 256, LG_R = 8;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        List<Character> dict = new ArrayList<>();
        for (char i = 0; i < R; i++) dict.add(i);
        char[] input = BinaryStdIn.readString().toCharArray();
        for (int i = 0; i < input.length; i++) {
            int index = dict.indexOf(input[i]);
            BinaryStdOut.write(index, LG_R);
            char x = dict.remove(index);
            dict.add(0, x);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        List<Character> dict = new ArrayList<>();
        for (char i = 0; i < R; i++) dict.add(i);
        char[] input = BinaryStdIn.readString().toCharArray();
        for (int i = 0; i < input.length; i++) {
            BinaryStdOut.write(dict.get(input[i]));
            char x = dict.remove(input[i]);
            dict.add(0, x);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}