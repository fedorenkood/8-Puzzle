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

        /*// test moves validity
        SlidingPuzzle sp = new SlidingPuzzle("1b2 345 678", "b12 345 678");
        sp.printState();

        // testing moves and manhattan
        sp.move("right");
        sp.printState();
        System.out.println(sp.manhattan());
        sp.move("left");
        sp.printState();
        System.out.println(sp.manhattan());
        sp.move("down");
        sp.printState();
        System.out.println(sp.manhattan());
        sp.move("down");
        sp.printState();
        System.out.println(sp.manhattan());

        // testing copy
        SlidingPuzzle sp2 = new SlidingPuzzle(sp);
        sp2.printState();

        // testing randomize
        sp2.randomizeState(1000);
        sp2.printState();
        sp.printState();*/

        // Testing Equals
        /*SlidingPuzzle sp = new SlidingPuzzle("1b2 345 678", "b12 345 678");
        SlidingPuzzle sp2 = new SlidingPuzzle(sp);
        System.out.println(sp.equals(sp2));*/

        // Testing Astar
        /*SlidingPuzzle sp = new SlidingPuzzle("b12f 345g 678h cdek", "b12f 345g 678h cdek");
        sp.randomizeState(1000);
        System.out.println(sp.misplacedTiles());
        sp.printState();
        SolveAstar Astar = new SolveAstar(sp);
        System.out.println("Solution: ");
        *//*for (SlidingPuzzle s : Astar.solution()) {
            s.printState();
        }*//*
        System.out.println(Astar.moves());*/


        // Testing Beam Search
        // 15-puzzle, lengths of optimal solutions range from 0 to 80 single-tile moves
        // SlidingPuzzle sp = new SlidingPuzzle("b12f 345g 678h cdek", "b12f 345g 678h cdek");
        SlidingPuzzle sp = new SlidingPuzzle("b12 345 678", "b12 345 678");
        int maxNodes = 10000000;
        int beamWidth = 20;
        sp.randomizeState(1000);
        sp.printState();
        BeamSearch Beam = new BeamSearch(sp, maxNodes, beamWidth);
        Beam.solve();
        System.out.println("Beam: ");
        System.out.println(Beam.moves());
        System.out.println(Beam.getExploredNodes());

        AstarSearch Astar = new AstarSearch(sp, maxNodes, beamWidth);
        Astar.solve();
        System.out.println("Astar: ");
        System.out.println(Astar.moves());
        System.out.println(Astar.getExploredNodes());
    }
}
