import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.dot.DOTImporter;
import org.jgrapht.traverse.*;
import org.jgrapht.io.*;

import java.io.*;
import java.io.File;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GraphData {
    private Graph<String, DefaultEdge> graphObject = new DefaultDirectedGraph<>(DefaultEdge.class);

    public void parseGraph(String filepath) throws IOException {

        // Import the graph from file
        DOTImporter<String, DefaultEdge> dotImporter = new DOTImporter<>();
        dotImporter.setVertexFactory(label -> label);
        String fileContent = Files.readString(Paths.get(filepath));
        dotImporter.importGraph(graphObject, new StringReader(fileContent));
        System.out.println("Graph successfully parsed!");
    }

    @Override
    public String toString() {
        String opt = "";
        opt+="Number of nodes: "+graphObject.vertexSet().size()+"\n";
        opt+="Node labels: "+graphObject.vertexSet()+"\n";
        opt+="Number of edges: "+graphObject.edgeSet().size()+"\n";
        StringBuilder edges = new StringBuilder();
        for (DefaultEdge e: graphObject.edgeSet()) {
            edges.append(e.toString().replace(":", "->"));
            edges.append(", ");
        }
        edges.deleteCharAt(edges.length() - 1);
        edges.deleteCharAt(edges.length() - 1);
        opt+="Node and edge directions: "+edges+"\n";
        return opt;
    }

    public void outputGraph(String filepath) throws IOException {
        String opt = toString();
        Files.writeString(Paths.get(filepath), opt, StandardCharsets.ISO_8859_1);
    }

    public void addNode(String label) {
        boolean existing = graphObject.vertexSet().stream().anyMatch(v -> Objects.equals(v, label));

        if (existing) {
            System.out.println("Node with label "+label+" already exists!");
        }
        else {
            graphObject.addVertex(label);
        }
    }

    public void addNodes(String[] labels) {
        for(String label: labels) {
            addNode(label);
        }
    }

    public void addEdge(String srcLabel, String dstLabel) {
        boolean srcnodeexisting = graphObject.vertexSet().stream().anyMatch(v -> Objects.equals(v, srcLabel));
        boolean dstnodeexisting = graphObject.vertexSet().stream().anyMatch(v -> Objects.equals(v, dstLabel));
        DefaultEdge edgeexisting = graphObject.getEdge(srcLabel, dstLabel);
        if (edgeexisting!=null) {
            System.out.println("Edge "+ edgeexisting + " already exists!");
            return;
        }
        if (!srcnodeexisting) {
            System.out.println("Node "+ srcLabel+" does not exist!");
        } else if (!dstnodeexisting) {
            System.out.println("Node "+ dstLabel+" does not exist!");
        } else {
            graphObject.addEdge(srcLabel, dstLabel);
        }
    }
    public static void main(String[] args) throws IOException {
        GraphData graphApi = new GraphData();
        graphApi.parseGraph("src/main/example.dot");
        System.out.println(graphApi.toString());
        graphApi.outputGraph("src/main/output.txt");
        graphApi.addNodes(new String[]{"Z", "X"});
        System.out.println(graphApi.toString());
        graphApi.addEdge("Z", "X");
        System.out.println(graphApi.toString());
        graphApi.addEdge("Z", "X");
        System.out.println(graphApi.toString());




        


    }
}
