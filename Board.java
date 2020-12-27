import dsa.Inversions;
//  import dsa.LinkedQueue;
//  import dsa.MinPQ;
import stdlib.In;
import stdlib.StdOut;
//  import java.util.Collection;
//  import java.util.Iterator;
//  import java.util.Queue;
import java.util.ArrayList;
//  A data type to represent a board in the 8-puzzle game or its generalizations.
public class Board {
    int [][] tiles; // 2d dimension
    int n; //  board size
    int hamming;  //  hamming distance (number of wrong tiles in placement)
    int manhattan; // mahattan distance (sum of x and y of tiles to goal boar)
    int blankPos; // (row major of blank tile)


    // Constructs a board from an n x n array; tiles[i][j] is the tile at row i and column j, with 0
    // denoting the blank tile.
    public Board(int[][] tiles) {
        this.tiles = tiles;
        this.n = tiles.length;
        int counter = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != counter && tiles[i][j] != 0) {
                    int current = tiles[i][j];
                    int realrow = (current - 1) / n; // the supposed row of the tile
                    int realcolumn = (current - 1) % n; // the supposed column of the tile
                    hamming++;
                    manhattan += Math.abs(realrow - i) + Math.abs(realcolumn - j);
                }
                counter++;
            }
        }
        int counter2 = 0;
        Integer[] blankfinder = new Integer[n * n]; // n * n to count blank tile
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blankfinder[counter2] = tiles[i][j];
                counter2++;
            }
        }
        for (int i = 0; i < blankfinder.length; i++) {
            if (blankfinder[i] == 0) {
                blankPos = i + 1; // because row major order starts count from 1
            }
        }

    }

    // Returns the size of this board.
    public int size() {
        return n;
    }

    // Returns the tile at row i and column j of this board.
    public int tileAt(int i, int j) {
        return tiles[i][j];
    }

    // Returns Hamming distance between this board and the goal board.
    public int hamming() {
        return hamming;
    }

    // Returns the Manhattan distance between this board and the goal board.
    public int manhattan() {
        return manhattan;
    }

    // Returns true if this board is the goal board, and false otherwise.
    public boolean isGoal() {
        return manhattan == 0;
    }

    // Returns true if this board is solvable, and false otherwise.
    public boolean isSolvable() {
        Integer[] solvable = new Integer[n * n - 1];
        int currentindex = 0;
        int counter = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != 0) {
                    solvable[currentindex] = tiles[i][j];
                    currentindex++;
                }

            }
        }
        int blankrow = (blankPos - 1) / n;
        int inversion = (int) Inversions.count(solvable);
        if (n % 2 == 1) {
            return inversion % 2 == 0;
        } else {
            return ((inversion + blankrow) % 2 == 1);
        }

    }

    // Returns an iterable object containing the neighboring boards of this board.
    public Iterable<Board> neighbors() {
        int i = (blankPos - 1) / n;
        int j = (blankPos - 1) % n;
        ArrayList<Board> q = new ArrayList<>();
        if ((i + 1) >= 0 && (i + 1) < n) { // up
            int[][] red = cloneTiles();
            red[i + 1][j] = 0;
            red[i][j] = tiles[i + 1][j];
            q.add(new Board(red));

        }
        if ((i - 1) >= 0 && (i - 1) < n) { // down
            int[][] yellow = cloneTiles();
            yellow[i - 1][j] = 0;
            yellow[i][j] = tiles[i - 1][j];
            q.add(new Board(yellow));

        }
        if ((j + 1) >= 0 && (j + 1) < n) { // right
            int[][]  purple = cloneTiles();
            purple[i][j + 1] = 0;
            purple[i][j] = tiles[i][j + 1];
            q.add(new Board(purple));

        }

        if ((j - 1) >= 0 && (j -1) < n) { // left
            int[][] orange = cloneTiles();
            orange[i][j - 1] = 0;
            orange[i][j] = tiles[i][j - 1];
            q.add(new Board(orange));

        }
        return q;


    }

    // Returns true if this board is the same as other, and false otherwise.
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        boolean sameTiles = true;
        boolean sameSize = true;
        Board a = this, b = (Board) other;
        if (a.n != b.n) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (a.tileAt(i, j) != b.tileAt(i, j)) {
                    sameTiles = false;
                    break;
                }
            }
        }
        return sameSize && sameTiles;
    }

    // Returns a string representation of this board.
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2s", tiles[i][j] == 0 ? " " : tiles[i][j]));
                if (j < n - 1) {
                    s.append(" ");
                }
            }
            if (i < n - 1) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    // Returns a defensive copy of tiles[][].
    private int[][] cloneTiles() {
        int [][] clone = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                clone[i][j] = tiles[i][j];
            }
        }
        return clone;
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
        Board board = new Board(tiles);
        StdOut.printf("The board (%d-puzzle):\n%s\n", n, board);
        String f = "Hamming = %d, Manhattan = %d, Goal? %s, Solvable? %s\n";
        StdOut.printf(f, board.hamming(), board.manhattan(), board.isGoal(), board.isSolvable());
        StdOut.println("Neighboring boards:");
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
            StdOut.println("----------");
        }

    }
}
