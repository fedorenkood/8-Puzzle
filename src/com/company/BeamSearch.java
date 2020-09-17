package com.company;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class BeamSearch extends Search{
    private int beamWidth;

    public BeamSearch(SlidingPuzzle initial, int maxNodes, int beamWidth) {
        super(initial, maxNodes);
        this.beamWidth = beamWidth;
        SearchNode.setTypeHeuristic("h2");
    }

    @Override
    protected void solve() {
        // PriorityQueue to keep nodes in order of exploration
        PriorityQueue<SearchNode> pq = new PriorityQueue<>();
        // Beam search explores all of the nodes in the beamWidth and saves childred into a separate
        // PriorityQueue. Then, children are pruned, cleared and saved into pq
        PriorityQueue<SearchNode> children = new PriorityQueue<>();
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
                    // add node to children
                    children.add(new SearchNode(nb, current));
                    visited.add(nb);
                    // debugging
                    exploredNodes++;
                }
            }

            // prune the children when breath of the beam is explored
            // Run when pq is empty and pass the children as parameter
            if (pq.isEmpty())
                pq = pruneQueue(children);
            // TODO: maybe do two iterations without pruning children?
            // TODO: If two or more paths reach a common node, delete all the paths that steam from the node with more moves
        }
        if (current.board.isGoal()) {
            solvable = true;
        }
    }

    /**
     * @param children after the expansion of all the nodes
     * @return new pruned queue
     */
    private PriorityQueue<SearchNode> pruneQueue(PriorityQueue<SearchNode> children) {
        PriorityQueue<SearchNode> pruned = new PriorityQueue<>();
        // children are pruned after the complete exploration of the beamWidth
        while (!children.isEmpty() && pruned.size() < beamWidth) {
            // copy the nodes off the top of the queue until cutoff is reached
            SearchNode child = children.poll();
            pruned.add(child);
        }
        // clear the children. Children of the selected nodes will be stored here
        children.clear();
        return pruned;
    }
}
