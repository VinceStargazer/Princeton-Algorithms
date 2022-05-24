/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private static class Node {
        private Point2D point;
        private Node left, right;
        private final boolean vertical;

        Node(Point2D point) {
            this(point, false);
        }

        Node(Point2D point, boolean vertical) {
            this.point = point;
            this.vertical = vertical;
        }

        public int compareTo(Node other) {
            int diff;
            if (this.point.distanceSquaredTo(other.point) == 0)
                diff = 0;
            else if (vertical) {
                diff = Double.compare(this.point.x(), other.point.x());
                if (diff == 0) diff--;
            } else {
                diff = Double.compare(this.point.y(), other.point.y());
                if (diff == 0) diff--;
            }
            return diff;
        }
    }

    private int size;
    private Node root;
    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!contains(p)) {
            root = insert(root, p, true);
            size++;
        }
    }

    private Node insert(Node n, Point2D p, boolean v) {
        if (n == null) return new Node(p, v);
        int cmp = n.compareTo(new Node(p));
        if (cmp > 0)
            n.left = insert(n.left, p, !v);
        else if (cmp < 0)
            n.right = insert(n.right, p, !v);
        else
            n.point = p;
        return n;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return contains(root, p);
    }

    private boolean contains(Node n, Point2D p) {
        if (n == null) return false;
        int cmp = n.compareTo(new Node(p));
        if (cmp == 0)
            return true;
        else if (cmp > 0)
            return contains(n.left, p);
        else
            return contains(n.right, p);
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, 0, 1, 0, 1);
    }

    private void draw(Node n, double xLower, double xUpper, double yLower, double yUpper) {
        if (n == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(n.point.x(), n.point.y());
        StdDraw.setPenRadius();
        if (n.vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.point.x(), yLower, n.point.x(), yUpper);
            draw(n.left, xLower, n.point.x(), yLower, yUpper);
            draw(n.right, n.point.x(), xUpper, yLower, yUpper);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(xLower, n.point.y(), xUpper, n.point.y());
            draw(n.left, xLower, xUpper, yLower, n.point.y());
            draw(n.right, xLower, xUpper, n.point.y(), yUpper);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> res = new ArrayList<>();
        range(res, root, rect);
        return res;
    }

    private void range(List<Point2D> res, Node n, RectHV rect) {
        if (n == null) return;
        if (nodeInRect(n, rect)) res.add(n.point);
        if (n.vertical) {
            if (rect.xmin() <= n.point.x()) range(res, n.left, rect);
            if (rect.xmax() >= n.point.x()) range(res, n.right, rect);
        } else {
            if (rect.ymin() <= n.point.y()) range(res, n.left, rect);
            if (rect.ymax() >= n.point.y()) range(res, n.right, rect);
        }
    }

    private boolean nodeInRect(Node n, RectHV rect) {
        return n.point.x() >= rect.xmin() && n.point.x() <= rect.xmax()
                && n.point.y() >= rect.ymin() && n.point.y() <= rect.ymax();
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        return nearest(root, p, root.point);
    }

    private Point2D nearest(Node n, Point2D goal, Point2D best) {
        if (n == null) return best;
        Node goodSide, badSide;
        if (n.point.distanceSquaredTo(goal) < best.distanceSquaredTo(goal))
            best = n.point;
        if (n.compareTo(new Node(goal)) > 0) {
            goodSide = n.left;
            badSide = n.right;
        } else {
            goodSide = n.right;
            badSide = n.left;
        }

        best = nearest(goodSide, goal, best);
        Point2D hypo = bestPossiblePoint(n, goal);
        if (hypo.distanceSquaredTo(goal) < best.distanceSquaredTo(goal))
            best = nearest(badSide, goal, best);

        return best;
    }

    private Point2D bestPossiblePoint(Node n, Point2D goal) {
        if (n.vertical)
            return new Point2D(n.point.x(), goal.y());
        else
            return new Point2D(goal.x(), n.point.y());
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kdt = new KdTree();
        double[][] points = {{0.7, 0.2}, {0.5, 0.4}, {0.2, 0.3}, {0.4, 0.7}, {0.9, 0.6}};
        for (double[] point : points)
            kdt.insert(new Point2D(point[0], point[1]));
        kdt.draw();
    }
}
