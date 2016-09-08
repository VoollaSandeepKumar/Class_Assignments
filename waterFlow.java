package waterFlow;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class waterFlow {

	static enum testCaseEnum {
		DFS, BFS, UCS
	}
	
	public static List<String> outputList = new LinkedList<String>();

	public static void main(String[] args) {

		ArrayList<TestCaseDetails> testCaseDetails = null;

		if (args.length > 1) {
			try {
				BufferedReader bufferedLineReader = new BufferedReader(
						new FileReader(args[1]));
				String inputLine;

				while ((inputLine = bufferedLineReader.readLine()) != null) {
					int totalTestCases = Integer.parseInt(inputLine);
					testCaseDetails = new ArrayList<TestCaseDetails>(
							totalTestCases);
					if (totalTestCases == 0) {

					} else {
						for (int i = 0; i < totalTestCases; i++) {
							TestCaseDetails testCaseDetailsEachInstance = new TestCaseDetails();
							testCaseDetailsEachInstance.testCaseName = bufferedLineReader
									.readLine();
							testCaseDetailsEachInstance.sourceNode = bufferedLineReader
									.readLine();
							testCaseDetailsEachInstance.destinationNodes = bufferedLineReader
									.readLine().split(" ");
							testCaseDetailsEachInstance.middleNodes = bufferedLineReader
									.readLine().split(" ");
							testCaseDetailsEachInstance.pipesNo = Integer
									.parseInt(bufferedLineReader.readLine());
							testCaseDetailsEachInstance.graphDetails = new String[testCaseDetailsEachInstance.pipesNo];
							for (int j = 0; j < testCaseDetailsEachInstance.pipesNo; j++) {
								testCaseDetailsEachInstance.graphDetails[j] = bufferedLineReader
										.readLine();
							}
							testCaseDetailsEachInstance.startTime = Integer
									.parseInt(bufferedLineReader.readLine());
							testCaseDetails.add(testCaseDetailsEachInstance);
							if (bufferedLineReader.readLine() != null) {

							}

						}
					}

				}

				bufferedLineReader.close();

				// checkTheInputFileRead(testCaseDetails);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
			}

		}

		for (TestCaseDetails testCaseDetailsinstance : testCaseDetails) {

			int testCase;
			List<EdgeOfNodes> edgeOfNodeslist = new LinkedList<EdgeOfNodes>();
			Map<String, List<String>> hashmap;
			Set<String> vertices = new HashSet<String>();
			for (int i = 0; i < testCaseDetailsinstance.graphDetails.length; i++) {
				String verticesReader = testCaseDetailsinstance.graphDetails[i];
				String[] tempArray = verticesReader.split(" ");
				for (int j = 0; j < 2; j++) {
					vertices.add(tempArray[j]);
				}
			}
			CommonGraph graph = new CommonGraph(vertices);
			for (int i = 0; i < testCaseDetailsinstance.graphDetails.length; i++) {
				String verticesReader = testCaseDetailsinstance.graphDetails[i];
				String[] tempArray = verticesReader.split(" ");
				graph.addEdge(tempArray[0], tempArray[1]);
				Map<String, Integer> edgeNCost = new HashMap<String, Integer>();
				List<Integer> offTimes = new LinkedList<Integer>();
				edgeNCost.put(tempArray[0] + "N" + tempArray[1],
						Integer.parseInt(tempArray[2]));
				if (tempArray.length > 3) {
					for (int j = 3; j < tempArray.length; j++) {
						try {
							offTimes.add(Integer.parseInt(tempArray[j]));
						} catch (Exception e) {
							String[] offTimesArray = tempArray[j].split("-");
							for (int k = Integer.parseInt(offTimesArray[0]); k <= Integer
									.parseInt(offTimesArray[1]); k++) {
								offTimes.add(k);
							}
						}
					}
				}
				EdgeOfNodes edgeOfNodes = new EdgeOfNodes(edgeNCost, offTimes);
				edgeOfNodeslist.add(edgeOfNodes);

			}

			hashmap = graph.getGraph();

			for (Map.Entry<String, List<String>> entrySet : hashmap.entrySet()) {
				Collections.sort(entrySet.getValue());
			}

			if (testCaseDetailsinstance.testCaseName.equals(testCaseEnum.BFS
					.toString())) {
				testCase = 0;
			} else if (testCaseDetailsinstance.testCaseName
					.equals(testCaseEnum.DFS.toString())) {
				testCase = 1;
			} else {
				testCase = 2;
			}
			switch (testCase) {

			case 0:
				BFS.perfomBFS(graph, testCaseDetailsinstance.sourceNode,
						testCaseDetailsinstance.startTime,
						testCaseDetailsinstance.destinationNodes);
				break;
			case 1:
				DFS.perfomDFS(graph, testCaseDetailsinstance.sourceNode,
						testCaseDetailsinstance.startTime,
						testCaseDetailsinstance.destinationNodes);
				break;
			case 2:
				UCS.perfomUCS(graph, testCaseDetailsinstance.sourceNode,
						testCaseDetailsinstance.startTime,
						testCaseDetailsinstance.destinationNodes,
						edgeOfNodeslist);
				break;
			}
		}
		PrintWriter out;
		try {
			out = new PrintWriter("output.txt");
			for (String line : outputList) {
		        out.println(line);
		    }
		    out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}

	// private static void checkTheInputFileRead(
	// ArrayList<TestCaseDetails> testCaseDetails) {
	// {
	// for (TestCaseDetails testCaseDetailsinstance : testCaseDetails) {
	//
	// System.out.println(testCaseDetailsinstance.testCaseName);
	// System.out.println(testCaseDetailsinstance.sourceNode);
	// System.out.println(Arrays
	// .toString(testCaseDetailsinstance.destinationNodes));
	// System.out.println(Arrays
	// .toString(testCaseDetailsinstance.middleNodes));
	// System.out.println(testCaseDetailsinstance.pipesNo);
	// for (int i = 0; i < testCaseDetailsinstance.graphDetails.length; i++) {
	// System.out.println(testCaseDetailsinstance.graphDetails[i]);
	// }
	// System.out.println(testCaseDetailsinstance.startTime);
	// System.out.println();
	//
	// }
	//
	// }
	//
	// }
	


}
