import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Unification {

	static Map<String, String> thetaAsTextBookString = new HashMap<String, String>();
	static Map<String, List<String>> factsList = new HashMap<String, List<String>>();
	static Map<String,Integer> infiniteLoopDetector = new HashMap<String, Integer>();
	static Map<String, String> thetaForAnd = new HashMap<String, String>();

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
			System.out.println(e);
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
					clausesString = standardizeVariables(clausesString, i);
					clausesList.add(clausesString);
					String predicate = null;
					String functionName = null;
					if (!clausesString.contains("=>")) {
						functionName = clausesString.split("\\(")[0];
						if (null != knowledgeBase.get(functionName)) {
							leftPartOfTheRule.add("");
							knowledgeBase.get(functionName).put(clausesString,
									leftPartOfTheRule);
						} else {
							leftPartOfTheRule.add("");
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
								knowledgeBase.get(functionName).put(predicate, leftPartOfTheRule);;
							}
						} else {
							leftPartOfTheRule.add(clausesString.split("=>")[0]);
							matchingClauses.put(predicate, leftPartOfTheRule);
							knowledgeBase.put(functionName, matchingClauses);
						}
					}

				}
				/*
				 * for (int i = 0; i < queryList.size(); i++) {
				 * 
				 * algorthmStart(queryList.get(i), clausesList);
				 * 
				 * }
				 */
				for (int i = 0; i < noOfClauses; i++) {
					String clausesString = bufferedLineReader.readLine()
							.replaceAll("\\s", "");
					clausesString = standardizeVariables(clausesString, i);
					clausesList.add(clausesString);
					String functionName = null;
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
				for (int i = 0; i < noOfQueries; i++) {
					backWardChainingAsk(knowledgeBase, queryList.get(i));
				}
				bufferedLineReader.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	/*
	 * private static void algorthmStart(String queryString, ArrayList<String>
	 * clausesList) {
	 * 
	 * for (int j = 0; j < clausesList.size(); j++) { unification("F(Bob)",
	 * "F(x)", thetaAsTextBookString);
	 * System.out.println(thetaAsTextBookString.toString()); }
	 * 
	 * }
	 */
	private static Map<String, String> unification(String queryString,
			String clausesString, Map<String, String> thetaAsTextBookString) {
		/*
		 * if (thetaAsTextBookString ) return thetaAsTextBookString; else
		 */if (queryString.equals(clausesString))
			return thetaAsTextBookString;
		else if (isvariableAtferVariableStandardization(queryString))
			return unifyVariable(queryString, clausesString,
					thetaAsTextBookString);
		else if (isvariableAtferVariableStandardization(clausesString))
			return unifyVariable(clausesString, queryString,
					thetaAsTextBookString);
		else if (isCompound(queryString) && isCompound(clausesString))
			return unifyOverCompoundItems(queryString, clausesString);
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

	/*
	 * private static String unifyOverListItems(String queryString, String
	 * clausesString) { // TODO Auto-generated method stub return null; }
	 */

	private static boolean isList(String stringTobeChecked) {

		if (stringTobeChecked.contains(",") && !stringTobeChecked.contains(")")
				&& !stringTobeChecked.contains("("))
			return true;
		else
			return false;
	}

	private static Map<String, String> unifyOverCompoundItems(
			String queryString, String clausesString) {

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
		/*
		 * else if(occursCheck(variableInUnification,stringToBeUnified,
		 * thetaAsTextBookString)) return null;
		 */
		else {
			thetaAsTextBookString.put(variableInUnification, stringToBeUnified);
			return thetaAsTextBookString;
		}
	}

	/*
	 * private static boolean occursCheck(String variableInUnification, String
	 * stringToBeUnified, Map<String, String> thetaAsTextBookString2) { // TODO
	 * Auto-generated method stub return false; }
	 */
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

	private static Map<String, List<String>> backWardChainingAsk(
			Map<String, Map<String, List<String>>> knowledgeBase,
			String queryString) {
		return backWardChainingOr(knowledgeBase, queryString,
				thetaAsTextBookString);

	}

	private static Map<String, List<String>> backWardChainingOr(
			Map<String, Map<String, List<String>>> knowledgeBase,
			String queryString, Map<String, String> thetaAsTextBookString) {
		Map<String, List<String>> rulesMatchingWithQuery = fetchRules(
				knowledgeBase, queryString);
		if (null != rulesMatchingWithQuery) {
			for (Map.Entry<String, List<String>> entry : rulesMatchingWithQuery
					.entrySet()) {
				for (String ruleString : entry.getValue()) {
					String leftPartOfRule = ruleString;
					String rightPartOfRule = entry.getKey();
					if(null != infiniteLoopDetector.get(rightPartOfRule))
					{
						int count = infiniteLoopDetector.get(rightPartOfRule);
						count++;
						infiniteLoopDetector.put(rightPartOfRule, count);
					}
					else
					{
						infiniteLoopDetector.put(rightPartOfRule, 0);
						
					}
					if(infiniteLoopDetector.get(rightPartOfRule) == 1)
					{
						return null;
					}
					thetaAsTextBookString = unification(queryString,
							rightPartOfRule, thetaAsTextBookString);
					backWardChainingAnd(knowledgeBase, leftPartOfRule,
							thetaAsTextBookString);
				}
			}
		} else {
			
			List<String> factsMatchingTheQuery = fetchFacts(queryString);
			for (Iterator<String> iterator = factsMatchingTheQuery.iterator(); iterator
					.hasNext();) {
				
				String factForSubstitution = iterator.next();
				if(queryString.equals(factForSubstitution))
				{
					return null;
				}
				thetaAsTextBookString = unification(factForSubstitution,
						queryString, thetaAsTextBookString);
				/*backWardChainingAnd(knowledgeBase, queryString,
						thetaAsTextBookString);*/
				
			}
			// TODO
			// i think when no matching for the given query loop through the
			// facts or return null
		}

		return null;
	}

	private static List<String> fetchFacts(String queryString) {
		List<String> listToBeReturned = new LinkedList<String>();
		boolean donotAdd = false;
		if(factsList.containsKey(queryString.split("\\(")[0]))
		{
			List<String> listOfFacts = factsList.get(queryString.split("\\(")[0]);
			for (Iterator<String> iterator = listOfFacts.iterator(); iterator.hasNext();) {
				String factString = (String) iterator.next();
				String [] factsArgumentArray = factString.substring(
						factString.indexOf("(") + 1, factString.lastIndexOf(")")).split(",");
				String[] argumentsArray = queryString.substring(
						queryString.indexOf("(") + 1, queryString.lastIndexOf(")")).split(",");
				for (int i = 0; i < argumentsArray.length; i++) {
					if(!isvariableAtferVariableStandardization(argumentsArray[i]))
					{
						if(argumentsArray[i].equals(factsArgumentArray[i]))
						{
							
						}
						else
						{
							donotAdd = true;
						}
					}
				}
				if(!donotAdd)
				{
					listToBeReturned.add(factString);
				}
				{
					donotAdd = false;
				}
				
			}
		}
		
		return listToBeReturned;
	}

	private static void backWardChainingAnd(
			Map<String, Map<String, List<String>>> knowledgeBase,
			String leftPartOfRule, Map<String, String> thetaAsTextBookString) {
		if (thetaAsTextBookString == null) {
			return;
		} else if (leftPartOfRule.length() == 0) {
			return;
		} else {
			String current = null;
			String rest = null;
			if (leftPartOfRule.contains("^")) {
				current = leftPartOfRule.split("\\^")[0];
				rest = leftPartOfRule
						.substring(leftPartOfRule.indexOf("^") + 1);
			} else {
				current = leftPartOfRule;
				rest = "";
			}
			backWardChainingOr(knowledgeBase,
					substitution(thetaAsTextBookString, current),
					thetaAsTextBookString);
			backWardChainingAnd(knowledgeBase, rest, thetaAsTextBookString);
		}
	}

	private static String substitution(
			Map<String, String> thetaAsTextBookString, String current) {
		for(Map.Entry<String, String> entry : thetaAsTextBookString.entrySet())
		{
			if(current.contains(entry.getKey()))
			{
				current= current.replaceAll(entry.getKey(), entry.getValue());
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
