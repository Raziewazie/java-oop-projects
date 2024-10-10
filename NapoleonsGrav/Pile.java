

import java.awt.event.*;
import java.util.*;
import java.awt.*;

public abstract class Pile  {
    
	
	public final static int panelH = 75;
	public final static int panelW = 100;
	
	protected final Stack<Card> cards;
	
	protected int x;
	protected int y;
	
	private boolean canAddToPile; // En flagga som indikerar om kort kan läggas till högen
	private boolean selectedPile; // En flagga som indikerar om högen är vald
    
	
	// Initialiserar variabler
    public Pile(int x, int y) {
		this.x = x;
		this.y = y;
		
		cards = new Stack<>();
		canAddToPile = false;
		selectedPile = false;
	}
	
	
	public int getX(){
		return x;
	} 

	
	public int getY(){
		return y;
	} 
	
	
	public int pileSize() {
		return cards.size(); 
    }
	
	
	public final void clear(){
		cards.clear();
	}
    
	
    public boolean isEmpty() {
        return cards.isEmpty(); // Är högen tom?
    }
    
	
    public Card getCard() {
        if(cards.isEmpty()){
			return null;
		}
		return cards.peek(); // Visar det översta kortet utan att det tas bort från den valda högen
    }
	
	
	public void shuffle(){
		Collections.shuffle(cards);
	}
	
	
	public Card removeCard(){
		return cards.pop();
	}
	
	
	public void addCard(Card card){
		
		card.setPosition(x, y);
		cards.push(card);
	}

	
	// Kontrollerar om givna koordinater befinner sig inom högen
    public boolean contains(int x1, int y1) {
        return x <= x1 && x1 <= x + panelW
                && y <= y1 && y1 <= y + panelH;
    }	
	
	
	public void setPotentialPile(boolean canAddToPile){
		this.canAddToPile = canAddToPile; // Kan kortet läggas till högen?
	}
	
	
	public void setSelectedP(boolean selectedPile){
		this.selectedPile = selectedPile;
	}
	
	
	public boolean potentialPile(){
		return canAddToPile; // Kan högen acceptera kortet?
	}


    public void draw(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, panelH, panelW);

        if (selectedPile) { // Belyser valda högen med gul färg
            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(new Color(250, 250, 0));
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawRect(x, y, panelH, panelW);
        }


        if (canAddToPile) { // Belyser högen som kan acceptera kortet - grön färg
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(0, 255, 0));
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawRect(x, y, panelH, panelW);
        }
	
		
		int yOffset = 3;
		int count = 0;
		
		for (int i = cards.size() - 2; i >= 0; i--) { // Andra kortet på högen (nedersta) är den som användaren tar
			Card card = cards.get(i);
			if (count < 1) {
				card.draw(g, x, y + yOffset); // Justerar kortet längs y-axeln så att kortet är centrerad inom panelen
				yOffset += Card.height / 9; // Justerar utrymme mellan första och andra kortet på högar som tillåter fler än ett kort.
											//	Ändra 9:an till något mindre siffra så skapas mer utrymme, höj nummret för motsatt effekt
				count++;
			} else {
				break; // Vid count < 1 ritas endast 2 kort. Count < 2 ritar tre kort osv.
			}
		}

		// Andra kortet på högen (nedersta) är den som användaren tar
		if (!cards.isEmpty()) {
			Card topCard = cards.peek(); // Visar valda kortet utan att kortet tas bort från högen
			topCard.draw(g, x, y + yOffset);
		}
		
    }
	
	
	// Kontroll om högen är tom/färdig (Kung och Ess-högen, dvs GuardPile och MiddlePile skall vara färdiga)
	public boolean isFinished(){
		return cards.isEmpty();
	}
    
	
    public abstract boolean canAddCard(Card card);
	public abstract boolean canDrawCard();
      
}