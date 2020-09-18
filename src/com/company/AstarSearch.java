package com.company;

import java.util.*;

public class AstarSearch extends Search{


    public AstarSearch(SlidingPuzzle initial, int maxNodes, String heuristic) {
        super(initial, maxNodes);
        SearchNode.setTypeHeuristic(heuristic);
    }

    @Override
    protected void solve() {
        // PriorityQueue to keep nodes in order of exploration
        PriorityQueue<SearchNode> pq = new PriorityQueue<>();
        // add HashSet to avoid repeating
        Map<SlidingPuzzle, Integer> visited = new HashMap<>();

        // add initial state
        current = new SearchNode(initial, null);
        pq.add(current);
        visited.put(initial, current.moves);

        while (!current.board.isGoal() && visited.size() < maxNodes) {
            // take the node of the top of the queue
            current = pq.poll();
            assert current != null;

            // PriorityQueue to keep nodes in order of exploration
            PriorityQueue<SearchNode> duplicates = new PriorityQueue<>();

            // add the neighbors if they were not visited yet
            for (SlidingPuzzle nb : current.board.neighbors()) {
                // Make sure that current board does not equal to its predecessor and we did not visit it.
                boolean containsKey = visited.containsKey(nb);
                if ((current.previousNode == null || !nb.equals(current.previousNode.board)) && !containsKey) {
                    pq.add(new SearchNode(nb, current));
                    visited.put(nb, current.moves + 1);
                    exploredNodes++;
                } else if (containsKey) {
                    // if new path has better path cost. Add new path to visited and add in pq
                    if (visited.get(nb) > current.moves + 1) {
                        duplicates.add(new SearchNode(nb, current));
                        visited.put(nb, current.moves + 1);
                        exploredNodes++;
                    }
                }
            }

            pq.addAll(duplicates);

            // TODO: If two or more paths reach a common node, change all the paths that steam from the node with more moves
            // TODO: bidirectional search. or iterative deepening
        }
        if (current.board.isGoal()) {
            solvable = true;
        }
    }

}
