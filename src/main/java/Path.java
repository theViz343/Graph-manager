import java.util.ArrayList;
import java.util.Collections;
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
        if (path != null) {
            Collections.reverse(path);
            String s = String.join("->", path);
            System.out.println(s);
        } else {
            System.out.println("Path is empty");
        }
    }
}
