import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    // construct an empty set of points

    private class Node {
        private final Point2D key;
        private Node left, right;

        public Node(Point2D key) {
            this.key = key;
        }
    }

    private Node root;
    private Node best;
    private double distBest;
    private int n;

    public KdTree() {
        n = 0;
    }

    private Node put(Node curr, Point2D key, int depth) {
        if (curr == null) {
            n++;
            return new Node(key);
        }

        if (key.distanceSquaredTo(curr.key)!=0) {
            double cmp;
            if (depth % 2 == 0)
                cmp = key.x() - curr.key.x();
            else
                cmp = key.y() - curr.key.y();

            depth++;
            if (cmp < 0)
                curr.left = put(curr.left, key, depth);
            else if (cmp >= 0)
                curr.right = put(curr.right, key, depth);
        }

        return curr;
    }

    private void draw(Node curr, int depth, double x0, double y0, double x1, double y1) {

        if (curr != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.text(curr.key.x(), curr.key.y() + 0.015, curr.key.toString());
            curr.key.draw();
            depth++;
            StdDraw.setPenRadius();
            if (depth % 2 == 0) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(curr.key.x(), y0, curr.key.x(), y1);
                draw(curr.left, depth, x0, y0, curr.key.x(), y1);
                draw(curr.right, depth, curr.key.x(), y0, x1, y1);
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(x0, curr.key.y(), x1, curr.key.y());
                draw(curr.left, depth, x0, y0, x1, curr.key.y());
                draw(curr.right, depth, x0, curr.key.y(), x1, y1);
            }
        }
    }

    private Queue<Point2D> range(Node curr, RectHV rect, Queue<Point2D> queue, int depth) {
        depth++;
        if (curr != null) {
            if (rect.contains(curr.key)) {
                queue.enqueue(curr.key);
                queue = range(curr.left, rect, queue, depth);
                queue = range(curr.right, rect, queue, depth);
            }
            else if (depth % 2 == 0) {
                if (rect.xmax() <= curr.key.x())
                    queue = range(curr.left, rect, queue, depth);
                else if (rect.xmin() >= curr.key.x())
                    queue = range(curr.right, rect, queue, depth);
                else {
                    queue = range(curr.left, rect, queue, depth);
                    queue = range(curr.right, rect, queue, depth);
                }
            }
            else {
                if (rect.ymax() <= curr.key.y())
                    queue = range(curr.left, rect, queue, depth);
                else if (rect.ymin() >= curr.key.y())
                    queue = range(curr.right, rect, queue, depth);
                else {
                    queue = range(curr.left, rect, queue, depth);
                    queue = range(curr.right, rect, queue, depth);
                }
            }
        }
        return queue;
    }

    private void nearest(Node curr, Point2D ref, RectHV rect, int depth) {

        if (curr == null || rect.distanceSquaredTo(ref) > distBest)
            return;

        depth++;
        double distCurr = curr.key.distanceSquaredTo(ref);

        if (distCurr < distBest) {
            best = curr;
            distBest = distCurr;
        }

        RectHV rectLeft;
        RectHV rectRight;

        if (depth % 2 == 0) {
            rectLeft = new RectHV(rect.xmin(), rect.ymin(), curr.key.x(), rect.ymax());
            rectRight = new RectHV(curr.key.x(), rect.ymin(), rect.xmax(), rect.ymax());
        }
        else {
            rectLeft = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), curr.key.y());
            rectRight = new RectHV(rect.xmin(), curr.key.y(), rect.xmax(), rect.ymax());
        }

        if (rectLeft.contains(ref)) {
            nearest(curr.left, ref, rectLeft, depth);
            nearest(curr.right, ref, rectRight, depth);
        }
        else {
            nearest(curr.right, ref, rectRight, depth);
            nearest(curr.left, ref, rectLeft, depth);
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // number of points in the set
    public int size() {
        return n;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        else {
            root = put(root, p, 0);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        else {
            Node curr = root;
            int depth = 0;
            while (curr != null) {

                if (curr.key.distanceSquaredTo(p) == 0)
                    return true;

                double cmp;
                if (depth % 2 == 0)
                    cmp = p.x() - curr.key.x();
                else
                    cmp = p.y() - curr.key.y();
                depth++;

                if (cmp < 0)
                    curr = curr.left;
                else if (cmp >= 0)
                    curr = curr.right;
            }
            return false;
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, -1, 0, 0, 1, 1);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        else {
            Queue<Point2D> queue;
            queue = range(root, rect, new Queue<Point2D>(), -1);
            return queue;
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null )
            throw new IllegalArgumentException();
        else {
            best = root;
            distBest = Double.POSITIVE_INFINITY;
            nearest(root, p, new RectHV(0, 0, 1, 1), -1);
            return best.key;
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename;
        if (args.length == 1)
            filename = args[0];
        else
            filename = "circle10.txt";
        In in = new In(filename);
        KdTree set = new KdTree();
        StdOut.print("Used file: " + filename + "\n");
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            set.insert(p);
            StdOut.print("Inserted Point: " + p.toString() + "\n");
            StdOut.print("- Is in set: " + set.contains(p) + "\n");
        }
        StdOut.print("----------------\n\n");
        set.draw();


        Point2D ref = new Point2D(0.2, 0.2);
        StdOut.print("Check Nearest point to: " + ref.toString() + "\n\n");
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.02);
        ref.draw();

        Point2D nearest = set.nearest(ref);
        StdDraw.setPenColor(StdDraw.BOOK_RED);
        StdDraw.setPenRadius(0.02);
        nearest.draw();

    }
}