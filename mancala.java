import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

public class mancala {

	static int typeOfGame;
	static int whatPlayerIAm;
	static int depthOfGame;
	static ArrayList<Integer> player2GameState = new ArrayList<Integer>();
	static ArrayList<Integer> player1GameState = new ArrayList<Integer>();
	static int stonesInPlayer2Mancala;
	static int stonesInPlayer1Mancala;
	static int[] actualGameState;
	static mancala tempObject = new mancala();
	static EvaluatingItem evaluatingItem = tempObject.new EvaluatingItem();
	static ArrayList<EvaluatingItem> differenceValuesList = new ArrayList<EvaluatingItem>();
	static Stack<MinmaxStateItem> stack = new Stack<mancala.MinmaxStateItem>();
	static int alpha = Integer.MIN_VALUE;
	static int beta = Integer.MAX_VALUE;
	static PrintWriter minmaxTraverseWriter = null;
	static PrintWriter alphabetaTraverseWriter = null;

	public static void main(String[] args) {
		File file;
		file = new File("next_state.txt");
		try {
			file.createNewFile();
			file = new File("traverse_log.txt");
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (args.length > 1) {
			try {
				BufferedReader bufferedLineReader = new BufferedReader(
						new FileReader(args[1]));
				String inputLine;
				while ((inputLine = bufferedLineReader.readLine()) != null) {
					typeOfGame = Integer.parseInt(inputLine);
					whatPlayerIAm = Integer.parseInt(bufferedLineReader
							.readLine());
					depthOfGame = Integer.parseInt(bufferedLineReader
							.readLine());
					String[] temp;
					temp = bufferedLineReader.readLine().split(" ");
					for (int i = 0; i < temp.length; i++) {
						player2GameState.add(Integer.parseInt(temp[i]));
					}
					temp = bufferedLineReader.readLine().split(" ");
					for (int i = 0; i < temp.length; i++) {
						player1GameState.add(Integer.parseInt(temp[i]));
					}
					stonesInPlayer2Mancala = Integer
							.parseInt(bufferedLineReader.readLine());
					stonesInPlayer1Mancala = Integer
							.parseInt(bufferedLineReader.readLine());
				}
				bufferedLineReader.close();
			} catch (Exception e) {

			}
		}
		actualGameState = gameArrayConstructor(player1GameState,
				stonesInPlayer1Mancala, player2GameState,
				stonesInPlayer2Mancala);
		switch (typeOfGame) {
		case 1:
			greedyMancala(actualGameState);
			break;
		case 2:
			try {
				minmaxTraverseWriter = new PrintWriter(new BufferedWriter(
						new FileWriter("traverse_log.txt", false)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			minMax(actualGameState);
			minmaxTraverseWriter.close();

			break;
		case 3:
			try {
				alphabetaTraverseWriter = new PrintWriter(new BufferedWriter(
						new FileWriter("traverse_log.txt", false)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			alpha_beta_pruning(actualGameState);
			alphabetaTraverseWriter.close();
			break;
		case 4:
			break;
		}

	}

	private static void minMax(int[] actualGameState) {
		int depth = 0;
		String player2 = "", player1 = "", player2Mancala = null, player1Mancala = null;
		MinmaxStateItem minmaxStateItem = tempObject.new MinmaxStateItem();
		minmaxStateItem.boradInThisState = new int[actualGameState.length];
		minmaxStateItem.boradInThisState = actualGameState.clone();
		minmaxStateItem.depth = depth;
		minmaxStateItem.evaluateMinOrMax = 1;
		minmaxStateItem.minMaxValue = Integer.MIN_VALUE;
		minmaxStateItem.parent_detils = null;
		minmaxStateItem.player_Name = "root";
		if (isPlayerBoardEmpty(actualGameState)
				&& isPlayer2BoardEmpty(actualGameState)) {
			minmaxStateItem.nextBestState = new int[actualGameState.length];
			minmaxStateItem.nextBestState = actualGameState.clone();

		}
		if (whatPlayerIAm == 1) {
			if (isPlayerBoardEmpty(actualGameState)) {
				for (int j = actualGameState.length / 2 + 1; j < actualGameState.length; j++) {
					actualGameState[0] = actualGameState[0]
							+ actualGameState[j];
					actualGameState[j] = 0;

				}
				minmaxStateItem.nextBestState = new int[actualGameState.length];
				minmaxStateItem.nextBestState = actualGameState.clone();
			}
			if (isPlayer2BoardEmpty(actualGameState)) {
				for (int j = 1; j < actualGameState.length / 2; j++) {
					actualGameState[actualGameState.length / 2] = actualGameState[actualGameState.length / 2]
							+ actualGameState[j];
					actualGameState[j] = 0;
				}
				minmaxStateItem.nextBestState = new int[actualGameState.length];
				minmaxStateItem.nextBestState = actualGameState.clone();
			}
		} else {
			if (isPlayerBoardEmpty(actualGameState)) {
				for (int j = actualGameState.length / 2 + 1; j < actualGameState.length; j++) {
					actualGameState[0] = actualGameState[0]
							+ actualGameState[j];
					actualGameState[j] = 0;

				}
				minmaxStateItem.nextBestState = new int[actualGameState.length];
				minmaxStateItem.nextBestState = actualGameState.clone();
			}
			if (isPlayer2BoardEmpty(actualGameState)) {
				for (int j = 1; j < actualGameState.length / 2; j++) {
					actualGameState[actualGameState.length / 2] = actualGameState[actualGameState.length / 2]
							+ actualGameState[j];
					actualGameState[j] = 0;
				}
				minmaxStateItem.nextBestState = new int[actualGameState.length];
				minmaxStateItem.nextBestState = actualGameState.clone();
			}

		}
		stack.push(minmaxStateItem);
		minmaxTraverseWriter.println("Node" + "," + "Depth" + "," + "Value");
		writeMinMaxTraverseLogToFile(minmaxStateItem.player_Name + ","
				+ minmaxStateItem.depth + "," + minmaxStateItem.minMaxValue);

		if (whatPlayerIAm == 1) {
			minPlayer1(depth + 1, actualGameState, minmaxStateItem);
		} else {
			minPlayer2(depth + 1, actualGameState, minmaxStateItem);
		}
		minmaxStateItem = stack.pop();
		if(minmaxStateItem.nextBestState == null)
		{
			minmaxStateItem.nextBestState = new int[actualGameState.length];
			minmaxStateItem.nextBestState = actualGameState.clone();
		}
		for (int i = minmaxStateItem.nextBestState.length - 1; i > minmaxStateItem.nextBestState.length / 2; i--) {
			player2 += Integer.toString(minmaxStateItem.nextBestState[i]) + " ";
		}
		for (int i = 1; i < minmaxStateItem.nextBestState.length / 2; i++) {

			player1 += Integer.toString(minmaxStateItem.nextBestState[i]) + " ";

		}
		player2Mancala = Integer.toString(minmaxStateItem.nextBestState[0]);
		player1Mancala = Integer
				.toString(minmaxStateItem.nextBestState[minmaxStateItem.nextBestState.length / 2]);
		try {
			PrintWriter fileWriter = new PrintWriter(new BufferedWriter(
					new FileWriter("next_state.txt", false)));
			/*
			 * String temp = new StringBuilder(player2).reverse().toString()
			 * .trim();
			 */
			fileWriter.println(player2);
			fileWriter.println(player1);
			fileWriter.println(player2Mancala);
			fileWriter.println(player1Mancala);

			// System.out.println(player2);
			// System.out.println(player1);
			// System.out.println(player2Mancala);
			// System.out.println(player1Mancala);
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void writeMinMaxTraverseLogToFile(String traverseString) {
		String[] split = traverseString.split(",");
		String splitString = split[2];
		int parseInt = Integer.parseInt(splitString);
		if (parseInt == Integer.MIN_VALUE) {
			splitString = "-Infinity";
		} else if (parseInt == Integer.MAX_VALUE) {
			splitString = "Infinity";
		}
		split[2] = splitString;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < split.length; ++i) {
			if (i < 2) {
				sb.append(split[i]).append(",");
			} else {
				sb.append(split[i]);
			}
		}
		minmaxTraverseWriter.println(sb);

	}

	private static void alpha_beta_pruning(int[] actualGameState) {
		int depth = 0;
		String player2 = "", player1 = "", player2Mancala = null, player1Mancala = null;
		MinmaxStateItem minmaxStateItem = tempObject.new MinmaxStateItem();
		minmaxStateItem.boradInThisState = new int[actualGameState.length];
		minmaxStateItem.boradInThisState = actualGameState.clone();
		minmaxStateItem.depth = depth;
		minmaxStateItem.evaluateMinOrMax = 1;
		minmaxStateItem.minMaxValue = Integer.MIN_VALUE;
		minmaxStateItem.parent_detils = null;
		minmaxStateItem.player_Name = "root";
		if (isPlayerBoardEmpty(actualGameState)
				&& isPlayer2BoardEmpty(actualGameState)) {
			minmaxStateItem.nextBestState = new int[actualGameState.length];
			minmaxStateItem.nextBestState = actualGameState.clone();

		}
		if (whatPlayerIAm == 1) {
			if (isPlayerBoardEmpty(actualGameState)) {
				for (int j = actualGameState.length / 2 + 1; j < actualGameState.length; j++) {
					actualGameState[0] = actualGameState[0]
							+ actualGameState[j];
					actualGameState[j] = 0;

				}
				minmaxStateItem.nextBestState = new int[actualGameState.length];
				minmaxStateItem.nextBestState = actualGameState.clone();
			}
			if (isPlayer2BoardEmpty(actualGameState)) {
				for (int j = 1; j < actualGameState.length / 2; j++) {
					actualGameState[actualGameState.length / 2] = actualGameState[actualGameState.length / 2]
							+ actualGameState[j];
					actualGameState[j] = 0;
				}
				minmaxStateItem.nextBestState = new int[actualGameState.length];
				minmaxStateItem.nextBestState = actualGameState.clone();
			}
		} else {
			if (isPlayerBoardEmpty(actualGameState)) {
				for (int j = actualGameState.length / 2 + 1; j < actualGameState.length; j++) {
					actualGameState[0] = actualGameState[0]
							+ actualGameState[j];
					actualGameState[j] = 0;

				}
				minmaxStateItem.nextBestState = new int[actualGameState.length];
				minmaxStateItem.nextBestState = actualGameState.clone();
			}
			if (isPlayer2BoardEmpty(actualGameState)) {
				for (int j = 1; j < actualGameState.length / 2; j++) {
					actualGameState[actualGameState.length / 2] = actualGameState[actualGameState.length / 2]
							+ actualGameState[j];
					actualGameState[j] = 0;
				}
				minmaxStateItem.nextBestState = new int[actualGameState.length];
				minmaxStateItem.nextBestState = actualGameState.clone();
			}

		}
		stack.push(minmaxStateItem);
		alphabetaTraverseWriter.println("Node" + "," + "Depth" + "," + "Value"
				+ "," + "Alpha" + "," + "Beta");
		alphabetaTraverseWriter(minmaxStateItem.player_Name + ","
				+ minmaxStateItem.depth + "," + minmaxStateItem.minMaxValue
				+ "," + alpha + "," + beta);
		if (whatPlayerIAm == 1) {
			alphabetaMinPlayer1(depth + 1, actualGameState, minmaxStateItem);
		} else {
			alphabetaMinPlayer2(depth + 1, actualGameState, minmaxStateItem);
		}
		minmaxStateItem = stack.pop();
		if(minmaxStateItem.nextBestState == null)
		{
			minmaxStateItem.nextBestState = new int[actualGameState.length];
			minmaxStateItem.nextBestState = actualGameState.clone();
		}
		for (int i = minmaxStateItem.nextBestState.length - 1; i > minmaxStateItem.nextBestState.length / 2; i--) {
			player2 += Integer.toString(minmaxStateItem.nextBestState[i]) + " ";
		}
		for (int i = 1; i < minmaxStateItem.nextBestState.length / 2; i++) {

			player1 += Integer.toString(minmaxStateItem.nextBestState[i]) + " ";

		}
		player2Mancala = Integer.toString(minmaxStateItem.nextBestState[0]);
		player1Mancala = Integer
				.toString(minmaxStateItem.nextBestState[minmaxStateItem.nextBestState.length / 2]);
		try {
			PrintWriter fileWriter = new PrintWriter(new BufferedWriter(
					new FileWriter("next_state.txt", false)));
			/*
			 * String temp = new StringBuilder(player2).reverse().toString()
			 * .trim();
			 */
			fileWriter.println(player2);
			fileWriter.println(player1);
			fileWriter.println(player2Mancala);
			fileWriter.println(player1Mancala);

			// System.out.println(player2);
			// System.out.println(player1);
			// System.out.println(player2Mancala);
			// System.out.println(player1Mancala);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void alphabetaTraverseWriter(String traverseString) {
		String[] split = traverseString.split(",");
		String splitString = split[2];
		int parseInt = Integer.parseInt(splitString);
		if (parseInt == Integer.MIN_VALUE) {
			splitString = "-Infinity";
		} else if (parseInt == Integer.MAX_VALUE) {
			splitString = "Infinity";
		}
		split[2] = splitString;
		String alphaString = split[3];
		int parseIntalpha = Integer.parseInt(alphaString);
		if (parseIntalpha == Integer.MIN_VALUE) {
			alphaString = "-Infinity";
		} else if (parseIntalpha == Integer.MAX_VALUE) {
			alphaString = "Infinity";
		}
		split[3] = alphaString;
		String betaString = split[4];
		int parseIntbeta = Integer.parseInt(betaString);
		if (parseIntbeta == Integer.MIN_VALUE) {
			betaString = "-Infinity";
		} else if (parseIntbeta == Integer.MAX_VALUE) {
			betaString = "Infinity";
		}
		split[4] = betaString;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < split.length; ++i) {
			if (i < 4) {
				sb.append(split[i]).append(",");
			} else {
				sb.append(split[i]);
			}
		}
		alphabetaTraverseWriter.println(sb);
	}

	private static void alphabetaMinPlayer2(int depth,
			int[] actualGameStateForEvaluation, MinmaxStateItem parent) {
		int stonesTobeMoved = 0;
		int differenceOfStonesInMancala = 0;
		boolean prune = false;
		for (int i = ((actualGameStateForEvaluation.length) - 1); i > actualGameStateForEvaluation.length / 2; i--) {
			int[] tempGameState = new int[actualGameStateForEvaluation.length];
			tempGameState = actualGameStateForEvaluation.clone();

			if (isPlayer2BoardEmpty(tempGameState)) {
				for (int j = 1; j < tempGameState.length / 2; j++) {
					tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
							+ tempGameState[j];
					tempGameState[j] = 0;

				}
				differenceOfStonesInMancala = tempGameState[0]
						- tempGameState[tempGameState.length / 2];
			}
			if (isPlayerBoardEmpty(tempGameState)) {
				for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
					tempGameState[0] = tempGameState[0] + tempGameState[j];
					tempGameState[j] = 0;

				}
				differenceOfStonesInMancala = tempGameState[0]
						- tempGameState[tempGameState.length / 2];
			}

			if (tempGameState[i] > 0) {
				MinmaxStateItem chilNode = tempObject.new MinmaxStateItem();
				chilNode.player_Name = "A" + (tempGameState.length - i + 1);
				stonesTobeMoved = tempGameState[i];
				tempGameState[i] = 0;

				int traverseOverGameState = i + 1;
				while (stonesTobeMoved != 0) {
					if (traverseOverGameState == tempGameState.length) {
						traverseOverGameState = 0;
					}
					if (traverseOverGameState == tempGameState.length / 2) {
						traverseOverGameState++;
					}
					if (stonesTobeMoved != 1) {
						if (traverseOverGameState == tempGameState.length) {
							traverseOverGameState = 0;
						}
						if (traverseOverGameState == tempGameState.length / 2) {
							traverseOverGameState++;
						}
						tempGameState[traverseOverGameState]++;
						traverseOverGameState++;
						stonesTobeMoved--;

					} else {
						if (traverseOverGameState == 0) {
							tempGameState[traverseOverGameState]++;
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
								differenceOfStonesInMancala = tempGameState[0]
										- tempGameState[tempGameState.length / 2];
							}
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
								differenceOfStonesInMancala = tempGameState[0]
										- tempGameState[tempGameState.length / 2];
							}
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 1;
							} else {
								chilNode.evaluateMinOrMax = 0;
							}

							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							/*
							 * if (depth == depthOfGame) { chilNode.minMaxValue
							 * = tempGameState[0] -
							 * tempGameState[tempGameState.length / 2]; }
							 */
							chilNode.alpha = parent.alpha;
							chilNode.beta = parent.beta;
							chilNode.parent_detils = parent;
							if (isPlayer2BoardEmpty(tempGameState)) {
								if (depth <= depthOfGame) {

									alphabetaTraverseWriter(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue
											+ ","
											+ chilNode.alpha
											+ ","
											+ chilNode.beta);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);

							alphabetaTraverseWriter(chilNode.player_Name + ","
									+ chilNode.depth + ","
									+ chilNode.minMaxValue + ","
									+ chilNode.alpha + "," + chilNode.beta);
							alphabetaMinPlayer2(depth, tempGameState, chilNode);
							stonesTobeMoved--;
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue >= parentNode.beta) {
										prune = true;
									} else {
										if (childNode.minMaxValue >= parentNode.alpha) {
											parentNode.alpha = childNode.minMaxValue;
										}
									}
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {

								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue <= parentNode.alpha) {
										prune = true;
									} else {
										if (childNode.minMaxValue <= parentNode.beta)
											parentNode.beta = childNode.minMaxValue;
									}
								}
							}
							stack.push(parentNode);

							alphabetaTraverseWriter(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue + ","
									+ parentNode.alpha + "," + parentNode.beta);
							if (prune == true) {
								return;
							}
							continue;

						} else if (tempGameState[traverseOverGameState] == 0
								&& traverseOverGameState > (tempGameState.length / 2)) {
							tempGameState[traverseOverGameState]++;
							int opponentsOppIndex = tempGameState.length
									- traverseOverGameState;
							/* if (tempGameState[opponentsOppIndex] != 0) { */
							tempGameState[0] = tempGameState[0]
									+ tempGameState[opponentsOppIndex]
									+ tempGameState[traverseOverGameState];
							tempGameState[traverseOverGameState] = 0;
							tempGameState[opponentsOppIndex] = 0;
							stonesTobeMoved--;
							/*
							 * } else { stonesTobeMoved--; }
							 */
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 0;
							} else {
								chilNode.evaluateMinOrMax = 1;
							}
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.alpha = parent.alpha;
							chilNode.beta = parent.beta;
							chilNode.parent_detils = parent;
							differenceOfStonesInMancala = tempGameState[0]
									- tempGameState[tempGameState.length / 2];

							if (isPlayer2BoardEmpty(tempGameState)) {
								if (depth < depthOfGame) {

									alphabetaTraverseWriter(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue
											+ ","
											+ chilNode.alpha
											+ ","
											+ chilNode.beta);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							if (chilNode.depth == depthOfGame) {
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);

							alphabetaTraverseWriter(chilNode.player_Name + ","
									+ chilNode.depth + ","
									+ chilNode.minMaxValue + ","
									+ chilNode.alpha + "," + chilNode.beta);
						} else {
							tempGameState[traverseOverGameState]++;
							stonesTobeMoved--;

							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 0;
							} else {
								chilNode.evaluateMinOrMax = 1;
							}
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.alpha = parent.alpha;
							chilNode.beta = parent.beta;
							chilNode.parent_detils = parent;
							differenceOfStonesInMancala = tempGameState[0]
									- tempGameState[tempGameState.length / 2];
							if (isPlayer2BoardEmpty(tempGameState)) {
								if (depth < depthOfGame) {

									alphabetaTraverseWriter(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue
											+ ","
											+ chilNode.alpha
											+ ","
											+ chilNode.beta);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							if (chilNode.depth == depthOfGame) {
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);

							alphabetaTraverseWriter(chilNode.player_Name + ","
									+ chilNode.depth + ","
									+ chilNode.minMaxValue + ","
									+ chilNode.alpha + "," + chilNode.beta);
						}
						if (depth < depthOfGame) {
							alphabetaMaxPlayer1(depth + 1, tempGameState,
									chilNode);
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue >= parentNode.beta) {
										prune = true;
									} else {
										if (childNode.minMaxValue >= parentNode.alpha) {
											parentNode.alpha = childNode.minMaxValue;
										}
									}
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {

								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue <= parentNode.alpha) {
										prune = true;
									} else {
										if (childNode.minMaxValue <= parentNode.beta)
											parentNode.beta = childNode.minMaxValue;
									}
								}
							}
							stack.push(parentNode);

							alphabetaTraverseWriter(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue + ","
									+ parentNode.alpha + "," + parentNode.beta);
							if (prune == true) {
								return;
							}

						} else {
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue >= parentNode.beta) {
										prune = true;
									} else {
										if (childNode.minMaxValue >= parentNode.alpha) {
											parentNode.alpha = childNode.minMaxValue;
										}
									}
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {

								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue <= parentNode.alpha) {
										prune = true;
									} else {
										if (childNode.minMaxValue <= parentNode.beta)
											parentNode.beta = childNode.minMaxValue;
									}
								}
							}
							stack.push(parentNode);

							alphabetaTraverseWriter(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue + ","
									+ parentNode.alpha + "," + parentNode.beta);
							if (prune == true) {
								return;
							}

						}
					}

				}
			}

		}
		return;
	}

	private static void alphabetaMaxPlayer1(int depth,
			int[] actualGameStateForEvaluation, MinmaxStateItem parent) {

		int stonesTobeMoved = 0;
		int differenceOfStonesInMancala = 0;
		boolean prune = false;
		for (int i = 1; i < (actualGameStateForEvaluation.length / 2); i++) {
			int[] tempGameState = new int[actualGameStateForEvaluation.length];
			tempGameState = actualGameStateForEvaluation.clone();

			if (isPlayerBoardEmpty(tempGameState)) {
				for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
					tempGameState[0] = tempGameState[0] + tempGameState[j];
					tempGameState[j] = 0;

				}
				differenceOfStonesInMancala = tempGameState[0]
						- tempGameState[tempGameState.length / 2];
			}
			if (isPlayer2BoardEmpty(tempGameState)) {
				for (int j = 1; j < tempGameState.length / 2; j++) {
					tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
							+ tempGameState[j];
					tempGameState[j] = 0;

				}

				differenceOfStonesInMancala = tempGameState[0]
						- tempGameState[tempGameState.length / 2];

			}

			if (tempGameState[i] > 0) {
				MinmaxStateItem chilNode = tempObject.new MinmaxStateItem();
				chilNode.player_Name = "B" + (i + 1);

				stonesTobeMoved = tempGameState[i];
				tempGameState[i] = 0;

				int traverseOverGameState = i + 1;
				while (stonesTobeMoved != 0) {
					if (traverseOverGameState == tempGameState.length) {
						traverseOverGameState = 1;
					}
					if (stonesTobeMoved != 1) {
						if (traverseOverGameState == tempGameState.length) {
							traverseOverGameState = 1;
						}
						tempGameState[traverseOverGameState]++;
						traverseOverGameState++;
						stonesTobeMoved--;

					} else {
						if (traverseOverGameState == (tempGameState.length / 2)) {
							tempGameState[traverseOverGameState]++;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 1;
							} else {
								chilNode.evaluateMinOrMax = 0;
							}
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
								differenceOfStonesInMancala = tempGameState[0]
										- tempGameState[tempGameState.length / 2];
							}
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;
								}
								differenceOfStonesInMancala = tempGameState[0]
										- tempGameState[tempGameState.length / 2];
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							/*
							 * if (depth == depthOfGame) { chilNode.minMaxValue
							 * = tempGameState[0] -
							 * tempGameState[tempGameState.length / 2]; }
							 */
							chilNode.alpha = parent.alpha;
							chilNode.beta = parent.beta;
							chilNode.parent_detils = parent;
							if (isPlayerBoardEmpty(tempGameState)) {
								if (depth <= depthOfGame) {

									alphabetaTraverseWriter(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue
											+ ","
											+ chilNode.alpha
											+ ","
											+ chilNode.beta);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);

							alphabetaTraverseWriter(chilNode.player_Name + ","
									+ chilNode.depth + ","
									+ chilNode.minMaxValue + ","
									+ chilNode.alpha + "," + chilNode.beta);
							alphabetaMaxPlayer1(depth, tempGameState, chilNode);
							stonesTobeMoved--;
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue >= parentNode.beta) {
										prune = true;
									} else {
										if (childNode.minMaxValue >= parentNode.alpha) {
											parentNode.alpha = childNode.minMaxValue;
										}
									}
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {

								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue <= parentNode.alpha) {
										prune = true;
									} else {
										if (childNode.minMaxValue <= parentNode.beta)
											parentNode.beta = childNode.minMaxValue;
									}
								}
							}
							stack.push(parentNode);

							alphabetaTraverseWriter(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue + ","
									+ parentNode.alpha + "," + parentNode.beta);
							if (prune == true) {
								return;
							}
							continue;

						} else if (tempGameState[traverseOverGameState] == 0
								&& traverseOverGameState < (tempGameState.length / 2)) {
							tempGameState[traverseOverGameState]++;
							int opponentsOppIndex = tempGameState.length
									- traverseOverGameState;
							/* if (tempGameState[opponentsOppIndex] != 0) { */
							tempGameState[tempGameState.length / 2] = tempGameState[tempGameState.length / 2]
									+ tempGameState[opponentsOppIndex]
									+ tempGameState[traverseOverGameState];
							tempGameState[traverseOverGameState] = 0;
							tempGameState[opponentsOppIndex] = 0;
							stonesTobeMoved--;
							/*
							 * } else { stonesTobeMoved--; }
							 */
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;
								}
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 0;
							} else {
								chilNode.evaluateMinOrMax = 1;
							}
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.alpha = parent.alpha;
							chilNode.beta = parent.beta;
							chilNode.parent_detils = parent;
							differenceOfStonesInMancala = tempGameState[0]
									- tempGameState[tempGameState.length / 2];
							if (isPlayerBoardEmpty(tempGameState)) {
								if (depth < depthOfGame) {

									alphabetaTraverseWriter(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue
											+ ","
											+ chilNode.alpha
											+ ","
											+ chilNode.beta);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							if (chilNode.depth == depthOfGame) {
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);

							alphabetaTraverseWriter(chilNode.player_Name + ","
									+ chilNode.depth + ","
									+ chilNode.minMaxValue + ","
									+ chilNode.alpha + "," + chilNode.beta);
						} else {
							tempGameState[traverseOverGameState]++;
							stonesTobeMoved--;
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;
								}
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 0;
							} else {
								chilNode.evaluateMinOrMax = 1;
							}
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.alpha = parent.alpha;
							chilNode.beta = parent.beta;
							chilNode.parent_detils = parent;
							differenceOfStonesInMancala = tempGameState[0]
									- tempGameState[tempGameState.length / 2];
							if (isPlayerBoardEmpty(tempGameState)) {
								if (depth < depthOfGame) {

									alphabetaTraverseWriter(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue
											+ ","
											+ chilNode.alpha
											+ ","
											+ chilNode.beta);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							if (chilNode.depth == depthOfGame) {
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);

							alphabetaTraverseWriter(chilNode.player_Name + ","
									+ chilNode.depth + ","
									+ chilNode.minMaxValue + ","
									+ chilNode.alpha + "," + chilNode.beta);
						}
						if (depth < depthOfGame) {
							alphabetaMinPlayer2(depth + 1, tempGameState,
									chilNode);
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue >= parentNode.beta) {
										prune = true;
									} else {
										if (childNode.minMaxValue >= parentNode.alpha) {
											parentNode.alpha = childNode.minMaxValue;
										}
									}
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {

								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue <= parentNode.alpha) {
										prune = true;
									} else {
										if (childNode.minMaxValue <= parentNode.beta)
											parentNode.beta = childNode.minMaxValue;
									}
								}
							}
							stack.push(parentNode);

							alphabetaTraverseWriter(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue + ","
									+ parentNode.alpha + "," + parentNode.beta);
							if (prune == true) {
								return;
							}
						} else {

							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue >= parentNode.beta) {
										prune = true;
									} else {
										if (childNode.minMaxValue >= parentNode.alpha) {
											parentNode.alpha = childNode.minMaxValue;
										}
									}
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {

								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue <= parentNode.alpha) {
										prune = true;
									} else {
										if (childNode.minMaxValue <= parentNode.beta)
											parentNode.beta = childNode.minMaxValue;
									}
								}
							}
							stack.push(parentNode);

							alphabetaTraverseWriter(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue + ","
									+ parentNode.alpha + "," + parentNode.beta);
							if (prune == true) {
								return;
							}

						}
					}
				}

			}
		}

		return;

	}

	private static void alphabetaMinPlayer1(int depth,
			int[] actualGameStateForEvaluation, MinmaxStateItem parent) {

		int stonesTobeMoved = 0;
		int differenceOfStonesInMancala = 0;
		boolean prune = false;
		for (int i = 1; i < (actualGameStateForEvaluation.length / 2); i++) {
			int[] tempGameState = new int[actualGameStateForEvaluation.length];
			tempGameState = actualGameStateForEvaluation.clone();

			if (isPlayerBoardEmpty(tempGameState)) {
				for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
					tempGameState[0] = tempGameState[0] + tempGameState[j];
					tempGameState[j] = 0;

				}
				differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
						- tempGameState[0];
			}
			if (isPlayer2BoardEmpty(tempGameState)) {
				for (int j = 1; j < tempGameState.length / 2; j++) {
					tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
							+ tempGameState[j];
					tempGameState[j] = 0;
				}
				differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
						- tempGameState[0];
			}

			if (tempGameState[i] > 0) {
				MinmaxStateItem chilNode = tempObject.new MinmaxStateItem();
				chilNode.player_Name = "B" + (i + 1);
				stonesTobeMoved = tempGameState[i];
				tempGameState[i] = 0;

				int traverseOverGameState = i + 1;
				while (stonesTobeMoved != 0) {
					if (traverseOverGameState == tempGameState.length) {
						traverseOverGameState = 1;
					}
					if (stonesTobeMoved != 1) {
						tempGameState[traverseOverGameState]++;
						traverseOverGameState++;
						stonesTobeMoved--;

					} else {
						if (traverseOverGameState == (tempGameState.length / 2)) {
							tempGameState[traverseOverGameState]++;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 1;
							} else {
								chilNode.evaluateMinOrMax = 0;
							}
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
								differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
										- tempGameState[0];
							}
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;
								}
								differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
										- tempGameState[0];
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							chilNode.alpha = parent.alpha;
							chilNode.beta = parent.beta;
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							/*
							 * if (depth == depthOfGame) { chilNode.minMaxValue
							 * = tempGameState[0] -
							 * tempGameState[tempGameState.length / 2]; }
							 */
							if (isPlayerBoardEmpty(tempGameState)) {
								if (depth <= depthOfGame) {

									alphabetaTraverseWriter(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue
											+ ","
											+ chilNode.alpha
											+ ","
											+ chilNode.beta);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							chilNode.parent_detils = parent;
							stack.push(chilNode);

							alphabetaTraverseWriter(chilNode.player_Name + ","
									+ chilNode.depth + ","
									+ chilNode.minMaxValue + ","
									+ chilNode.alpha + "," + chilNode.beta);
							alphabetaMinPlayer1(depth, tempGameState, chilNode);
							stonesTobeMoved--;
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue >= parentNode.beta) {
										prune = true;
									} else {
										if (childNode.minMaxValue >= parentNode.alpha) {
											parentNode.alpha = childNode.minMaxValue;
										}
									}
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {

								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue <= parentNode.alpha) {
										prune = true;
									} else {
										if (childNode.minMaxValue <= parentNode.beta)
											parentNode.beta = childNode.minMaxValue;
									}
								}
							}
							stack.push(parentNode);

							alphabetaTraverseWriter(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue + ","
									+ parentNode.alpha + "," + parentNode.beta);
							if (prune == true) {
								return;
							}
							continue;

						} else if (tempGameState[traverseOverGameState] == 0
								&& traverseOverGameState < (tempGameState.length / 2)) {
							tempGameState[traverseOverGameState]++;
							int opponentsOppIndex = tempGameState.length
									- traverseOverGameState;
							/* if (tempGameState[opponentsOppIndex] != 0) { */
							tempGameState[tempGameState.length / 2] = tempGameState[tempGameState.length / 2]
									+ tempGameState[opponentsOppIndex]
									+ tempGameState[traverseOverGameState];
							tempGameState[traverseOverGameState] = 0;
							tempGameState[opponentsOppIndex] = 0;
							stonesTobeMoved--;
							/*
							 * } else { stonesTobeMoved--; }
							 */
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;
								}
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 0;
							} else {
								chilNode.evaluateMinOrMax = 1;
							}
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.parent_detils = parent;
							chilNode.alpha = parent.alpha;
							chilNode.beta = parent.beta;
							differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
									- tempGameState[0];
							if (isPlayerBoardEmpty(tempGameState)) {
								if (depth < depthOfGame) {

									alphabetaTraverseWriter(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue
											+ ","
											+ chilNode.alpha
											+ ","
											+ chilNode.beta);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							if (chilNode.depth == depthOfGame) {
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);

							alphabetaTraverseWriter(chilNode.player_Name + ","
									+ chilNode.depth + ","
									+ chilNode.minMaxValue + ","
									+ chilNode.alpha + "," + chilNode.beta);
						} else {
							tempGameState[traverseOverGameState]++;
							stonesTobeMoved--;
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;
								}
								differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
										- tempGameState[0];
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 0;
							} else {
								chilNode.evaluateMinOrMax = 1;
							}
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.parent_detils = parent;
							chilNode.alpha = parent.alpha;
							chilNode.beta = parent.beta;
							differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
									- tempGameState[0];
							if (isPlayerBoardEmpty(tempGameState)) {
								if (depth < depthOfGame) {

									alphabetaTraverseWriter(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue
											+ ","
											+ chilNode.alpha
											+ ","
											+ chilNode.beta);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							if (chilNode.depth == depthOfGame) {
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);

							alphabetaTraverseWriter(chilNode.player_Name + ","
									+ chilNode.depth + ","
									+ chilNode.minMaxValue + ","
									+ chilNode.alpha + "," + chilNode.beta);
						}

						if (depth < depthOfGame) {
							alphabetaMaxPlayer2(depth + 1, tempGameState,
									chilNode);
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue >= parentNode.beta) {
										prune = true;
									} else {
										if (childNode.minMaxValue >= parentNode.alpha) {
											parentNode.alpha = childNode.minMaxValue;
										}
									}
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {

								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue <= parentNode.alpha) {
										prune = true;
									} else {
										if (childNode.minMaxValue <= parentNode.beta)
											parentNode.beta = childNode.minMaxValue;
									}
								}
							}
							stack.push(parentNode);

							alphabetaTraverseWriter(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue + ","
									+ parentNode.alpha + "," + parentNode.beta);
							if (prune == true) {
								return;
							}

						} else {
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue >= parentNode.beta) {
										prune = true;
									} else {
										if (childNode.minMaxValue >= parentNode.alpha) {
											parentNode.alpha = childNode.minMaxValue;
										}
									}
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {

								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue <= parentNode.alpha) {
										prune = true;
									} else {
										if (childNode.minMaxValue <= parentNode.beta)
											parentNode.beta = childNode.minMaxValue;
									}
								}
							}
							stack.push(parentNode);

							alphabetaTraverseWriter(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue + ","
									+ parentNode.alpha + "," + parentNode.beta);
							if (prune == true) {
								return;
							}

						}
					}

				}
			}

		}

		return;
	}

	private static void alphabetaMaxPlayer2(int depth,
			int[] actualGameStateForEvaluation, MinmaxStateItem parent) {
		int stonesTobeMoved = 0;
		int differenceOfStonesInMancala = 0;
		boolean prune = false;
		for (int i = ((actualGameStateForEvaluation.length) - 1); i > actualGameStateForEvaluation.length / 2; i--) {
			int[] tempGameState = new int[actualGameStateForEvaluation.length];
			tempGameState = actualGameStateForEvaluation.clone();

			if (isPlayer2BoardEmpty(tempGameState)) {
				for (int j = 1; j < tempGameState.length / 2; j++) {
					tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
							+ tempGameState[j];
					tempGameState[j] = 0;

				}
				differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
						- tempGameState[0];
			}
			if (isPlayerBoardEmpty(tempGameState)) {
				for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
					tempGameState[0] = tempGameState[0] + tempGameState[j];
					tempGameState[j] = 0;

				}
				differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
						- tempGameState[0];
			}

			if (tempGameState[i] > 0) {
				MinmaxStateItem chilNode = tempObject.new MinmaxStateItem();
				chilNode.player_Name = "A" + (tempGameState.length - i + 1);
				stonesTobeMoved = tempGameState[i];
				tempGameState[i] = 0;

				int traverseOverGameState = i + 1;
				while (stonesTobeMoved != 0) {
					if (traverseOverGameState == tempGameState.length) {
						traverseOverGameState = 0;
					}
					if (traverseOverGameState == tempGameState.length / 2) {
						traverseOverGameState++;
					}
					if (stonesTobeMoved != 1) {
						tempGameState[traverseOverGameState]++;
						traverseOverGameState++;
						stonesTobeMoved--;

					} else {
						if (traverseOverGameState == 0) {
							tempGameState[traverseOverGameState]++;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 1;
							} else {
								chilNode.evaluateMinOrMax = 0;
							}
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
								differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
										- tempGameState[0];
							}
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
								differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
										- tempGameState[0];
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.parent_detils = parent;
							chilNode.alpha = parent.alpha;
							chilNode.beta = parent.beta;
							/*
							 * if (depth == depthOfGame) { chilNode.minMaxValue
							 * = tempGameState[tempGameState.length / 2] -
							 * tempGameState[0]; }
							 */
							if (isPlayer2BoardEmpty(tempGameState)) {
								if (depth <= depthOfGame) {

									alphabetaTraverseWriter(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue
											+ ","
											+ chilNode.alpha
											+ ","
											+ chilNode.beta);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);

							alphabetaTraverseWriter(chilNode.player_Name + ","
									+ chilNode.depth + ","
									+ chilNode.minMaxValue + ","
									+ chilNode.alpha + "," + chilNode.beta);
							alphabetaMaxPlayer2(depth, tempGameState, chilNode);
							stonesTobeMoved--;
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue >= parentNode.beta) {
										prune = true;
									} else {
										if (childNode.minMaxValue >= parentNode.alpha) {
											parentNode.alpha = childNode.minMaxValue;
										}
									}
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {

								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue <= parentNode.alpha) {
										prune = true;
									} else {
										if (childNode.minMaxValue <= parentNode.beta)
											parentNode.beta = childNode.minMaxValue;
									}
								}
							}
							stack.push(parentNode);

							alphabetaTraverseWriter(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue + ","
									+ parentNode.alpha + "," + parentNode.beta);
							if (prune == true) {
								return;
							}
							continue;
						} else if (tempGameState[traverseOverGameState] == 0
								&& traverseOverGameState > (tempGameState.length / 2)) {
							tempGameState[traverseOverGameState]++;
							int opponentsOppIndex = tempGameState.length
									- traverseOverGameState;
							/* if (tempGameState[opponentsOppIndex] != 0) { */
							tempGameState[0] = tempGameState[0]
									+ tempGameState[opponentsOppIndex]
									+ tempGameState[traverseOverGameState];
							tempGameState[traverseOverGameState] = 0;
							tempGameState[opponentsOppIndex] = 0;
							stonesTobeMoved--;
							/*
							 * } else { stonesTobeMoved--; }
							 */
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 0;
							} else {
								chilNode.evaluateMinOrMax = 1;
							}
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.parent_detils = parent;
							chilNode.alpha = parent.alpha;
							chilNode.beta = parent.beta;
							differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
									- tempGameState[0];
							if (isPlayer2BoardEmpty(tempGameState)) {
								if (depth < depthOfGame) {

									alphabetaTraverseWriter(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue
											+ ","
											+ chilNode.alpha
											+ ","
											+ chilNode.beta);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							if (chilNode.depth == depthOfGame) {
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);

							alphabetaTraverseWriter(chilNode.player_Name + ","
									+ chilNode.depth + ","
									+ chilNode.minMaxValue + ","
									+ chilNode.alpha + "," + chilNode.beta);
						} else {
							tempGameState[traverseOverGameState]++;
							stonesTobeMoved--;
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 0;
							} else {
								chilNode.evaluateMinOrMax = 1;
							}
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.alpha = parent.alpha;
							chilNode.beta = parent.beta;
							chilNode.parent_detils = parent;
							differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
									- tempGameState[0];

							if (isPlayer2BoardEmpty(tempGameState)) {
								if (depth < depthOfGame) {
									alphabetaTraverseWriter(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue
											+ ","
											+ chilNode.alpha
											+ ","
											+ chilNode.beta);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							if (chilNode.depth == depthOfGame) {
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);

							alphabetaTraverseWriter(chilNode.player_Name + ","
									+ chilNode.depth + ","
									+ chilNode.minMaxValue + ","
									+ chilNode.alpha + "," + chilNode.beta);
						}
						if (depth < depthOfGame) {
							alphabetaMinPlayer1(depth + 1, tempGameState,
									chilNode);
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue >= parentNode.beta) {
										prune = true;
									} else {
										if (childNode.minMaxValue >= parentNode.alpha) {
											parentNode.alpha = childNode.minMaxValue;
										}
									}
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {

								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue <= parentNode.alpha) {
										prune = true;
									} else {
										if (childNode.minMaxValue <= parentNode.beta)
											parentNode.beta = childNode.minMaxValue;
									}
								}
							}
							stack.push(parentNode);

							alphabetaTraverseWriter(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue + ","
									+ parentNode.alpha + "," + parentNode.beta);
							if (prune == true) {
								return;
							}

						} else {
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue >= parentNode.beta) {
										prune = true;
									} else {
										if (childNode.minMaxValue >= parentNode.alpha) {
											parentNode.alpha = childNode.minMaxValue;
										}
									}
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {

								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (childNode.minMaxValue <= parentNode.alpha) {
										prune = true;
									} else {
										if (childNode.minMaxValue <= parentNode.beta)
											parentNode.beta = childNode.minMaxValue;
									}
								}
							}
							stack.push(parentNode);
							alphabetaTraverseWriter(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue + ","
									+ parentNode.alpha + "," + parentNode.beta);
							if (prune == true) {
								return;
							}

						}
					}
					//
				}
			}

		}
		return;
	}

	private static int[] minPlayer2(int depth,
			int[] actualGameStateForEvaluation, MinmaxStateItem parent) {
		int stonesTobeMoved = 0;
		int differenceOfStonesInMancala = 0;
		for (int i = ((actualGameStateForEvaluation.length) - 1); i > actualGameStateForEvaluation.length / 2; i--) {
			int[] tempGameState = new int[actualGameStateForEvaluation.length];
			tempGameState = actualGameStateForEvaluation.clone();

			if (isPlayer2BoardEmpty(tempGameState)) {
				for (int j = 1; j < tempGameState.length / 2; j++) {
					tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
							+ tempGameState[j];
					tempGameState[j] = 0;

				}
				differenceOfStonesInMancala = tempGameState[0]
						- tempGameState[tempGameState.length / 2];
			}
			if (isPlayerBoardEmpty(tempGameState)) {
				for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
					tempGameState[0] = tempGameState[0] + tempGameState[j];
					tempGameState[j] = 0;

				}
				differenceOfStonesInMancala = tempGameState[0]
						- tempGameState[tempGameState.length / 2];
			}

			if (tempGameState[i] > 0) {
				MinmaxStateItem chilNode = tempObject.new MinmaxStateItem();
				chilNode.player_Name = "A" + (tempGameState.length - i + 1);
				stonesTobeMoved = tempGameState[i];
				tempGameState[i] = 0;

				int traverseOverGameState = i + 1;
				while (stonesTobeMoved != 0) {
					if (traverseOverGameState == tempGameState.length) {
						traverseOverGameState = 0;
					}
					if (traverseOverGameState == tempGameState.length / 2) {
						traverseOverGameState++;
					}
					if (stonesTobeMoved != 1) {
						if (traverseOverGameState == tempGameState.length) {
							traverseOverGameState = 0;
						}
						if (traverseOverGameState == tempGameState.length / 2) {
							traverseOverGameState++;
						}
						tempGameState[traverseOverGameState]++;
						traverseOverGameState++;
						stonesTobeMoved--;

					} else {
						if (traverseOverGameState == 0) {
							tempGameState[traverseOverGameState]++;
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
								differenceOfStonesInMancala = tempGameState[0]
										- tempGameState[tempGameState.length / 2];
							}
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
								differenceOfStonesInMancala = tempGameState[0]
										- tempGameState[tempGameState.length / 2];
							}
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 1;
							} else {
								chilNode.evaluateMinOrMax = 0;
							}

							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							/*
							 * if (depth == depthOfGame) { chilNode.minMaxValue
							 * = tempGameState[0] -
							 * tempGameState[tempGameState.length / 2]; }
							 */
							chilNode.parent_detils = parent;
							if (isPlayer2BoardEmpty(tempGameState)) {
								if (depth <= depthOfGame) {
									writeMinMaxTraverseLogToFile(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);
							writeMinMaxTraverseLogToFile(chilNode.player_Name
									+ "," + chilNode.depth + ","
									+ chilNode.minMaxValue);
							minPlayer2(depth, tempGameState, chilNode);
							stonesTobeMoved--;
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {
								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
								}
							}
							stack.push(parentNode);
							writeMinMaxTraverseLogToFile(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue);

							continue;

						} else if (tempGameState[traverseOverGameState] == 0
								&& traverseOverGameState > (tempGameState.length / 2)) {
							tempGameState[traverseOverGameState]++;
							int opponentsOppIndex = tempGameState.length
									- traverseOverGameState;
							/* if (tempGameState[opponentsOppIndex] != 0) { */
							tempGameState[0] = tempGameState[0]
									+ tempGameState[opponentsOppIndex]
									+ tempGameState[traverseOverGameState];
							tempGameState[traverseOverGameState] = 0;
							tempGameState[opponentsOppIndex] = 0;
							stonesTobeMoved--;
							/*
							 * } else { stonesTobeMoved--; }
							 */
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 0;
							} else {
								chilNode.evaluateMinOrMax = 1;
							}
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.parent_detils = parent;
							differenceOfStonesInMancala = tempGameState[0]
									- tempGameState[tempGameState.length / 2];
							if (isPlayer2BoardEmpty(tempGameState)) {
								if (depth < depthOfGame) {
									writeMinMaxTraverseLogToFile(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							if (chilNode.depth == depthOfGame) {
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);
							writeMinMaxTraverseLogToFile(chilNode.player_Name
									+ "," + chilNode.depth + ","
									+ chilNode.minMaxValue);

						} else {
							tempGameState[traverseOverGameState]++;
							stonesTobeMoved--;

							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 0;
							} else {
								chilNode.evaluateMinOrMax = 1;
							}
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.parent_detils = parent;
							differenceOfStonesInMancala = tempGameState[0]
									- tempGameState[tempGameState.length / 2];
							if (isPlayer2BoardEmpty(tempGameState)) {
								if (depth < depthOfGame) {
									writeMinMaxTraverseLogToFile(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							if (chilNode.depth == depthOfGame) {
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);
							writeMinMaxTraverseLogToFile(chilNode.player_Name
									+ "," + chilNode.depth + ","
									+ chilNode.minMaxValue);
						}
						if (depth < depthOfGame) {
							maxPlayer1(depth + 1, tempGameState, chilNode);
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {
								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
								}
							}
							stack.push(parentNode);
							writeMinMaxTraverseLogToFile(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue);

						} else {
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {
								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
								}
							}
							stack.push(parentNode);
							writeMinMaxTraverseLogToFile(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue);

						}
					}

				}
			}

		}
		return null;
	}

	private static int[] maxPlayer1(int depth,
			int[] actualGameStateForEvaluation, MinmaxStateItem parent) {

		int stonesTobeMoved = 0;
		int differenceOfStonesInMancala = 0;
		for (int i = 1; i < (actualGameStateForEvaluation.length / 2); i++) {
			int[] tempGameState = new int[actualGameStateForEvaluation.length];
			tempGameState = actualGameStateForEvaluation.clone();

			if (isPlayerBoardEmpty(tempGameState)) {
				for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
					tempGameState[0] = tempGameState[0] + tempGameState[j];
					tempGameState[j] = 0;

				}
				differenceOfStonesInMancala = tempGameState[0]
						- tempGameState[tempGameState.length / 2];
			}
			if (isPlayer2BoardEmpty(tempGameState)) {
				for (int j = 1; j < tempGameState.length / 2; j++) {
					tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
							+ tempGameState[j];
					tempGameState[j] = 0;

				}

				differenceOfStonesInMancala = tempGameState[0]
						- tempGameState[tempGameState.length / 2];

			}

			if (tempGameState[i] > 0) {
				MinmaxStateItem chilNode = tempObject.new MinmaxStateItem();
				chilNode.player_Name = "B" + (i + 1);

				stonesTobeMoved = tempGameState[i];
				tempGameState[i] = 0;

				int traverseOverGameState = i + 1;
				while (stonesTobeMoved != 0) {
					if (traverseOverGameState == tempGameState.length) {
						traverseOverGameState = 1;
					}
					if (stonesTobeMoved != 1) {
						if (traverseOverGameState == tempGameState.length) {
							traverseOverGameState = 1;
						}
						tempGameState[traverseOverGameState]++;
						traverseOverGameState++;
						stonesTobeMoved--;

					} else {
						if (traverseOverGameState == (tempGameState.length / 2)) {
							tempGameState[traverseOverGameState]++;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 1;
							} else {
								chilNode.evaluateMinOrMax = 0;
							}
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
								differenceOfStonesInMancala = tempGameState[0]
										- tempGameState[tempGameState.length / 2];
							}
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;
								}
								differenceOfStonesInMancala = tempGameState[0]
										- tempGameState[tempGameState.length / 2];
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							/*
							 * if (depth == depthOfGame) { chilNode.minMaxValue
							 * = tempGameState[0] -
							 * tempGameState[tempGameState.length / 2]; }
							 */
							chilNode.parent_detils = parent;
							if (isPlayerBoardEmpty(tempGameState)) {
								if (depth <= depthOfGame) {
									writeMinMaxTraverseLogToFile(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);
							writeMinMaxTraverseLogToFile(chilNode.player_Name
									+ "," + chilNode.depth + ","
									+ chilNode.minMaxValue);
							maxPlayer1(depth, tempGameState, chilNode);
							stonesTobeMoved--;
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {
								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
								}
							}
							stack.push(parentNode);
							writeMinMaxTraverseLogToFile(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue);

							continue;

						} else if (tempGameState[traverseOverGameState] == 0
								&& traverseOverGameState < (tempGameState.length / 2)) {
							tempGameState[traverseOverGameState]++;
							int opponentsOppIndex = tempGameState.length
									- traverseOverGameState;
							/* if (tempGameState[opponentsOppIndex] != 0) { */
							tempGameState[tempGameState.length / 2] = tempGameState[tempGameState.length / 2]
									+ tempGameState[opponentsOppIndex]
									+ tempGameState[traverseOverGameState];
							tempGameState[traverseOverGameState] = 0;
							tempGameState[opponentsOppIndex] = 0;
							stonesTobeMoved--;
							/*
							 * } else { stonesTobeMoved--; }
							 */
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;
								}
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 0;
							} else {
								chilNode.evaluateMinOrMax = 1;
							}
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.parent_detils = parent;
							differenceOfStonesInMancala = tempGameState[0]
									- tempGameState[tempGameState.length / 2];
							if (isPlayerBoardEmpty(tempGameState)) {
								if (depth < depthOfGame) {
									writeMinMaxTraverseLogToFile(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							if (chilNode.depth == depthOfGame) {
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);
							writeMinMaxTraverseLogToFile(chilNode.player_Name
									+ "," + chilNode.depth + ","
									+ chilNode.minMaxValue);
						} else {
							tempGameState[traverseOverGameState]++;
							stonesTobeMoved--;
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;
								}
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 0;
							} else {
								chilNode.evaluateMinOrMax = 1;
							}
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.parent_detils = parent;
							differenceOfStonesInMancala = tempGameState[0]
									- tempGameState[tempGameState.length / 2];
							if (isPlayerBoardEmpty(tempGameState)) {
								if (depth < depthOfGame) {
									writeMinMaxTraverseLogToFile(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							if (chilNode.depth == depthOfGame) {
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);
							writeMinMaxTraverseLogToFile(chilNode.player_Name
									+ "," + chilNode.depth + ","
									+ chilNode.minMaxValue);
						}
						if (depth < depthOfGame) {
							minPlayer2(depth + 1, tempGameState, chilNode);
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {
								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
								}
							}
							stack.push(parentNode);
							writeMinMaxTraverseLogToFile(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue);
						} else {

							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {
								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
								}
							}
							stack.push(parentNode);
							writeMinMaxTraverseLogToFile(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue);

						}
					}
				}

			}
		}

		return null;

	}

	private static int[] minPlayer1(int depth,
			int[] actualGameStateForEvaluation, MinmaxStateItem parent) {

		int stonesTobeMoved = 0;
		int differenceOfStonesInMancala = 0;
		for (int i = 1; i < (actualGameStateForEvaluation.length / 2); i++) {
			int[] tempGameState = new int[actualGameStateForEvaluation.length];
			tempGameState = actualGameStateForEvaluation.clone();

			if (isPlayerBoardEmpty(tempGameState)) {
				for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
					tempGameState[0] = tempGameState[0] + tempGameState[j];
					tempGameState[j] = 0;

				}
				differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
						- tempGameState[0];
			}
			if (isPlayer2BoardEmpty(tempGameState)) {
				for (int j = 1; j < tempGameState.length / 2; j++) {
					tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
							+ tempGameState[j];
					tempGameState[j] = 0;
				}
				differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
						- tempGameState[0];
			}

			if (tempGameState[i] > 0) {
				MinmaxStateItem chilNode = tempObject.new MinmaxStateItem();
				chilNode.player_Name = "B" + (i + 1);
				stonesTobeMoved = tempGameState[i];
				tempGameState[i] = 0;

				int traverseOverGameState = i + 1;
				while (stonesTobeMoved != 0) {
					if (traverseOverGameState == tempGameState.length) {
						traverseOverGameState = 1;
					}
					if (stonesTobeMoved != 1) {
						tempGameState[traverseOverGameState]++;
						traverseOverGameState++;
						stonesTobeMoved--;

					} else {
						if (traverseOverGameState == (tempGameState.length / 2)) {
							tempGameState[traverseOverGameState]++;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 1;
							} else {
								chilNode.evaluateMinOrMax = 0;
							}
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
								differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
										- tempGameState[0];
							}
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;
								}
								differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
										- tempGameState[0];
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							/*
							 * if (depth == depthOfGame) { chilNode.minMaxValue
							 * = tempGameState[0] -
							 * tempGameState[tempGameState.length / 2]; }
							 */
							if (isPlayerBoardEmpty(tempGameState)) {
								if (depth <= depthOfGame) {
									writeMinMaxTraverseLogToFile(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							chilNode.parent_detils = parent;
							stack.push(chilNode);
							writeMinMaxTraverseLogToFile(chilNode.player_Name
									+ "," + chilNode.depth + ","
									+ chilNode.minMaxValue);
							minPlayer1(depth, tempGameState, chilNode);
							stonesTobeMoved--;
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {
								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
								}
							}
							stack.push(parentNode);
							writeMinMaxTraverseLogToFile(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue);

							continue;

						} else if (tempGameState[traverseOverGameState] == 0
								&& traverseOverGameState < (tempGameState.length / 2)) {
							tempGameState[traverseOverGameState]++;
							int opponentsOppIndex = tempGameState.length
									- traverseOverGameState;
							/* if (tempGameState[opponentsOppIndex] != 0) { */
							tempGameState[tempGameState.length / 2] = tempGameState[tempGameState.length / 2]
									+ tempGameState[opponentsOppIndex]
									+ tempGameState[traverseOverGameState];
							tempGameState[traverseOverGameState] = 0;
							tempGameState[opponentsOppIndex] = 0;
							stonesTobeMoved--;
							/*
							 * } else { stonesTobeMoved--; }
							 */
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;
								}
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 0;
							} else {
								chilNode.evaluateMinOrMax = 1;
							}
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.parent_detils = parent;
							differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
									- tempGameState[0];
							if (isPlayerBoardEmpty(tempGameState)) {
								if (depth < depthOfGame) {
									writeMinMaxTraverseLogToFile(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							if (chilNode.depth == depthOfGame) {
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);
							writeMinMaxTraverseLogToFile(chilNode.player_Name
									+ "," + chilNode.depth + ","
									+ chilNode.minMaxValue);
						} else {
							tempGameState[traverseOverGameState]++;
							stonesTobeMoved--;
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;
								}
								differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
										- tempGameState[0];
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 0;
							} else {
								chilNode.evaluateMinOrMax = 1;
							}
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.parent_detils = parent;
							differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
									- tempGameState[0];
							if (isPlayerBoardEmpty(tempGameState)) {
								if (depth < depthOfGame) {
									writeMinMaxTraverseLogToFile(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							if (chilNode.depth == depthOfGame) {
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);
							writeMinMaxTraverseLogToFile(chilNode.player_Name
									+ "," + chilNode.depth + ","
									+ chilNode.minMaxValue);
						}

						if (depth < depthOfGame) {
							maxPlayer2(depth + 1, tempGameState, chilNode);
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {
								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
								}
							}
							stack.push(parentNode);
							writeMinMaxTraverseLogToFile(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue);

						} else {
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {
								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
								}
							}
							stack.push(parentNode);
							writeMinMaxTraverseLogToFile(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue);

						}
					}

				}
			}

		}

		return null;
	}

	private static int[] maxPlayer2(int depth,
			int[] actualGameStateForEvaluation, MinmaxStateItem parent) {

		int stonesTobeMoved = 0;
		int differenceOfStonesInMancala = 0;
		for (int i = ((actualGameStateForEvaluation.length) - 1); i > actualGameStateForEvaluation.length / 2; i--) {
			int[] tempGameState = new int[actualGameStateForEvaluation.length];
			tempGameState = actualGameStateForEvaluation.clone();

			if (isPlayer2BoardEmpty(tempGameState)) {
				for (int j = 1; j < tempGameState.length / 2; j++) {
					tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
							+ tempGameState[j];
					tempGameState[j] = 0;

				}
				differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
						- tempGameState[0];
			}
			if (isPlayerBoardEmpty(tempGameState)) {
				for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
					tempGameState[0] = tempGameState[0] + tempGameState[j];
					tempGameState[j] = 0;

				}
				differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
						- tempGameState[0];
			}

			if (tempGameState[i] > 0) {
				MinmaxStateItem chilNode = tempObject.new MinmaxStateItem();
				chilNode.player_Name = "A" + (tempGameState.length - i + 1);
				stonesTobeMoved = tempGameState[i];
				tempGameState[i] = 0;

				int traverseOverGameState = i + 1;
				while (stonesTobeMoved != 0) {
					if (traverseOverGameState == tempGameState.length) {
						traverseOverGameState = 0;
					}
					if (traverseOverGameState == tempGameState.length / 2) {
						traverseOverGameState++;
					}
					if (stonesTobeMoved != 1) {
						tempGameState[traverseOverGameState]++;
						traverseOverGameState++;
						stonesTobeMoved--;

					} else {
						if (traverseOverGameState == 0) {
							tempGameState[traverseOverGameState]++;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 1;
							} else {
								chilNode.evaluateMinOrMax = 0;
							}
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
								differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
										- tempGameState[0];
							}
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
								differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
										- tempGameState[0];
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.parent_detils = parent;
							/*
							 * if (depth == depthOfGame) { chilNode.minMaxValue
							 * = tempGameState[tempGameState.length / 2] -
							 * tempGameState[0]; }
							 */
							if (isPlayer2BoardEmpty(tempGameState)) {
								if (depth <= depthOfGame) {
									writeMinMaxTraverseLogToFile(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);
							writeMinMaxTraverseLogToFile(chilNode.player_Name
									+ "," + chilNode.depth + ","
									+ chilNode.minMaxValue);
							maxPlayer2(depth, tempGameState, chilNode);
							stonesTobeMoved--;
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {
								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
								}
							}
							stack.push(parentNode);
							writeMinMaxTraverseLogToFile(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue);

							continue;
						} else if (tempGameState[traverseOverGameState] == 0
								&& traverseOverGameState > (tempGameState.length / 2)) {
							tempGameState[traverseOverGameState]++;
							int opponentsOppIndex = tempGameState.length
									- traverseOverGameState;
							/* if (tempGameState[opponentsOppIndex] != 0) { */
							tempGameState[0] = tempGameState[0]
									+ tempGameState[opponentsOppIndex]
									+ tempGameState[traverseOverGameState];
							tempGameState[traverseOverGameState] = 0;
							tempGameState[opponentsOppIndex] = 0;
							stonesTobeMoved--;
							/*
							 * } else { stonesTobeMoved--; }
							 */
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 0;
							} else {
								chilNode.evaluateMinOrMax = 1;
							}
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.parent_detils = parent;
							differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
									- tempGameState[0];
							if (isPlayer2BoardEmpty(tempGameState)) {
								if (depth < depthOfGame) {
									writeMinMaxTraverseLogToFile(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							if (chilNode.depth == depthOfGame) {
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);
							writeMinMaxTraverseLogToFile(chilNode.player_Name
									+ "," + chilNode.depth + ","
									+ chilNode.minMaxValue);
						} else {
							tempGameState[traverseOverGameState]++;
							stonesTobeMoved--;
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							chilNode.boradInThisState = new int[tempGameState.length];
							chilNode.boradInThisState = tempGameState.clone();
							chilNode.depth = depth;
							if (parent.evaluateMinOrMax == 1) {
								chilNode.evaluateMinOrMax = 0;
							} else {
								chilNode.evaluateMinOrMax = 1;
							}
							if (chilNode.evaluateMinOrMax == 1) {
								chilNode.minMaxValue = Integer.MIN_VALUE;
							} else {
								chilNode.minMaxValue = Integer.MAX_VALUE;
							}
							chilNode.parent_detils = parent;
							differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
									- tempGameState[0];

							if (isPlayer2BoardEmpty(tempGameState)) {
								if (depth < depthOfGame) {
									writeMinMaxTraverseLogToFile(chilNode.player_Name
											+ ","
											+ chilNode.depth
											+ ","
											+ chilNode.minMaxValue);
								}
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							if (chilNode.depth == depthOfGame) {
								chilNode.minMaxValue = differenceOfStonesInMancala;
							}
							stack.push(chilNode);
							writeMinMaxTraverseLogToFile(chilNode.player_Name
									+ "," + chilNode.depth + ","
									+ chilNode.minMaxValue);
						}
						if (depth < depthOfGame) {
							minPlayer1(depth + 1, tempGameState, chilNode);
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {
								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
								}
							}
							stack.push(parentNode);
							writeMinMaxTraverseLogToFile(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue);

						} else {
							MinmaxStateItem childNode = stack.pop();
							MinmaxStateItem parentNode = stack.pop();
							if (parentNode.evaluateMinOrMax == 1) {
								if (childNode.minMaxValue > parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
									if (parentNode.depth == 1) {
										if (childNode.nextBestState != null
												&& childNode.depth == 1) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.nextBestState
													.clone();
										} else {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parent.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
									if (parentNode.depth == 0
											&& childNode.nextBestState != null) {
										parentNode.nextBestState = new int[childNode.boradInThisState.length];
										parentNode.nextBestState = childNode.nextBestState
												.clone();
									}
									if (parentNode.depth == 0) {

										if (childNode.evaluateMinOrMax == 0) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										} else if (childNode.nextBestState == null) {
											parentNode.nextBestState = new int[childNode.boradInThisState.length];
											parentNode.nextBestState = childNode.boradInThisState
													.clone();
										}
									}
								}
							} else {
								if (childNode.minMaxValue < parentNode.minMaxValue) {
									parentNode.minMaxValue = childNode.minMaxValue;
								}
							}
							stack.push(parentNode);
							writeMinMaxTraverseLogToFile(parentNode.player_Name
									+ "," + parentNode.depth + ","
									+ parentNode.minMaxValue);

						}
					}
					//
				}
			}

		}

		return null;
	}

	private static void greedyMancala(int[] actualGameState) {

		String player2 = "", player1 = "", player2Mancala = null, player1Mancala = null;
		if (whatPlayerIAm == 1) {
			evaluateMyNextMovePlayer1(actualGameState);
		} else {
			evaluateMyNextMovePlayer2(actualGameState);
		}
		for (int i = (evaluatingItem.gameStateForThisDifference.length - 1); i > evaluatingItem.gameStateForThisDifference.length / 2; i--) {
			player2 += Integer
					.toString(evaluatingItem.gameStateForThisDifference[i])
					+ " ";
		}
		for (int i = 1; i < evaluatingItem.gameStateForThisDifference.length / 2; i++) {

			player1 += Integer
					.toString(evaluatingItem.gameStateForThisDifference[i])
					+ " ";

		}
		player2Mancala = Integer
				.toString(evaluatingItem.gameStateForThisDifference[0]);
		player1Mancala = Integer
				.toString(evaluatingItem.gameStateForThisDifference[evaluatingItem.gameStateForThisDifference.length / 2]);

		try {
			PrintWriter fileWriter = new PrintWriter(new BufferedWriter(
					new FileWriter("next_state.txt", false)));
			/*
			 * String temp = new StringBuilder(player2).reverse().toString()
			 * .trim();
			 */
			fileWriter.println(player2);
			fileWriter.println(player1);
			fileWriter.println(player2Mancala);
			fileWriter.println(player1Mancala);

			// System.out.println(player2);
			// System.out.println(player1);
			// System.out.println(player2Mancala);
			// System.out.println(player1Mancala);
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void evaluateMyNextMovePlayer2(
			int[] actualGameStateForEvaluation) {

		int stonesTobeMoved = 0;
		int differenceOfStonesInMancala = 0;
		for (int i = ((actualGameStateForEvaluation.length) - 1); i > actualGameStateForEvaluation.length / 2; i--) {
			int[] tempGameState = new int[actualGameStateForEvaluation.length];
			tempGameState = actualGameStateForEvaluation.clone();

			if (isPlayer2BoardEmpty(tempGameState)) {
				for (int j = 1; j < tempGameState.length / 2; j++) {
					tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
							+ tempGameState[j];
					tempGameState[j] = 0;

				}
				differenceOfStonesInMancala = tempGameState[0]
						- tempGameState[tempGameState.length / 2];
				if (differenceOfStonesInMancala > evaluatingItem.differenceOfStonesInMancala) {
					evaluatingItem.differenceOfStonesInMancala = differenceOfStonesInMancala;
					evaluatingItem.gameStateForThisDifference = new int[tempGameState.length];
					evaluatingItem.gameStateForThisDifference = tempGameState
							.clone();
					evaluatingItem.player2Mancala = tempGameState[0];
					evaluatingItem.player1Mancala = tempGameState[tempGameState.length / 2];
				}
				if (differenceOfStonesInMancala < 0
						&& isPlayer2BoardEmpty(tempGameState)) {
					evaluatingItem.differenceOfStonesInMancala = differenceOfStonesInMancala;
					evaluatingItem.gameStateForThisDifference = new int[tempGameState.length];
					evaluatingItem.gameStateForThisDifference = tempGameState
							.clone();
					evaluatingItem.player2Mancala = tempGameState[0];
					evaluatingItem.player1Mancala = tempGameState[tempGameState.length / 2];
				}
			}

			if (tempGameState[i] > 0) {
				stonesTobeMoved = tempGameState[i];
				tempGameState[i] = 0;

				int traverseOverGameState = i + 1;
				while (stonesTobeMoved != 0) {
					if (traverseOverGameState == tempGameState.length) {
						traverseOverGameState = 0;
					}
					if (traverseOverGameState == tempGameState.length / 2) {
						traverseOverGameState++;
					}
					if (stonesTobeMoved != 1) {
						if (traverseOverGameState == tempGameState.length) {
							traverseOverGameState = 0;
						}
						if (traverseOverGameState == tempGameState.length / 2) {
							traverseOverGameState++;
						}
						tempGameState[traverseOverGameState]++;
						traverseOverGameState++;
						stonesTobeMoved--;

					} else {
						if (traverseOverGameState == 0) {
							tempGameState[traverseOverGameState]++;
							evaluateMyNextMovePlayer2(tempGameState);
							stonesTobeMoved--;
						} else if (tempGameState[traverseOverGameState] == 0
								&& traverseOverGameState > (tempGameState.length / 2)) {
							tempGameState[traverseOverGameState]++;
							int opponentsOppIndex = tempGameState.length
									- traverseOverGameState;
						//	if (tempGameState[opponentsOppIndex] != 0) {
								tempGameState[0] = tempGameState[0]
										+ tempGameState[opponentsOppIndex]
										+ tempGameState[traverseOverGameState];
								tempGameState[traverseOverGameState] = 0;
								tempGameState[opponentsOppIndex] = 0;
								stonesTobeMoved--;
							/*} else {
								stonesTobeMoved--;
							}*/
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							differenceOfStonesInMancala = tempGameState[0]
									- tempGameState[tempGameState.length / 2];
							if (differenceOfStonesInMancala > evaluatingItem.differenceOfStonesInMancala) {
								evaluatingItem.differenceOfStonesInMancala = differenceOfStonesInMancala;
								evaluatingItem.gameStateForThisDifference = new int[tempGameState.length];
								evaluatingItem.gameStateForThisDifference = tempGameState
										.clone();
								evaluatingItem.player2Mancala = tempGameState[0];
								evaluatingItem.player1Mancala = tempGameState[tempGameState.length / 2];
							}
							if (differenceOfStonesInMancala < 0
									&& isPlayer2BoardEmpty(tempGameState)) {
								evaluatingItem.differenceOfStonesInMancala = differenceOfStonesInMancala;
								evaluatingItem.gameStateForThisDifference = new int[tempGameState.length];
								evaluatingItem.gameStateForThisDifference = tempGameState
										.clone();
								evaluatingItem.player2Mancala = tempGameState[0];
								evaluatingItem.player1Mancala = tempGameState[tempGameState.length / 2];
							}
						} else {
							tempGameState[traverseOverGameState]++;
							stonesTobeMoved--;
							if (isPlayer2BoardEmpty(tempGameState)) {
								for (int j = 1; j < tempGameState.length / 2; j++) {
									tempGameState[actualGameStateForEvaluation.length / 2] = tempGameState[actualGameStateForEvaluation.length / 2]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							differenceOfStonesInMancala = tempGameState[0]
									- tempGameState[tempGameState.length / 2];
							if (differenceOfStonesInMancala > evaluatingItem.differenceOfStonesInMancala) {
								evaluatingItem.differenceOfStonesInMancala = differenceOfStonesInMancala;
								evaluatingItem.gameStateForThisDifference = new int[tempGameState.length];
								evaluatingItem.gameStateForThisDifference = tempGameState
										.clone();
								evaluatingItem.player2Mancala = tempGameState[0];
								evaluatingItem.player1Mancala = tempGameState[tempGameState.length / 2];

							}
							if (differenceOfStonesInMancala < 0
									&& isPlayer2BoardEmpty(tempGameState)) {
								evaluatingItem.differenceOfStonesInMancala = differenceOfStonesInMancala;
								evaluatingItem.gameStateForThisDifference = new int[tempGameState.length];
								evaluatingItem.gameStateForThisDifference = tempGameState
										.clone();
								evaluatingItem.player2Mancala = tempGameState[0];
								evaluatingItem.player1Mancala = tempGameState[tempGameState.length / 2];
							}
						}
					}
				}
			}
		}

	}

	private static boolean isPlayerBoardEmpty(int[] actualGameStateForEvaluation) {
		int count = 0;

		for (int i = 1; i < actualGameStateForEvaluation.length / 2; i++) {
			if (actualGameStateForEvaluation[i] == 0) {
				count++;
			}
		}

		if (count == (actualGameStateForEvaluation.length / 2 - 1))
			return true;

		return false;
	}

	private static boolean isPlayer2BoardEmpty(
			int[] actualGameStateForEvaluation) {
		int count = 0;

		for (int i = actualGameStateForEvaluation.length / 2 + 1; i < actualGameStateForEvaluation.length; i++) {
			if (actualGameStateForEvaluation[i] == 0) {
				count++;
			}
		}

		if (count == (actualGameStateForEvaluation.length / 2 - 1))
			return true;

		return false;
	}

	private static void evaluateMyNextMovePlayer1(
			int[] actualGameStateForEvaluation) {
		int stonesTobeMoved = 0;
		int differenceOfStonesInMancala = 0;
		for (int i = 1; i < (actualGameStateForEvaluation.length / 2); i++) {
			int[] tempGameState = new int[actualGameStateForEvaluation.length];
			tempGameState = actualGameStateForEvaluation.clone();

			if (isPlayerBoardEmpty(tempGameState)) {
				for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
					tempGameState[0] = tempGameState[0] + tempGameState[j];
					tempGameState[j] = 0;

				}
				differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
						- tempGameState[0];
				if (differenceOfStonesInMancala > evaluatingItem.differenceOfStonesInMancala) {
					evaluatingItem.differenceOfStonesInMancala = differenceOfStonesInMancala;
					evaluatingItem.gameStateForThisDifference = new int[tempGameState.length];
					evaluatingItem.gameStateForThisDifference = tempGameState
							.clone();
					evaluatingItem.player2Mancala = tempGameState[0];
					evaluatingItem.player1Mancala = tempGameState[tempGameState.length / 2];
				}
				if (differenceOfStonesInMancala < 0
						&& isPlayerBoardEmpty(tempGameState)) {
					evaluatingItem.differenceOfStonesInMancala = differenceOfStonesInMancala;
					evaluatingItem.gameStateForThisDifference = new int[tempGameState.length];
					evaluatingItem.gameStateForThisDifference = tempGameState
							.clone();
					evaluatingItem.player2Mancala = tempGameState[0];
					evaluatingItem.player1Mancala = tempGameState[tempGameState.length / 2];
				}
			}

			if (tempGameState[i] > 0) {
				stonesTobeMoved = tempGameState[i];
				tempGameState[i] = 0;

				int traverseOverGameState = i + 1;
				while (stonesTobeMoved != 0) {
					if (traverseOverGameState == tempGameState.length) {
						traverseOverGameState = 1;
					}
					if (stonesTobeMoved != 1) {
						if (traverseOverGameState == tempGameState.length) {
							traverseOverGameState = 1;
						}
						tempGameState[traverseOverGameState]++;
						traverseOverGameState++;
						stonesTobeMoved--;

					} else {
						if (traverseOverGameState == (tempGameState.length / 2)) {
							tempGameState[traverseOverGameState]++;
							evaluateMyNextMovePlayer1(tempGameState);
							stonesTobeMoved--;
						} else if (tempGameState[traverseOverGameState] == 0
								&& traverseOverGameState < (tempGameState.length / 2)) {
							tempGameState[traverseOverGameState]++;
							int opponentsOppIndex = tempGameState.length
									- traverseOverGameState;
							//if (tempGameState[opponentsOppIndex] != 0) {
								tempGameState[tempGameState.length / 2] = tempGameState[tempGameState.length / 2]
										+ tempGameState[opponentsOppIndex]
										+ tempGameState[traverseOverGameState];
								tempGameState[traverseOverGameState] = 0;
								tempGameState[opponentsOppIndex] = 0;
								stonesTobeMoved--;
							/*} else {
								stonesTobeMoved--;
							}*/
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
									- tempGameState[0];
							if (differenceOfStonesInMancala > evaluatingItem.differenceOfStonesInMancala) {
								evaluatingItem.differenceOfStonesInMancala = differenceOfStonesInMancala;
								evaluatingItem.gameStateForThisDifference = new int[tempGameState.length];
								evaluatingItem.gameStateForThisDifference = tempGameState
										.clone();
								evaluatingItem.player2Mancala = tempGameState[0];
								evaluatingItem.player1Mancala = tempGameState[tempGameState.length / 2];
							}
							if (differenceOfStonesInMancala < 0
									&& isPlayerBoardEmpty(tempGameState)) {
								evaluatingItem.differenceOfStonesInMancala = differenceOfStonesInMancala;
								evaluatingItem.gameStateForThisDifference = new int[tempGameState.length];
								evaluatingItem.gameStateForThisDifference = tempGameState
										.clone();
								evaluatingItem.player2Mancala = tempGameState[0];
								evaluatingItem.player1Mancala = tempGameState[tempGameState.length / 2];
							}
						} else {
							tempGameState[traverseOverGameState]++;
							stonesTobeMoved--;
							if (isPlayerBoardEmpty(tempGameState)) {
								for (int j = tempGameState.length / 2 + 1; j < tempGameState.length; j++) {
									tempGameState[0] = tempGameState[0]
											+ tempGameState[j];
									tempGameState[j] = 0;

								}
							}
							differenceOfStonesInMancala = tempGameState[tempGameState.length / 2]
									- tempGameState[0];
							if (differenceOfStonesInMancala > evaluatingItem.differenceOfStonesInMancala) {
								evaluatingItem.differenceOfStonesInMancala = differenceOfStonesInMancala;
								evaluatingItem.gameStateForThisDifference = new int[tempGameState.length];
								evaluatingItem.gameStateForThisDifference = tempGameState
										.clone();
								evaluatingItem.player2Mancala = tempGameState[0];
								evaluatingItem.player1Mancala = tempGameState[tempGameState.length / 2];

							}
							if (differenceOfStonesInMancala < 0
									&& isPlayerBoardEmpty(tempGameState)) {
								evaluatingItem.differenceOfStonesInMancala = differenceOfStonesInMancala;
								evaluatingItem.gameStateForThisDifference = new int[tempGameState.length];
								evaluatingItem.gameStateForThisDifference = tempGameState
										.clone();
								evaluatingItem.player2Mancala = tempGameState[0];
								evaluatingItem.player1Mancala = tempGameState[tempGameState.length / 2];
							}
						}
					}
				}
			}
		}
	}

	private static int[] gameArrayConstructor(ArrayList<Integer> player,
			int stonesInPlayersMancala, ArrayList<Integer> opponent,
			int stonesInOpponentsMancala) {
		int count = opponent.size();
		int[] actualGameState = new int[1 + player.size() + 1 + opponent.size()];
		for (int i = 0; i < actualGameState.length; i++) {
			if (i == 0) {
				actualGameState[i] = stonesInOpponentsMancala;
			} else if (i == player.size() + 1) {
				actualGameState[i] = stonesInPlayersMancala;
			} else if (i != 0 && i < (actualGameState.length / 2)) {
				actualGameState[i] = player.get(i - 1);
			} else {

				actualGameState[i] = opponent.get(count - 1);
				count--;
			}
		}
		return actualGameState;
	}

	class EvaluatingItem {

		int differenceOfStonesInMancala = Integer.MIN_VALUE;
		int player2Mancala;
		int player1Mancala;
		int[] gameStateForThisDifference;

		public int getDifferenceOfStonesInMancala() {
			return differenceOfStonesInMancala;
		}

		public void setDifferenceOfStonesInMancala(
				int differenceOfStonesInMancala) {
			this.differenceOfStonesInMancala = differenceOfStonesInMancala;
		}

		public int[] getGameStateForThisDifference() {
			return gameStateForThisDifference;
		}

		public void setGameStateForThisDifference(
				int[] gameStateForThisDifference) {
			this.gameStateForThisDifference = gameStateForThisDifference;
		}

		public int getPlayer2Mancala() {
			return player2Mancala;
		}

		public void setPlayer2Mancala(int player2Mancala) {
			this.player2Mancala = player2Mancala;
		}

		public int getPlayer1Mancala() {
			return player1Mancala;
		}

		public void setPlayer1Mancala(int player1Mancala) {
			this.player1Mancala = player1Mancala;
		}

	}

	class MinmaxStateItem {

		int[] boradInThisState;
		int depth = 0;
		String player_Name = null;
		MinmaxStateItem parent_detils = null;
		int minMaxValue = Integer.MIN_VALUE;
		int evaluateMinOrMax = 0;
		int[] nextBestState;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;

		public int[] getBoradInThisState() {
			return boradInThisState;
		}

		public void setBoradInThisState(int[] boradInThisState) {
			this.boradInThisState = boradInThisState;
		}

		public int getDepth() {
			return depth;
		}

		public void setDepth(int depth) {
			this.depth = depth;
		}

		public String getPlayer_Name() {
			return player_Name;
		}

		public void setPlayer_Name(String player_Name) {
			this.player_Name = player_Name;
		}

		public MinmaxStateItem getParent_detils() {
			return parent_detils;
		}

		public void setParent_detils(MinmaxStateItem parent_detils) {
			this.parent_detils = parent_detils;
		}

		public int getMinMaxValue() {
			return minMaxValue;
		}

		public void setMinMaxValue(int minMaxValue) {
			this.minMaxValue = minMaxValue;
		}

		public int getEvaluateMinOrMax() {
			return evaluateMinOrMax;
		}

		public void setEvaluateMinOrMax(int evaluateMinOrMax) {
			this.evaluateMinOrMax = evaluateMinOrMax;
		}

		public int[] getNextBestState() {
			return nextBestState;
		}

		public void setNextBestState(int[] nextBestState) {
			this.nextBestState = nextBestState;
		}

		public int getAlpha() {
			return alpha;
		}

		public void setAlpha(int alpha) {
			this.alpha = alpha;
		}

		public int getBeta() {
			return beta;
		}

		public void setBeta(int beta) {
			this.beta = beta;
		}

	}
}
