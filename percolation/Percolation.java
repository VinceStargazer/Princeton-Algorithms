/* *****************************************************************************
 *  Name:              Guoqing Yu
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private boolean[][] isOpen;
    private final int top, bottom, len;
    private int count;
    private final WeightedQuickUnionUF unionSites, unionWB;
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        len = n;
        isOpen = new boolean[n][n];
        top = n * n;
        bottom = n * n + 1;
        unionSites = new WeightedQuickUnionUF(n * n + 2);
        unionWB = new WeightedQuickUnionUF(n * n + 2);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) return;
        isOpen[row - 1][col - 1] = true;
        count++;
        if (row == 1) {
            unionSites.union(index(row, col), top);
            unionWB.union(index(row, col), top);
        }
        if (row == len) {
            unionSites.union(index(row, col), bottom);
        }

        for (int[] dir : dirs) {
            int nr = row + dir[0], nc = col + dir[1];
            if (nr > 0 && nr <= len && nc > 0 && nc <= len && isOpen(nr, nc)) {
                unionSites.union(index(row, col), index(nr, nc));
                unionWB.union(index(row, col), index(nr, nc));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return isOpen[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && unionWB.find(index(row, col)) == unionWB.find(top);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        return unionSites.find(top) == unionSites.find(bottom);
    }

    private void validate(int row, int col) {
        if (row < 1 || row > len || col < 1 || col > len) {
            throw new IllegalArgumentException();
        }
    }

    private int index(int row, int col) {
        return (row - 1) * len + col - 1;
    }

    // test client (optional)
    public static void main(String[] args) {
        System.out.println("done");
    }
}
