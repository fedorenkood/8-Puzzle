package com.company;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class BeamSearch extends Search{
    private int beamWidth;

    // rewrite the priority funciton of the Search node

    static class SearchNode extends Search.SearchNode {
        /**
         * @param board        initial board
         * @param previousNode previous board
         */
        public SearchNode(SlidingPuzzle board, BeamSearch.SearchNode previousNode) {
            super(board, previousNode);
        }

        @Override
        public void countPriority() {
            this.priority = this.heuristic;
        }

        @Override
        public int compareTo(Search.SearchNode o) {
            BeamSearch.SearchNode searchNode = (BeamSearch.SearchNode) o;
            searchNode.countPriority();
            if ( this.priority < searchNode.priority ) return -1;
            else if (this.priority > searchNode.priority) return 1;
            else return 0;
        }
    }

    protected BeamSearch.SearchNode current;

    public BeamSearch(SlidingPuzzle initial, int maxNodes, int beamWidth) {
        super(initial, maxNodes);
        this.beamWidth = beamWidth;
        SearchNode.setTypeHeuristic("h2");
    }

    @Override
    public BeamSearch.SearchNode getCurrent() {
        return current;
    }

    @Override
    protected void solve() {
        // PriorityQueue to keep nodes in order of exploration
        PriorityQueue<BeamSearch.SearchNode> pq = new PriorityQueue<>();
        // Beam search explores all of the nodes in the beamWidth and saves childred into a separate
        // PriorityQueue. Then, children are pruned, cleared and saved into pq
        PriorityQueue<BeamSearch.SearchNode> children = new PriorityQueue<>();
        // add HashSet to avoid repeating
        Set<SlidingPuzzle> visited = new HashSet<>();

        // add initial state
        current = new BeamSearch.SearchNode(initial, null);
        pq.add(current);
        visited.add(initial);

        while (!current.board.isGoal() && visited.size() < maxNodes) {
            // take the node of the top of the queue
            current = pq.poll();
            assert current != null;

            // add the neighbors if they were not visited yet
            for (SlidingPuzzle nb : current.board.neighbors()) {
                // Make sure that current board does not equal to its predecessor and we did not visit it.
                if (visited.contains(nb)) {
                    boolean contains = true;
                }
                if ((current.previousNode == null || !nb.equals(current.previousNode.board)) && !visited.contains(nb)) {
                    // add node to children
                    children.add(new BeamSearch.SearchNode(nb, current));
                    visited.add(nb);
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
        PriorityQueue<BeamSearch.SearchNode> pruned = new PriorityQueue<>();
        // children are pruned after the complete exploration of the beamWidth
        while (!children.isEmpty() && pruned.size() < beamWidth) {
            // copy the nodes off the top of the queue until cutoff is reached
            BeamSearch.SearchNode child = children.poll();
            pruned.add(child);
        }
        // clear the children. Children of the selected nodes will be stored here
        children.clear();
        return pruned;
    }
}
