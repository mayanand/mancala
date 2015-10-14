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
		int currIndex = 0;			//the index of the bucket being played right now, initializing to zero
		int returnedValue;
		
		currIndex = 1;	//hardcoding the value of index for now
		
		List< Integer> results = new ArrayList<Integer>();

		gameArraySize = game.size();
		firstMancalaIndex = (gameArraySize / 2) - 1;
		secondMancalaIndex = gameArraySize - 1;
		int bestValue;
		
		//setting level as 1 in the 1st step
		minValue(game, currIndex, currGameLevel, currMaxValue, myPlayer);
		
		/*//code to traverse for second player
		List<Integer> secondTraversalOrder = new ArrayList<Integer>();
		for (int i = secondMancalaIndex -1; i > firstMancalaIndex; i--){
			System.out.println("inside the for loop");
			secondTraversalOrder.add(game.get(i));
		}*/
		/*
		if (myPlayer == "1"){
			for (int i = 0; i < firstMancalaIndex; i++){
				//returnedValue = Math.max(currMaxValue, );
				results.add(minValue(game, i));
			}
		}
		else if (myPlayer == "2"){
			for (int i = secondMancalaIndex - 1; i > firstMancalaIndex; i--){
				//returnedValue = Math.max(currMaxValue, );
				System.out.println(minValue(game, i));
				//results.add(minValue(game, i, firstMancalaIndex, secondMancalaIndex));
			}
		}
		
		
		bestValue = Collections.max(results);
		System.out.println(bestValue);
	*/

		return 0;
	}


	static int minValue(List game, int index, int level, int newEvalValue,String currPlayer){
		//terminal test condition needs to be added here
		
		if (cutOffDepth == level){
			return newEvalValue;
		}
		
		
		int currMaxValue = -100000;
		List newGameState;
				
		//System.out.println(play(game, 2, 3, 7));
		//these values need to be dynamic
		newGameState =  play(game, index, 3, 7);
		newEvalValue = eval(newGameState);
		
		System.out.println("printing new game state and eval value below");
		System.out.println(newGameState);
		System.out.println(newEvalValue);
		
		if (bonusChance){
			System.out.println("inside bonus chance");
			//play this again for all the possible configuration
			if (currPlayer == "1"){
				for (int i = 0; i<firstMancalaIndex; i++){
					if ((int)newGameState.get(i) > 0){
						minValue(newGameState, i, level, newEvalValue, currPlayer);
					}
				}
			}
			else if (currPlayer == "2"){
				for (int i = secondMancalaIndex - 1; i > firstMancalaIndex; i--){
					if ((int)newGameState.get(i) > 0){
						minValue(newGameState, i, level, newEvalValue, currPlayer);
					}
				}
			}
		}
		
		
		System.out.println("!!!bonus printing the game state and eval value below");
		System.out.println(newGameState);
		System.out.println(newEvalValue);
		
		
		return 0;
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
				
				//setting the flag for bonus chance to be played
				if (iter == noOfCoins - 1 && index == myMancalaIndex){
					bonusChance = true;
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
