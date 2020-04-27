import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdRandom;

public class Percolation {

    private boolean[] openGrid;
    private int nOpen = 0;
    private int nSize;
    private int iTop;
    private int iBottom;
    private WeightedQuickUnionUF wquUF;

    private int rowCols2Indx(int row, int col) {
        return (row - 1) * nSize + (col - 1);
    }

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n must be positive");
        }
        else {
            // grid init
            nSize = n;
            wquUF = new WeightedQuickUnionUF(n * n + 2);
            iTop = n * n;
            iBottom = iTop + 1;
            openGrid = new boolean[n * n];

            // grid populate
            for (int i = 0; i < n; i++) {
                wquUF.union(i, iTop);
            }
            for (int i = n * n - n; i < n * n; i++) {
                wquUF.union(i, iBottom);
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row > nSize || row < 1 || col > nSize || col < 1) {
            throw new IllegalArgumentException("row and cols must be in range");
        }
        else if (!isOpen(row, col)) {
            int indx = rowCols2Indx(row, col);
            openGrid[indx] = true;
            nOpen++;
            if (row > 1 && openGrid[indx - nSize]) {
                wquUF.union(indx, indx - nSize);
            }
            if (row < nSize && openGrid[indx + nSize]) {
                wquUF.union(indx, indx + nSize);
            }
            if (col > 1 && openGrid[indx - 1]) {
                wquUF.union(indx, indx - 1);
            }
            if (col < nSize && openGrid[indx + 1]) {
                wquUF.union(indx, indx + 1);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > nSize || row < 1 || col > nSize || col < 1) {
            throw new IllegalArgumentException("row and cols must be in range");
        }
        int indx = rowCols2Indx(row, col);
        return openGrid[indx];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > nSize || row < 1 || col > nSize || col < 1) {
            throw new IllegalArgumentException("row and cols must be in range");
        }
        if (isOpen(row, col)) {
            int indx = rowCols2Indx(row, col);
            return wquUF.connected(iTop, indx);
        }
        else {
            return false;
        }
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return nOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return wquUF.connected(iTop, iBottom);
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 20;  // number of particles (default 20)
        double t = 300;
        double sumPercentage = 0;
        if (args.length == 1) {
            n = Integer.parseInt(args[0]);
        }
        double n2 = n * n;
        int row;
        int col;
        for (int i = 0; i < t; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                row = StdRandom.uniform(n) + 1;
                col = StdRandom.uniform(n) + 1;
                percolation.open(row, col);
            }
            sumPercentage += percolation.numberOfOpenSites() / n2;
        }
        System.out.print(sumPercentage / t);
    }
}