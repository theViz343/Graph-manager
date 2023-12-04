## Instructions
- Clone the repository, or download the zip file and unzip it.
- Run the command `mvn package`.
- This will build the project, create a `JAR` file in the `targets/` folder and run all tests.
- Opening the project folder in IntelliJ as a project will also enable running the tests and inspecting the code.

### API Functions
The API is contained in the `GraphData.java` file at `src/main/java/` location. The various functions implemented are listed below:

- `boolean parseGraph(String filepath)` :  Import DOT file to create a JGrapht graph object. Returns `true` if successful else `false`.
- `String toString()` : Display graph information such as node and edge number in string format. Returns a `String`.
- `boolean outputGraph(String filepath)` : Writes graph details to a file at `filepath`. Returns `true` if successful else `false`.
- `boolean addNode(String label)` : Adds a node to the graph. Returns `true` if successful else `false`.
- `boolean addNodes(String[] labels)` : Adds a list of nodes to the graph. Returns `true` if successful else `false`.
- `boolean addEdge(String srcLabel, String dstLabel)` : Adds an edge to the graph. Returns `true` if successful else `false`.
- `boolean outputDOTGraph(String path)` : Outputs the `JGraphT` graph object to a `DOT` file at the specified `path`. Returns `true` if successful else `false`.
- `void outputGraphics(String path, String format)` : Outputs the `JGraphT` graph object to a file with file format `format` at the specified `path`.

### How to use (Example code)
- GraphData object creation
```
GraphData graphApi = new GraphData();  
```

- Parse a graph
```
graphApi.parseGraph("src/main/resources/example.dot");
```
Expected Output:
![[Pasted image 20231011191820.png]]
- Display graph data
```
graphApi.toString();
```
Expected Output:
   ```
   Number of nodes: 4  
   Node labels: [A, B, C, D]  
   Number of edges: 3  
   Node and edge directions: (A -> B), (A -> C), (A -> D)
   ```

- Output graph data to file
```
graphApi.outputGraph("src/main/resources/output.txt");
```
Expected Output (in `output.txt`):
   ```
   Number of nodes: 4  
   Node labels: [A, B, C, D]  
   Number of edges: 3  
   Node and edge directions: (A -> B), (A -> C), (A -> D)
   ```

- Add nodes
```
graphApi.addNode("Y");
graphApi.addNodes(new String[]{"Z", "X"});
```

- Add edge
```
graphApi.addEdge("Z", "C");
```

- Output graph in DOT file format
```
graphApi.outputDOTGraph("src/main/resources/gen_graph.dot");
```
Expected Output (in `gen_graph.dot`):
   ```
   strict digraph G {  
  A;  
  B;  
  C;  
  D;
  Y;  
  Z;
  X;  
  A -> B;  
  A -> C;  
  A -> D;  
  Z -> C;  
}
   ```

- Output graph as PNG file
```
graphApi.outputGraphics("src/main/resources/", "png");
```

- GraphSearch API
```
Path path = graphApi.GraphSearch("C","D", Algorithm.BFS); 
// Algorithm.DFS can also be used.
path.printPath();
```
Expected Output
```
Using BFS
C->A->D
```

### Project Part 3
#### Refactors
- [refactor: Encapsulate graphObject and create a getter function.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/e45a2ce14883c4aeb67f4e0d2690915c36e25566)
  This refactor commit aims to encapsulate the `graphObject` object by
  making it private to the `GraphData` class. A getter function called
  `getGraph` is now used to safely access the object.
- [refactor: Rename opt variable to graphString.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/1d4cf4c5c13e876740ae2c57690330be4ed6c287)
  The variable name 'opt' does not provide a good idea about its purpose,
  so I rename it to `graphString` to make it clearer.
- [refactor: Convert test filepaths to static final variables.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/12569c6a57a00f3ed376a8ebec8f425daf416e08)
  This commit converts test filepaths to `static` `final` variables. This
  helps to organise the test parameters a bit more easily.
- [refactor: Add nullcheck in printPath function.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/390f17afecffe4c7d76adca79a21303f7e9bc83d)
  This commit aims to avoid a `NullPointerException` by adding a null check
  to the function printPath.
- [refactor: Add comments in multiple functions](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/4478848e3902af99ab67b6f840414f7a34ff132b)
  This commit introduces comments at various parts of the codebase to
  explain written code better.
#### Template Pattern
The Template Pattern involves:
- Creating an abstract class (`GraphSearch`) to define common graph search steps.
- Extending this template with concrete subclasses (`BFS` and `DFS`) that implement specific traversal methods by overriding abstract template methods.
- This structure provides a shared algorithm skeleton while allowing `BFS` and `DFS` to have their unique implementations.

#### Strategy Pattern
The Strategy Pattern involves:
- Defining a strategy interface (`SearchStrategy`) that declares a method signature for the algorithm.
- Creating concrete strategy classes (`BFS` and `DFS`) that implement the strategy interface with their specific algorithm implementations.
- Implementing a context class (`Context`) that holds a reference to the strategy interface and utilizes it to execute the selected strategy.
- Utilizing the `Context` class in the main code to dynamically switch between different strategies (`BFS` or `DFS`) based on input.

