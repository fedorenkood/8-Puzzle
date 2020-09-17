package com.company;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class AstarSearch extends Search{
    private int beamWidth;


    // TODO: add heuristic choice and remove beamWidth
    public AstarSearch(SlidingPuzzle initial, int maxNodes) {
        super(initial, maxNodes);
        this.beamWidth = 3000;
        SearchNode.setTypeHeuristic("h2");
    }

    @Override
    protected void solve() {
        // PriorityQueue to keep nodes in order of exploration
        PriorityQueue<SearchNode> pq = new PriorityQueue<>();
        // add HashSet to avoid repeating
        Set<SlidingPuzzle> visited = new HashSet<>();

        // add initial state
        current = new SearchNode(initial, null);
        pq.add(current);
        visited.add(initial);

        while (!current.board.isGoal() && visited.size() < maxNodes) {
            // take the node of the top of the queue
            current = pq.poll();
            assert current != null;

            // add the neighbors if they were not visited yet
            for (SlidingPuzzle nb : current.board.neighbors()) {
                // Make sure that current board does not equal to its predecessor and we did not visit it.
                if ((current.previousNode == null || !nb.equals(current.previousNode.board)) && !visited.contains(nb)) {
                    pq.add(new SearchNode(nb, current));
                    visited.add(nb);

                    // debugging
                    exploredNodes++;
                }
            }

            // prune the queue
            // This is only for puzzles of 4*4 and more
            // the answer will be wrong, but too many nodes will be created
            if (pq.size() > beamWidth * 2)
                pq = pruneQueue(pq);

            // TODO: If two or more paths reach a common node, delete all the paths that steam from the node with more moves
            // TODO: bidirectional search. or iterative deepening
        }
        if (current.board.isGoal()) {
            solvable = true;
        }
    }

    private PriorityQueue<SearchNode> pruneQueue(PriorityQueue<SearchNode> pq) {
        PriorityQueue<SearchNode> pruned = new PriorityQueue<>();
        while (!pq.isEmpty() && pruned.size() < beamWidth) {
            // copy the nodes off the top of the queue until cutoff is reached
            SearchNode child = pq.poll();
            pruned.add(child);
        }
        return pruned;
    }
}
