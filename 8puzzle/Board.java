/* *****************************************************************************
 *  Name: VinceStargazer
 *  Date: 2022/05/07
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int[] DIR = {-1, 0, 1, 0, -1};
    private int emptyR, emptyC, hamDist, manDist;
    private final int n;
    private final int[][] board;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        board = new int[n][n];
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                board[r][c] = tiles[r][c];
                if (tiles[r][c] == 0) {
                    emptyR = r;
                    emptyC = c;
                    continue;
                }
                int idx = r == n - 1 && c == n - 1 ? 0 : r * n + c + 1;
                if (tiles[r][c] != idx) hamDist++;
                int nr = (tiles[r][c] - 1) / n, nc = (tiles[r][c] - 1) % n;
                manDist += Math.abs(r - nr) + Math.abs(c - nc);
            }
        }
    }

    private int[][] copyBoard() {
        int[][] newTiles = new int[n][n];
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                newTiles[r][c] = board[r][c];
        return newTiles;
    }

    private void exchangeTiles(int[][] grid, int r, int c, int nr, int nc) {
        int temp = grid[r][c];
        grid[r][c] = grid[nr][nc];
        grid[nr][nc] = temp;
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n + "\n");
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++)
                sb.append(" " + board[r][c] + " ");
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hamDist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manDist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamDist == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null || y.getClass() != getClass())
            return false;
        Board other = (Board) y;
        if (other.dimension() != n) return false;
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                if (board[r][c] != other.board[r][c])
                    return false;

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int nr = emptyR + DIR[i], nc = emptyC + DIR[i + 1];
            if (nr < 0 || nr >= n || nc < 0 || nc >= n) continue;
            int[][] newTiles = copyBoard();
            exchangeTiles(newTiles, emptyR, emptyC, nr, nc);
            neighbors.add(new Board(newTiles));
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twin = copyBoard();
        int i = 0, j = 0, i2 = 1, j2 = 1;
        if (twin[i][j] == 0) j++;
        if (twin[i2][j2] == 0) j2--;
        exchangeTiles(twin, i, j, i2, j2);
        return new Board(twin);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {{1, 0, 3}, {4, 2, 5}, {7, 8, 6}};
        Board board = new Board(tiles);
        System.out.println(board.toString());
        for (Board neighbor : board.neighbors())
            System.out.println(neighbor.toString());

        int[][] tiles2 = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board board2 = new Board(tiles2);
        System.out.println(board2.hamming());
        System.out.println(board2.manhattan());
    }
}
