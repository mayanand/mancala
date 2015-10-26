import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class mancala {

	static int gameArraySize = 0;
	static int firstMancalaIndex = 0;
	static int secondMancalaIndex = 0;
	static int cutOffDepth = 0;
	static int bestValue;
	static int bestMoveIndex;
	static List<String> outputList = new ArrayList<String>();
	static List<String> nextStateList = new ArrayList<String>();
	static String myPlayer = new String();
	static String parentNode = new String();
	static List <String> positionGame = new ArrayList<String>();

	public static void main(String[] args) {

		inputParser input_obj = new inputParser(args[1]);
		List<String> testCase = input_obj.parse(args[1]);

		/*System.out.println("read the file");
		System.out.println(testCase);
		 */
		int caseType = Integer.parseInt(testCase.get(0));
		myPlayer = testCase.get(1);
		cutOffDepth = Integer.parseInt(testCase.get(2));
		String[] player2Board = (testCase.get(3)).split("\\s+");
		String[] player1Board = (testCase.get(4)).split("\\s+");
		int player2Mancala = Integer.parseInt(testCase.get(5));
		int player1Mancala = Integer.parseInt(testCase.get(6));

		List <Integer> game = new ArrayList<Integer>();

		int i = 2;
		for (String b: player1Board){	
			game.add(Integer.parseInt(b));
			String position = "B" + i;
			positionGame.add(position);
			i = i + 1;
		}

		game.add(player1Mancala);
		positionGame.add("bMancala");

		for (int j = player2Board.length - 1; j >= 0; j --){
			game.add(Integer.parseInt(player2Board[j]));
		}

		game.add(player2Mancala);

		for (int j = player2Board.length + 1; j > 1; j--){
			String position = "A" + j;
			positionGame.add(position);
		}
		positionGame.add("aMancala");

		boolean bonusChance = false;

		/*game.add(2);	//adding 1st element of player 1: B1
		game.add(2);
		game.add(2);	//adding last element of player 1: Bn
		game.add(0);	//adding mancala of player 1
		game.add(2);	//adding last element of player 2: An
		game.add(2);
		game.add(2);	//adding last element of player 2: A1
		game.add(0);	//adding mancala of player 2
		 */


		/*positionGame.add("B2");
		positionGame.add("B3");
		positionGame.add("B4");
		positionGame.add("bMancala");
		positionGame.add("A4");
		positionGame.add("A3");
		positionGame.add("A2");
		positionGame.add("aMancala");
		 */

		state stateObj = new state(game, bonusChance);

		if (caseType == 1){
			cutOffDepth = 1;
			int value = minimaxDecision(stateObj, myPlayer);
			nextStateList = getNextState(stateObj,mancala.bestMoveIndex, value);
			outputDump(nextStateList, "next_state.txt");			
		}

		if (caseType == 2){
			int value = minimaxDecision(stateObj, myPlayer);
			outputDump(outputList, "traverse_log.txt");
			nextStateList = getNextState(stateObj,mancala.bestMoveIndex, value);
			outputDump(nextStateList, "next_state.txt");			
		}

		if (caseType == 3){
			int value = alphaBetaDecision(stateObj, myPlayer);
			outputDump(outputList, "traverse_log.txt");
			nextStateList = getNextState(stateObj,mancala.bestMoveIndex, value);
			outputDump(nextStateList, "next_state.txt");
		}

	}

	static  int alphaBetaDecision(state stateObj, String myPlayer){

		int currGameLevel = 0;
		int myMancalaIndex = 0;
		int otherMancalaIndex = 0;
		int currValue = -2147483648;
		int alpha = -2147483648;
		int beta = 2147483647;
		String newPlayer = null;

		List<Integer> results = new ArrayList<Integer>();
		List<Integer> legalMoves = null;
		state newGameState = null;

		gameArraySize = stateObj.getGameList().size();
		firstMancalaIndex = (gameArraySize / 2) - 1;
		secondMancalaIndex = gameArraySize - 1;
		int bestValue;

		System.out.println("Node,Depth,Value,Alpha,Beta");
		System.out.println("root,0,-Infinity,-Infinity,Infinity");

		outputList.add("Node,Depth,Value,Alpha,Beta");
		outputList.add("root,0,-Infinity,-Infinity,Infinity");

		String parent = "root";

		//check every possible move for the max player

		if (myPlayer.equals("1")){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}

		legalMoves = getAllLegalMoves(stateObj.getGameList(), myPlayer);

		for (Integer index: legalMoves){

			List<Integer> newGame = new ArrayList<Integer>(stateObj.getGameList());
			state newStateObj = new state(newGame, stateObj.getBonusChance());

			newGameState =  play(newStateObj, index, myMancalaIndex, otherMancalaIndex);
			int newValue;

			if (newGameState.getBonusChance() == true){

				System.out.println(positionGame.get(index) + "," + (currGameLevel+1) + "," + "-Infinity" + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
				outputList.add(positionGame.get(index) + "," + (currGameLevel+1) + "," + "-Infinity" + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
				newValue = alphaBetaMaxValue(newGameState, currGameLevel, myPlayer, parent, positionGame.get(index), alpha, beta);

				results.add(newValue);
			}
			else{		
				if (myPlayer.equals("1")){ newPlayer = "2"; }
				else {newPlayer = "1";}

				newValue = alphaBetaMinValue(newGameState, currGameLevel+1, newPlayer, parent, positionGame.get(index), alpha, beta);

				results.add(newValue);
			}

			currValue = Math.max(currValue, newValue);
			alpha = currValue;

			System.out.println(parent + "," + "0" + "," + currValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta)  );
			outputList.add(parent + "," + "0" + "," + currValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
		}

		bestValue = Collections.max(results);

		System.out.println(results);

		String bestMove = new String();
		int newValueIndex = new ArrayList<Integer>(results).indexOf(bestValue);


		//we take the index of the legal values
		//the legal values list contains the index of positiongame
		int bestValueIndex= legalMoves.get(newValueIndex);

		if (myPlayer.equals("1")){
			bestMove = positionGame.get(bestValueIndex);
		}
		else{
			bestMove = positionGame.get(secondMancalaIndex - bestValueIndex);
		}
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + bestValue);
		System.out.println("######### " + bestMove);

		mancala.bestValue = bestValue;
		mancala.bestMoveIndex = bestValueIndex;

		return 0;
	}


	static int alphaBetaMinValue(state stateObj, int level, String currPlayer, String parent, String child, int alpha, int beta){

		if (stateObj.getGameOver()){
			System.out.println("!!! game over baby in min methode");

			int value = eval(stateObj.getGameList());

			System.out.println(child + "," + level + "," + value + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
			outputList.add(child + "," + level + "," + value + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta));

			return value;
		}


		//terminal condition test
		if (cutOffDepth == level){

			int value = eval(stateObj.getGameList());

			System.out.println(child + "," + cutOffDepth + "," + value + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
			outputList.add(child + "," + cutOffDepth + "," + value + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );

			return value;
		}

		state newGameState = null;
		List <Integer> legalMoves = null;

		int myMancalaIndex = 0;
		int otherMancalaIndex = 0;
		int currMinValue = 2147483647;

		if (!stateObj.getBonusChance()){
			System.out.println(child + "," + level + "," + "Infinity" + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
			outputList.add(child + "," + level + "," + "Infinity" + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
		}
		if (currPlayer.equals("1")){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}


		legalMoves = getAllLegalMoves(stateObj.getGameList(), currPlayer);

		for (Integer index: legalMoves){

			int newMinValue;
			String currMove = positionGame.get(index);

			List<Integer> newGame = new ArrayList<Integer>(stateObj.getGameList());
			state newStateObj = new state(newGame, stateObj.getBonusChance());

			newGameState =  play(newStateObj, index, myMancalaIndex, otherMancalaIndex);

			if (newGameState.getBonusChance() == true){

				System.out.println(positionGame.get(index) + "," + (level+1) + "," + "Infinity" + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
				outputList.add(positionGame.get(index) + "," + (level+1) + "," + "Infinity" + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
				newMinValue = alphaBetaMinValue(newGameState, level, currPlayer, parent, currMove, alpha, beta);
			}
			else{
				String newPlayer = null;
				if (currPlayer.equals("1")){ newPlayer = "2"; }
				else {newPlayer = "1";}

				newMinValue = alphaBetaMaxValue(newGameState, level + 1, newPlayer, parent, currMove, alpha, beta);
			}

			currMinValue = Math.min(currMinValue, newMinValue);


			if (currMinValue <= alpha){

				if (newStateObj.getBonusChance()){
					System.out.println(child + "," + (level+1) + "," + currMinValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
					outputList.add(child + "," + (level+1) + "," + currMinValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
				}
				else{
					System.out.println(child + "," + level + "," + currMinValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) ) ;
					outputList.add(child + "," + level + "," + currMinValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
				}

				return currMinValue; 
			}

			beta = Math.min(currMinValue, beta);

			if (newStateObj.getBonusChance()){
				System.out.println(child + "," + (level+1) + "," + currMinValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
				outputList.add(child + "," + (level+1) + "," + currMinValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
			}
			else{
				System.out.println(child + "," + level + "," + currMinValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) ) ;
				outputList.add(child + "," + level + "," + currMinValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
			}
		}
		return currMinValue;
	}

	static int alphaBetaMaxValue(state stateObj, int level, String currPlayer, String parent, String child, int alpha, int beta){

		if (stateObj.getGameOver()){
			System.out.println("!!! game over baby in max call");

			int value = eval(stateObj.getGameList());

			System.out.println(child + "," + level + "," + value + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
			outputList.add(child + "," + level + "," + value  + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );

			return value;
		}


		//terminal condition test
		if (cutOffDepth == level){
			int value = eval(stateObj.getGameList());

			System.out.println(child + "," + cutOffDepth + "," + value  + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
			outputList.add(child + "," + cutOffDepth + "," + value + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
			return  value;
		}

		int currMaxValue = -2147483648;
		state newGameState = null;
		List <Integer> legalMoves = null;

		int myMancalaIndex =0;
		int otherMancalaIndex = 0;

		if (!stateObj.getBonusChance()){
			System.out.println(child + "," + level + "," + "-Infinity" + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
			outputList.add(child + "," + level + "," + "-Infinity" + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
		}
		if (currPlayer.equals("1")){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}

		legalMoves = getAllLegalMoves(stateObj.getGameList(), currPlayer);

		for (Integer index: legalMoves){
			int newMaxValue;
			state newStateObj = new state(stateObj.getGameList(), stateObj.getBonusChance());
			String currMove = positionGame.get(index);
			newGameState =  play(newStateObj, index, myMancalaIndex, otherMancalaIndex);


			if (newGameState.getBonusChance() == true){
				System.out.println(positionGame.get(index) + "," + (level+1) + "," + "-Infinity" + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
				outputList.add(positionGame.get(index) + "," + (level+1) + "," + "-Infinity" + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
				newMaxValue = alphaBetaMaxValue(newGameState, level, currPlayer, parent, currMove, alpha, beta);
			}


			else{

				String newPlayer = null;
				if (currPlayer.equals("1")){ newPlayer = "2"; }
				else {newPlayer = "1";}

				newMaxValue = alphaBetaMinValue(newGameState, level + 1, newPlayer, parent, currMove, alpha, beta);
			}

			currMaxValue = Math.max(currMaxValue, newMaxValue);

			if (currMaxValue >= beta){

				if (newStateObj.getBonusChance()){
					System.out.println(child + "," + (level+1) + "," + currMaxValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
					outputList.add(child + "," + (level+1) + "," + currMaxValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
				}
				else{
					System.out.println(child + "," + level + "," + currMaxValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
					outputList.add(child + "," + level + "," + currMaxValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
				}

				return currMaxValue;
			}

			alpha = Math.max(currMaxValue, alpha);

			if (newStateObj.getBonusChance()){
				System.out.println(child + "," + (level+1) + "," + currMaxValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
				outputList.add(child + "," + (level+1) + "," + currMaxValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
			}
			else{
				System.out.println(child + "," + level + "," + currMaxValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
				outputList.add(child + "," + level + "," + currMaxValue + "," + stringifyAlphaBeta(alpha) + "," + stringifyAlphaBeta(beta) );
			}

		}

		return currMaxValue;
	}


	static  int minimaxDecision(state stateObj, String myPlayer){

		int currGameLevel = 0;
		int myMancalaIndex = 0;
		int otherMancalaIndex = 0;
		int currValue = -2147483648;
		String newPlayer = null;

		List<Integer> results = new ArrayList<Integer>();
		List<Integer> legalMoves = null;
		state newGameState = null;

		gameArraySize = stateObj.getGameList().size();
		firstMancalaIndex = (gameArraySize / 2) - 1;
		secondMancalaIndex = gameArraySize - 1;
		int bestValue;

		System.out.println("Node,Depth,Value");
		System.out.println("root,0,-Infinity");

		outputList.add("Node,Depth,Value");
		outputList.add("root,0,-Infinity");

		String parent = "root";

		//check every possible move for the max player

		if (myPlayer.equals("1")){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}

		legalMoves = getAllLegalMoves(stateObj.getGameList(), myPlayer);

		for (Integer index: legalMoves){

			List<Integer> newGame = new ArrayList<Integer>(stateObj.getGameList());
			state newStateObj = new state(newGame, stateObj.getBonusChance());

			newGameState =  play(newStateObj, index, myMancalaIndex, otherMancalaIndex);
			int newValue;
			
			if (newGameState.getBonusChance() == true){

				System.out.println(positionGame.get(index) + "," + (currGameLevel+1) + "," + "-Infinity" );
				outputList.add(positionGame.get(index) + "," + (currGameLevel+1) + "," + "-Infinity");
				newValue = maxValue(newGameState, currGameLevel, myPlayer, parent, positionGame.get(index));

				results.add(newValue);
			}
			else{		
				if (myPlayer.equals("1")){ newPlayer = "2"; }
				else {newPlayer = "1";}

				newValue = minValue(newGameState, currGameLevel+1, newPlayer, parent, positionGame.get(index));

				results.add(newValue);
			}

			currValue = Math.max(currValue, newValue);

			System.out.println(parent + "," + "0" + "," + currValue );
			outputList.add(parent + "," + "0" + "," + currValue );
		}

		bestValue = Collections.max(results);

		System.out.println(results);

		String bestMove = new String();
		int newValueIndex = new ArrayList<Integer>(results).indexOf(bestValue);


		//we take the index of the legal values
		//the legal values list contains the index of positiongame
		int bestValueIndex= legalMoves.get(newValueIndex);

		if (myPlayer.equals("1")){
			bestMove = positionGame.get(bestValueIndex);
		}
		else{
			bestMove = positionGame.get(secondMancalaIndex - bestValueIndex);
		}
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + bestValue);
		System.out.println("######### " + bestMove);

		mancala.bestValue = bestValue;
		mancala.bestMoveIndex = bestValueIndex;

		return 0;
	}


	static int minValue(state stateObj, int level, String currPlayer, String parent, String child){

		if (stateObj.getGameOver()){
			System.out.println("!!! game over baby in min methode");

			int value = eval(stateObj.getGameList());

			System.out.println(child + "," + level + "," + value );
			outputList.add(child + "," + level + "," + value );

			return value;
		}


		//terminal condition test
		if (cutOffDepth == level){

			int value = eval(stateObj.getGameList());

			System.out.println(child + "," + cutOffDepth + "," + value );
			outputList.add(child + "," + cutOffDepth + "," + value );

			return value;
		}

		state newGameState = null;
		List <Integer> legalMoves = null;

		int myMancalaIndex = 0;
		int otherMancalaIndex = 0;
		int currMinValue = 2147483647;

		if (!stateObj.getBonusChance()){
			System.out.println(child + "," + level + "," + "Infinity" );
			outputList.add(child + "," + level + "," + "Infinity" );
		}
		if (currPlayer.equals("1")){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}


		legalMoves = getAllLegalMoves(stateObj.getGameList(), currPlayer);

		for (Integer index: legalMoves){

			int newMinValue;
			String currMove = positionGame.get(index);

			List<Integer> newGame = new ArrayList<Integer>(stateObj.getGameList());
			state newStateObj = new state(newGame, stateObj.getBonusChance());

			newGameState =  play(newStateObj, index, myMancalaIndex, otherMancalaIndex);

			if (newGameState.getBonusChance() == true){

				System.out.println(positionGame.get(index) + "," + (level+1) + "," + "Infinity" );
				outputList.add(positionGame.get(index) + "," + (level+1) + "," + "Infinity" );
				newMinValue = minValue(newGameState, level, currPlayer, parent, currMove);
			}
			else{
				String newPlayer = null;
				if (currPlayer.equals("1")){ newPlayer = "2"; }
				else {newPlayer = "1";}

				newMinValue = maxValue(newGameState, level + 1, newPlayer, parent, currMove);
			}

			currMinValue = Math.min(currMinValue, newMinValue);

			if (newStateObj.getBonusChance()){
				System.out.println(child + "," + (level+1) + "," + currMinValue);
				outputList.add(child + "," + (level+1) + "," + currMinValue);
			}
			else{
				System.out.println(child + "," + level + "," + currMinValue);
				outputList.add(child + "," + level + "," + currMinValue);
			}
		}
		return currMinValue;
	}

	static int maxValue(state stateObj, int level, String currPlayer, String parent, String child){

		if (stateObj.getGameOver()){
			System.out.println("!!! game over baby in max call");

			int value = eval(stateObj.getGameList());

			System.out.println(child + "," + level + "," + value );
			outputList.add(child + "," + level + "," + value );

			return value;
		}


		//terminal condition test
		if (cutOffDepth == level){
			int value = eval(stateObj.getGameList());

			System.out.println(child + "," + cutOffDepth + "," + value );
			outputList.add(child + "," + cutOffDepth + "," + value );
			return  value;
		}

		int currMaxValue = -2147483648;
		state newGameState = null;
		List <Integer> legalMoves = null;

		int myMancalaIndex =0;
		int otherMancalaIndex = 0;

		if (!stateObj.getBonusChance()){
			System.out.println(child + "," + level + "," + "-Infinity" );
			outputList.add(child + "," + level + "," + "-Infinity" );
		}
		if (currPlayer.equals("1")){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}

		legalMoves = getAllLegalMoves(stateObj.getGameList(), currPlayer);

		for (Integer index: legalMoves){
			int newMaxValue;
			state newStateObj = new state(stateObj.getGameList(), stateObj.getBonusChance());
			String currMove = positionGame.get(index);
			newGameState =  play(newStateObj, index, myMancalaIndex, otherMancalaIndex);


			if (newGameState.getBonusChance() == true){
				System.out.println(positionGame.get(index) + "," + (level+1) + "," + "-Infinity" );
				outputList.add(positionGame.get(index) + "," + (level+1) + "," + "-Infinity" );
				newMaxValue = maxValue(newGameState, level, currPlayer, parent, currMove);
			}


			else{

				String newPlayer = null;
				if (currPlayer.equals("1")){ newPlayer = "2"; }
				else {newPlayer = "1";}

				newMaxValue = minValue(newGameState, level + 1, newPlayer, parent, currMove);
			}

			currMaxValue = Math.max(currMaxValue, newMaxValue);

			if (newStateObj.getBonusChance()){
				System.out.println(child + "," + (level+1) + "," + currMaxValue);
				outputList.add(child + "," + (level+1) + "," + currMaxValue);
			}
			else{
				System.out.println(child + "," + level + "," + currMaxValue);
				outputList.add(child + "," + level + "," + currMaxValue);
			}

		}

		return currMaxValue;
	}


	static List getNextState(state stateObj, int bestMoveIndex, int value){

		state newStateObj = new state(stateObj.getGameList(), stateObj.getBonusChance());
		List<String> nextStateList = new ArrayList<String>();

		int myMancalaIndex;
		int otherMancalaIndex;

		if (myPlayer.equals("1")){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}

		while(true){
			newStateObj =  play(newStateObj, mancala.bestMoveIndex, myMancalaIndex, otherMancalaIndex);
			if (newStateObj.getBonusChance() == false){
				break;
			}
			alphaBetaDecision(newStateObj, myPlayer);	
		}

		List<Integer> nextState = newStateObj.getGameList();

		System.out.println("next state: " + nextState);

		String temp = new String();
		for (Integer i = secondMancalaIndex - 1; i > firstMancalaIndex; i--){
			if (!temp.equals("")){
				temp = temp + " ";
			}
			temp = temp + Integer.toString(nextState.get(i));
		}

		nextStateList.add(temp);

		temp = "";

		for (Integer i = 0; i<firstMancalaIndex; i++){
			if (!temp.equals("")){
				temp = temp + " ";
			}
			temp = temp + Integer.toString(nextState.get(i));
		}

		nextStateList.add(temp);
		nextStateList.add(Integer.toString(nextState.get(secondMancalaIndex)));
		nextStateList.add(Integer.toString(nextState.get(firstMancalaIndex)));

		return nextStateList;

	}

	static List getAllLegalMoves(List game, String player){

		List<Integer> legalMoves = new ArrayList<Integer>();

		if (player.equals("1")){
			for (Integer i = 0; i<firstMancalaIndex; i++){
				if ((Integer)game.get(i) > 0){
					legalMoves.add(i);
				}
			}
		}
		else if (player.equals("2")){
			for (Integer i = secondMancalaIndex - 1; i > firstMancalaIndex; i--){
				if ((Integer)game.get(i) > 0){
					legalMoves.add(i);
				}
			}
		}

		else{
			System.out.println("There is something wrog with my equals method.");

		}
		return legalMoves;
	}


	static state play(state stateObj, int index, int myMancalaIndex, int oppositionMancalaIndex){

		List<Integer> myIndices = new ArrayList<Integer>();

		//setting up the indices if the current player and is used to check if the last pebble
		//fell into empty box of the current player
		if (myMancalaIndex<oppositionMancalaIndex){
			myIndices = IntStream.rangeClosed(0, myMancalaIndex-1).boxed().collect(Collectors.toList());
		}
		else{
			myIndices = IntStream.rangeClosed(oppositionMancalaIndex + 1, myMancalaIndex - 1).boxed().collect(Collectors.toList());
		}
		
		List<Integer> game = new ArrayList<Integer>(stateObj.getGameList()); 
		int noOfCoins = (int)game.get(index);
		boolean isBonus = false;
		int oppMirrorIndex;

		game.set(index, 0);
		index = index + 1;

		for (int iter = 0; iter < noOfCoins; iter++){

			if (index == game.size()){
				index = 0;
			}

			if (index == oppositionMancalaIndex){
				//add a pebble to the next available box instead of opposition's mancala
				index = index + 1;

				if (index == game.size()){
					index = 0;
				}
				if (iter == noOfCoins - 1 && myIndices.contains(index) && (int)game.get(index)==0){
					if (myMancalaIndex < oppositionMancalaIndex){
						oppMirrorIndex = oppositionMancalaIndex - index - 1;
					}
					else{
						oppMirrorIndex = myMancalaIndex - index - 1;
					}
					
					int oppMirrorPebbles = game.get(oppMirrorIndex);
					game.set(oppMirrorIndex, 0);
					int newValue = oppMirrorPebbles + 1 + (int)game.get(myMancalaIndex);
					game.set(myMancalaIndex, newValue);	
				}
				else{

					int newValue = (int)game.get(index) + 1;
					game.set(index, newValue);
				}
			}
			else{

				if (iter == noOfCoins - 1 && myIndices.contains(index) && (int)game.get(index)==0){
					if (myMancalaIndex < oppositionMancalaIndex){
						oppMirrorIndex = oppositionMancalaIndex - index - 1;
					}
					else{
						oppMirrorIndex = myMancalaIndex - index - 1;
					}
					int oppMirrorPebbles = game.get(oppMirrorIndex);
					game.set(oppMirrorIndex, 0);
					int newValue = oppMirrorPebbles + 1 + (int)game.get(myMancalaIndex);
					game.set(myMancalaIndex, newValue);	
				}
				else{
					int newValue = (int)game.get(index) + 1;
					game.set(index, newValue);
				}
			}
			//setting the flag for bonus chance to be played
			if (iter == noOfCoins - 1 && index == myMancalaIndex){
				isBonus = true;
			}
			index = index + 1;
		}


		//checking if the game over condition has been met or not
		int player1TotalPebbles = 0;
		int player2TotalPebbles = 0;
		int player1MancalaValue = game.get(firstMancalaIndex);
		int player2MancalaValue = game.get(secondMancalaIndex);

		for (int b = 0;  b < firstMancalaIndex; b++){
			player1TotalPebbles = player1TotalPebbles + game.get(b);
		}
		for (int a = firstMancalaIndex + 1; a < secondMancalaIndex; a++){
			player2TotalPebbles = player2TotalPebbles + game.get(a);
		}

		if (player1TotalPebbles == 0 || player2TotalPebbles == 0){

			if (player1TotalPebbles == 0){
				player2MancalaValue = player2MancalaValue + player2TotalPebbles;
			}

			else if (player2TotalPebbles == 0){
				player1MancalaValue = player1MancalaValue + player1TotalPebbles;
			}

			List<Integer> gameOverList = new ArrayList<Integer>();

			for (int i = 0 ; i < firstMancalaIndex; i++){
				gameOverList.add(0);
			}

			gameOverList.add(player1MancalaValue);

			for (int j = firstMancalaIndex + 1; j < secondMancalaIndex; j++){
				gameOverList.add(0);
			}

			gameOverList.add(player2MancalaValue);

			state gameOverState = new state(gameOverList, false);
			gameOverState.setGameOver(true);

			return gameOverState;
		}

		state playedStateObj = new state(game, isBonus);

		return playedStateObj;
	}

	static int eval(List<Integer> game){

		int eval = 0;

		if (myPlayer.equals("1")){
			eval = (int)game.get(firstMancalaIndex) - (int)game.get(secondMancalaIndex);
		}
		else if (myPlayer.equals("2")){
			eval = (int)game.get(secondMancalaIndex) - (int)game.get(firstMancalaIndex);
		}
		else{
			System.out.println("There is an error in an error at eval function");
		}
		return eval;
	}

	static void outputDump(List<String> outputList, String outFileName){

		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileName), "utf-8"));
			for (String output : outputList){
				writer.write(output + "\n");
			}
		} catch (IOException ex) {
			// report
			System.out.println("IO Exception found!!");
		} finally {
			try {
				writer.close();
			} 
			catch (Exception ex) {
				System.out.println("May be I need to do something");
			}
		}

	}

	
	static String stringifyAlphaBeta(int number){
		if (number == -2147483648){
			return "-Infinity";
		}
		else if (number == 2147483647){
			return "Infinity";
		}
		else{
			return Integer.toString(number);
		}
	}
	
	
}

	