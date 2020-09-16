package com.company;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

import static java.util.Map.entry;

public class SlidingPuzzle {
    private char[][] currentState;
    // Storing the coordinates of the goal state
    private static Map<Character, int[]> goal;
    private static char blank = 'b';
    private int[] blankCoordinates;
    private static int width = 0;
    private static int height = 0;
    private static final int H = 0;
    private static final int W = 1;
    private List<SlidingPuzzle> neighbors = new ArrayList<>();

    private SlidingPuzzle() {
        /*width = 0;
        height = 0;
        blank = 'b';*/
    }

    // TODO: duplicate characters error
    SlidingPuzzle(String currentState, String goalState) {
        this();
        this.setState(currentState);

        // find coordinates of the goal state
        if (SlidingPuzzle.goal == null) {
            SlidingPuzzle.goal = new HashMap<>();
            try {
                char[][] goalArray = SlidingPuzzle.convertState(goalState);
                for (int h = 0; h < height; h++) {
                    for (int w = 0; w < width; w++) {
                        SlidingPuzzle.goal.put(goalArray[h][w], new int[]{h, w});
                    }
                }
            } catch (InconsistentMeasures inconsistentMeasures) {
                inconsistentMeasures.printStackTrace();
            }
        }

        // blank position
        int h = 0, w = 0;
        for (; h < height; h++) {
            w = 0;
            for (; w < width; w++) {
                if (this.currentState[h][w] == blank) {
                    setBlankCoordinates(new int[]{h, w});
                }
            }
        }
    }

    SlidingPuzzle(SlidingPuzzle puzzle) {
        this.currentState = new char[height][width];
        for (int h = 0; h < height; h++) {
            System.arraycopy(puzzle.currentState[h], 0, this.currentState[h], 0, width);
        }
        this.blankCoordinates = new int[]{puzzle.blankCoordinates[H], puzzle.blankCoordinates[W]};
        // this.neighbors.addAll(puzzle.neighbors);
    }

    public int[] getBlankCoordinates() {
        return blankCoordinates;
    }

    public void setBlankCoordinates(int[] blankCoordinates) {
        this.blankCoordinates = blankCoordinates;
    }

    public void setState(String state) {
        try {
            this.currentState = SlidingPuzzle.convertState(state);
        } catch (InconsistentMeasures inconsistentMeasures) {
            inconsistentMeasures.printStackTrace();
        }
    }

    private static char[][] convertState(String state) throws InconsistentMeasures {

        int[] measures = SlidingPuzzle.evalMeasures(state);
        if (height == 0 && width == 0) {
            width = (measures[W]);
            height = (measures[H]);
        } else if (width != measures[W] && height != measures[H]) {
            throw new InconsistentMeasures("Measures of the state are wrong.");
        }

        int currentWidth = 0;
        int currentHeight = 0;
        char[][] charState = new char[height][width];
        for (int i = 0; i < state.length(); i++) {

            if (state.charAt(i) == ' ') {
                currentWidth = 0;
                currentHeight++;
            } else {
                charState[currentHeight][currentWidth] = state.charAt(i);
                currentWidth++;
            }
        }
        return charState;
    }

    private static int[] evalMeasures(String state) throws InconsistentMeasures {
        int currentWidth = 0;
        int currentHeight = 1;
        int maxWidth = 0;
        for (int i = 0; i < state.length(); i++) {
            if (state.charAt(i) == ' ') {

                if (maxWidth == 0) {
                    maxWidth = currentWidth;
                } else if (maxWidth != currentWidth) {
                    throw new InconsistentMeasures("Width is wrong at " + currentHeight);
                }
                currentWidth = 0;
                ++currentHeight;
            } else {
                currentWidth++;
            }
        }

        if (maxWidth != currentWidth || maxWidth == 0) {
            throw new InconsistentMeasures("Width is wrong at " + currentHeight);
        }

        if (maxWidth < 3 || currentHeight < 3) {
            throw new InconsistentMeasures("The measures are too small");
        }
        return new int[]{currentHeight, maxWidth};
    }

    public void printState() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < height; i++) {
            out.append(Arrays.toString(currentState[i])).append("\n");
        }
        System.out.println(out);
    }

    public void move(String move) {
        int blankH = getBlankCoordinates()[H];
        int blankW = getBlankCoordinates()[W];

        // Move the char
        char toMove = ' ';
        int charH = blankH;
        int charW = blankW;

        switch (move.toLowerCase()) {
            case "up" -> charH--;
            case "down" -> charH++;
            case "left" -> charW--;
            case "right" -> charW++;
        }

        if (isMoveValid(charH, charW)) {
            toMove = currentState[charH][charW];
            currentState[blankH][blankW] = toMove;
            currentState[charH][charW] = blank;
            setBlankCoordinates(new int[]{charH, charW});
        } else {
            try {
                throw new InvalidMove("Move " + move + " is invalid");
            } catch (InvalidMove invalidMove) {
                System.out.println(invalidMove.getMessage());
                invalidMove.printStackTrace();
            }
        }
    }

    private boolean isMoveValid (int height, int width) {
        return height < SlidingPuzzle.height && width < SlidingPuzzle.width && height >= 0 && width >= 0;
    }

    public void randomizeState(int n) {
        int moves = 0;
        // debug
        int up = 0, down = 0, left = 0, right = 0;
        while (moves < n) {
            int rand = (int) (Math.random() * 4.0);

            int charH = getBlankCoordinates()[H];
            int charW = getBlankCoordinates()[W];

            switch (rand) {
                case 2 -> charH--;
                case 3 -> charH++;
                case 0 -> charW--;
                case 1 -> charW++;
            }

            if (isMoveValid(charH, charW)) {
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
        int blankH = getBlankCoordinates()[H];
        int blankW = getBlankCoordinates()[W];
        if(neighbors.isEmpty()) {
            if (isMoveValid(blankH + 1, blankW)) {
                SlidingPuzzle sp = new SlidingPuzzle(this);
                sp.move("down");
                neighbors.add(sp);
            }
            if (isMoveValid(blankH, blankW + 1)) {
                SlidingPuzzle sp = new SlidingPuzzle(this);
                sp.move("right");
                neighbors.add(sp);
            }
            if (isMoveValid(blankH, blankW - 1)) {
                SlidingPuzzle sp = new SlidingPuzzle(this);
                sp.move("left");
                neighbors.add(sp);
            }
            if (isMoveValid(blankH - 1, blankW)) {
                SlidingPuzzle sp = new SlidingPuzzle(this);
                sp.move("up");
                neighbors.add(sp);
            }
        }
        return neighbors;
    }

    // TODO: goal is null
    public int manhattan() {
        int manhattan = 0;
        // blank position
        int h = 0, w = 0;
        for (; h < height; h++) {
            w = 0;
            for (; w < width; w++) {
                if (currentState[h][w] == blank) continue;
                int goalH = goal.get(currentState[h][w])[H];
                int goalW = goal.get(currentState[h][w])[W];
                manhattan += Math.abs(goalH - h) + Math.abs(goalW - w);
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
        for (int h = 0; h < height; h++) {
            result = result && Arrays.equals(that.currentState[h], this.currentState[h]);
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

