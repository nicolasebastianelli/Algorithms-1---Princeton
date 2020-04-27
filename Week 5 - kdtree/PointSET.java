import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {

    private final SET<Point2D> rbBST;

    // construct an empty set of points
    public PointSET() {
        rbBST = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return rbBST.isEmpty();
    }

    // number of points in the set
    public int size() {
        return rbBST.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        rbBST.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return rbBST.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : rbBST)
            p.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        Queue<Point2D> queue = new Queue<Point2D>();
        for (Point2D p : rbBST)
            if (rect.contains(p))
                queue.enqueue(p);
        return queue;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        Point2D nearestP = null;
        double d = Double.POSITIVE_INFINITY;
        for (Point2D pToCheck : rbBST) {
            double currD = p.distanceSquaredTo(pToCheck);
            if (currD < d) {
                d = currD;
                nearestP = pToCheck;
            }
        }
        return nearestP;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename;
        if (args.length == 1)
            filename = args[0];
        else
            filename = "circle10.txt";
        In in = new In(filename);
        PointSET set = new PointSET();
        StdOut.print("Used file: " + filename + "\n");
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            set.insert(p);
            StdOut.print("Inserted Point: (" + x + "," + y + ")\n");
        }
        StdOut.print("----------------\n\n");
        set.draw();
        Point2D referenceP = new Point2D(0.5, 0.5);

        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(0.02);
        referenceP.draw();

        Point2D nearestP = set.nearest(referenceP);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.02);
        nearestP.draw();

        StdOut.print("Nearest point to " + referenceP.toString() + " is " + nearestP.toString()
                             + ", with dinstance " + referenceP.distanceSquaredTo(nearestP) + "\n");

        RectHV rect = new RectHV(0.0, 0.0, 0.4, 1);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        rect.draw();

        StdOut.print("Points inside rectangle: " + rect.toString() + "\n");
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.02);
        for (Point2D inP : set.range(rect)) {
            StdOut.print("- " + inP.toString() + "\n");
            inP.draw();
        }

    }
}