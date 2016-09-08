package waterFlow;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class DFS {

	static void perfomDFS(CommonGraph graph, String sourceNode, int startTime,
			String[] destinationNodes) {
		boolean foundDestination = false;
		Stack<GraphNode> dfsStack = new Stack<GraphNode>();
		Map<String, List<String>> graphToTraverse = graph.getGraph();
		List<String> exploredNodesList = new LinkedList<String>();
		int costToDestination = startTime;
		String visited = sourceNode;
		GraphNode rootNode = new GraphNode();
		rootNode.nodeName = sourceNode;
		rootNode.costToReachNode = startTime;
		rootNode.parentNode = null;
		dfsStack.push(rootNode);
		rootNode.visited = true;
		exploredNodesList.add(sourceNode);

		while (!dfsStack.isEmpty()) {

			GraphNode vertex = dfsStack.pop();
			visited = vertex.getNodeName();
			if (vertex.costToReachNode == startTime) {
				costToDestination = costToDestination + 1;
				startTime = startTime + 1;
			}
			test: for (int i = (graphToTraverse.get(visited).size() - 1); i >= 0; i--) {
				List<String> adjacencytList = graphToTraverse.get(visited);
				GraphNode node = new GraphNode();
				node.nodeName = adjacencytList.get(i);
				node.costToReachNode = costToDestination;
				node.parentNode = vertex;
				node.visited = true;
				if (Arrays.toString(destinationNodes).contains(node.nodeName)) {
					waterFlow.outputList.add(node.nodeName+" "+costToDestination);
					foundDestination=true;
					break test;
				} else {
					if (!exploredNodesList.contains(node.nodeName)) {
						dfsStack.add(node);
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
