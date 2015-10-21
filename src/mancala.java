import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class mancala {
	
	static int gameArraySize = 0;
	static int firstMancalaIndex = 0;
	static int secondMancalaIndex = 0;
	static String myPlayer = "1";		//harcoded it for now
	static int cutOffDepth = 2;
	static boolean bonusChance = false;
	static String parentNode = new String();
	static List <String> positionGame = new ArrayList<String>();

	public static void main(String[] args) {
		//		System.out.println("Hello world");
		List <Integer> game = new ArrayList<Integer>();

		game.add(2);	//adding 1st element of player 1: B1
		game.add(2);
		game.add(2);	//adding last element of player 1: Bn
		game.add(0);	//adding mancala of player 1
		game.add(2);	//adding last element of player 2: An
		game.add(2);
		game.add(2);	//adding last element of player 2: A1
		game.add(0);	//adding mancala of player 2

		positionGame.add("b2");
		positionGame.add("b3");
		positionGame.add("b4");
		positionGame.add("bMancala");
		positionGame.add("a4");
		positionGame.add("a3");
		positionGame.add("a2");
		positionGame.add("aMancala");
		
		minimaxDecision(game, myPlayer);

	}

	static  int minimaxDecision(List<Integer> game, String myPlayer){

		int currGameLevel = 0;
		int returnedValue;
		int newEvalValue = 0;
		int myMancalaIndex = 0;
		int otherMancalaIndex = 0;
		String newPlayer = null;
		
		List<Integer> results = new ArrayList<Integer>();
		List<Integer> legalMoves = null;
		List<Integer> newGameState = null;

		gameArraySize = game.size();
		firstMancalaIndex = (gameArraySize / 2) - 1;
		secondMancalaIndex = gameArraySize - 1;
		int bestValue;
		
		System.out.println("root,0,-Infinity");
		
		//check every possible move for the max player
		
		if (myPlayer == "1"){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}

		
		legalMoves = getAllLegalMoves(game, myPlayer);
		for (Integer index: legalMoves){
			
			List<Integer> newGame = new ArrayList<Integer>(game);
			
			newGameState =  play(newGame, index, myMancalaIndex, otherMancalaIndex);
			newEvalValue = eval(newGameState);
			
			if (bonusChance == true){
				results.add(maxValue(newGameState, currGameLevel, myPlayer, positionGame.get(index)));
				//System.out.println("current game state after 1st move with bonus");
			}
			else{		

				//System.out.println(positionGame.get(index) + "," + currGameLevel + "," + "infinity");
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


	static int minValue(List game, int level, String currPlayer, String parent){
		
		//terminal condition test
		if (cutOffDepth == level){
			System.out.println("min's terminal condition");
			return eval(game);
		}
		
		int currMinValue = 100000;
		List<Integer> newGameState = null;
		List <Integer> legalMoves = null;
		
		int myMancalaIndex = 0;
		int otherMancalaIndex = 0;

		
		if (!bonusChance){
			parentNode = parent;
		}
		
		System.out.println(parent + "," + level + "," + currMinValue );
		
		
		if (currPlayer == "1"){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}
				
		legalMoves = getAllLegalMoves(game, currPlayer);
		for (Integer index: legalMoves){
			
			System.out.println("printingplayers:  " + positionGame.get(index));
			
			List<Integer> newGame = new ArrayList<Integer>(game);
			
			newGameState =  play(newGame, index, myMancalaIndex, otherMancalaIndex);
			
			int newMinValue;
			
			
			if (bonusChance){
				System.out.println("calling bonus chance with");
				newMinValue = minValue(newGameState, level, currPlayer, positionGame.get(index));
			}
			else{
				String newPlayer = null;
				if (currPlayer == "1"){ newPlayer = "2"; }
				else {newPlayer = "1";}
				
				newMinValue = maxValue(newGameState, level + 1, newPlayer, parent);
				
			}
			
			currMinValue = Math.min(currMinValue, newMinValue);
			
			int parentLevel = level;
			int newLevel = level + 1;
		
			System.out.println(positionGame.get(index) + "," + newLevel + "," + currMinValue);
			System.out.println(parent + "," + level + "," + currMinValue);
			
			

		}
		return currMinValue;
	}

static int maxValue(List game, int level, String currPlayer, String parent){
		
	System.out.println("!!!!!!!!!!!calling amx value");	
	
	
	//terminal condition test
		if (cutOffDepth == level){
			System.out.println("in terminal condition of max");
			return eval(game);
		}
		
		int currMaxValue = -100000;
		List newGameState = null;
		List <Integer> legalMoves = null;
		
		int myMancalaIndex =0;
		int otherMancalaIndex = 0;
		
		if (!bonusChance){
			parentNode = parent;
		}
		
		if (currPlayer == "1"){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}
		
		legalMoves = getAllLegalMoves(game, currPlayer);
		for (Integer index: legalMoves){
			
			System.out.println(parent + "," + level + "," + currMaxValue );
			List<Integer> newGame = new ArrayList<Integer>(game);
	
			newGameState =  play(newGame, index, myMancalaIndex, otherMancalaIndex);
			
			int newMaxValue;
			
			if (bonusChance){
				System.out.println("inside bonus chance");
				//play this again for all the possible configuration
				newMaxValue = maxValue(newGameState, level, currPlayer, positionGame.get(index));	
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
			
			System.out.println(positionGame.get(index) + "," + level + currMaxValue);
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

	
	static List play(List game, int index, int myMancalaIndex, int oppositionMancalaIndex){

		//device a strategy to just traverse thru both the player's
		//boxes and mancala until move is over
		
		bonusChance = false;	//resetting bonus flag for next move
		
		int noOfCoins = (int)game.get(index);
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
					bonusChance = true;
					System.out.println("********************");
				}
				index = index + 1;
			}
			
//			System.out.println("game state now: "+ game + " index value: " + index);
		}
		
		//System.out.println("game status now: " + game);
		//System.out.println("eval " + eval(game));
		return game;

	}
	
	static int eval(List<Integer> game){
		
		int eval = 0;
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
