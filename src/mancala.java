import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class mancala {
	
	static int gameArraySize = 0;
	static int firstMancalaIndex = 0;
	static int secondMancalaIndex = 0;
	static String myPlayer = "1";		//harcoded it for now
	static int cutOffDepth = 2;
	//static boolean bonusChance = false;
	static String parentNode = new String();
	static List <String> positionGame = new ArrayList<String>();

	public static void main(String[] args) {
		
		List <Integer> game = new ArrayList<Integer>();
		boolean bonusChance = false;
		
		game.add(2);	//adding 1st element of player 1: B1
		game.add(2);
		game.add(2);	//adding last element of player 1: Bn
		game.add(0);	//adding mancala of player 1
		game.add(2);	//adding last element of player 2: An
		game.add(2);
		game.add(2);	//adding last element of player 2: A1
		game.add(0);	//adding mancala of player 2

		state stateObj = new state(game, bonusChance);
		
		positionGame.add("b2");
		positionGame.add("b3");
		positionGame.add("b4");
		positionGame.add("bMancala");
		positionGame.add("a4");
		positionGame.add("a3");
		positionGame.add("a2");
		positionGame.add("aMancala");
		
		minimaxDecision(stateObj, myPlayer);

	}

	static  int minimaxDecision(state stateObj, String myPlayer){

		int currGameLevel = 0;
		int returnedValue;
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
		
		//check every possible move for the max player
		
		if (myPlayer == "1"){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}

		
		legalMoves = getAllLegalMoves(stateObj.getGameList(), myPlayer);
		for (Integer index: legalMoves){
			
			List<Integer> newGame = new ArrayList<Integer>(stateObj.getGameList());
			state newStateObj = new state(newGame, stateObj.getBonusChance());
			
			newGameState =  play(newStateObj, index, myMancalaIndex, otherMancalaIndex);
			
			if (newGameState.getBonusChance() == true){
				int newValue = maxValue(newGameState, currGameLevel, myPlayer, positionGame.get(index));
				results.add(newValue);
			}
			else{		
				if (myPlayer == "1"){ newPlayer = "2"; }
				else {newPlayer = "1";}
				
				results.add(minValue(newGameState, currGameLevel+1, newPlayer, positionGame.get(index) ));
			}
		}
		
		
		bestValue = Collections.max(results);
		
		System.out.println(results);
		
		String bestMove = new String();
		int bestValueIndex = new ArrayList<Integer>(results).indexOf(bestValue);
		
		if (myPlayer == "1"){
			bestMove = positionGame.get(bestValueIndex);
		}
		else{
			bestMove = positionGame.get(secondMancalaIndex - bestValueIndex);
		}
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + bestValue);
		System.out.println("######### " + bestMove);
	

		return 0;
	}


	static int minValue(state stateObj, int level, String currPlayer, String parent){
		
		//System.out.println(game + " , " + level + " , " + currPlayer + " , " + parent);
		
		//terminal condition test
		if (cutOffDepth == level){
			System.out.println("min's terminal condition");
			return eval(stateObj.getGameList());
		}
		
		int currMinValue = 100000;
		state newGameState = null;
		List <Integer> legalMoves = null;
		
		int myMancalaIndex = 0;
		int otherMancalaIndex = 0;
		
		System.out.println(parent + "," + level + "," + currMinValue );
		
		
		if (currPlayer == "1"){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}
		
		System.out.println("state of game to begin with");
		System.out.println(stateObj.getGameList());
		
		
		legalMoves = getAllLegalMoves(stateObj.getGameList(), currPlayer);
		
		System.out.println("printingegal moves");
		System.out.println(legalMoves);
		
		for (Integer index: legalMoves){
			
			/*System.out.println("printingplayers:  " + positionGame.get(index) + " curentlevel " + level);
			System.out.println(positionGame);
			*/
			
			int newMinValue;
			List<Integer> newGame = new ArrayList<Integer>(stateObj.getGameList());
			state newStateObj = new state(newGame, stateObj.getBonusChance());
			
			newGameState =  play(newStateObj, index, myMancalaIndex, otherMancalaIndex);
			
			
			if (newGameState.getBonusChance() == true){
				System.out.println("printing board state in bonus conditon");
				System.out.println(newStateObj.getGameList());
				parent = positionGame.get(index);
				System.out.println("calling bonus chance with level: " + level);
				newMinValue = minValue(newGameState, level, currPlayer, parent);
			}
			else{
				String newPlayer = null;
				if (currPlayer == "1"){ newPlayer = "2"; }
				else {newPlayer = "1";}
				
				System.out.println("printing board state in if conditon of min");
				System.out.println(newStateObj.getGameList());
				
				newMinValue = maxValue(newGameState, level + 1, newPlayer, parent);
				
			}
			
			System.out.println("$$$$$$$$$$$$$$$$$");
			System.out.println("current value: " + currMinValue);
			System.out.println("newmin value: " + newMinValue);
			
			currMinValue = Math.min(currMinValue, newMinValue);
			
			int parentLevel = level;
			int newLevel = level + 1;
		
			System.out.println(positionGame.get(index) + "," + newLevel + "," + newMinValue);
			System.out.println(parent + "," + level + "," + currMinValue);
			
		}
		return currMinValue;
	}

