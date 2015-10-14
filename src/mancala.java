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

		int currMinValue = 100000;
		int currMaxValue = -100000;
		int currGameLevel = 1;
		int returnedValue;
		
		List< Integer> results = new ArrayList<Integer>();

		gameArraySize = game.size();
		firstMancalaIndex = (gameArraySize / 2) - 1;
		secondMancalaIndex = gameArraySize - 1;
		int bestValue;
		
		//setting level as 1 in the 1st step
		minValue(game, currGameLevel, currMaxValue, myPlayer);
		
		
		/*
		bestValue = Collections.max(results);
		System.out.println(bestValue);
	*/

		return 0;
	}


	static int minValue(List game, int level, int newEvalValue,String currPlayer){
		//terminal test condition needs to be added here
		
		if (cutOffDepth == level){
			return newEvalValue;
		}
		
		
		int currMaxValue = -100000;
		List newGameState = null;
		List <Integer> legalMoves = null;
				
		//System.out.println(play(game, 2, 3, 7));
		//these values need to be dynamic
		
		System.out.println("$$$$$$$$$$$4calling play with values game, index");
		System.out.println(game);
		//System.out.println(index);
		
		//this mancala index should be dynamic based on the player playing here
		
		legalMoves = getAllLegalMoves(game, currPlayer);
		for (Integer index: legalMoves){
			newGameState =  play(game, index, firstMancalaIndex, secondMancalaIndex);
			newEvalValue = eval(newGameState);
		}
		
		System.out.println("printing new game state and eval value below");
		System.out.println(newGameState);
		System.out.println(newEvalValue);
		
		
		if (bonusChance){
			System.out.println("inside bonus chance");
			//play this again for all the possible configuration
			minValue(newGameState, level, newEvalValue,currPlayer);	
		}
		return 0;
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
		
		int noOfCoins = (int)game.get(index);
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
				System.out.println("updating stones of new index: "+ index);
				
				//setting the flag for bonus chance to be played
				if (iter == noOfCoins - 1 && index == myMancalaIndex){
					bonusChance = true;
					System.out.println("********************");
				}
				
				index = index + 1;
			}
			
			System.out.println("game state now: "+ game + " index value: " + index);
		}
		
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
