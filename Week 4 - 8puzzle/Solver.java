import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private final int moves;
    private final Stack<Board> stackSolution = new Stack<Board>();

    private class SearchNode implements Comparable<SearchNode> {

        public final Board board;
        public final int move;
        public final SearchNode previous;
        private final int manhattan;

        public int priorityManhattan() {
            return move + manhattan;
        }

        public SearchNode(Board board, int move, SearchNode previous) {
            this.board = board;
            this.move = move;
            this.previous = previous;
            this.manhattan = board.manhattan();
        }

        public int compareTo(SearchNode that) {
            return this.priorityManhattan() - that.priorityManhattan();
        }

    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        else {
            MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
            SearchNode solution = new SearchNode(initial, 0, null);
            pq.insert(solution);

            while (!solution.board.isGoal()) {
                solution = pq.delMin();
                for (Board subBoard : solution.board.neighbors()) {
                    if (solution.previous == null || !solution.previous.board.equals(subBoard)) {
                        SearchNode subNode = new SearchNode(subBoard, solution.move + 1, solution);
                        pq.insert(subNode);
                    }
                }
            }

            moves = solution.move;
            while (solution.previous != null) {
                stackSolution.push(solution.board);
                solution = solution.previous;
            }
            stackSolution.push(solution.board);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return true;
    }

    // min number of moves to solve initial board
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return stackSolution;
    }

    // test client (see below) 
    public static void main(String[] args) {
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
            Board initial = new Board(tiles);
            Solver solver = new Solver(initial);
            StdOut.println("Solution Steps\n");
            int i = 0;
            for (Board step : solver.solution()) {
                StdOut.println("Step: " + (i++));
                StdOut.println(step.toString());
                StdOut.println("Manhattan: " + step.manhattan()
                                       + ", isGoal: " + step.isGoal() + "\n");
            }
            StdOut.println("Minimum steps to reach the solution: " + solver.moves());

        }
    }

}