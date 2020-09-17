package com.company;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class CommandReader {
    SlidingPuzzle board;
    int nodesLimit;

    CommandReader() {
        board = null;
        nodesLimit = 20000000;
    }

    /**
     * processes the arguments fed to the program by the user or from a file
     * @param args
     */
    public void processArguments(String[] args) {

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("exit"))
                System.exit(0);
            else if (args[0].equalsIgnoreCase("printState"))
                printState();
            else
                invalidCommand();
        }

        else if (args.length == 2) {

            if (args[0].equalsIgnoreCase("randomizeState")) {
                try {
                    randomizeState(Integer.parseInt(args[1]));
                } catch (NumberFormatException n) {
                    wrongInputType();
                }
            }
            else if (args[0].equalsIgnoreCase("move"))
                move(args[1]);
            else if (args[0].equalsIgnoreCase("maxNodes")) {
                try {
                    maxNodes(Integer.parseInt(args[1]));
                } catch (NumberFormatException n) {
                    wrongInputType();
                }
            }
            else if (args[0].equalsIgnoreCase("readMatrixFile")) {
                this.matrixPuzzleReader(args[1]);
            }
            else
                invalidCommand();
        }

        else if (args.length == 3) {

            if (args[0].equalsIgnoreCase("solve"))
            {
                if (args[1].equalsIgnoreCase("A-star")) {
                    solveAStar(args[2]);
                }
                else if (args[1].equalsIgnoreCase("beam")) {
                    try {
                        solveBeam(Integer.parseInt(args[2]));
                    } catch (NumberFormatException n) {
                        wrongInputType();
                    }
                }
                else
                    invalidCommand();
            }
            else
                invalidCommand();
        }

        else if (args.length >= 4) {
            if (args[0].equalsIgnoreCase("setState")) {
                String[] rows = new String[args.length - 1];
                System.arraycopy(args, 1, rows, 0, args.length - 1);
                setState(rows);
            }
            else
                invalidCommand();
        }

        else
            invalidCommand();
    }

    /**
     * continuously retrieves the commands the user feeds to the program
     * @param file
     */
    public void getFileInput(File file) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            scanner.useDelimiter("\n");
            while (scanner.hasNextLine()) {

                String line = scanner.nextLine().replaceAll("\n", "");
                line = line.replaceAll("<", "");
                line = line.replaceAll(">", "");

                // return pressed
                if (line.length() == 0)
                    continue;

                // split line into arguments
                String[] args = line.split(" ");

                processArguments(args);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * continuously retrieves the commands the user feeds to the program
     */
    public void getUserInput() {
        Scanner scanner = new Scanner(System.in);

        for (prompt(); scanner.hasNextLine(); prompt()) {

            String line = scanner.nextLine().replaceAll("\n", "");
            line = line.replaceAll("<", "");
            line = line.replaceAll(">", "");

            // return pressed
            if (line.length() == 0)
                continue;

            // split line into arguments
            String args[] = line.split(" ");

            processArguments(args);
        }
    }

    /**
     * prompt format for commands to be entered
     */
    public static void prompt() {
        System.out.print(">> ");
    }

    /**
     *  prints a message when the command entered to the program is invalid
     */
    public static void invalidCommand() {
        System.out.println("INVALID COMMAND: Please enter a valid command and parameter if one is necessary");
    }

    /**
     *  prints a message when the input to a command must be an integer but was not
     */
    public static void wrongInputType() {
        System.out.println("INVALID ARGUMENT: Argument to this command must be an Integer");
    }


    private void maxNodes(int nodesLimit) {
        this.nodesLimit = nodesLimit;
        System.out.println("\nNodes Limit is Set to: " + nodesLimit);
    }

    private void move(String direction) {
        if (board != null) {
            board.move(direction);
            System.out.println("\nMove: " + direction);
        } else {
            System.out.println("Board is undefined.");
        }
    }

    private void solveAStar(String heuristic) {
        if (board != null) {
            AstarSearch Astar = new AstarSearch(board, this.nodesLimit, heuristic);
            Astar.solve();
            System.out.println("\nAstar: ");
            System.out.println("Heuristic: " + heuristic);
            System.out.println("Num moves: " + Astar.moves());
            System.out.println("Explored nodes: " + Astar.getExploredNodes());
        } else {
            System.out.println("Board is undefined.");
        }
    }

    private void solveBeam(int beamWidth) {
        if (board != null) {
            BeamSearch Beam = new BeamSearch(board, this.nodesLimit, beamWidth);
            Beam.solve();
            System.out.println("\nBeam: ");
            System.out.println("Beam width: " + beamWidth);
            System.out.println("Num moves: " + Beam.moves());
            System.out.println("Explored nodes: " + Beam.getExploredNodes());
        } else {
            System.out.println("Board is undefined.");
        }
    }

    private void setState(String[] rows) {
        // System.out.println("Note: blank is the first element in the ordered queue of your input, unless specified");
        try {
            char[][] currentState = CommandReader.stringPuzzleReader(rows);
            char[][] goalState = CommandReader.findGoal(currentState, 'b');
            this.board = new SlidingPuzzle(currentState, goalState, 'b');
            System.out.println("\nState is set to: ");
            board.printState();
        } catch (InconsistentMeasures inconsistentMeasures) {
            System.out.println(inconsistentMeasures.getMessage());
        }

    }

    private void printState() {
        if (board != null) {
            System.out.println("\nState: ");
            board.printState();
        } else {
            System.out.println("Board is undefined.");
        }
    }

    private void randomizeState(int numMoves) {
        if (board != null) {
            board.randomizeState(numMoves);
            System.out.println("\nRandomized by " + numMoves + " of moves:");
            board.printState();
        } else {
            System.out.println("Board is undefined.");
        }
    }


    public void matrixPuzzleReader(String matrixFile) {
        try {
            Scanner matrixScanner = new Scanner(new File(matrixFile));
            matrixScanner.useDelimiter("\n");
            while (matrixScanner.hasNext()) {
                String matrixString = matrixScanner.nextLine();
                Scanner lineScanner = new Scanner(matrixString);
                System.out.println(matrixScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public static char[][] stringPuzzleReader(String[] rows) throws InconsistentMeasures {
        int maxColumns = rows[0].length();
        int maxRows = rows.length;
        char[][] charState = new char[maxRows][maxColumns];

        for (int r = 0; r < maxRows; r++) {

            if (maxColumns == rows[r].length()) {
                for (int c = 0; c < maxColumns; c++) {
                    charState[r][c] = rows[r].charAt(c);
                }
            }
            else {
                throw new InconsistentMeasures("columns is wrong at " + r);
            }
        }

        if (maxColumns < 3 || maxRows < 3) {
            throw new InconsistentMeasures("The measures are too small");
        }
        return charState;
    }

    public static char[][] findGoal(char[][] state, Character blank) {
        int maxColumns = state[0].length;
        int maxRows = state.length;
        PriorityQueue<Character> priorityQueue = new PriorityQueue<>();
        char[][] goal = new char[maxRows][maxColumns];

        for (int r = 0; r < maxRows; r++) {
            for (int c = 0; c < maxColumns; c++) {
                priorityQueue.add(state[r][c]);
            }
        }

        for (int r = 0; r < maxRows; r++) {
            for (int c = 0; c < maxColumns; c++) {

                if (priorityQueue.size() > 0) {

                    if (blank != null && r == 0 && c == 0) {
                        goal[r][c] = blank;
                    } else {
                        char current = priorityQueue.poll();
                        if (current != blank)
                            goal[r][c] = current;
                    }
                }
            }
        }
        return goal;
    }


}
