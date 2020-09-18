package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public abstract class Search {
    protected abstract void solve();

    protected static class SearchNode implements Comparable<SearchNode> {
        protected SlidingPuzzle board;
        protected int heuristic;
        protected int moves = 0;
        protected SearchNode previousNode;
        protected int priority;
        protected static String typeHeuristic = "h2";

        /**
         * @param board initial board
         * @param previousNode previous board
         */
        public SearchNode(SlidingPuzzle board, SearchNode previousNode) {
            if ( board == null ) throw new IllegalArgumentException();
            // No need to create ne neighbor because new is created in neighbors function in SlidingPuzzle
            // this.board = new SlidingPuzzle(board);
            this.board = board;

            // set predecessor
            if (previousNode != null) {
                this.moves = previousNode.moves + 1;
                this.previousNode = previousNode;
            }

            // calculate heuristic
            if (SearchNode.typeHeuristic.equals("h1")) {
                this.heuristic = board.hamming();
            } else {
                this.heuristic = board.manhattan();
            }

            // count priority
            countPriority();
        }

        public void countPriority() {
            this.priority = this.moves + this.heuristic;
        }

        /**
         * @param typeHeuristic set heuristic in constructor for Search
         */
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

    /**
     * @param initial initial state of the board
     * @param maxNodes the maximum number of nodes to explore
     */
    public Search(SlidingPuzzle initial, int maxNodes) {
        this.initial = initial;
        this.maxNodes = maxNodes;
    }

    public SearchNode getCurrent() {
        return current;
    }

    /**
     * is the initial board solvable? (see below)
     * @return whether the board is solvable
     */
    public boolean isSolvable() {
        return solvable;
    }

    /**
     * @return min number of moves to solve initial board
     */
    public int moves() {
        if(isSolvable())
            return getCurrent().moves;
        else return -1;
    }

    /**
     * @return numOfNodesExplored
     */
    public int getExploredNodes() {
        return exploredNodes;
    }

    /**
     * @return sequence of boards in a shortest solution
     */
    public Iterable<SlidingPuzzle> solution() {
        // solve
        this.solve();
        SearchNode temp = new SearchNode(getCurrent().board,getCurrent().previousNode);

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
