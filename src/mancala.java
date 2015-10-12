import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.print.attribute.standard.MediaSize.Other;


public class mancala {

	public static void main(String[] args) {
		//		System.out.println("Hello world");
		List <Integer> game = new ArrayList<Integer>();
		List <String> positionGame = new ArrayList<String>();

		game.add(2);	//adding last element of player 2: An
		game.add(2);
		game.add(2);	//adding 1st element of player 2: A1
		game.add(0);	//adding mancala of player 2
		game.add(2);	//adding 1st element of player 1: B1
		game.add(2);
		game.add(2);	//adding last element of player 1: Bn
		game.add(0);	//adding mancala of player 1

		positionGame.add("a3");
		positionGame.add("a2");
		positionGame.add("a1");
		positionGame.add("aMancala");
		positionGame.add("b1");
		positionGame.add("b2");
		positionGame.add("b3");
		positionGame.add("bMancala");


		String myPlayer = "2";
		Integer cutOffDepth = 2;
		//		Integer player1Mancala = 0;
		//		Integer player2Mancala = 0;

		minimaxDecision(game, myPlayer, cutOffDepth);

	}

	static  int minimaxDecision(List<Integer> game, String myPlayer, Integer cutOffDepth){

		int currMinValue = 100000;
		int currMaxValue = -100000;
		int returnedValue;

		int gameArraySize = game.size();
		int firstMancalaIndex = (gameArraySize / 2) - 1;
		int secondMancalaIndex = gameArraySize - 1;
		
		System.out.println(firstMancalaIndex);
		System.out.println(secondMancalaIndex);
		
		
		/*//code to traverse for second player
		List<Integer> secondTraversalOrder = new ArrayList<Integer>();
		for (int i = secondMancalaIndex -1; i > firstMancalaIndex; i--){
			System.out.println("inside the for loop");
			secondTraversalOrder.add(game.get(i));
		}*/
		
		if (myPlayer == "1"){
			returnedValue = Math.max(currMaxValue, minValue(game, myPlayer, firstMancalaIndex, secondMancalaIndex));
		}
		/*else if (myPlayer == "2"){
			returnedValue = Math.min(currMinValue, maxValue(game, myPlayer, firstMancalaIndex, secondMancalaIndex);)
		}*/
	

		return 0;
	}


/*	static int minValue(List game, String player)minValue(game, myPlayer, firstMancalaIndex, secondMancalaIndex));{
		
		
		
		
		int noOfCoins = (int)game.get(index);



		play(game, player, 6);


		return 0;
	}
*/
	static int play(List game, String player, int index, int noOfCoins, int myMancalaIndex, int oppositionMancalaIndex){

		//System.out.println(noOfCoins);

		//device a strategy to just traverse thru both the player's
		//boxes and mancala until move is over

		//System.out.println(game);
		//System.out.println(noOfCoins);

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
				int newValue = (int)game.get(index) + 1;
				game.set(index, newValue);
			}
			System.out.println(game.get(index));
			index = index + 1;

		}
		
		int eval = 0;
		
		if (player == "1"){		//player 1 has the lower end ofthe mancala
			eval = (int)game.get(myMancalaIndex) - (int)game.get(oppositionMancalaIndex);
			}
		else if (player == "2"){
			eval = (int)game.get(oppositionMancalaIndex) - (int)game.get(myMancalaIndex);
			}
		else{
			System.out.println("There is an issue with assignment of opposition's mancala.");
		}
		
		System.out.println("the eval of this board is" +eval);
		
		return eval;


	}
}
