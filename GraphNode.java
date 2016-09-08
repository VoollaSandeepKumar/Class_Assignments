package waterFlow;

public class GraphNode {

	String nodeName;
	int costToReachNode;
	boolean visited = false;
	GraphNode parentNode;
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public int getCostToReachNode() {
		return costToReachNode;
	}
	public void setCostToReachNode(int costToReachNode) {
		this.costToReachNode = costToReachNode;
	}
	public GraphNode getParentNode() {
		return parentNode;
	}
	public void setParentNode(GraphNode parentNode) {
		this.parentNode = parentNode;
	}
	public boolean isVisited() {
		return visited;
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	
	
}
