import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
		for (String b: player2Board){	
			game.add(Integer.parseInt(b));
			String position = "B" + i;
			positionGame.add(position);
			i = i + 1;
		}
		game.add(player1Mancala);
		positionGame.add("bMancala");

		i = 2;
		for (String a: player1Board){	
			game.add(Integer.parseInt(a));
			String position = "A" + i;
			positionGame.add(position);
			i = i + 1;
		}
		game.add(player2Mancala);
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
			minimaxDecision(stateObj, myPlayer);
		}

		if (caseType == 2){

			System.out.println("before mini mac call: " +stateObj.getGameList());
			int value = minimaxDecision(stateObj, myPlayer);
			nextStateList = getNextState(stateObj,mancala.bestMoveIndex, value);
			outputDump(outputList, "traverse_log.txt");
			outputDump(nextStateList, "next_state.txt");
			System.out.println("after mini max call: " + stateObj.getGameList());
			
		}

	}

	static  int minimaxDecision(state stateObj, String myPlayer){

		int currGameLevel = 1;
		int myMancalaIndex = 0;
		int otherMancalaIndex = 0;
		String newPlayer = null;

		List<Integer> results = new ArrayList<Integer>();
		List<Integer> legalMoves = null;
		state newGameState = null;

		gameArraySize = stateObj.getGameList().size();
		firstMancalaIndex = (gameArraySize / 2) - 1;
		secondMancalaIndex = gameArraySize - 1;
		int bestValue;

		System.out.println("root,0,-Infinity");

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

				System.out.println(positionGame.get(index) + "," + currGameLevel + "," + "-Infinity" );
				newValue = maxValue(newGameState, currGameLevel, myPlayer, parent, positionGame.get(index));

				results.add(newValue);
			}
			else{		
				if (myPlayer.equals("1")){ newPlayer = "2"; }
				else {newPlayer = "1";}

				System.out.println(positionGame.get(index) + "," + currGameLevel + "," + "Infinity" );
				newValue = minValue(newGameState, currGameLevel+1, newPlayer, parent, positionGame.get(index));

				results.add(newValue);
			}

			System.out.println(positionGame.get(index) + "," + "1" + "," + newValue );
		}

		bestValue = Collections.max(results);

		System.out.println("root" + "," + "0" + "," + bestValue);

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

		System.out.println(stateObj.getGameList() + " , " + level + " , " + currPlayer + " , " + parent);

		//terminal condition test
		if (cutOffDepth == level - 1){
			System.out.println("min's terminal condition");

			int value = eval(stateObj.getGameList());
			System.out.println(child + "," + cutOffDepth + "," + value );

			return value;
		}


		state newGameState = null;
		List <Integer> legalMoves = null;

		int myMancalaIndex = 0;
		int otherMancalaIndex = 0;
		int currMinValue = 100000;

		if (currPlayer.equals("1")){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}


		legalMoves = getAllLegalMoves(stateObj.getGameList(), currPlayer);

		for (Integer index: legalMoves){

			int newMinValue;
			int newLevel;
			String currMove = positionGame.get(index);

			List<Integer> newGame = new ArrayList<Integer>(stateObj.getGameList());
			state newStateObj = new state(newGame, stateObj.getBonusChance());

			newGameState =  play(newStateObj, index, myMancalaIndex, otherMancalaIndex);

			//System.out.println("The new player is: " +parent);

			if (newGameState.getBonusChance() == true){

				//parent = positionGame.get(index);

				System.out.println(parent + "," + level + "," + "Infinity" );

				newMinValue = minValue(newGameState, level, currPlayer, parent, currMove);

				newLevel = level;
			}
			else{
				String newPlayer = null;
				if (currPlayer.equals("1")){ newPlayer = "2"; }
				else {newPlayer = "1";}

				/*
				System.out.println("control goes to else condition and parent is: " + parent);
				System.out.println("the current indx played is: "+positionGame.get(index) );
				 */
				newMinValue = maxValue(newGameState, level + 1, newPlayer, parent, currMove);

				newLevel = level + 1;
			}

			currMinValue = Math.min(currMinValue, newMinValue);

			//System.out.println(positionGame.get(index) + "," + level + "," + newMinValue);
			System.out.println(parent + "," + (level-1) + "," + currMinValue);

		}
		return currMinValue;
	}

	static int maxValue(state stateObj, int level, String currPlayer, String parent, String child){

		//terminal condition test
		if (cutOffDepth == level - 1){
			int value = eval(stateObj.getGameList());

			System.out.println(child + "," + cutOffDepth + "," + value );
			return  value;
		}

		int currMaxValue = -100000;
		state newGameState = null;
		List <Integer> legalMoves = null;

		int myMancalaIndex =0;
		int otherMancalaIndex = 0;


		if (currPlayer.equals("1")){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}

		legalMoves = getAllLegalMoves(stateObj.getGameList(), currPlayer);
		for (Integer index: legalMoves){

			int newMaxValue;
			int newLevel;
			state newStateObj = new state(stateObj.getGameList(), stateObj.getBonusChance());
			String currMove = positionGame.get(index);
			newGameState =  play(newStateObj, index, myMancalaIndex, otherMancalaIndex);


			if (newGameState.getBonusChance() == true){
				//parent = positionGame.get(index);				
				newMaxValue = minValue(newGameState, level, currPlayer, parent, currMove);
				newLevel = level;
			}


			else{

				String newPlayer = null;
				if (currPlayer.equals("1")){ newPlayer = "2"; }
				else {newPlayer = "1";}

				newMaxValue = minValue(newGameState, level + 1, newPlayer, parent, currMove);
				newLevel = level + 1;
			}

			currMaxValue = Math.max(currMaxValue, newMaxValue);

			System.out.println(positionGame.get(index) + "," + newLevel + newMaxValue);
			System.out.println(parent + "," + level + "," + currMaxValue);

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
			minimaxDecision(newStateObj, myPlayer);	
		}
		
		List<Integer> nextState = newStateObj.getGameList();
		
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

		List<Integer> game = new ArrayList<Integer>(); 
		game = stateObj.getGameList();
		int noOfCoins = (int)game.get(index);
		boolean isBonus = false;

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
				int newValue = (int)game.get(index) + 1;
				game.set(index, newValue);
			}
			else{
				int newValue = (int)game.get(index) + 1;
				game.set(index, newValue);

				//setting the flag for bonus chance to be played
				if (iter == noOfCoins - 1 && index == myMancalaIndex){
					isBonus = true;
					System.out.println("setting bonus as true for play");
				}
				index = index + 1;
			}	
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

}
