/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private static final int ENERGY_DEF = 1000;
    private Picture pic;
    private int w, h;
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("null argument");
        pic = new Picture(picture);
        w = picture.width();
        h = picture.height();
        energy = new double[w][h];
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++)
                energy[i][j] = energy(i, j);
    }

    // current picture
    public Picture picture() {
        return new Picture(pic);
    }

    // width of current picture
    public int width() {
        return w;
    }

    // height of current picture
    public int height() {
        return h;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= w || y < 0 || y >= h)
            throw new IllegalArgumentException("index out of bounds");
        if (x == 0 || x == w - 1 || y == 0 || y == h - 1) return ENERGY_DEF;
        Color left = pic.get(x - 1, y), right = pic.get(x + 1, y);
        Color up = pic.get(x, y - 1), down = pic.get(x, y + 1);
        return Math.sqrt(getDiff(left, right) + getDiff(up, down));
    }

    private int getDiff(Color a, Color b) {
        int rd = a.getRed() - b.getRed();
        int gd = a.getGreen() - b.getGreen();
        int bd = a.getBlue() - b.getBlue();
        return rd * rd + gd * gd + bd * bd;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return horizontalSeam(energy);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return horizontalSeam(transposeMat(energy));
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam);
        if (h <= 1) throw new IllegalArgumentException("insufficient pixels");
        if (seam.length != w) throw new IllegalArgumentException("inaccurate length");
        Picture newPic = new Picture(w, h - 1);
        double[][] newEnergy = new double[w][h - 1];
        for (int i = 0; i < w; i++) {
            int pos = seam[i];
            if (pos < 0 || pos >= h)
                throw new IllegalArgumentException("index out of bounds");
            for (int j = 0; j < h; j++) {
                Color color = pic.get(i, j);
                double eng = energy[i][j];
                if (j < pos) {
                    newPic.set(i, j, color);
                    newEnergy[i][j] = eng;
                } else if (j > pos) {
                    newPic.set(i, j - 1, color);
                    newEnergy[i][j - 1] = eng;
                }
            }
        }

        pic = newPic;
        h--;
        energy = newEnergy;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam);
        if (w <= 1) throw new IllegalArgumentException("insufficient pixels");
        if (seam.length != h) throw new IllegalArgumentException("inaccurate length");
        Picture newPic = new Picture(w - 1, h);
        double[][] newEnergy = new double[w - 1][h];
        for (int j = 0; j < h; j++) {
            int pos = seam[j];
            if (pos < 0 || pos >= w)
                throw new IllegalArgumentException("index out of bounds");
            for (int i = 0; i < w; i++) {
                Color color = pic.get(i, j);
                double eng = energy[i][j];
                if (i < pos) {
                    newPic.set(i, j, color);
                    newEnergy[i][j] = eng;
                } else if (i > pos) {
                    newPic.set(i - 1, j, color);
                    newEnergy[i - 1][j] = eng;
                }
            }
        }

        pic = newPic;
        w--;
        energy = newEnergy;
    }

    private int[] horizontalSeam(double[][] mat) {
        int m = mat.length, n = mat[0].length;
        int[] seam = new int[m];
        double[][] dp = new double[m][n];
        for (int i = 0; i < n; i++) dp[0][i] = mat[0][i];
        for (int j = 1; j < m; j++) {
            for (int i = 0; i < n; i++) {
                dp[j][i] = mat[j][i] + dp[j - 1][i];
                if (i > 0) dp[j][i] = Math.min(dp[j][i], mat[j][i] + dp[j - 1][i - 1]);
                if (i < n - 1) dp[j][i] = Math.min(dp[j][i], mat[j][i] + dp[j - 1][i + 1]);
            }
        }

        double minSum = Double.POSITIVE_INFINITY;
        int minIdx = 0;
        for (int i = 0; i < n; i++) {
            if (dp[m - 1][i] < minSum) {
                minSum = dp[m - 1][i];
                minIdx = i;
            }
        }

        for (int j = m - 1; j >= 0; j--) {
            seam[j] = minIdx;
            double prev = dp[j][minIdx] - mat[j][minIdx];
            double minDiff = Double.POSITIVE_INFINITY;
            int lastIdx = minIdx;
            for (int i = -1; i <= 1 && j > 0; i++) {
                int ni = lastIdx + i;
                if (ni < 0 || ni >= n) continue;
                if (Math.abs(dp[j - 1][ni] - prev) < minDiff) {
                    minDiff = Math.abs(dp[j - 1][ni] - prev);
                    minIdx = ni;
                }
            }
        }

        return seam;
    }

    private double[][] transposeMat(double[][] mat) {
        int m = mat.length, n = mat[0].length;
        double[][] newMat = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                newMat[i][j] = mat[j][i];
        return newMat;
    }

    private void validateSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("null argument");
        int prev = seam[0];
        for (int i : seam) {
            if (Math.abs(prev - i) > 1)
                throw new IllegalArgumentException("invalid seam");
            prev = i;
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        System.out.println("done");
    }
}
