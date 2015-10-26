import java.util.ArrayList;
import java.util.List;


public class state {
	private List<Integer> game = new ArrayList<Integer>();
	private boolean bonusChance = false;
	private boolean gameOver = false;

	public state(List<Integer> game, boolean bonusChance){
		this.game = game;
		this.bonusChance = bonusChance;
	}
	public List<Integer> getGameList(){
		return game;
	}
	public boolean getBonusChance() {
		return bonusChance;
	}
	public void setBonusChance(boolean bonusChance) {
		this.bonusChance = bonusChance;
	}
	public void setGameList(List<Integer> game) {
		this.game = game;
	}
	public boolean getGameOver() {
		return gameOver;
	}
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
}
