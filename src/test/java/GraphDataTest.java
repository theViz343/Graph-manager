import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GraphDataTest {
    GraphData graphApi = new GraphData();
    static final String GRAPH_PATH = "src/test/test_graph.dot";
    static final String CANVAS_GRAPH_PATH = "src/main/resources/input2.dot";
    static final String OUTPUT_GRAPH_STRING_PATH = "src/test/output.txt";
    static final String OUTPUT_GRAPH_DOT_PATH = "src/test/gen_graph.dot";
    static final String EXPECTED_GRAPH_DOT_PATH = "src/test/expected.dot";

    @BeforeEach
    public void init_graph() {
        assertTrue(graphApi.parseGraph(GRAPH_PATH));
    }

    @Test
    @DisplayName("Test initial graph parsing")
    public void TestParseGraph() {
        Set<String> nodes = new HashSet<String>();
        nodes.add("A");
        nodes.add("B");
        nodes.add("C");
        nodes.add("D");
        assertEquals(nodes, graphApi.getGraph().vertexSet());
        Set<String> edges = new HashSet<String>();
        Set<String> expected_edges = new HashSet<String>();
        edges.add("(A : B)");
        edges.add("(A : C)");
        edges.add("(A : D)");
        for (DefaultEdge e: graphApi.getGraph().edgeSet()) {
            expected_edges.add(e.toString());
        }
        assertEquals(expected_edges, edges);
    }

    @Test
    @DisplayName("Test toString")
    public void TestToString() {
        String expected_value = "Number of nodes: 4\n" +
                "Node labels: [A, B, C, D]\n" +
                "Number of edges: 3\n" +
                "Node and edge directions: (A -> B), (A -> C), (A -> D)\n";

        assertEquals(expected_value, graphApi.toString());
    }

    @Test
    @DisplayName("Test outputGraph")
    public void TestOutputGraph() throws IOException {
        String expected_value = """
                Number of nodes: 4
                Node labels: [A, B, C, D]
                Number of edges: 3
                Node and edge directions: (A -> B), (A -> C), (A -> D)
                """;
        assertTrue(graphApi.outputGraph(OUTPUT_GRAPH_STRING_PATH));
        assertEquals(expected_value, Files.readString(Paths.get(OUTPUT_GRAPH_STRING_PATH)));
    }

    @Test
    @DisplayName("Test outputGraph if node does not exist already")
    public void TestAddNode(){
        assertTrue(graphApi.addNode("Z"));
        assertTrue(graphApi.getGraph().vertexSet().stream().anyMatch(v -> (v.equals("Z"))));
    }

    @Test
    @DisplayName("Test outputGraph if node exists already")
    public void TestAddNodeIfExists(){
        graphApi.addNode("Z");
        assertFalse(graphApi.addNode("Z"));
    }

    @Test
    @DisplayName("Test addition of multiple nodes")
    public void TestAddNodes(){
        String[] nodes = {"Z", "X", "Y"};
        graphApi.addNodes(nodes);
        for(String node:nodes) {
            assertTrue(graphApi.getGraph().vertexSet().stream().anyMatch(v -> (v.equals(node))));
        }
    }

    @Test
    @DisplayName("Test addition of multiple nodes if nodes exist")
    public void TestAddNodesIfExistAlready(){
        String[] nodes = {"A", "B", "C"};
        assertFalse(graphApi.addNodes(nodes));
    }

    @Test
    @DisplayName("Test removeNode if node exists.")
    public void TestRemoveNode() throws Exception {
        graphApi.removeNode("A");
        assertFalse(graphApi.getGraph().vertexSet().stream().anyMatch(v -> (v.equals("A"))));
    }

    @Test
    @DisplayName("Test removeNode if node does not exist.")
    public void TestRemoveNodeIfExists() {
        assertThrows(Exception.class, () -> graphApi.removeNode("Z"));
    }

    @Test
    @DisplayName("Test removal of multiple nodes")
    public void TestRemoveNodes() throws Exception {
        String[] nodes = {"A", "B", "C"};
        graphApi.removeNodes(nodes);
        for(String node:nodes) {
            assertFalse(graphApi.getGraph().vertexSet().stream().anyMatch(v -> (v.equals(node))));
        }
    }

    @Test
    @DisplayName("Test removal of multiple nodes which do not exist")
    public void TestRemoveNodesIfNotExist() throws Exception {
        String[] nodes = {"X", "Y", "Z"};
        assertThrows(Exception.class, () -> graphApi.removeNodes(nodes));
    }


    @Test
    @DisplayName("Test addition of edge")
    public void TestAddEdge(){
        assertTrue(graphApi.addEdge("B","C"));
    }

    @Test
    @DisplayName("Test addition of alreardy existing edge")
    public void TestAddEdgeIfExists(){
        assertFalse(graphApi.addEdge("A","C"));
    }

    @Test
    @DisplayName("Test addition of edge")
    public void TestAddEdgeIfNodeDoesNotExist(){
        assertFalse(graphApi.addEdge("Z","C"));
    }

    @Test
    @DisplayName("Test removal of edge")
    public void TestRemoveEdge() throws Exception {
        graphApi.removeEdge("A","B");
    }

    @Test
    @DisplayName("Test addition of already existing edge")
    public void TestRemoveEdgeIfExists(){
        assertThrows(Exception.class, () -> graphApi.removeEdge("B","C"));
    }


    @Test
    @DisplayName("Test bfs graph search api")
    public void TestGraphSearchBFS() {
        Path path = graphApi.GraphSearch("C", "D", GraphData.Algorithm.BFS);
        List<String> expected = List.of(new String[]{"D", "A", "C"});
        assertEquals(path.path, expected);
    }

    @Test
    @DisplayName("Test bfs graph search api (node does not exist)")
    public void TestGraphSearchNotExistBFS() {
        Path path = graphApi.GraphSearch("C", "X", GraphData.Algorithm.BFS);
        assertNull(path);
    }

    @Test
    @DisplayName("Test dfs graph search api")
    public void TestGraphSearchDFS() {
        Path path = graphApi.GraphSearch("C", "D", GraphData.Algorithm.DFS);
        List<String> expected = List.of(new String[]{"D", "A", "C"});
        assertEquals(path.path, expected);
    }

    @Test
    @DisplayName("Test dfs graph search api (node does not exist)")
    public void TestGraphSearchNotExistDFS() {
        Path path = graphApi.GraphSearch("C", "X", GraphData.Algorithm.DFS);
        assertNull(path);
    }

    @Test
    @DisplayName("Test rws graph search api")
    public void TestGraphSearchRWS() {
        graphApi.parseGraph(CANVAS_GRAPH_PATH);
        Path path = graphApi.GraphSearch("a", "c", GraphData.Algorithm.RWS);
        List<String> expected = List.of(new String[]{"c", "b", "a"});
        assertEquals(path.path, expected);
    }

    @Test
    @DisplayName("Test rws graph search api (node does not exist)")
    public void TestGraphSearchNotExistRWS() {
        graphApi.parseGraph(CANVAS_GRAPH_PATH);
        Path path = graphApi.GraphSearch("h", "e", GraphData.Algorithm.RWS);
        assertNull(path);
    }

    @Test
    @DisplayName("Test DOT graph generation")
    public void TestOutputDOTGraph() throws IOException {
        graphApi.addNode("Z");
        graphApi.addEdge("Z","C");
        String expected_value = Files.readString(Paths.get(EXPECTED_GRAPH_DOT_PATH));
        assertTrue(graphApi.outputDOTGraph(OUTPUT_GRAPH_DOT_PATH));
        assertEquals(expected_value, Files.readString(Paths.get(OUTPUT_GRAPH_DOT_PATH)));
    }

}
