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
        // the outcomes are always the same. The algorithms are consistent
        String state = "h1k5 4b6c f2ge d873";
        String goal = "b12f 345g 678h cdek";
        SlidingPuzzle sp = null;
        try {
            sp = new SlidingPuzzle(CommandReader.stringPuzzleReader(state.split(" ")), CommandReader.stringPuzzleReader(goal.split(" ")));
        } catch (InconsistentMeasures inconsistentMeasures) {
            inconsistentMeasures.printStackTrace();
        }

        // TODO: read File. And read the puzzle matrix
        // TODO: set blank
        // SlidingPuzzle sp = new SlidingPuzzle("b12 345 678", "b12 345 678");
        int maxNodes = 2000000;
        int beamWidth = 1000;
        // sp.randomizeState(1000);
        sp.printState();
        BeamSearch Beam = new BeamSearch(sp, maxNodes, beamWidth);
        Beam.solve();
        System.out.println("Beam: ");
        System.out.println(Beam.moves());
        System.out.println(Beam.getExploredNodes());


        // testing Astar
        /*AstarSearch Astar = new AstarSearch(sp, maxNodes);
        Astar.solve();
        System.out.println("Astar: ");
        System.out.println(Astar.moves());
        System.out.println(Astar.getExploredNodes());*/


        // testing Char conversion
        //System.out.println((char) 100);
        //System.out.println(Integer.parseInt("asd"));
        /*String s = "asa dfb asb dfb";
        System.out.println(s.split(" ").length);*/

        // testing file reader
        CommandReader cr = new CommandReader();

        String s = "asa dfb asb dfb";
        try {
            char[][] testBoard = CommandReader.stringPuzzleReader(s.split(" "));
            for (char[] line: testBoard) {
                System.out.println(Arrays.toString(line));
            }
        } catch (InconsistentMeasures inconsistentMeasures) {
            inconsistentMeasures.printStackTrace();
        }
        //cr.getUserInput();
    }
}
