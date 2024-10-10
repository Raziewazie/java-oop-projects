

// Klassen som representerar talongen där man drar kort ifrån - kortens baksida visas
class DeckPile extends Pile {
	
	public DeckPile(int x, int y){
		super(x, y);
		
	}
	
	// Inga kort kan läggas till
	public boolean canAddCard(Card card){
		return false;
	}
	
	
	
	public boolean canDrawCard(){
		return !cards.isEmpty();
	}


}