package com.company;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class AstarSearch extends Search{
    private int beamWidth;


    // TODO: add heuristic choice and remove beamWidth
    public AstarSearch(SlidingPuzzle initial, int maxNodes, int beamWidth) {
        super(initial, maxNodes);
        this.beamWidth = beamWidth;
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

            // debugging
            int manhattan = current.priority;

            // add the neighbors if they were not visited yet
            for (SlidingPuzzle nb : current.board.neighbors()) {
                // TODO: (current.previousNode == null || !nb.equals(current.previousNode.board))
                if ((current.previousNode == null || !nb.equals(current.previousNode.board)) && !visited.contains(nb)) {
                    pq.add(new SearchNode(nb, current));
                    visited.add(nb);

                    // debugging
                    exploredNodes++;
                }
            }

            // debugging
            if (visited.size() % 5000 == 0) {
                // System.out.println(visited.size());
            }

            // prune the queue
            /*if (pq.size() > beamWidth)
                pq = pruneQueue(pq);*/

            // TODO: If two or more paths reach a common node, delete all those paths except for the one that reaches the common node with minimum cost.
            // TODO: cut of the whose manhattan is too big (maybe by depth)
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
