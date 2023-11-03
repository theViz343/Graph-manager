import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GraphDataTest {
    GraphData graphApi = new GraphData();
    @BeforeEach
    public void init_graph() {
        assertTrue(graphApi.parseGraph("src/test/test_graph.dot"));
    }

    @Test
    @DisplayName("Test initial graph parsing")
    public void TestParseGraph() {
        Set<String> nodes = new HashSet<String>();
        nodes.add("A");
        nodes.add("B");
        nodes.add("C");
        nodes.add("D");
        assertEquals(nodes, graphApi.graphObject.vertexSet());
        Set<String> edges = new HashSet<String>();
        Set<String> expected_edges = new HashSet<String>();
        edges.add("(A : B)");
        edges.add("(A : C)");
        edges.add("(A : D)");
        for (DefaultEdge e: graphApi.graphObject.edgeSet()) {
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
        assertTrue(graphApi.outputGraph("src/test/output.txt"));
        assertEquals(expected_value, Files.readString(Paths.get("src/test/output.txt")));
    }

    @Test
    @DisplayName("Test outputGraph if node does not exist already")
    public void TestAddNode(){
        assertTrue(graphApi.addNode("Z"));
        assertTrue(graphApi.graphObject.vertexSet().stream().anyMatch(v -> (v.equals("Z"))));
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
            assertTrue(graphApi.graphObject.vertexSet().stream().anyMatch(v -> (v.equals(node))));
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
        assertFalse(graphApi.graphObject.vertexSet().stream().anyMatch(v -> (v.equals("A"))));
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
            assertFalse(graphApi.graphObject.vertexSet().stream().anyMatch(v -> (v.equals(node))));
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
    @DisplayName("Test DOT graph generation")
    public void TestOutputDOTGraph() throws IOException {
        graphApi.addNode("Z");
        graphApi.addEdge("Z","C");
        String expected_value = Files.readString(Paths.get("src/test/expected.dot"));
        assertTrue(graphApi.outputDOTGraph("src/test/gen_graph.dot"));
        assertEquals(expected_value, Files.readString(Paths.get("src/test/gen_graph.dot")));
    }

}
