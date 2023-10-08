import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.*;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.*;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.dot.DOTImporter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    public void outputDOTGraph(String path) throws IOException {
        DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>(v -> v);
        Writer writer = new StringWriter();
        exporter.exportGraph(graphObject, writer);
        Files.writeString(Paths.get(path), writer.toString(), StandardCharsets.ISO_8859_1);
    }

    public void outputGraphics(String path, String format) throws IOException {
        JGraphXAdapter<String, DefaultEdge> graphAdapter =
                new JGraphXAdapter<String, DefaultEdge>(graphObject);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File(path+"gen_graph."+format);
        ImageIO.write(image, "PNG", imgFile);
    }
    public static void main(String[] args) throws IOException {
        GraphData graphApi = new GraphData();
        graphApi.parseGraph("src/main/resources/example.dot");
        System.out.println(graphApi.toString());
        graphApi.outputGraph("src/main/resources/output.txt");
        graphApi.addNodes(new String[]{"Z", "X"});
        System.out.println(graphApi.toString());
        graphApi.addEdge("Z", "X");
        System.out.println(graphApi.toString());
        graphApi.addEdge("Z", "X");
        System.out.println(graphApi.toString());
        graphApi.outputDOTGraph("src/main/resources/gen_graph.dot");
        graphApi.outputGraphics("src/main/resources/", "png");




        


    }
}
