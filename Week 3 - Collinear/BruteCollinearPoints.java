import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

import java.util.Arrays;

public class BruteCollinearPoints {

    private LineSegment[] lineSegments;
    private Point[] usedPoints;
    private double[] usedSlopes;

    private int lsi;

    private void InsertInSegment(Point p, Point s) {
        if (lsi == lineSegments.length) {
            LineSegment suppSegment[] = new LineSegment[lsi * 2];
            System.arraycopy(lineSegments, 0, suppSegment, 0, lsi);
            suppSegment[lsi] = new LineSegment(p, s);
            lineSegments = suppSegment;

            Point suppPoints[] = new Point[lsi * 2];
            System.arraycopy(usedPoints, 0, suppPoints, 0, lsi);
            suppPoints[lsi] = s;
            usedPoints = suppPoints;

            double suppSlopes[] = new double[lsi * 2];
            System.arraycopy(usedSlopes, 0, suppSlopes, 0, lsi);
            suppSlopes[lsi] = p.slopeTo(s);
            usedSlopes = suppSlopes;
        }
        else {
            lineSegments[lsi] = new LineSegment(p, s);
            usedSlopes[lsi] = p.slopeTo(s);
            usedPoints[lsi] = s;
        }
        lsi++;
    }

    private void finalResize() {
        LineSegment suppSegment[] = new LineSegment[numberOfSegments()];
        System.arraycopy(lineSegments, 0, suppSegment, 0, numberOfSegments());
        lineSegments = suppSegment;
    }

    private boolean isUsedPoint(Point p, Point s) {
        for (int i = 0; i < usedPoints.length; i++) {
            if (usedPoints[i] != null) {
                if ((usedPoints[i].slopeTo(p) == Double.NEGATIVE_INFINITY
                        || usedPoints[i].slopeTo(s) == Double.NEGATIVE_INFINITY)
                        && usedSlopes[i] == p.slopeTo(s))
                    return true;
            }
        }
        return false;
    }

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException();
        }

        lineSegments = new LineSegment[1];
        usedPoints = new Point[1];
        usedSlopes = new double[1];
        Point[] points2 = points.clone();
        Arrays.sort(points2);
        Point[] supp;
        lsi = 0;
        for (int i = 0; i < points2.length && points2.length > 1; i++) {
            supp = points2.clone();
            Arrays.sort(supp, points2[i].slopeOrder());
            Point p = points2[i];
            Point s = supp[1];
            Point sPrev = s;
            int n = 1;
            double currSlope = Double.NEGATIVE_INFINITY;
            for (int j = 2; j < supp.length + 1; j++) {
                if (p.slopeTo(s) == currSlope) {
                    n++;
                }
                else {
                    if (n >= 3 && !isUsedPoint(p, sPrev)) {
                        InsertInSegment(p, sPrev);
                    }
                    n = 1;
                }
                currSlope = p.slopeTo(s);
                sPrev = s;
                if (j != supp.length) {
                    s = supp[j];
                    if (s.slopeTo(sPrev) == Double.NEGATIVE_INFINITY)
                        throw new IllegalArgumentException();
                }
                else if (n >= 3 && !isUsedPoint(p, sPrev)) {
                    InsertInSegment(p, sPrev);
                }

            }

        }
        finalResize();
    }

    // the number of line segments
    public int numberOfSegments() {
        return lsi;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments;
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