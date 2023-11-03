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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GraphData {
    Graph<String, DefaultEdge> graphObject = new DefaultDirectedGraph<>(DefaultEdge.class);

    public boolean parseGraph(String filepath) {

        // Import the graph from file
        DOTImporter<String, DefaultEdge> dotImporter = new DOTImporter<>();
        dotImporter.setVertexFactory(label -> label);
        try {
            String fileContent = Files.readString(Paths.get(filepath));
            dotImporter.importGraph(graphObject, new StringReader(fileContent));
            System.out.println("Graph successfully parsed!");
            return true;
        } catch (IOException e) {
            System.out.println("Cannot read file " + filepath);
            System.out.println(e);
            return false;
        }
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

    public boolean outputGraph(String filepath) {
        String opt = toString();
        try {
            Files.writeString(Paths.get(filepath), opt, StandardCharsets.ISO_8859_1);
            return true;
        } catch (IOException e) {
            System.out.println("Cannot write file at " + filepath);
            System.out.println(e);
            return false;
        }
    }

    public boolean addNode(String label) {
        boolean existing = graphObject.vertexSet().stream().anyMatch(v -> Objects.equals(v, label));

        if (existing) {
            System.out.println("Node with label "+label+" already exists!");
            return false;
        }
        else {
            graphObject.addVertex(label);
            return true;
        }
    }

    public boolean addNodes(String[] labels) {
        boolean result = true;
        for(String label: labels) {
            result = result && addNode(label);
        }
        return result;
    }

    public void removeNode(String label) throws Exception {
        boolean existing = graphObject.vertexSet().stream().anyMatch(v -> Objects.equals(v, label));

        if (existing) {
            graphObject.removeVertex(label);
        }
        else {
            throw new Exception("Node with label "+label+" does not exist!");
        }
    }

    public void removeNodes(String[] labels) throws Exception {
        for(String label: labels) {
            removeNode(label);
        }
    }

    public boolean addEdge(String srcLabel, String dstLabel) {
        boolean srcnodeexisting = graphObject.vertexSet().stream().anyMatch(v -> Objects.equals(v, srcLabel));
        boolean dstnodeexisting = graphObject.vertexSet().stream().anyMatch(v -> Objects.equals(v, dstLabel));
        DefaultEdge edgeexisting = graphObject.getEdge(srcLabel, dstLabel);
        if (edgeexisting!=null) {
            System.out.println("Edge "+ edgeexisting + " already exists!");
            return false;
        }
        if (!srcnodeexisting) {
            System.out.println("Node "+ srcLabel+" does not exist!");
            return false;
        } else if (!dstnodeexisting) {
            System.out.println("Node "+ dstLabel+" does not exist!");
            return false;
        } else {
            graphObject.addEdge(srcLabel, dstLabel);
            return true;
        }
    }

    public boolean outputDOTGraph(String path) {
        DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>(v -> v);
        Writer writer = new StringWriter();
        exporter.exportGraph(graphObject, writer);
        try {
            Files.writeString(Paths.get(path), writer.toString(), StandardCharsets.ISO_8859_1);
            return true;
        } catch (IOException e) {
            System.out.println("Cannot write file at " + path);
            System.out.println(e);
            return false;
        }
    }

    public void outputGraphics(String path, String format) {
        JGraphXAdapter<String, DefaultEdge> graphAdapter =
                new JGraphXAdapter<String, DefaultEdge>(graphObject);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File(path+"gen_graph."+format);
        try {
            ImageIO.write(image, "PNG", imgFile);
        } catch (IOException e) {
            System.out.println("Cannot write file at " + path);
            System.out.println(e);
        }
    }
    public static void main(String[] args) {
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
