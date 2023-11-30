import org.jgrapht.Graph;
import org.jgrapht.Graphs;

import java.util.*;

abstract class GraphSearchAlgorithm {
    protected HashMap<String, Boolean> visited;
    protected HashMap<String, String> parent;
    protected Path path;
    protected String source;
    protected String destination;
    protected Graph graphObject;

    public GraphSearchAlgorithm(String src, String dst, Graph graph) {
        source = src;
        destination = dst;
        graphObject = graph;
        visited = new HashMap<>();
        parent = new HashMap<>();
        path = new Path();
    }

    // Template method defining the common steps
    public Path graphSearch() {
        selectAlgorithm();
        executeAlgorithm();
        return getPath();
    }

    // Abstract methods to be implemented by subclasses
    protected abstract void selectAlgorithm();
    protected abstract void executeAlgorithm();
    protected abstract Path getPath();
}

class BFS extends GraphSearchAlgorithm implements SearchStrategy {
    private LinkedList<String> queue;

    public BFS(String src, String dst, Graph graph) {
        super(src, dst, graph);
        queue = new LinkedList<>();
    }

    @Override
    public void selectAlgorithm() {
        System.out.println("Using BFS");
        visited.put(source, true);
        queue.add(source);
    }
    @Override
    public void executeAlgorithm() {
        while (!queue.isEmpty()) {
            String src = queue.poll();
            if (src.equals(destination)) {
                break;
            }
            List<String> successors = Graphs.neighborListOf(graphObject, src);
            for (String node : successors) {
                if (visited.get(node) == null) {
                    visited.put(node, true);
                    parent.put(node, src);
                    queue.add(node);
                }
            }
        }
    }

    @Override
    public Path getPath() {
        String node = destination;
        path.add(node);
        if (visited.get(destination) != null) {
            while (true) {
                if (node.equals(source)) {
                    return path;
                }
                node = parent.get(node);
                path.add(node);
            }
        } else {
            return null;
        }
    }
}

class DFS extends GraphSearchAlgorithm implements SearchStrategy {
    private Stack<String> stack;

    public DFS(String src, String dst, Graph graph) {
        super(src, dst, graph);
        stack = new Stack<>();
    }

    @Override
    public void selectAlgorithm() {
        System.out.println("Using DFS");
        visited.put(source, true);
        stack.push(source);
    }

    @Override
    public void executeAlgorithm() {
        while (!stack.isEmpty()) {
            String src = stack.pop();
            if (src.equals(destination)) {
                break;
            }
            List<String> successors = Graphs.neighborListOf(graphObject, src);
            for (String node : successors) {
                if (visited.get(node) == null) {
                    visited.put(node, true);
                    parent.put(node, src);
                    stack.push(node);
                }
            }
        }
    }

    @Override
    public Path getPath() {
        String node = destination;
        path.add(node);
        if (visited.get(destination) != null) {
            while (true) {
                if (node.equals(source)) {
                    return path;
                }
                node = parent.get(node);
                path.add(node);
            }
        } else {
            return null;
        }
    }
}

class RWS extends GraphSearchAlgorithm implements SearchStrategy {
    private List<String> arr;

    public RWS(String src, String dst, Graph graph) {
        super(src, dst, graph);
        arr = new ArrayList<>();
    }

    @Override
    public void selectAlgorithm() {
        System.out.println("Using Random Walk Search (RWS)");
        visited.put(source, true);
        arr.add(source);
    }

    @Override
    public void executeAlgorithm() {
        while (!arr.isEmpty()) {
            String src = arr.get((int) (Math.random() * arr.size()));
            arr.remove(src);
            System.out.println("Visiting "+src);
            if (src.equals(destination)) {
                break;
            }
            List<String> successors = Graphs.successorListOf(graphObject, src);
            if (successors.isEmpty()) {
                arr.remove(src);
                continue;
            }
            Collections.shuffle(successors);
            for (String node : successors) {
                if (visited.get(node) == null) {
                    visited.put(node, true);
                    parent.put(node, src);
                    arr.add(node);
                }
            }
        }
    }

    @Override
    public Path getPath() {
        String node = destination;
        path.add(node);
        if (visited.get(destination) != null) {
            while (true) {
                if (node.equals(source)) {
                    return path;
                }
                node = parent.get(node);
                path.add(node);
            }
        } else {
            return null;
        }
    }
}
