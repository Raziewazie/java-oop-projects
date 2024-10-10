
// Klass för öppna fyrkanten
public class FoundationPile extends Pile {
    public FoundationPile(int x, int y) {
        super(x, y);
    }


    // Ett kort kan läggas när högen är tom.
    public boolean canAddCard(Card card) {
        return cards.isEmpty();
    }
	
	
    // Kan plocka kortet om högen inte är tom
    public boolean canDrawCard() {
        return !cards.isEmpty();
    }
	
	
 
}
