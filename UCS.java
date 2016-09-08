package waterFlow;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class UCS {

	public static void perfomUCS(CommonGraph graph, String sourceNode,
			int startTime, String[] destinationNodes,
			List<EdgeOfNodes> edgeOfNodeslist) {
		boolean foundDestination = false;
		GraphNodeComparator mygrpahNodeComparator = new GraphNodeComparator();
		PriorityQueue<GraphNode> ucsQueue = new PriorityQueue<GraphNode>(
				mygrpahNodeComparator);
		Map<String, List<String>> graphToTraverse = graph.getGraph();
		List<String> exploredNodesList = new LinkedList<String>();
		String visited = sourceNode;
		GraphNode rootNode = new GraphNode();
		rootNode.nodeName = sourceNode;
		rootNode.costToReachNode = Math.floorMod(startTime, 24);
		rootNode.parentNode = null;
		ucsQueue.add(rootNode);
		rootNode.visited = true;
		exploredNodesList.add(sourceNode);

		while (!ucsQueue.isEmpty()) {
			GraphNode vertex = ucsQueue.poll();
			visited = vertex.getNodeName();
			if (Arrays.toString(destinationNodes).contains(vertex.nodeName)) {
				if (!sourceNode.equals(vertex.nodeName))
				{
					waterFlow.outputList.add(vertex.nodeName + " "
							+ vertex.costToReachNode);
					foundDestination=true;
					break;
				}
				
			}

			List<GraphNode> currentVertexedgeList = new LinkedList<GraphNode>();

			for (String adjacentNode : graphToTraverse.get(visited)) {
				boolean foundEdge = false;

				for (int j = 0; j < edgeOfNodeslist.size(); j++) {
					if (!foundEdge) {
						EdgeOfNodes edgeofCurrentNodes = edgeOfNodeslist.get(j);
						GraphNode node = new GraphNode();
						if (edgeofCurrentNodes.edgeNcost.containsKey(visited
								+ "N" + adjacentNode)) {
							node.nodeName = adjacentNode;
							node.costToReachNode = vertex.costToReachNode
									+ edgeofCurrentNodes.edgeNcost.get(visited
											+ "N" + adjacentNode);
							node.parentNode = vertex;
							foundEdge = true;
							if (!edgeofCurrentNodes.offTimes
									.contains(vertex.costToReachNode)) {
								currentVertexedgeList.add(node);
							}

						}
					}
				}
			}
			currentVertexedgeList.sort(mygrpahNodeComparator);
			for (GraphNode graphNode : currentVertexedgeList) {
				graphNode.visited = true;
				if (!exploredNodesList.contains(graphNode.nodeName))
					ucsQueue.add(graphNode);
			}

			exploredNodesList.add(visited);

		}
		if(!foundDestination)
		waterFlow.outputList.add("NONE");

	}

	public static void main(String[] args) {

	}

}
