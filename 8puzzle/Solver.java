/* *****************************************************************************
 *  Name: VinceStargazer
 *  Date: 2022/05/07
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Deque;
import java.util.LinkedList;

public class Solver {
    private class Node implements Comparable<Node> {
        private final Board board;
        private final int moves, priority;
        private final Node prev;

        Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            priority = board.manhattan() + moves;
        }

        public int compareTo(Node other) {
            return priority - other.priority;
        }
    }

    private Node goal;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("null argument");
        MinPQ<Node> pq = new MinPQ<>();
        pq.insert(new Node(initial, 0, null));
        while (!pq.isEmpty()) {
            Node curr = pq.delMin();
            if (curr.board.isGoal()) {
                goal = curr;
                break;
            } else if (curr.board.hamming() == 2 && curr.board.twin().isGoal())
                break;

            for (Board neighbor : curr.board.neighbors()) {
                if (curr.prev == null || !neighbor.equals(curr.prev.board))
                    pq.insert(new Node(neighbor, curr.moves + 1, curr));
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return goal != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable() ? goal.moves : -1;
    }

    // sequence of boards in the shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Deque<Board> res = new LinkedList<>();
        while (goal != null) {
            res.addFirst(goal.board);
            goal = goal.prev;
        }
        return res;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
