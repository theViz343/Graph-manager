import java.util.ArrayList;
import java.util.List;

public class Path {
    List<String> path;
    Path() {
        path = new ArrayList<String>();
    }

    public void add(String node) {
        path.add(node);
    }

    public void printPath() {
        String s = String.join("->", path.reversed());
        System.out.println(s);
    }
}
