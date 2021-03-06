package waterFlow;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BFS {

	static void perfomBFS(CommonGraph graph, String sourceNode, int startTime,
			String[] destinationNodes) {
		boolean foundDestination = false;
		Queue<GraphNode> bfsQueue = new LinkedList<GraphNode>();
		Map<String, List<String>> graphToTraverse = graph.getGraph();
		List<String> exploredNodesList = new LinkedList<String>();
		int costToDestination = startTime;
		String visited = sourceNode;
		GraphNode rootNode = new GraphNode();
		rootNode.nodeName = sourceNode;
		rootNode.costToReachNode = startTime;
		rootNode.parentNode = null;
		bfsQueue.add(rootNode);
		rootNode.visited = true;
		exploredNodesList.add(sourceNode);

		while (!bfsQueue.isEmpty()) {

			GraphNode vertex = bfsQueue.poll();
			visited = vertex.getNodeName();
			if (vertex.costToReachNode == startTime) {
				costToDestination = costToDestination + 1;
				startTime = startTime + 1;
			}
			test: for (String adjacentNode : graphToTraverse.get(visited)) {
				GraphNode node = new GraphNode();
				node.nodeName = adjacentNode;
				node.costToReachNode = costToDestination;
				node.parentNode = vertex;
				node.visited = true;
				if (Arrays.toString(destinationNodes).contains(node.nodeName)) {
					waterFlow.outputList.add(node.nodeName+" "+costToDestination);
					foundDestination = true;
					break test;
				} else {
					if (!exploredNodesList.contains(node.nodeName)) {
						bfsQueue.add(node);
					}

				}

			}

			exploredNodesList.add(visited);

		}
		if(!foundDestination)
		waterFlow.outputList.add("NONE");

	}

	public static void main(String[] args) {

	}

}
