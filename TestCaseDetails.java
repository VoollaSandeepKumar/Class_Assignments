package waterFlow;


public class TestCaseDetails {
	
	String testCaseName;
	String sourceNode;
	String [] destinationNodes;
	String [] middleNodes;
	int pipesNo;
	String [] graphDetails;
	int startTime;
	public String getTestCaseName() {
		return testCaseName;
	}
	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}
	public String getSourceNode() {
		return sourceNode;
	}
	public void setSourceNode(String sourceNode) {
		this.sourceNode = sourceNode;
	}
	public String[] getDestinationNodes() {
		return destinationNodes;
	}
	public void setDestinationNodes(String[] destinationNodes) {
		this.destinationNodes = destinationNodes;
	}
	public String[] getMiddleNodes() {
		return middleNodes;
	}
	public void setMiddleNodes(String[] middleNodes) {
		this.middleNodes = middleNodes;
	}
	public int getPipesNo() {
		return pipesNo;
	}
	public void setPipesNo(int pipesNo) {
		this.pipesNo = pipesNo;
	}
	public String[] getGraphDetails() {
		return graphDetails;
	}
	public void setGraphDetails(String[] graphDetails) {
		this.graphDetails = graphDetails;
	}
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

}
