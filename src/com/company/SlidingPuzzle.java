package com.company;

import java.lang.reflect.Array;
import java.util.*;
import static java.util.Map.entry;

public class SlidingPuzzle {
    private char[][] currentState;
    private char[][] goalState;
    private char blank;
    private int[] blankCoordinates;
    private int width;
    private int height;
    private static final int H = 0;
    private static final int W = 1;

    private SlidingPuzzle() {
        width = 0;
        height = 0;
        blank = 'b';
    }

    SlidingPuzzle(String currentState, String goalState) {
        this();
        this.setState(currentState);
        this.setGoalState(goalState);

        // blank position
        int h = 0, w = 0;
        for (; h < getHeight(); h++) {
            w = 0;
            for (; w < getWidth(); w++) {
                if (this.currentState[h][w] == blank) {
                    setBlankCoordinates(new int[]{h, w});
                }
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int[] getBlankCoordinates() {
        return blankCoordinates;
    }

    public void setBlankCoordinates(int[] blankCoordinates) {
        this.blankCoordinates = blankCoordinates;
    }

    public void setState(String state) {
        try {
            this.currentState = this.convertState(state);
        } catch (InconsistentMeasures inconsistentMeasures) {
            inconsistentMeasures.printStackTrace();
        }
    }

    private void setGoalState(String state) {
        try {
            this.goalState = this.convertState(state);
        } catch (InconsistentMeasures inconsistentMeasures) {
            inconsistentMeasures.printStackTrace();
        }
    }

    private char[][] convertState(String state) throws InconsistentMeasures {

        int[] measures = this.evalMeasures(state);
        if (getHeight() == 0 && getWidth() == 0) {
            setWidth(measures[W]);
            setHeight(measures[H]);
        } else if (getWidth() != measures[W] && getHeight() != measures[H]) {
            throw new InconsistentMeasures("Measures of the state are wrong.");
        }

        int currentWidth = 0;
        int currentHeight = 0;
        char[][] charState = new char[getHeight()][getWidth()];
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

    private int[] evalMeasures(String state) throws InconsistentMeasures {
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
        String out = "";
        for (int i = 0; i < getHeight(); i++) {
            out += Arrays.toString(currentState[i]) + "\n";
        }
        System.out.println(out);
    }

    public void move(String move) {

        if (isMoveValid(move)) {
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

    private boolean isMoveValid (String move) {

        Map<Character, Boolean> validMoves = validMoves();

        return switch (move.toLowerCase()) {
            case "up", "down", "left", "right" -> validMoves.get(move.charAt(0));
            default -> false;
        };
    }

    public Map<Character, Boolean> validMoves () {
        int h = getBlankCoordinates()[0];
        int w = getBlankCoordinates()[1];
        Map<Character, Boolean> valid = new HashMap<>();
        valid.put('u', true);
        valid.put('r', true);
        valid.put('l', true);
        valid.put('d', true);

        if (h == 0) {
            valid.put('u', false);
        }
        if (h == getHeight()) {
            valid.put('d', false);
        }
        if (w == 0) {
            valid.put('l', false);
        }
        if (w == getWidth()) {
            valid.put('r', false);
        }
        return valid;
    }


}

