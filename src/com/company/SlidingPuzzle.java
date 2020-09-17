package com.company;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

import static java.util.Map.entry;

public class SlidingPuzzle {
    private char[][] currentState;
    // Storing the coordinates of the goal state
    private Map<Character, int[]> goal;
    private char blank = '0';
    private int[] blankCoordinates;
    // remove static from these
    private int columns = 0;
    private int rows = 0;
    private static final int ROW = 0;
    private static final int COLUMN = 1;
    private List<SlidingPuzzle> neighbors = new ArrayList<>();

    private SlidingPuzzle() {
        /*columns = 0;
        rows = 0;
        blank = 'b';*/
    }

    // TODO: duplicate characters error
    SlidingPuzzle(char[][] currentState, char[][] goalState, char blank) throws InconsistentMeasures {
        this();
        // assigning the number of rows and columns from the passed array
        this.rows = currentState.length;
        this.columns = currentState[0].length;
        this.blank = blank;

        // vetting both arrays before creating new arrays
        boolean consistent = this.vetState(currentState) && this.vetState(goalState);

        // copy the elements if consistent
        if (consistent) {
            this.currentState = new char[rows][columns];
            for (int r = 0; r < rows; r++) {
                System.arraycopy(currentState[r], 0, this.currentState[r], 0, columns);
            }

            // find coordinates of the goal state
            if (this.goal == null) {
                this.goal = new HashMap<>();
                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < columns; c++) {
                        this.goal.put(goalState[r][c], new int[]{r, c});
                    }
                }
            }
            this.findBlank();
        }
    }

    SlidingPuzzle(SlidingPuzzle puzzle) {
        // assigning the number of rows and columns from the passed puzzle
        this.rows = puzzle.rows;
        this.columns = puzzle.columns;
        this.blank = puzzle.blank;

        this.currentState = new char[rows][columns];
        for (int r = 0; r < rows; r++) {
            System.arraycopy(puzzle.currentState[r], 0, this.currentState[r], 0, columns);
        }

        // I do not need to copy the goal state since it is the same for all the nodes of the same type
        this.goal = puzzle.goal;

        this.blankCoordinates = new int[]{puzzle.blankCoordinates[ROW], puzzle.blankCoordinates[COLUMN]};
    }

    private boolean vetState(char[][] state) throws InconsistentMeasures {
        int maxColumns = state[0].length;
        int maxRows = state.length;

        boolean measuresConsistent = true;

        for (int r = 0; r < maxRows; r++) {
            if (maxColumns != state[r].length || maxRows != rows || maxColumns != columns) {
                measuresConsistent = false;
                throw new InconsistentMeasures("columns is wrong at " + r);
            }
        }

        if (maxColumns < 3 || maxRows < 3) {
            measuresConsistent = false;
            throw new InconsistentMeasures("The measures are too small");
        }
        return measuresConsistent;
    }

    public int[] getBlankCoordinates() {
        return blankCoordinates;
    }

    public void setBlankCoordinates(int[] blankCoordinates) {
        this.blankCoordinates = blankCoordinates;
    }

    public void findBlank () {
        // blank position
        int r = 0, c = 0;
        for (; r < rows; r++) {
            c = 0;
            for (; c < columns; c++) {
                if (this.currentState[r][c] == blank) {
                    setBlankCoordinates(new int[]{r, c});
                }
            }
        }
    }

    public void printStateInt() {
        StringBuilder out = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                int number = (int) currentState[r][c];
                out.append(number);
                if (number < 10) {
                    out.append("   ");
                } else if (number < 100) {
                    out.append("  ");
                } else {
                    out.append(" ");
                }
            }
            out.append("\n");
        }
        System.out.println(out);
    }

    public void printState() {
        StringBuilder out = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            out.append(Arrays.toString(currentState[r])).append("\n");
        }
        System.out.println(out);
    }

    public void move(String move) {
        int blankR = getBlankCoordinates()[ROW];
        int blankC = getBlankCoordinates()[COLUMN];

        // Move the char
        char toMove = ' ';
        int charR = blankR;
        int charC = blankC;

        switch (move.toLowerCase()) {
            case "up" -> charR--;
            case "down" -> charR++;
            case "left" -> charC--;
            case "right" -> charC++;
        }

        if (isMoveValid(charR, charC)) {
            toMove = currentState[charR][charC];
            currentState[blankR][blankC] = toMove;
            currentState[charR][charC] = blank;
            setBlankCoordinates(new int[]{charR, charC});
        } else {
            try {
                throw new InvalidMove("Move " + move + " is invalid");
            } catch (InvalidMove invalidMove) {
                System.out.println(invalidMove.getMessage());
                invalidMove.printStackTrace();
            }
        }
    }

    private boolean isMoveValid (int rows, int columns) {
        return rows < this.rows && columns < this.columns && rows >= 0 && columns >= 0;
    }

    public void randomizeState(int n) {
        int moves = 0;
        // debug
        int up = 0, down = 0, left = 0, right = 0;
        while (moves < n) {
            int rand = (int) (Math.random() * 4.0);

            int charR = getBlankCoordinates()[ROW];
            int charC = getBlankCoordinates()[COLUMN];

            switch (rand) {
                case 2 -> charR--;
                case 3 -> charR++;
                case 0 -> charC--;
                case 1 -> charC++;
            }

            if (isMoveValid(charR, charC)) {
                switch (rand) {
                    case 2 -> this.move("up");
                    case 3 -> this.move("down");
                    case 0 -> this.move("left");
                    case 1 -> this.move("right");
                }
                moves++;
                // debug
                /*switch (rand) {
                    case 2 -> up++;
                    case 3 -> down++;
                    case 0 -> left++;
                    case 1 -> right++;
                }
                System.out.println("Move: " + moves);
                this.printState();*/
            }
        }
        // debug
        //System.out.println(String.format("Up: %d Down: %d Left: %d Right: %d", up, down, left, right));
    }

    // all neighboring boards
    public Iterable<SlidingPuzzle> neighbors() {
        int blankR = getBlankCoordinates()[ROW];
        int blankC = getBlankCoordinates()[COLUMN];
        if(neighbors.isEmpty()) {
            if (isMoveValid(blankR + 1, blankC)) {
                SlidingPuzzle sp = new SlidingPuzzle(this);
                sp.move("down");
                neighbors.add(sp);
            }
            if (isMoveValid(blankR, blankC + 1)) {
                SlidingPuzzle sp = new SlidingPuzzle(this);
                sp.move("right");
                neighbors.add(sp);
            }
            if (isMoveValid(blankR, blankC - 1)) {
                SlidingPuzzle sp = new SlidingPuzzle(this);
                sp.move("left");
                neighbors.add(sp);
            }
            if (isMoveValid(blankR - 1, blankC)) {
                SlidingPuzzle sp = new SlidingPuzzle(this);
                sp.move("up");
                neighbors.add(sp);
            }
        }
        return neighbors;
    }

    // hamming function
    public int hamming() {
        int outOfPlace = 0;
        int r = 0, c = 0;
        for (; r < rows; r++) {
            c = 0;
            for (; c < columns; c++) {
                if (currentState[r][c] == blank) continue;
                int goalR = goal.get(currentState[r][c])[ROW];
                int goalC = goal.get(currentState[r][c])[COLUMN];
                if (goalR != r || goalC != c)
                    outOfPlace++;
            }
        }
        return outOfPlace;
    }

    public int manhattan() {
        int manhattan = 0;
        // count manhattan
        int r = 0, c = 0;
        for (; r < rows; r++) {
            c = 0;
            for (; c < columns; c++) {
                if (currentState[r][c] == blank) continue;
                int goalR = goal.get(currentState[r][c])[ROW];
                int goalC = goal.get(currentState[r][c])[COLUMN];
                manhattan += Math.abs(goalR - r) + Math.abs(goalC - c);
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // does this board equal o
    @Override
    public boolean equals(Object o) {
        boolean result = true;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlidingPuzzle that = (SlidingPuzzle) o;
        for (int r = 0; r < rows; r++) {
            result = result && Arrays.equals(that.currentState[r], this.currentState[r]);
        }
        return result;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(currentState);
        result = 31 * result + Arrays.hashCode(blankCoordinates);
        return result;
    }
}

