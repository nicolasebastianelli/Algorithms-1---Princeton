import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private final int[] board;
    private final int r;
    private final Queue<Board> queueNeighbors = new Queue<Board>();
    private String stringBoard;

    private void ComputeNeighbors() {
        int oPos = 0;
        for (int i = 0; i < r * r; i++) {
            if (board[i] == 0) {
                oPos = i;
                break;
            }
        }

        int row = oPos / r;
        int col = oPos % r;
        int[][] newBoard = new int[r][r];

        if (row - 1 >= 0) {
            for (int i = 0; i < r * r; i++)
                newBoard[i / r][i % r] = board[i];
            newBoard[row][col] = newBoard[row - 1][col];
            newBoard[row - 1][col] = 0;
            queueNeighbors.enqueue(new Board(newBoard));
        }
        if (row + 1 < r) {
            for (int i = 0; i < r * r; i++)
                newBoard[i / r][i % r] = board[i];
            newBoard[row][col] = newBoard[row + 1][col];
            newBoard[row + 1][col] = 0;
            queueNeighbors.enqueue(new Board(newBoard));
        }
        if (col - 1 >= 0) {
            for (int i = 0; i < r * r; i++)
                newBoard[i / r][i % r] = board[i];
            newBoard[row][col] = newBoard[row][col - 1];
            newBoard[row][col - 1] = 0;
            queueNeighbors.enqueue(new Board(newBoard));
        }
        if (col + 1 < r) {
            for (int i = 0; i < r * r; i++)
                newBoard[i / r][i % r] = board[i];
            newBoard[row][col] = newBoard[row][col + 1];
            newBoard[row][col + 1] = 0;
            queueNeighbors.enqueue(new Board(newBoard));
        }

    }

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null)
            throw new IllegalArgumentException();
        else {
            r = tiles.length;
            board = new int[r * r];
            for (int i = 0; i < r; i++)
                for (int j = 0; j < r; j++) {
                    board[i * r + j] = tiles[i][j];
                }
        }
    }

    // string representation of this board
    public String toString() {
        if (stringBoard==null) {
            StringBuilder sb = new StringBuilder("" + r);
            for (int i = 0; i < r * r; i++) {
                if (i % r == 0)
                    sb.append("\n ");
                else
                    sb.append(" ");
                sb.append(board[i] + " ");

            }
            stringBoard= sb.toString();
        }
        return stringBoard;
    }

    // board dimension n
    public int dimension() {
        return r ;
    }

    // number of tiles out of place
    public int hamming() {
        int res = 0;
        for (int i = 0; i < r * r; i++) {
            if (board[i] != (i + 1)) {
                res++;
            }
        }
        return res - 1;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int res = 0;
        for (int i = 0; i < r * r; i++) {
            if (board[i] != (i + 1) && board[i] != 0) {
                res += Math.abs(((board[i] - 1) / r) - (i / r))  + Math.abs(((board[i] - 1) % r) - (i % r));
            }
        }
        return res;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y)
            return true;
        if (y == null)
            return false;
        if (getClass() != y.getClass())
            return false;
        Board yBoard = (Board) y;
        if (yBoard.dimension() != this.dimension())
            return false;
        if (this.toString().equals(yBoard.toString()))
            return true;
        else
            return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        if (queueNeighbors.isEmpty())
            ComputeNeighbors();
        return queueNeighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] suppBoard = new int[r][r];
        for (int i = 0; i < r * r; i++)
            suppBoard[i / r][i % r] = board[i];
        int i = 0;
        int j = 1;
        while (board[i] == 0 || board[j] == 0 || j == i) {
            if (board[i] == 0 || j == i)
                i++;
            if (board[j] == 0 || j == i)
                j++;
        }
        suppBoard[i / r][i % r] = board[j];
        suppBoard[j / r][j % r] = board[i];
        return new Board(suppBoard);
    }

    // unit testing (not graded)
    public static void main(String[] args) {

        // read the n points from a file
        for (String filename : args) {

            // read in the board specified in the filename
            StdOut.println("\n----------------");
            StdOut.println(filename);
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }
            Board board = new Board(tiles);
            StdOut.println("Original Board:");
            StdOut.println(board.toString());
            StdOut.println("Hamming: " + board.hamming() + ", Manhattan: " + board.manhattan()
                                   + ", isGoal: " + board.isGoal());
            StdOut.println("\n Neighbors Board: ");
            for (Board neigh : board.neighbors()) {
                StdOut.println(neigh.toString());
                StdOut.println("Hamming: " + neigh.hamming() + ", Manhattan: " + neigh.manhattan()
                                       + ", isGoal: " + neigh.isGoal() + "\n");
            }

            StdOut.println("\n Twin Board: ");
            Board twin = board.twin();
            StdOut.println(twin.toString());
            StdOut.println(
                    "Hamming: " + twin.hamming() + ", Manhattan: " + twin.manhattan() + ", isGoal: "
                            + twin.isGoal() + "\n");

        }

    }

}