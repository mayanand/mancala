import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class mancala {
	
	static int gameArraySize = 0;
	static int firstMancalaIndex = 0;
	static int secondMancalaIndex = 0;
	static String myPlayer = "1";		//harcoded it for now
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
		

		String myPlayer = "2";
		Integer cutOffDepth = 2;
		//		Integer player1Mancala = 0;
		//		Integer player2Mancala = 0;

		minimaxDecision(game, myPlayer, cutOffDepth);

	}

	static  int minimaxDecision(List<Integer> game, String myPlayer, Integer cutOffDepth){

		int currMinValue = 100000;
		int returnedValue;
		
		List< Integer> results = new ArrayList<Integer>();

		gameArraySize = game.size();
		firstMancalaIndex = (gameArraySize / 2) - 1;
		secondMancalaIndex = gameArraySize - 1;
		int bestValue;
		
		
		minValue(game, 2);
		
		
//		System.out.println(firstMancalaIndex);
//		System.out.println(secondMancalaIndex);
//		
		
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


	static int minValue(List game, int index){
		//terminal test condition needs to be added here
		
		System.out.println("game " + game);
		System.out.println("index " + index);
		System.out.println("first index " + firstMancalaIndex);
		System.out.println("second index " + secondMancalaIndex);
		
		int currMaxValue = -100000;
				
		System.out.println(play(game, 2, 7));

		return 0;
	}

	static List play(List game, int index, int oppositionMancalaIndex){

		//device a strategy to just traverse thru both the player's
		//boxes and mancala until move is over
		
		int noOfCoins = (int)game.get(index);
		game.set(index, 0);
		
		System.out.println("printing game now: " + game);
		System.out.println(noOfCoins);

		for (int iter = 0; iter < noOfCoins; iter++){
			if (index == game.size()){
				index = 0;
			}
			
			if (index == oppositionMancalaIndex){
				//add a pebble to the next available box instead of opposition's mancala
				index = index + 1;
				int newValue = (int)game.get(index) + 1;
				game.set(index, newValue);
			}
			else{
				index = index + 1;
				int newValue = (int)game.get(index) + 1;
				game.set(index, newValue);
			}
			//System.out.println(game.get(index));

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
		
		System.out.println("prinitjng eval " + eval + "board state: " + game);
		return eval;
	}
}
