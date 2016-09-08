package waterFlow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonGraph {
	
	Map<String, List<String>> graph;
	
	public CommonGraph(Set<String> vertices)
	{
		graph = new HashMap<String, List<String>>();
		for (String string : vertices) {
			graph.put(string, new LinkedList<String>());
		}
	}
	
	public void addEdge(String source, String destination)
	{
		List<String> connectedNodesList = graph.get(source);
		connectedNodesList.add(destination);
	}

	public List<String> readEdge(String source)
	{
		List <String> connectedNodesList = new LinkedList<String>();
		if(graph.containsKey(source))
		{
			connectedNodesList = graph.get(source);
		}
		return connectedNodesList;
	}

	public Map<String, List<String>> getGraph() {
		return graph;
	}
	
}
