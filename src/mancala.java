import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


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
		int returnedValue;

		returnedValue = minValue(game, myPlayer);

		return 0;
	}


	static int minValue(List game, String player){

		play(game, player, 6);


		return 0;
	}

	static int play(List game, String player, int index){

		int noOfCoins = (int)game.get(index);
		
		int oppositionMancalaIndex = 0;		//initializing the variable
		int myMancalaIndex = 0;
		int gameArraySize = game.size();
		
		if (player == "1"){		//player 1 has the lower end ofthe mancala
			oppositionMancalaIndex = (gameArraySize / 2) - 1;
			myMancalaIndex = gameArraySize - 1;
			}
		else if (player == "2"){
			oppositionMancalaIndex = gameArraySize - 1;
			myMancalaIndex = (gameArraySize / 2) - 1;
			}
		else{
			System.out.println("There is an issue with assignment of opposition's mancala.");
		}

		System.out.println(noOfCoins);

		//device a strategy to just traverse thru both the player's
		//boxes and mancala until move is over

		System.out.println(game);
		//System.out.println(noOfCoins);

		for (int iter = 0; iter < noOfCoins; iter++){
			if (index == game.size()){
				index = 0;
			}
			
			if (index == oppositionMancalaIndex){
				
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
			
			}
		else if (player == "2"){
			eval = (int)game.get(oppositionMancalaIndex) - (int)game.get(myMancalaIndex);
			}
		else{
			System.out.println("There is an issue with assignment of opposition's mancala.");
		}
		
		return eval;


	}
}
