package com.company;

import java.io.File;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.FileHandler;

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
        /*String state = "h1k5 4b6c f2ge d873";
        String goal = "b12f 345g 678h cdek";
        SlidingPuzzle sp = null;
        try {
            sp = new SlidingPuzzle(CommandReader.stringPuzzleReader(state.split(" ")), CommandReader.stringPuzzleReader(goal.split(" ")), 'b');
        } catch (InconsistentMeasures inconsistentMeasures) {
            inconsistentMeasures.printStackTrace();
        }

        // SlidingPuzzle sp = new SlidingPuzzle("b12 345 678", "b12 345 678");
        int maxNodes = 2000000;
        int beamWidth = 1000;
        // sp.randomizeState(1000);
        sp.printState();
        BeamSearch Beam = new BeamSearch(sp, maxNodes, beamWidth);
        Beam.solve();
        System.out.println("Beam: ");
        System.out.println(Beam.moves());
        System.out.println(Beam.getExploredNodes());*/


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

        // testing string converter and goal finder
        /*CommandReader cr = new CommandReader();
        String s = "b12 345 678";
        try {
            char[][] testBoard = CommandReader.stringPuzzleReader(s.split(" "));
            testBoard = CommandReader.findGoal(testBoard, 'b');
            for (char[] line: testBoard) {
                System.out.println(Arrays.toString(line));
            }
        } catch (InconsistentMeasures inconsistentMeasures) {
            inconsistentMeasures.printStackTrace();
        }*/
        //cr.getUserInput();

        // TODO: everything must look nice
        // TODO: expand commands.txt
        // TODO: add print solution

        CommandReader commandReader = new CommandReader();
        File root = null;
        try {
            root = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI());
            File file = new File(root, "TestFiles/commands.txt");
            commandReader.getFileInput(file);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        String state = "b123 4567 89cd efgh";
        String goal = "b123 4567 89cd efgh";
        try {
            SlidingPuzzle sp = new SlidingPuzzle(CommandReader.stringPuzzleReader(state.split(" ")), CommandReader.stringPuzzleReader(goal.split(" ")), 'b');
            int maxNodes = 2000000;
            int beamWidth = 1000;
            sp.randomizeState(100);
            sp.printState();
            BeamSearch Beam = new BeamSearch(sp, maxNodes, beamWidth);
            Beam.solve();
            System.out.println("Beam: ");
            System.out.println(Beam.moves());
            System.out.println(Beam.getExploredNodes());

            for (SlidingPuzzle s : Beam.solution()) {
                //s.printState();
                //System.out.println();
            }

            Map<SlidingPuzzle, Boolean> visited = new HashMap<>();

            // add initial state
            visited.put(sp, true);
            boolean contains = visited.containsKey(new SlidingPuzzle(sp));
            if (contains) {
                visited.size();
            }
        } catch (InconsistentMeasures inconsistentMeasures) {
            inconsistentMeasures.printStackTrace();
        }




    }
}
