import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class Context {
    SearchStrategy strategy;
    Context(SearchStrategy strategy)
    {
        this.strategy = strategy;
    }

    Path searchByStrategy(){
        strategy.selectAlgorithm();
        strategy.executeAlgorithm();
        return strategy.getPath();
    }
}