static int maxValue(state stateObj, int level, String currPlayer, String parent){
	
	//terminal condition test
		if (cutOffDepth == level){
			System.out.println(" **193** board state in cutoff of max");
			System.out.println(stateObj.getGameList());
			//System.out.println("in terminal condition of max");
			return  eval(stateObj.getGameList());
		}
		
		int currMaxValue = -100000;
		state newGameState = null;
		List <Integer> legalMoves = null;
		
		int myMancalaIndex =0;
		int otherMancalaIndex = 0;
		
		
		if (currPlayer == "1"){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}
		
		legalMoves = getAllLegalMoves(stateObj.getGameList(), currPlayer);
		for (Integer index: legalMoves){
			
			System.out.println(parent + "," + level + "," + currMaxValue );
			int newMaxValue;
			state newStateObj = new state(stateObj.getGameList(), stateObj.getBonusChance());
			
			newGameState =  play(newStateObj, index, myMancalaIndex, otherMancalaIndex);
							
			
			if (newGameState.getBonusChance() == true){
				parent = positionGame.get(index);
				newMaxValue = minValue(newGameState, level, currPlayer, parent);
			}
			
			
			else{
				
				System.out.println("inside else of max value with parent and new player");
				
				String newPlayer = null;
				if (currPlayer == "1"){ newPlayer = "2"; }
				else {newPlayer = "1";}
				
				System.out.println(parent + "," + newPlayer);
				
				newMaxValue = minValue(newGameState, level + 1, newPlayer, parent);
				//currMaxValue = Math.max(currMaxValue, minValue(newGameState, level + 1, newPlayer));
			}
			
			currMaxValue = Math.max(currMaxValue, newMaxValue);
			
			int newLevel = level + 1;
			
			System.out.println(positionGame.get(index) + "," + level + newMaxValue);
			System.out.println(parent + "," + level + "," + currMaxValue);
			
		}
		
		return currMaxValue;
	}

	static List getAllLegalMoves(List game, String player){

		List<Integer> legalMoves = new ArrayList<Integer>();
		
		if (player == "1"){
			for (Integer i = 0; i<firstMancalaIndex; i++){
				if ((Integer)game.get(i) > 0){
					legalMoves.add(i);
				}
			}
		}
		else if (player == "2"){
			for (Integer i = secondMancalaIndex - 1; i > firstMancalaIndex; i--){
				if ((Integer)game.get(i) > 0){
					legalMoves.add(i);
				}
			}
		}
		return legalMoves;
	}

	
	static state play(state stateObj, int index, int myMancalaIndex, int oppositionMancalaIndex){

		//device a strategy to just traverse thru both the player's
		//boxes and mancala until move is over
		
		List<Integer> game = new ArrayList<Integer>(); 
		game = stateObj.getGameList();
		int noOfCoins = (int)game.get(index);
		boolean isBonus = false;
		
		game.set(index, 0);	
		index = index + 1;
		/*System.out.println("no of coins and the index");
		System.out.println(noOfCoins);
		System.out.println(index);
	*/
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
				//System.out.println("adding value at " + index);
				//System.out.println("updating stones of new index: "+ index);
				
				//setting the flag for bonus chance to be played
				if (iter == noOfCoins - 1 && index == myMancalaIndex){
					isBonus = true;
					System.out.println("********************");
				}
				index = index + 1;
			}
			
//			System.out.println("game state now: "+ game + " index value: " + index);
		}
		
		//System.out.println("game status now: " + game);
		//System.out.println("eval " + eval(game));
		
		state playedStateObj = new state(game, isBonus);
		return playedStateObj;
	}
	
	static int eval(List<Integer> game){
		
		int eval = 0;
		
		System.out.println("^^^^^^^^^^^^value of board inside eval function");
		System.out.println(game);
		
		if (myPlayer == "1"){
			eval = (int)game.get(firstMancalaIndex) - (int)game.get(secondMancalaIndex);
		}
		else if (myPlayer == "2"){
			eval = (int)game.get(secondMancalaIndex) - (int)game.get(firstMancalaIndex);
		}
		else{
			System.out.println("There is an error in an error at eval function");
		}
		
		return eval;
	}
}
