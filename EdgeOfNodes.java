package waterFlow;

import java.util.List;
import java.util.Map;

public class EdgeOfNodes {
	
	Map<String,Integer> edgeNcost;
	List<Integer> offTimes;
	
	public EdgeOfNodes(Map<String, Integer> edgeNcost, List<Integer> offTimes) {
		super();
		this.edgeNcost = edgeNcost;
		this.offTimes = offTimes;
	}
	public Map<String, Integer> getEdgeNcost() {
		return edgeNcost;
	}
	public void setEdgeNcost(Map<String, Integer> edgeNcost) {
		this.edgeNcost = edgeNcost;
	}
	public List<Integer> getOffTimes() {
		return offTimes;
	}
	public void setOffTimes(List<Integer> offTimes) {
		this.offTimes = offTimes;
	}
	

}
