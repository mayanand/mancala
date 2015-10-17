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

		positionGame.add("b1");
		positionGame.add("b2");
		positionGame.add("b3");
		positionGame.add("bMancala");
		positionGame.add("a3");
		positionGame.add("a2");
		positionGame.add("a1");
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
		
		//check every possible move for the max player
		
		if (myPlayer == "1"){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}

		
		legalMoves = getAllLegalMoves(game, myPlayer);
		for (Integer index: legalMoves){
			
			System.out.println("calling play inside mim max with player and game state as:");
			System.out.println(game);
			System.out.println(myPlayer);
			
			List<Integer> newGame = new ArrayList<Integer>(game);
			
			newGameState =  play(newGame, index, myMancalaIndex, otherMancalaIndex);
			newEvalValue = eval(newGameState);
			
			if (bonusChance == true){
				results.add(maxValue(newGameState, currGameLevel, -10000, myPlayer));
				System.out.println("current game state after 1st move with bonus");
			}
			else{
				System.out.println("game status after 1st dummy move without bonus move");
				System.out.println(newGameState);
				System.out.println(newEvalValue);
				
				if (myPlayer == "1"){ newPlayer = "2"; }
				else {newPlayer = "1";}
				results.add(minValue(newGameState, currGameLevel+1, 10000, newPlayer ));
			}
		}
		
		/*
		bestValue = Collections.max(results);
		System.out.println(bestValue);
	*/

		return 0;
	}


	static int minValue(List game, int level, int newEvalValue,String currPlayer){
		
		//terminal condition test
		if (cutOffDepth == level - 1){
			return newEvalValue;
		}
		
		int currMinValue = 100000;
		List newGameState = null;
		List <Integer> legalMoves = null;
		
		int myMancalaIndex = 0;
		int otherMancalaIndex = 0;
		
		if (currPlayer == "1"){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}
		

		System.out.println("into min vlaue $$$$$$$$$current player " + currPlayer);
		
		legalMoves = getAllLegalMoves(game, currPlayer);
		for (Integer index: legalMoves){
			
			List<Integer> newGame = new ArrayList<Integer>(game);
			
			newGameState =  play(newGame, index, myMancalaIndex, otherMancalaIndex);
			newEvalValue = eval(newGameState);
			
			System.out.println("printing new game state and eval value below in min function");
			System.out.println(newGameState);
			System.out.println(newEvalValue);
			
			if (bonusChance){
				System.out.println("inside bonus chance");
				//play this again for all the possible configuration
				minValue(newGameState, level, newEvalValue,currPlayer);	
			}
			else{
				String newPlayer = null;
				if (currPlayer == "1"){ newPlayer = "2"; }
				else {newPlayer = "1";}
				System.out.println("current player " + currPlayer);
				System.out.println("calling max value now and the player is " + newPlayer);
				
				currMinValue = Math.min(currMinValue, maxValue(newGameState, level + 1, currMinValue, newPlayer));
				
			}
		}
		
		
		return currMinValue;
	}

static int maxValue(List game, int level, int newEvalValue, String currPlayer){
		
		//terminal condition test
		if (cutOffDepth == level - 1){
			return newEvalValue;
		}
		
		int currMaxValue = -100000;
		List newGameState = null;
		List <Integer> legalMoves = null;
		
		int myMancalaIndex =0;
		int otherMancalaIndex = 0;
		
		if (currPlayer == "1"){ myMancalaIndex = firstMancalaIndex; otherMancalaIndex = secondMancalaIndex; }
		else {myMancalaIndex = secondMancalaIndex; otherMancalaIndex = firstMancalaIndex;}
		
		System.out.println("inside macvlaue $$$$$$$$$current player " + currPlayer);
		
		
		legalMoves = getAllLegalMoves(game, currPlayer);
		for (Integer index: legalMoves){
			
			List<Integer> newGame = new ArrayList<Integer>(game);
			//newGame = game;
			
			newGameState =  play(newGame, index, myMancalaIndex, otherMancalaIndex);
			newEvalValue = eval(newGameState);
			
			
			System.out.println("inside max function printing new game state and eval value below");
			System.out.println(newGameState);
			System.out.println(newEvalValue);
			
			
			if (bonusChance){
				System.out.println("inside bonus chance");
				//play this again for all the possible configuration
				maxValue(newGameState, level, newEvalValue,currPlayer);	
			}
			
			else{
				String newPlayer = null;
				if (currPlayer == "1"){ newPlayer = "2"; }
				else {newPlayer = "1";}
				
				System.out.println("^^^^^^^^^^inside else of max function new player, level " + newPlayer);
				System.out.println(level);
				
				currMaxValue = Math.max(currMaxValue, minValue(newGameState, level + 1, currMaxValue, newPlayer));
			}
		}
		
		
			
		return currMaxValue;
	}
	static List getAllLegalMoves(List game, String player){

		List<Integer> legalMoves = new ArrayList<Integer>();
		
		System.out.println("gettig legal moves for player: " + player);

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
		System.out.println("no of coins and the index");
		System.out.println(noOfCoins);
		System.out.println(index);
	
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
		
		System.out.println("game status now: " + game);
		
		return game;

	}
	
	static int eval(List game){
		
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
		
		//System.out.println("prinitjng eval " + eval + "board state: " + game);
		return eval;
	}
}
