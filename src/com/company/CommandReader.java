package com.company;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class CommandReader {

    CommandReader() {
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
                for (int i = 0; i < args.length; i++) {
                    if (i != 0)
                        rows[i] = args [i + 1];
                }
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
     * @param filename
     */
    public void getFileInput(String filename) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filename));
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
        System.out.println("maxNodes " + nodesLimit);
    }

    private void move(String direction) {
        System.out.println("move " + direction);
    }

    private void solveAStar(String heuristic) {
        System.out.println("solveAStar " + heuristic);
    }

    private void solveBeam(int beamWidth) {
        System.out.println("solveBeam " + beamWidth);
    }

    private void setState(String[] rows) {
        System.out.println("setState " + Arrays.toString(rows));
    }

    private void printState() {
        System.out.println("printState");
    }

    private void randomizeState(int numMoves) {
        System.out.println("randomizeState " + numMoves);
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


}
