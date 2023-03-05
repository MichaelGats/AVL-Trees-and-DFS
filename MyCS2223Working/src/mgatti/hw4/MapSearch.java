package mgatti.hw4;

import algs.hw4.map.HighwayMap;



import algs.hw4.map.Information;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.Stack;
/**
 * Copy this class into USERID.hw4 and make changes.
 */
public class MapSearch {

	/**
	 * This method must create a copy of the graph, which you can do by creating a new graph with 
	 * the same number of vertices as the original one, BUT you only add an edge to the copy
	 * if the edge in the original graph IS NOT involved in the M25.
	 * 
	 * For example, in the data set you will see two nodes:
	 * 
	 *      E13@6a(M1)&E30@21(M25)&M1@6a&M25@21 51.716288 -0.385208
	 * 		E30/M25@+M25(X14) 51.713257 -0.421343
	 * 
	 * These lines correspond to vertex #114 (the first one) and vertex #1196 (the second one).
	 * Because the label for both of these vertices includes "M25" this edge must not appear in 
	 * the copied graph, since it is a highway segment involving the M25.
	 * 
	 * Note that the edge is eliminated even when only one of the nodes involves M25.
	 */
	static Information remove_M25_segments(Information info) {

		edu.princeton.cs.algs4.Graph g = info.graph;
		edu.princeton.cs.algs4.Graph copy = new edu.princeton.cs.algs4.Graph(g.V());
		for (int v = 0; v < g.V(); v++) {
			for (int u: g.adj(v)) {
				if(!(info.labels.get(v).contains("M25")) && !(info.labels.get(u).contains("M25"))) {
					copy.addEdge(v, u);

				}
			}
		}

		Information newInfo = new Information(copy, info.positions, info.labels);
		return newInfo;
	}

	/** 
	 * This helper method returns the western-most data point in the Information, given its latitude and
	 * longitude.
	 * 
	 * https://en.wikipedia.org/wiki/Latitude
	 * https://en.wikipedia.org/wiki/Longitude
	 * 
	 */
	public static int westernMostVertex(Information info) {
		int size = info.positions.size();
		double westMost = Integer.MAX_VALUE;
		int indexWest = Integer.MIN_VALUE;
		for ( int i = 0; i < size; i++) {
			if (info.positions.get(i).latitude < westMost) {
				indexWest = i; 
				westMost = info.positions.get(i).latitude;
			}
		}
		return indexWest; 
	}

	/** 
	 * This helper method returns the eastern-most data point in the Information, given its latitude and
	 * longitude.
	 * 
	 * https://en.wikipedia.org/wiki/Latitude
	 * https://en.wikipedia.org/wiki/Longitude
	 * 
	 */
	public static int easternMostVertex(Information info) {
		int size = info.positions.size();
		double eastMost = Integer.MIN_VALUE;
		int indexEast = Integer.MIN_VALUE;
		for (int j = 0; j < size; j++) {
			if (info.positions.get(j).latitude > eastMost) {
				indexEast = j; 
				eastMost = info.positions.get(j).latitude;

			}
		}
		return indexEast;
	}

	/** 
	 * This helper method returns the southern-most data point in the Information, given its latitude and
	 * longitude.
	 * 
	 * https://en.wikipedia.org/wiki/Latitude
	 * https://en.wikipedia.org/wiki/Longitude
	 * 
	 */
	public static int southernMostVertex(Information info) {
		int size = info.positions.size();
		double southMost = Integer.MAX_VALUE;
		int indexSouth = Integer.MIN_VALUE;
		for (int k = 0; k < size; k++) {
			if (info.positions.get(k).longitude < southMost) {
				indexSouth = k; 
				southMost = info.positions.get(k).longitude;
			}
		}
		return indexSouth;
	}

	/** 
	 * This helper method returns the northern-most data point in the Information, given its latitude and
	 * longitude.
	 * 
	 * https://en.wikipedia.org/wiki/Latitude
	 * https://en.wikipedia.org/wiki/Longitude
	 * 
	 */
	public static int northernMostVertex(Information info) {
		int size = info.positions.size(); 
		double northMost = Integer.MIN_VALUE; 
		int indexNorth = Integer.MIN_VALUE; 
		for (int x = 0; x < size; x++) {
			if (info.positions.get(x).longitude > northMost) {
				indexNorth = x;
				northMost = info.positions.get(x).longitude;
			}
		}
		return indexNorth;
	}

	//implementing Breadth first search (BFS) to find the shortest path based off of the code from BreadthFirstPaths

