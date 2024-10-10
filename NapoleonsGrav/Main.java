

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.JMenuBar;


public class Main extends JPanel implements MouseListener {
    
	private final static ArrayList<Pile> piles = new ArrayList<Pile>(); // Array för alla högar på spelpanelen
	private final ArrayList<Card> cardDeck = new ArrayList<Card>(); // Array för kortleken
	
	// Initiella x- och y-koordinater för högar
	static int xInitial = 75;
	static int yInitial = 75;
	
	
	// Avstånd mellan högar
	static int xSpacing = Pile.panelW + 50; 
	static int ySpacing = Pile.panelH + 50;
	
	private static JFrame frame; // Huvudfönstret
	
	private Pile selectedPile = null;
	private boolean finalAttempt = false; // "Går inte patiensen ut efter första genomgången av talongen, får man köra igenom talongen en gång till."

	// Initaliserar GUI
	Main(ArrayList<Card> cardDeck, JFrame frame){
		this.cardDeck.addAll(cardDeck);
		this.frame = frame;
		setBackground(new Color(0, 100, 0)); // Färg på bakgrunden blir mörkgrön
		createCardPiles();
		initializeDeckPile();
		createMenu();
		addMouseListener(this);
	}
	
	
	// Skapar högar i 3x3 tabell. Parking, Deck och Discard befinner sig vid sidan om "tabellen"
	public void createCardPiles(){
		// Översta raden
		piles.add(new GuardPile(xInitial, yInitial)); // NV
		piles.add(new FoundationPile(xInitial + xSpacing, yInitial)); // Nord
		piles.add(new GuardPile(xInitial + xSpacing * 2, yInitial)); //NÖ	
		
		// Mittersta raden
		piles.add(new FoundationPile(xInitial, yInitial + ySpacing)); // Väst
		piles.add(new MiddlePile(xInitial + xSpacing, yInitial + ySpacing)); // Mitten
		piles.add(new FoundationPile(xInitial + xSpacing * 2, yInitial + ySpacing)); // Öst	
		
		// Nedersta raden
		piles.add(new GuardPile(xInitial, yInitial + ySpacing * 2)); // SV
		piles.add(new FoundationPile(xInitial + xSpacing, yInitial + ySpacing * 2)); // Syd
		piles.add(new GuardPile(xInitial + xSpacing * 2, yInitial + ySpacing * 2)); // SÖ
		
		piles.add(new ParkingPile(xInitial + xSpacing * 3, yInitial + ySpacing * 2)); // Bredvid SÖ
		piles.add(new DeckPile(xInitial + xSpacing * 4, yInitial + ySpacing * 2)); // Bredvid Parking	
		piles.add(new DiscardPile(xInitial + xSpacing * 3, yInitial)); // Bredvid NÖ

		
	}
	
