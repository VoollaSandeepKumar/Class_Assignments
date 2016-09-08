import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class inference {
	static Map<String, List<String>> factsList = new HashMap<String, List<String>>();
	static List<String> loopFinder = new LinkedList<String>();
	static Map<String, String> thetaForAnd = new HashMap<String, String>();
	static Map<String, Map<String, List<String>>> standardizedKB = new HashMap<String, Map<String, List<String>>>();
	static int globalCount = 0;
	static List<String> outputWriter = new LinkedList<String>();

	public static void main(String[] args) {
		int noOfQueries = 0;
		int noOfClauses = 0;
		ArrayList<String> queryList = new ArrayList<String>();
		ArrayList<String> clausesList = new ArrayList<String>();
		Map<String, Map<String, List<String>>> knowledgeBase = new HashMap<String, Map<String, List<String>>>();
		try {
			File file;
			file = new File("output.txt");
			file.createNewFile();
		} catch (Exception e) {
			System.out.print(e);
		}
		if (args.length > 1) {
			try {
				BufferedReader bufferedLineReader = new BufferedReader(
						new FileReader(args[1]));
				noOfQueries = Integer.parseInt(bufferedLineReader.readLine());
				for (int i = 0; i < noOfQueries; i++) {
					queryList.add(bufferedLineReader.readLine());
				}
				noOfClauses = Integer.parseInt(bufferedLineReader.readLine());
				for (int i = 0; i < noOfClauses; i++) {
					Map<String, List<String>> matchingClauses = new HashMap<String, List<String>>();
					List<String> leftPartOfTheRule = new LinkedList<String>();
					String clausesString = bufferedLineReader.readLine()
							.replaceAll("\\s", "");
					// clausesString = standardizeVariables(clausesString, i);
					clausesList.add(clausesString);
					String predicate = null;
					String functionName = null;
					if (!clausesString.contains("=>")) {
						functionName = clausesString.split("\\(")[0];
						if (null != knowledgeBase.get(functionName)) {
							knowledgeBase.get(functionName).put(clausesString,
									leftPartOfTheRule);
						} else {
							matchingClauses.put(clausesString,
									leftPartOfTheRule);
							knowledgeBase.put(functionName, matchingClauses);
						}
						} else {
							predicate = clausesString.split("=>")[1];
							functionName = predicate.split("\\(")[0];
							if (null != knowledgeBase.get(functionName)) {
								if (null != knowledgeBase.get(functionName).get(
										predicate)) {
									knowledgeBase.get(functionName).get(predicate)
											.add(clausesString.split("=>")[0]);
								} else {
	
									leftPartOfTheRule
											.add(clausesString.split("=>")[0]);
									matchingClauses.put(predicate,
											leftPartOfTheRule);
									knowledgeBase.get(functionName).put(predicate,
											leftPartOfTheRule);
								}
							} else {
								leftPartOfTheRule.add(clausesString.split("=>")[0]);
								matchingClauses.put(predicate, leftPartOfTheRule);
								knowledgeBase.put(functionName, matchingClauses);
							}
						}

				}

				for (int i = 0; i < noOfClauses; i++) {
					String functionName = null;
					String clausesString = clausesList.get(i);
					if (!clausesString.contains("=>")) {
						functionName = clausesString.split("\\(")[0];
						if (null != factsList.get(functionName)) {
							factsList.get(functionName).add(clausesString);
						} else {
							List<String> tempList = new LinkedList<String>();
							tempList.add(clausesString);
							factsList.put(functionName, tempList);
						}
					}
				}
				for (String predicateName : knowledgeBase.keySet()) {
					Map<String, List<String>> newSetOfMatchingRules = new HashMap<String, List<String>>();
					for (String consequent : knowledgeBase.get(predicateName)
							.keySet()) {
						String changedConsequent = null;
						List<String> premisesList = new LinkedList<String>();
						for (String premise : knowledgeBase.get(predicateName)
								.get(consequent)) {
							String ruleString = premise + "=>" + consequent;
							ruleString = standardizeVariables(ruleString,
									globalCount);
							premisesList.add(ruleString.split("=>")[0]);
							changedConsequent = ruleString.split("=>")[1];
						}
						if (null != changedConsequent) {
							newSetOfMatchingRules.put(changedConsequent,
									premisesList);
						} else {
							newSetOfMatchingRules.put(consequent, premisesList);
						}
						globalCount++;
						standardizedKB
								.put(predicateName, newSetOfMatchingRules);
					}

				}
				PrintWriter fileWriter = new PrintWriter(new BufferedWriter(
						new FileWriter("output.txt", false)));
				for (int i = 0; i < noOfQueries; i++) {
					try {
						List<String> factsMatching = factsList.get(queryList.get(i).split("\\(")[0]);
						if(null!=factsMatching)
						{
							if(factsMatching.contains(queryList.get(i)))
							{
								outputWriter.add("TRUE");
							}
							else
							{
								List<String> goalList = new LinkedList<String>();
								goalList.add(queryList.get(i));
								Map<String, String> thetaAsTextBook = new HashMap<String, String>();
								thetaAsTextBook = backWardChainingAsk(standardizedKB,
										goalList, thetaAsTextBook);
								loopFinder.clear();
								System.out.println(thetaAsTextBook);
								if (null != thetaAsTextBook
										&& thetaAsTextBook.size() > 0) {
									outputWriter.add("TRUE");
								} else {
									outputWriter.add("FALSE");
								}
							}
						}
						else
						{

							List<String> goalList = new LinkedList<String>();
							goalList.add(queryList.get(i));
							Map<String, String> thetaAsTextBook = new HashMap<String, String>();
							thetaAsTextBook = backWardChainingAsk(standardizedKB,
									goalList, thetaAsTextBook);
							loopFinder.clear();
							System.out.println(thetaAsTextBook);
							if (null != thetaAsTextBook
									&& thetaAsTextBook.size() > 0) {
								outputWriter.add("TRUE");
							} else {
								outputWriter.add("FALSE");
							}
						
						}
						
					} catch (Exception e) {
						System.out.println(e);
						outputWriter.add("FALSE");
					}
				}
				for (int i = 0; i < outputWriter.size(); i++) {
					fileWriter.println(outputWriter.get(i));
				}
				bufferedLineReader.close();
				fileWriter.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	private static Map<String, String> unification(String queryString,
			String clausesString, Map<String, String> thetaAsTextBookString) {
		if (null == thetaAsTextBookString)
			return null;
		else if (queryString.equals(clausesString))
			return thetaAsTextBookString;
		else if (isvariableAtferVariableStandardization(queryString))
			return unifyVariable(queryString, clausesString,
					thetaAsTextBookString);
		else if (isvariableAtferVariableStandardization(clausesString))
			return unifyVariable(clausesString, queryString,
					thetaAsTextBookString);
		else if (isCompound(queryString) && isCompound(clausesString))
			return unifyOverCompoundItems(queryString, clausesString,
					thetaAsTextBookString);
		else if (isList(queryString) && isList(clausesString)) {
			String[] querySplit = queryString.split(",");
			String[] clausesSplit = clausesString.split(",");
			if (querySplit.length != clausesSplit.length) {
				return null;
			}
			thetaAsTextBookString = unification(
					queryString.substring(queryString.indexOf(",") + 1),
					clausesString.substring(clausesString.indexOf(",") + 1),
					unification(querySplit[0], clausesSplit[0],
							thetaAsTextBookString));
			return thetaAsTextBookString;
		} else
			return null;
	}

	private static boolean isList(String stringTobeChecked) {

		if (stringTobeChecked.contains(",") && !stringTobeChecked.contains(")")
				&& !stringTobeChecked.contains("("))
			return true;
		else
			return false;
	}

	private static Map<String, String> unifyOverCompoundItems(
			String queryString, String clausesString,
			Map<String, String> thetaAsTextBookString) {

		String queryStringArgs = queryString.substring(
				queryString.indexOf("(") + 1, queryString.lastIndexOf(")"));
		String clausesStringArgs = clausesString.substring(
				clausesString.indexOf("(") + 1, clausesString.lastIndexOf(")"));
		String queryStringPredicate = predicate(queryString);
		String clausesStringPredicate = predicate(clausesString);
		String[] querySplit = queryStringArgs.split(",");
		String[] clausesSplit = clausesStringArgs.split(",");
		if (querySplit.length != clausesSplit.length) {
			return null;
		}
		if (!queryStringPredicate.equals(clausesStringPredicate)) {
			return null;
		}
		return unification(
				queryStringArgs,
				clausesStringArgs,
				unification(queryStringPredicate, clausesStringPredicate,
						thetaAsTextBookString));
	}

	private static String predicate(String predicateString) {
		return predicateString.substring(0, predicateString.indexOf("(") + 1);
	}

	private static boolean isCompound(String stringTobeChecked) {

		if (stringTobeChecked.contains("(") && stringTobeChecked.contains(")"))
			return true;
		else
			return false;
	}

	private static Map<String, String> unifyVariable(
			String variableInUnification, String stringToBeUnified,
			Map<String, String> thetaAsTextBookString) {

		if (thetaAsTextBookString.containsKey(variableInUnification))
			return unification(
					thetaAsTextBookString.get(variableInUnification),
					stringToBeUnified, thetaAsTextBookString);
		else if (thetaAsTextBookString.containsKey(stringToBeUnified))
			return unification(variableInUnification,
					thetaAsTextBookString.get(stringToBeUnified),
					thetaAsTextBookString);
		else {
			thetaAsTextBookString.put(variableInUnification, stringToBeUnified);
			return thetaAsTextBookString;
		}
	}

	private static String standardizeVariables(String stringToBeChecked, int i) {
		if (stringToBeChecked.contains("=>")) {
			String leftPart = stringToBeChecked.split("=>")[0];
			String rightPart = stringToBeChecked.split("=>")[1];
			String rightPartArguments = rightPart.substring(
					rightPart.indexOf("(") + 1, rightPart.lastIndexOf(")"));
			String[] rightPartArgumentsArray = rightPartArguments.split(",");
			for (int j = 0; j < rightPartArgumentsArray.length; j++) {
				if (isVariableBeforeVariableStandardization(rightPartArgumentsArray[j])) {
					rightPartArgumentsArray[j] = rightPartArgumentsArray[j] + i;
				}
			}
			rightPart = rightPart.split("\\(")[0]
					+ "("
					+ Arrays.toString(rightPartArgumentsArray)
							.substring(
									Arrays.toString(rightPartArgumentsArray)
											.indexOf("[") + 1,
									Arrays.toString(rightPartArgumentsArray)
											.lastIndexOf("]"))
							.replaceAll("\\s", "") + ")";
			if (leftPart.contains("^")) {
				String leftPartConstructor = "";
				String[] leftPartSplitArray = leftPart.split("\\^");
				for (int j = 0; j < leftPartSplitArray.length; j++) {
					String leftPartArguments = leftPartSplitArray[j].substring(
							leftPartSplitArray[j].indexOf("(") + 1,
							leftPartSplitArray[j].lastIndexOf(")"));
					String[] leftPartArgumentsArray = leftPartArguments
							.split(",");

					for (int k = 0; k < leftPartArgumentsArray.length; k++) {
						if (isVariableBeforeVariableStandardization(leftPartArgumentsArray[k])) {
							leftPartArgumentsArray[k] = leftPartArgumentsArray[k]
									+ i;
						}
					}
					if (j < leftPartSplitArray.length - 1) {
						leftPartConstructor += leftPartSplitArray[j]
								.split("\\(")[0]
								+ "("
								+ Arrays.toString(leftPartArgumentsArray)
										.substring(
												Arrays.toString(
														leftPartArgumentsArray)
														.indexOf("[") + 1,
												Arrays.toString(
														leftPartArgumentsArray)
														.lastIndexOf("]"))
										.replaceAll("\\s", "") + ")^";
					} else {
						leftPartConstructor += leftPartSplitArray[j]
								.split("\\(")[0]
								+ "("
								+ Arrays.toString(leftPartArgumentsArray)
										.substring(
												Arrays.toString(
														leftPartArgumentsArray)
														.indexOf("[") + 1,
												Arrays.toString(
														leftPartArgumentsArray)
														.lastIndexOf("]"))
										.replaceAll("\\s", "") + ")";
					}

				}
				leftPart = leftPartConstructor;
			} else {
				String leftPartArguments = leftPart.substring(
						leftPart.indexOf("(") + 1, leftPart.lastIndexOf(")"));
				String[] leftPartArgumentsArray = leftPartArguments.split(",");
				for (int j = 0; j < leftPartArgumentsArray.length; j++) {
					if (isVariableBeforeVariableStandardization(leftPartArgumentsArray[j])) {
						leftPartArgumentsArray[j] = leftPartArgumentsArray[j]
								+ i;
					}
					leftPart = leftPart.split("\\(")[0]
							+ "("
							+ Arrays.toString(leftPartArgumentsArray)
									.substring(
											Arrays.toString(
													leftPartArgumentsArray)
													.indexOf("[") + 1,
											Arrays.toString(
													leftPartArgumentsArray)
													.lastIndexOf("]"))
									.replaceAll("\\s", "") + ")";
				}
			}
			return leftPart + "=>" + rightPart;
		}
		return stringToBeChecked;

	}

	private static boolean isVariableBeforeVariableStandardization(
			String stringToBeChecked) {
		if (stringToBeChecked.length() == 1) {
			Character cr = stringToBeChecked.charAt(0);
			if (Character.isLowerCase(cr)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private static boolean isvariableAtferVariableStandardization(
			String stringToBeChecked) {
		Character cr = stringToBeChecked.charAt(0);
		if (Character.isLowerCase(cr)) {
			try {
				Integer.parseInt(stringToBeChecked.substring(stringToBeChecked
						.indexOf(cr) + 1));
			} catch (Exception e) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	private static Map<String, String> backWardChainingAsk(
			Map<String, Map<String, List<String>>> knowledgeBase,
			List<String> queryList, Map<String, String> thetaAsTextBooxString) {

		System.out.println("ASK:" +queryList.get(0) );
		System.out.println();
		Map<String, String> answers = new HashMap<String, String>();
		String queryUsedForSubstitution = null;

		if (!queryList.isEmpty()) {
			queryUsedForSubstitution = substitution(thetaAsTextBooxString,
					queryList.get(0));
			queryList.remove(0);
		} else {
			answers.putAll(thetaAsTextBooxString);
			return answers;
		}
		boolean allConstants = true;
		String [] allVariables = queryUsedForSubstitution.substring(
				queryUsedForSubstitution.indexOf("(") + 1, queryUsedForSubstitution.lastIndexOf(")")).split(",");
		for (int i = 0; i < allVariables.length; i++) {
			if(isvariableAtferVariableStandardization(allVariables[i]))
			{
				allConstants = false;
			}
		}
		if(allConstants)
		{
		if (loopFinder.contains(queryUsedForSubstitution)) {
			return answers;
		}
		List<String> factsMatching = factsList.get(queryUsedForSubstitution.split("\\(")[0]);
		if(null!=factsMatching)
		{
			if(factsMatching.contains(queryUsedForSubstitution))
			{
				
			}
			else
			{
				loopFinder.add(queryUsedForSubstitution);
			}
		}
		else
		{
		loopFinder.add(queryUsedForSubstitution);
		}
		}

		Map<String, List<String>> rulesMatchingTheQuery = fetchRules(
				knowledgeBase, queryUsedForSubstitution);
		if(null == rulesMatchingTheQuery)
		{
			return answers;
		}
		List<String> manipulatedKeysList = manuplateFactsToBegining(rulesMatchingTheQuery);
		for (String key : manipulatedKeysList) {
			Map<String, String> thetaDashAsTextBookString = new HashMap<String, String>();
			List<String> premisesMatchingTheQuery;
			thetaDashAsTextBookString = unification(key,
					queryUsedForSubstitution, new HashMap<String, String>());
			if (null != thetaDashAsTextBookString) {
				thetaDashAsTextBookString.putAll(thetaAsTextBooxString);
				thetaDashAsTextBookString = combiningTheta(thetaDashAsTextBookString,thetaAsTextBooxString);
				if (rulesMatchingTheQuery.get(key).size() != 0) {
					premisesMatchingTheQuery = rulesMatchingTheQuery.get(key);
					for (Iterator<String> iterator = premisesMatchingTheQuery
							.iterator(); iterator.hasNext();) {
						String oneOfThePremise = iterator.next();
						String ruleString = oneOfThePremise + "=>" + key;
						List<String> leftPartOfRule = conjunctionRemover(ruleString
								.split("=>")[0]);
						Map<String, String> copyOfthetaAsTextBooxString = new HashMap<String, String>();
						List<String> newQueries = new LinkedList<String>();
						if (null != thetaDashAsTextBookString) {

							newQueries.addAll(leftPartOfRule);
							newQueries.addAll(queryList);
							copyOfthetaAsTextBooxString.putAll(thetaDashAsTextBookString);
						}
						Map<String, String> oneLevelAnswers = backWardChainingAsk(
								knowledgeBase, newQueries,
								copyOfthetaAsTextBooxString);
						if(oneLevelAnswers.size()>0)
						{
							loopFinder.remove(queryUsedForSubstitution);
						}
						else
						{
							newQueries.clear();
						}
						answers.putAll(oneLevelAnswers);
					}
				} else if (queryList.size() > 0) {
					List<String> leftPartOfRule = new LinkedList<String>();
					Map<String, String> copyOfthetaAsTextBooxString = new HashMap<String, String>();
					copyOfthetaAsTextBooxString.putAll(thetaAsTextBooxString);
					List<String> newQueries = new LinkedList<String>();
					if (null != thetaDashAsTextBookString) {

						newQueries.addAll(leftPartOfRule);
						newQueries.addAll(queryList);
						copyOfthetaAsTextBooxString
								.putAll(thetaDashAsTextBookString);
					}
					Map<String, String> oneLevelAnswers = backWardChainingAsk(
							knowledgeBase, newQueries,
							copyOfthetaAsTextBooxString);
					if(oneLevelAnswers.size()>0)
					{
						loopFinder.remove(queryUsedForSubstitution);
					}
					else
					{
						newQueries.clear();
					}
					answers.putAll(oneLevelAnswers);
				} else if (queryList.size() == 0) {
					Map<String, String> oneLevelAnswers =combiningTheta(thetaDashAsTextBookString,thetaAsTextBooxString);
					if(oneLevelAnswers.size()>0)
					{
						loopFinder.remove(queryUsedForSubstitution);
					}
					answers.putAll(oneLevelAnswers);
				}
			}

		}
	
		return answers;
	}

	private static Map<String, String> combiningTheta(
			Map<String, String> thetaDashAsTextBookString,
			Map<String, String> thetaAsTextBooxString) {
		
		Map<String,String> tobReturnedMap = new HashMap<String,String>();
		tobReturnedMap.putAll(thetaDashAsTextBookString);
		for(String key : thetaAsTextBooxString.keySet())
		{
			if(!tobReturnedMap.containsKey(key))
			{
				tobReturnedMap.put(key,thetaAsTextBooxString.get(key));
			}
		}
		return associationRemover(tobReturnedMap);
	}

	private static Map<String, String> associationRemover(
			Map<String, String> tobReturnedMap) {
		if(tobReturnedMap!=null){
			for(String key:tobReturnedMap.keySet()){
				if(isvariableAtferVariableStandardization(tobReturnedMap.get(key)) && tobReturnedMap.containsKey(tobReturnedMap.get(key))){
					if(!isvariableAtferVariableStandardization(tobReturnedMap.get(tobReturnedMap.get(key)))){
						tobReturnedMap.put(key, tobReturnedMap.get(tobReturnedMap.get(key)));
					}
				}
			}
			}
			
			return tobReturnedMap;
	}

	private static List<String> manuplateFactsToBegining(
			Map<String, List<String>> rulesMatchingTheQuery) {
		List<String> factsMap = new LinkedList<String>();
		List<String> rulesMap = new LinkedList<String>();
		for (String key : rulesMatchingTheQuery.keySet()) {
			// ...
			if (rulesMatchingTheQuery.get(key).size() == 0) {
				factsMap.add(key);
			}
		}
		for (String key : rulesMatchingTheQuery.keySet()) {
			// ...
			if (rulesMatchingTheQuery.get(key).size() != 0) {
				rulesMap.add(key);
			}
		}
		factsMap.addAll(rulesMap);
		return factsMap;
	}

	private static List<String> conjunctionRemover(String leftPartOfRule) {

		List<String> temp = new LinkedList<String>();
		if (leftPartOfRule.contains("^")) {
			return Arrays.asList(leftPartOfRule.split("\\^"));
		}
		temp.add(leftPartOfRule);
		return temp;
	}

	private static String substitution(
			Map<String, String> thetaAsTextBookString, String current) {
		for (Map.Entry<String, String> entry : thetaAsTextBookString.entrySet()) {
			if (current.contains(entry.getKey())) {
				current = current.replaceAll(entry.getKey(), entry.getValue());
			}
		}
		return current;
	}

	private static Map<String, List<String>> fetchRules(
			Map<String, Map<String, List<String>>> knowledgeBase,
			String queryString) {

		if (knowledgeBase.containsKey("" + queryString.split("\\(")[0])) {
			return knowledgeBase.get(queryString.split("\\(")[0]);
		}
		return null;
	}
	
}
