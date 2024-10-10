

import java.util.Scanner;

public class Nm {
	private int remainingMatches;
	private Player[] players;
	private int currentPlayerIndex;
	


	// Initierar spelet med angivna antalet stickor
	public Nm(int initialMatches) {
		remainingMatches = initialMatches;
		players = new Player[2]; // Skapar en array (lista) där jag anger antal spelare.
		players[0] = new ComputerPlayer("Computer"); // Spelare 1 är datorn
		players[1] = new HumanPlayer("Human"); // Spelare 2 är människan
		currentPlayerIndex = 0; // Spelare 1 börjar
	}

	
	// Metod för att påbörja spelet
	public void startGame() {
		System.out.println("Welcome to Nm");
		System.out.println("Player 1: " + players[0]);
		System.out.println("Player 2: " + players[1]);
		System.out.println("Remaining matches: " + remainingMatches);
		
		// While-loopen som är själva "spelet" när programmet körs
		while(remainingMatches > 1) {
			
			Player currentPlayer = players[currentPlayerIndex];
			int matchesToRemove = currentPlayer.getMatchesToRemove(remainingMatches); // Få antalet stickor som ska plockas av spelaren när det är spelarens tur
			if (matchesToRemove < 1 || matchesToRemove > remainingMatches / 2){
				System.out.println("Invalid move. Please choose at least one match and at most " + remainingMatches / 2); // Kontrollerar om spelarens drag är lagligt
				continue; // Spelet ber spelaren att välja ett lagligt drag.
			}
			
			System.out.println(currentPlayer + " removes " + matchesToRemove + " matches.");
			remainingMatches -= matchesToRemove; // Uppdaterar antalet kvarstående stickor 
			System.out.println("Remaining matches: " + remainingMatches);
			switchPlayer();
		}
		
		System.out.println(players[currentPlayerIndex] + " loses, " + players[1- currentPlayerIndex] + " wins.");
		
	}
	
	// Metoden som bestämmer vems tur det är
	private void switchPlayer() {
		currentPlayerIndex = (1 - currentPlayerIndex) % 2;
	}
	
	
	// Main metoden som startar spelet
	public static void main(String[] args) {
	
		int initialMatches = Integer.parseInt(args[0]);
		// Lägger till en "soft cap" på max antal stickor tillåtna samt minst 4 för att undvika automatisk förlust för en av spelarna om remainingMatches <= 3
		if(initialMatches < 4 || initialMatches > 30) {
			
			System.out.println("Invalid number of matches. Start the program with at least 4 or at most 30.");
			return;
		}
		
		Nm game = new Nm(initialMatches);
		game.startGame();
	}
}



abstract class Player {
	protected String name;
	
	public Player(String name) {
		this.name = name;
	}
	
	public abstract int getMatchesToRemove(int remainingMatches);
	
	@Override
	public String toString(){
		return name;
	}
}


// Subklassen Människa där programmet låter människan att ange antalet stickor som ska plockas.
class HumanPlayer extends Player {
	
	public HumanPlayer(String name) {
		super(name);
	}
	
	@Override
	public int getMatchesToRemove(int remainingMatches){
		Scanner scanner = new Scanner(System.in);
		System.out.println("Your move. There are " + remainingMatches + " matches.");
		int matchesToRemove = scanner.nextInt();
		return matchesToRemove;
	}
}


// Subklassen Dator med en enkel algoritm som tillåter den att spela.
class ComputerPlayer extends Player {
	
	public ComputerPlayer(String name) {
		super(name);
	}
	
	@Override
	public int getMatchesToRemove(int remainingMatches){
		return (int) (Math.random() * (Math.min(remainingMatches / 2, 1)) + 1);
	}
}