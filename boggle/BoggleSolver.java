/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private static class Node {
        private Node[] next = new Node[26];
        private boolean isString;
    }

    private static class MyTrie {
        private Node root;

        private MyTrie() {
            // nothing to initiate
        }

        private boolean containsKey(String key) {
            Node x = get(root, key, 0);
            return x != null && x.isString;
        }

        private boolean containsPrefix(String prefix) {
            return get(root, prefix, 0) != null;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            int c = key.charAt(d) - 'A';
            return get(x.next[c], key, d + 1);
        }

        private void add(String key) {
            root = add(root, key, 0);
        }

        private Node add(Node x, String key, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) x.isString = true;
            else {
                int c = key.charAt(d) - 'A';
                x.next[c] = add(x.next[c], key, d + 1);
            }
            return x;
        }


    }

    private static final int[][] DIRS = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
    private final MyTrie trie;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        trie = new MyTrie();
        for (String s : dictionary) trie.add(s);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> res = new HashSet<>();
        int m = board.rows(), n = board.cols();
        boolean[][] visited = new boolean[m][n];
        for (int r = 0; r < m; r++)
            for (int c = 0; c < n; c++)
                dfs(res, board, r, c, visited, new StringBuilder());

        return res;
    }

    private void dfs(Set<String> res, BoggleBoard board, int r, int c, boolean[][] visited,
                     StringBuilder sb) {
        char cc = board.getLetter(r, c);
        sb.append(cc);
        if (cc == 'Q') sb.append('U');
        if (!trie.containsPrefix(sb.toString())) {
            sb.deleteCharAt(sb.length() - 1);
            if (cc == 'Q') sb.deleteCharAt(sb.length() - 1);
            return;
        }
        if (sb.length() > 2 && trie.containsKey(sb.toString()))
            res.add(sb.toString());
        visited[r][c] = true;
        for (int[] dir : DIRS) {
            int nr = r + dir[0], nc = c + dir[1];
            if (nr < 0 || nr >= board.rows() || nc < 0 || nc >= board.cols()) continue;
            if (!visited[nr][nc]) dfs(res, board, nr, nc, visited, sb);
        }
        visited[r][c] = false;
        sb.deleteCharAt(sb.length() - 1);
        if (cc == 'Q') sb.deleteCharAt(sb.length() - 1);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int n = word.length();
        if (n < 3 || !trie.containsKey(word)) return 0;
        if (n <= 4) return 1;
        if (n == 5) return 2;
        if (n == 6) return 3;
        if (n == 7) return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