	// Initialiserar Deck högen
	public void initializeDeckPile(){
		piles.forEach(pile ->{
			pile.clear();
			pile.setSelectedP(false);
			pile.setPotentialPile(false);	
		});
		
		Pile deckPile = piles.get(10);
		for (Card card : cardDeck){
			if(card.isFaceUp()){
				card.flip();
			}
			deckPile.addCard(card);
		}
		deckPile.shuffle();
	}
	
	
	private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        // Restart option
        JMenuItem restartItem = new JMenuItem("Restart");
        restartItem.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                restartGame();
				repaint();
            }
        });

        // End game option
        JMenuItem endGameItem = new JMenuItem("End Game");
        endGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endGame();
				repaint();
            }
        });

        gameMenu.add(restartItem);
        gameMenu.add(endGameItem);
        menuBar.add(gameMenu);
        frame.setJMenuBar(menuBar);
    }
	
	
	public void restartGame(){
		initializeDeckPile();
		finalAttempt = false;
		repaint();
	}
	
	
	public void endGame(){
		int option = JOptionPane.showOptionDialog(null, "Game over.", "Would you like to play again?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Restart", "Exit"}, "Restart");
        if (option == JOptionPane.YES_OPTION) {
            restartGame(); // Restart the game
        } else {
            System.exit(0); // Exit the application
        }
	}
		
		
	// Initialiserar korten
	public static ArrayList<Card> initializeCards(){
		ArrayList<Card> cardDeck = new ArrayList<Card>();
		
		for(String suit : Card.Suit){
			for(int rank : Card.Rank){
				String imgPath = suit + "";
				imgPath += String.valueOf(rank);
				imgPath += ".gif";
				
				Card card = new Card(rank, suit, 0, 0);
				card.setCardImg(imgPath);
				cardDeck.add(card);
			}
			
		}
		return cardDeck;
	}
	
	
	public Pile findPile(int x, int y){
		for(Pile p : piles){
			if(p.contains(x, y)){
				return p;
			}
		} 
		return null;
	}
	
	
	// Spelets logik
	public void checkGameState(){
		Pile deck = piles.get(10);
		Pile discard = piles.get(11);
		
		if (deck.isEmpty() && !finalAttempt){
			while(!discard.isEmpty()){
				deck.addCard(discard.removeCard());
				deck.getCard().flip();
			}
			finalAttempt = true;
		}
		
		boolean g1, g2, g3, g4, m1; // Kontroll om Guard & Middle högen är färdiga
		
		g1 = piles.get(0).isFinished();
		g2 = piles.get(2).isFinished();
		g3 = piles.get(6).isFinished();
		g4 = piles.get(8).isFinished();
		m1 = piles.get(4).isFinished();
		
		if(g1 && g2 && g3 && g4 && m1){
			JOptionPane.showMessageDialog(this, "Game won!");
			restartGame();
		}
		
	}
	
	// Ritar panelerna samt bokstav- och nummervärde för respektive rad och kolumn.
	public void paintComponent (Graphics g){
		super.paintComponent(g);
		piles.forEach(pile -> pile.draw(g));
	
		// Textfärg
		g.setColor(Color.WHITE);
		// Rader
		g.drawString("A", xInitial / 3, yInitial + Pile.panelH - 20);
        g.drawString("B", xInitial / 3, yInitial + ySpacing + Pile.panelH - 22);
        g.drawString("C", xInitial / 3, yInitial + ySpacing * 2 + Pile.panelH - 22);
		// Kolumner
        g.drawString("1", xInitial + Pile.panelW / 3, 40);
        g.drawString("2", xInitial + xSpacing + Pile.panelW / 3, 40);
        g.drawString("3", xInitial + xSpacing * 2 + Pile.panelW / 3, 40);
		// Diverse paneler
		g.drawString("Parking", xInitial + (xSpacing * 3) + 15, yInitial + ySpacing + 120);
		g.drawString("Deck", xInitial + (xSpacing * 4) + 20, yInitial + ySpacing + 120);
		g.drawString("Discard", xInitial + (xSpacing * 3) + 15, yInitial - 7);
		

	}
	
	
	// Mouse listener: logik
	public void mouseClicked(MouseEvent e){
		int x = e.getX();
		int y = e.getY();
		Pile currentPile = findPile(x, y);
		
		if(currentPile != null){
			if(currentPile.canDrawCard() && !currentPile.potentialPile()){
				if(selectedPile != null ){
				selectedPile.setSelectedP(false);
				}
				selectedPile = currentPile;
				currentPile.setSelectedP(true);
				if(!currentPile.getCard().isFaceUp()){
					currentPile.getCard().flip();
				}
				for (Pile pile : piles){
					if (currentPile == pile) {
						continue;
					}
					if(pile.canAddCard(currentPile.getCard())){
						pile.setPotentialPile(true);
					}
				}
			} else if (selectedPile != null && currentPile.canAddCard(selectedPile.getCard())){
				currentPile.addCard(selectedPile.removeCard());
				selectedPile.setSelectedP(false);
				selectedPile = null;
				for (Pile pile : piles){
					pile.setPotentialPile(false);
				}
			}
		} else {
			selectedPile = null;
			for (Pile pile : piles){
				pile.setPotentialPile(false);
				pile.setSelectedP(false);
			}
		}
		repaint();
		checkGameState();
	}
	
 
	public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
	

	// Huvudmetoden
	public static void main(String[] args){
		ArrayList<Card> cards = initializeCards();
		JFrame frame = new JFrame("Napoleons Grav");
		JPanel panel = new Main(cards, frame);
		
		frame.getContentPane().add(panel);
		frame.setBounds(0, 0, 800, 550);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}