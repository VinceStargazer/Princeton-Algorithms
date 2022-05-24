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

public class FastCollinearPoints {
    private final List<LineSegment> lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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
        for (Point orig : points) {
            Arrays.sort(temp, orig.slopeOrder());
            int i = 1;
            while (i < n) {
                double slope = orig.slopeTo(temp[i]);
                int start = i, min = 0, max = 0;
                while (i < n && Double.compare(slope, orig.slopeTo(temp[i])) == 0) {
                    min = temp[min].compareTo(temp[i]) < 0 ? min : i;
                    max = temp[max].compareTo(temp[i]) < 0 ? i : max;
                    i++;
                }
                if (i - start >= 3 && min == 0)
                    lineSegments.add(new LineSegment(temp[0], temp[max]));
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
