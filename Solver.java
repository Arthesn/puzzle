import dsa.LinkedStack;
import dsa.MinPQ;
import stdlib.In;
import stdlib.StdOut;

// A data type that implements the A* algorithm for solving the 8-puzzle and its generalizations.
public class Solver {
    int moves; // fastest possible of moves
    LinkedStack<Board> solution; // stack of boards from initial to goal
    MinPQ<SearchNode> pq; // // neighbor boards, its moves, and previous
    // Finds a solution to the initial board using the A* algorithm.
    public Solver(Board board) {
        if (board == null) {
            throw new NullPointerException("board is null");
        }
        if (!board.isSolvable()) {
            throw new IllegalArgumentException("board is unsolvable");
        }
        moves = 0;
        solution = new LinkedStack<Board>();
        pq = new MinPQ<SearchNode>();
        Board initial = new Board(board.tiles); // initital board
        pq.insert(new SearchNode(initial, 0, null)); // add initial board to pq
        while (!pq.isEmpty()) {
            SearchNode node = pq.delMin();
            if (node.board.isGoal()) { // if board is goal
                moves = node.moves;
                while(node != null) { // add boards until and including the initial board
                    solution.push(node.board);
                    node = node.previous;

                }
                break;
            } else {
                for (Board neighbor: node.board.neighbors()) { //  iterate through neighbor boards
                    if(node.previous == null ||!node.previous.board.equals(neighbor)) {
                        //  no duplicating previous board
                        // insert the neighbor boards to pq and and each neighbor has moves plus 1
                        pq.insert(new SearchNode(neighbor, node.moves + 1, node));

                    }
                }
            }

        }
    }

    // Returns the minimum number of moves needed to solve the initial board.
    public int moves() {
        return moves;
    }

    // Returns a sequence of boards in a shortest solution of the initial board.
    public Iterable<Board> solution() {
        return solution;
    }

    // A data type that represents a search node in the game tree. Each node includes a
    // reference to a board, the number of moves to the node from the initial node, and a
    // reference to the previous node.
    private class SearchNode implements Comparable<SearchNode> {
        Board board; // board
        int moves; // number of moves
        SearchNode previous; // previous node


        // Constructs a new search node.
        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }

        // Returns a comparison of this node and other based on the following sum:
        //   Manhattan distance of the board in the node + the # of moves to the node
        public int compareTo(SearchNode other) {
            int currentboard =  this.board.manhattan() + this.moves;
            int otherboard = other.board.manhattan() + other.moves;
            return Integer.compare(currentboard, otherboard);
        }
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.printf("Solution (%d moves):\n", solver.moves());
            StdOut.println(initial);
            StdOut.println("----------");
            int moves = 0;
            for (Board board : solver.solution()) {
                StdOut.println("move number:" + moves);
                StdOut.println(board);
                StdOut.println("----------");
                moves++;
            }

        } else {
            StdOut.println("Unsolvable puzzle");

        }
    }
}
