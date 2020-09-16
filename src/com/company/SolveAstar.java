package com.company;

import java.util.*;
import java.util.function.Predicate;

public class SolveAstar {
    private static class SearchNode implements Comparable<SearchNode> {
        SlidingPuzzle board;
        int manhattan;
        int moves=0;
        SearchNode previousNode;
        int priority;

        public SearchNode(SlidingPuzzle board, SearchNode previousNode) {
            if ( board == null ) throw new IllegalArgumentException();
            // new created in neighbors
            this.board = new SlidingPuzzle(board);
            this.manhattan = board.manhattan();
            if (previousNode != null) {
                this.moves = previousNode.moves + 1;
                this.previousNode = previousNode;
            }
            this.priority = this.moves + this.manhattan;
        }

        @Override
        public int compareTo(SearchNode o) {
            if ( this.priority < o.priority ) return -1;
            else if (this.priority > o.priority) return 1;
            else return 0;
        }
    }


    // SearchNode current2;
    private SearchNode current;

    private boolean solvable = false;
    private List<SlidingPuzzle> dequer = new ArrayList<>();

    public SolveAstar(SlidingPuzzle initial) {
        PriorityQueue<SearchNode> pq = new PriorityQueue<>();
        // add HashSet to avoid repeating
        Set<SlidingPuzzle> visited = new HashSet<>();

        current = new SearchNode(initial, null);
        pq.add(current);
        visited.add(initial);
        // TODO: try bidirectional search?
        int numNodes = 0;
        while (!current.board.isGoal()) {
            current = pq.poll();
            assert current != null;
            // int manhattan = current.priority;
            for (SlidingPuzzle nb : current.board.neighbors()) {
                if ((current.previousNode == null || !nb.equals(current.previousNode.board)) && !visited.contains(nb)) {

                    pq.add(new SearchNode(nb, current));
                    visited.add(nb);
                }
            }
            // TODO: If two or more paths reach a common node, delete all those paths except for the one that reaches the common node with minimum cost.
            if (numNodes % 5000 == 0) {
                System.out.println(numNodes);
                /*pq.removeIf(new Predicate<SearchNode>() {
                    @Override
                    public boolean test(SearchNode searchNode) {
                        return manhattan * 1.2 < searchNode.priority;
                    }
                });*/
            }
            numNodes++;
            // TODO: cutt of the whose manhattan is too big (maybe by depth)
        }
        if (current.board.isGoal()) {
            solvable = true;
        }
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

    // sequence of boards in a shortest solution
    public Iterable<SlidingPuzzle> solution() {
        SearchNode temp=new SearchNode(current.board,current.previousNode);
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
