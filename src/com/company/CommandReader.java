package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 */
public class CommandReader {
    SlidingPuzzle board;
    int nodesLimit;

    CommandReader() {
        board = null;
        nodesLimit = 20000000;
    }

    /**
     * processes the arguments fed to the program by the user or from a file
     * @param args gets the argument array
     */
    public void processArguments(String[] args) {

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("exit"))
                System.exit(0);
            else if (args[0].equalsIgnoreCase("printState"))
                printState();
            else if (args[0].equalsIgnoreCase("printStateInt"))
                printStateInt();
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
     * @param file reads the command file
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


    /**
     * @param nodesLimit sets node limit
     */
    private void maxNodes(int nodesLimit) {
        this.nodesLimit = nodesLimit;
        System.out.println("\nNodes Limit is Set to: " + nodesLimit);
    }


    /**
     * @param direction moves in specified direction
     */
    private void move(String direction) {
        if (board != null) {
            board.move(direction);
            System.out.println("\nMove: " + direction);
        } else {
            System.out.println("Board is undefined.");
        }
    }


    /**
     * @param heuristic sets heuristic and solves the puzzle
     */
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

    /**
     * @param beamWidth sets beam width and solves the puzzle
     */
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

    /**
     * @param rows sets state from the array of rows.
     */
    private void setState(String[] rows) {
        // System.out.println("Note: blank is the first element in the ordered queue of your input, unless specified");
        try {
            char[][] currentState = CommandReader.stringPuzzleReader(rows);
            char[][] goalState = CommandReader.findGoal(currentState, 'b');
            this.board = new SlidingPuzzle(currentState, goalState, 'b');
            System.out.println("\n\n\nState is set to: ");
            board.printState();
        } catch (InconsistentMeasures inconsistentMeasures) {
            System.out.println(inconsistentMeasures.getMessage());
        }

    }

    /**
     * prints current state in chars
     */
    private void printState() {
        if (board != null) {
            System.out.println("\nState: ");
            board.printState();
        } else {
            System.out.println("Board is undefined.");
        }
    }

    /**
     * prints current state in ints
     */
    private void printStateInt() {
        if (board != null) {
            System.out.println("\nState: ");
            board.printStateInt();
        } else {
            System.out.println("Board is undefined.");
        }
    }

    /**
     * @param numMoves randomize state by the number of moves
     */
    private void randomizeState(int numMoves) {
        if (board != null) {
            board.randomizeState(numMoves);
            System.out.println("\nRandomized by " + numMoves + " of moves:");
        } else {
            System.out.println("Board is undefined.");
        }
    }

    /**
     * @param matrixFile the filiname with matrix code
     */
    public void matrixPuzzleReader(String matrixFile) {
        try {
            File root = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI());
            File file = new File(root, matrixFile);
            Scanner matrixScanner = new Scanner(file);
            matrixScanner.useDelimiter("\n");
            int columns = Integer.parseInt(matrixScanner.nextLine().split(" ")[0]);
            // TODO: what if it has dimentions for rows
            char[][] currentState = new char[columns][];
            int r = 0;
            while (matrixScanner.hasNext()) {
                // set the scanner for the row
                String matrixString = matrixScanner.nextLine();
                Scanner lineScanner = new Scanner(matrixString);
                // Scan the row and save all the inputs into char array
                char[] colArray = new char[columns];
                int c = 0;
                while (lineScanner.hasNextInt()) {
                    colArray[c] = (char) lineScanner.nextInt();
                    c++;
                }

                // save the char array into board array
                currentState[r] = colArray;
                r++;
            }

            // create the board with newly scanned from file
            char[][] goalState = CommandReader.findGoal(currentState, (char) 0);
            this.board = new SlidingPuzzle(currentState, goalState, (char) 0);
            System.out.println("\n\n\nState is set to: ");
            board.printStateInt();

        } catch (FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        } catch (InconsistentMeasures inconsistentMeasures) {
            System.out.println(inconsistentMeasures.getMessage());
        }

    }


    /**
     * @param rows reads the rows of the matrix puzzle and transfers into array
     * @return converted array of chars
     * @throws InconsistentMeasures if the matrix is inconsistent
     */
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

    /**
     * @param state current state to know the symbols. Symbols are arranged in ascii order
     * @param blank the blank char is excluded from sorting
     * @return sorted array which is the goal
     */
    public static char[][] findGoal(char[][] state, Character blank) {
        int maxColumns = state[0].length;
        int maxRows = state.length;
        PriorityQueue<Character> priorityQueue = new PriorityQueue<>();
        char[][] goal = new char[maxRows][maxColumns];

        for (int r = 0; r < maxRows; r++) {
            for (int c = 0; c < maxColumns; c++) {
                priorityQueue.add((state[r][c]));
            }
        }
        int r = 0;
        int c = 0;
        goal[r][c] = blank;
        c++;
        while (!priorityQueue.isEmpty()) {
            char current = priorityQueue.poll();
            if (blank == current) continue;

            goal[r][c] = current;
            if (c == maxColumns - 1) {
                r++;
                c = 0;
            } else {
                c++;
            }
        }

        return goal;
    }


}
