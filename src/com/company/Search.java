package com.company;

import org.w3c.dom.Node;

import java.util.*;
import java.util.function.Predicate;

public abstract class Search {
    protected abstract void solve();

    protected static class SearchNode implements Comparable<SearchNode> {
        protected SlidingPuzzle board;
        protected int heuristic;
        protected int moves = 0;
        protected SearchNode previousNode;
        protected int priority;
        protected static String typeHeuristic = "h2";

        public SearchNode(SlidingPuzzle board, SearchNode previousNode) {
            if ( board == null ) throw new IllegalArgumentException();
            // TODO: new created in neighbors
            this.board = new SlidingPuzzle(board);
            //this.board = board;

            // set predecessor
            if (previousNode != null) {
                this.moves = previousNode.moves + 1;
                this.previousNode = previousNode;
            }

            // calculate heuristic
            if (SearchNode.typeHeuristic.equals("h1")) {
                this.heuristic = board.misplacedTiles();
            } else {
                this.heuristic = board.manhattan();
            }

            // count priority
            this.priority = this.moves + this.heuristic;
        }

        // set heuristic in constructor for Search
        public static void setTypeHeuristic(String typeHeuristic) {
            SearchNode.typeHeuristic = typeHeuristic;
        }

        @Override
        public int compareTo(SearchNode o) {
            if ( this.priority < o.priority ) return -1;
            else if (this.priority > o.priority) return 1;
            else return 0;
        }
    }


    // Search parameters
    protected SearchNode current;
    protected SlidingPuzzle initial;
    protected int maxNodes;
    protected int exploredNodes;

    // Search outcomes
    protected boolean solvable = false;
    protected List<SlidingPuzzle> dequer = new ArrayList<>();

    public Search(SlidingPuzzle initial, int maxNodes) {
        this.initial = initial;
        this.maxNodes = maxNodes;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        if(isSolvable())
            return current.moves;
        else return -1;
    }

    // numOfNodesExplored
    public int getExploredNodes() {
        return exploredNodes;
    }

    // sequence of boards in a shortest solution
    public Iterable<SlidingPuzzle> solution() {
        // solve
        this.solve();
        SearchNode temp = new SearchNode(current.board,current.previousNode);

        // solve changes solvable state. If it is solvable, the solution will be returned
        if(isSolvable()) {
            while (temp != null) {
                dequer.add(temp.board);
                temp = temp.previousNode;
            }
            Collections.reverse(dequer);
            return dequer;
        }
        else return null;
    }
}