	public static Iterable<Integer> bfs(Graph G, int s, int t) {
		boolean[] marked;  
		int[] edgeTo;     
		int[] distTo;  

		Queue<Integer> q = new Queue<Integer>();
		marked = new boolean[G.V()];
		distTo = new int[G.V()];
		edgeTo = new int[G.V()];
		for (int v = 0; v < G.V(); v++) { distTo[v] = Integer.MAX_VALUE; }
		distTo[s] = 0;
		marked[s] = true;
		q.enqueue(s);

		while (!q.isEmpty()) {
			int v = q.dequeue();
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					marked[w] = true;
					q.enqueue(w);
				}
			}
		}
		if (!marked[t]) return null;
		Stack<Integer> path = new Stack<Integer>();
		int x;
		for (x = t; distTo[x] != 0; x = edgeTo[x])
			path.push(x);
		path.push(x);
		return path;

	}

	static boolean[] markedD;
	static int[] edgeToD; 

	//Creating a DFS based off of the code from the DepthFirstPaths from day 21
	public static Iterable<Integer> depthFirstSearch(Graph g, int s, int t) {
		markedD = new boolean[g.V()];
		edgeToD = new int[g.V()];

		dfs(g, s);

		if(!hasPathTo(t)) {
			return null; 
		}

		Stack<Integer> path = new Stack<Integer>();
		int v = t;
		while (v != s) {
			path.push(v);
			v = edgeToD[v];
		}
		path.push(s);
		return path;

	}

	public static void dfs (Graph g, int v) {
		markedD[v] = true;
		for(int w : g.adj(v)) {
			if(!markedD[w]) {
				edgeToD[w] = v; 
				dfs(g, w);
			}
		}
	}
	public static boolean hasPathTo(int v) { 
		return markedD[v];
	}





	public static void main(String[] args) {
		Information info = HighwayMap.undirectedGraph();
		int west = westernMostVertex(info);
		int east = easternMostVertex(info);

		int south = southernMostVertex(info);
		int north = northernMostVertex(info);

		int westEastEdges = 0;
		int southNorthEdges = 0;

		int westEastEdgesD = 0; 
		int southNorthEdgesD = 0; 




		String westName = info.labels.get(west);
		String eastName = info.labels.get(east);
		String southName = info.labels.get(south);
		String northName = info.labels.get(north);


		System.out.println("BreadthFirst Search from West to East:");

		Iterable<Integer> pathsWE = bfs(info.graph, west, east);

		for (int i: pathsWE) {
			westEastEdges++;
		}
		System.out.println("BFS: " + westName + "(" + west + ") to " + eastName + "(" + east + ") has " +  westEastEdges + " edges.");


		System.out.println("\nBreadthFirst Search from South to North:");

		Iterable<Integer> pathsSN = bfs(info.graph, south, north);

		for (int j: pathsSN) {
			southNorthEdges++;
		}
		System.out.println("BFS: " + southName + "(" + south + ") to " + northName + "(" + north + ") has " + southNorthEdges + " edges.");

		System.out.println("\nDepthFirst Search from West to East:");
		Iterable<Integer> pathsWED = depthFirstSearch(info.graph, west, east);
		if (pathsWED == null) {
			System.out.println("No path");
		}
		else {
			for (int v : pathsWED) {
				westEastEdgesD++;
			}
		}

		System.out.println("DFS: " + westName + "(" + west + ") to " + eastName + "(" + east + ") has " + westEastEdgesD + " edges.");

		System.out.println("\nDepthFirst Search from South to North:");

		Iterable<Integer> pathsSND = depthFirstSearch(info.graph, south, north);
		if (pathsSND == null) {
			System.out.println("No path");

		}
		else {
			for (int v: pathsSND) {
				southNorthEdgesD++;
			}
		}

		System.out.println("DFS: " + southName + "(" + south + ") to " + northName + "(" + north + ") has " + southNorthEdgesD + " edges.");
		//ANSWERING 2.2
		/* Why is BreadthFristSearch better for this application rather than DepthFirstSearch? 
		 * 
		 * As shown in the result when running this program, the resulting iterable is around 10x larger when performing a DFS when compared to a BFS.
		 * This is due to how DFS is written/run compared to BFS. 
		 * BFS uses a queue and goes from all the nodes at the same depth to the next depth and by the end of the search it returns the shortest path found. 
		 * DFS uses a stack and traverses through the nodes until it finds the end. It will return a stack but it will not guaruntee that its the shortest path.
		 * This is why in this implementation of it, breadth first search is correct.  
		 */




		Information info_no_m25 = remove_M25_segments(info);
		int west25 = westernMostVertex(info_no_m25);
		int east25 = easternMostVertex(info_no_m25);
		int south25 = southernMostVertex(info_no_m25);
		int north25 = northernMostVertex(info_no_m25);
		int westEastEdges25 = 0; 
		int southNorthEdges25 = 0; 
		String westName25 = info_no_m25.labels.get(west25);
		String eastName25 = info_no_m25.labels.get(east25);
		String southName25 = info_no_m25.labels.get(south25);
		String northName25 = info_no_m25.labels.get(north25);



		System.out.println("\nNow without M25 edges...\n");
		System.out.println("WEST to EAST");
		Iterable<Integer> we25 = bfs(info_no_m25.graph, west25, east25);

		if (we25 == null) {
			System.out.println("No path");
		}
		else {
			for (int w : we25) {
				westEastEdges25++;
			}
		}


		System.out.println("BFS: " + westName25 + "(" + west25 + ") to " + eastName25 + "(" + east25 + ") has " + westEastEdges25 + " edges.");

		System.out.println("\nNORTH to SOUTH");
		Iterable<Integer> sn25 = bfs(info_no_m25.graph, north25, south25);
		if (sn25 == null) {
			System.out.println("No path");
		}
		else {
			for (int s : sn25) {
				southNorthEdges25++;
			}
		}
		System.out.println("BFS: " + northName25 + "(" + north25 + ") to " + southName25 + "(" + south25 + ") has " + southNorthEdges25 + " edges.");

	}

}

/*ANSWERING 2.5 BONUS:  
 *
 *The M25 does not complete the full circle because on the east most side there is a crossing called the A282 that passes over a river. 
 *That is not considered part of the M25 and thus it cuts what would be complete circle. This is shown in the data at edge "A282(31)/A1306/E15 51.486974 0.266917" (line 480 in the document).
 *and the edge "A2/A282/E15/M25(2) 51.426267 0.238395" (line 532 in the document). 
 */

