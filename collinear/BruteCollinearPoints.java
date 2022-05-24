/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final List<LineSegment> lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("null input");
        for (Point point : points)
            if (point == null) throw new IllegalArgumentException("null point");
        int n = points.length;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (i != j && points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("repeated points");

        Point[] temp = Arrays.copyOf(points, n);
        lineSegments = new ArrayList<>();
        Arrays.sort(temp);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    for (int h = k + 1; h < n; h++) {
                        double sp1 = temp[i].slopeTo(temp[j]);
                        double sp2 = temp[i].slopeTo(temp[k]);
                        double sp3 = temp[i].slopeTo(temp[h]);
                        if (sp1 == sp2 && sp2 == sp3)
                            lineSegments.add(new LineSegment(temp[i], temp[h]));
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