#### Random Walk Search
Exampe graph (from Canvas)
![[Pasted image 20231203183455.png]]

Running this code,
```
Path path = graphApi.GraphSearch("a","c", Algorithm.RWS);  
path.printPath();
```
gives the following outputs -
![[Pasted image 20231203183730.png]]
![[Pasted image 20231203183757.png]]
![[Pasted image 20231203183807.png]]


### Commits
#### main
- [Initial commit](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/68f578cbb07ce42c62e9474c30c1f34977b95783)
- [Add maven pom file.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/0610d6db3ae5f77860a9f7880511aebe4d62aa7a)
- [Update pom file.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/00ee3f874f5a5ee361bf261510dce1ab3c4d3116)
- [Implement first feature.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/41c210e8c432d21d3759468b239b5010236fd42c)
- [Implement second feature.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/64a24a0b9a829a5a9a6498bf7bd6845485f00006)
- [Implement third feature.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/86547b127f2687381278c142af5f4a1e16d12e3d)
- [Implement fourth feature.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/5f9a2f3fcdb0940d1b4c117fb0bdf60ddcecbb11)
- [Handle IO exceptions.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/eb738c4c6074d1c7df9ca486cee51028c3beb929)
- [Add tests](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/5e1a8613730fbdc33c1ca93f821dcb14abcbef1c)
- [Update pom file.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/f6c4256ebb0d7cb7d45a9c1c37c74cb019fcfdee)
- [Update readme.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/c68ed7bb76106c60ac9bf308ff23e07b0a3a045c)
- [Create maven.yml for buld automation.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/e28860c24db821401dcb607d46c034d209775c6e)
- [Update maven.yml to Java 21](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/bd783892ff250b0db9002bd8a27160f931608c8b)
- [Add features to remove nodes.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/ef225100ea2c2d93f4fcae6f8fc9c11f351317f4)
- [Add exception throws to functions.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/640ea3ce61b0b3f583e9611984420fb601763019)
- [Add features to remove edges.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/25aa76529b1b614a5ff506cfdca0765f85729540)
- [Update maven.yml](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/ff27eb0ede87b61e57f5c018d570e269f1a327fa)
- [Add Path class.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/a74906cb89f84e33af56954bf4efcbe5c0e2da5e)
- [Add GraphSearch API with BFS algorithm.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/22e3a2a30a0c315bc7615213efd1e81a3d762560)
- [Add bfs GraphSearch tests.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/2200302f4dd9003a576f8c0989ae77040b115d0a)
-  [Add GraphSearch API with DFS algorithm.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/9fd0504b907dbaac5b1ec14bd0bbbb12d67ade71)
- [Add dfs GraphSearch tests.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/3b37cee0d51486a164900ca3f8bba6b49218efa5)
- [Merge commit.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/aac797bfcb297ac7cea61b7470511c583ed920ad)

#### bfs
- [Add GraphSearch API with BFS algorithm.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/22e3a2a30a0c315bc7615213efd1e81a3d762560)
- [Add bfs GraphSearch tests.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/2200302f4dd9003a576f8c0989ae77040b115d0a)

#### dfs
- [Add GraphSearch API with DFS algorithm.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/9fd0504b907dbaac5b1ec14bd0bbbb12d67ade71)
- [Add dfs GraphSearch tests.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/3b37cee0d51486a164900ca3f8bba6b49218efa5)

#### refactors
- [refactor: Encapsulate graphObject and create a getter function.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/e45a2ce14883c4aeb67f4e0d2690915c36e25566)
- [refactor: Rename opt variable to graphString.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/1d4cf4c5c13e876740ae2c57690330be4ed6c287)
- [refactor: Convert test filepaths to static final variables.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/12569c6a57a00f3ed376a8ebec8f425daf416e08)
- [refactor: Add nullcheck in printPath function.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/390f17afecffe4c7d76adca79a21303f7e9bc83d)
- [refactor: Add comments in multiple functions](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/4478848e3902af99ab67b6f840414f7a34ff132b)

#### Template & Strategy patterns
- [Implement template pattern for graph search algorithms.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/3c6fa30a096abfb07f256444f9ba0b43fec1877a)
- [Add strategy pattern to graph search functionality.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/e1702573c0d81d6e8b86c2da5e29c1eb18eb13e8)

#### Random Walk Search
- [Add Random Walk search algorithm as option.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/94ce68fe1eae225458cd85d3d5c4140a0b72990c)

#### PR Review commits
- [Extract getPath function to template class.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/3a7d5a593ba2e7d3e0a83c828989aa3e3030f371)
- [Add random walk search tests.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/ca4ae7201f17e78cc08c98599eba09e3f2a59087)
- [Add input test dot file for RWS test.](https://github.com/theViz343/CSE-464-2023-vpillai9/commit/6140ba9cc9016ab8961317c156cb4a05b848b9b4)