package com.company;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        // test evalMeasures
        /*try {
            System.out.println(Arrays.toString(new SlidingPuzzle().evalMeasures("ab as")));
        } catch (InconsistentMeasures inconsistentMeasures) {
            inconsistentMeasures.printStackTrace();
        }*/

        // test moves validity
        SlidingPuzzle sp = new SlidingPuzzle("612 345 b78", "b12 345 678");
        sp.printState();
        System.out.println(sp.validMoves().toString());

        // testing moves
        sp.move("right");
        sp.printState();
        sp.move("up");
        sp.printState();
        sp.move("left");
        sp.printState();
        sp.move("down");
        sp.printState();
    }
}
