

	class GuardPile extends Pile {
 

    public GuardPile(int x, int y) {
        super(x, y);
        
    }
	
	// Kort med värde 7 och högre är tillåtna, stigande sekvens
    public boolean canAddCard(Card card) {
        if (card == null) {
			return false;
		}
		if(cards.isEmpty()){
			return card.getRank() == 7;
		}
		return getCard().getRank() + 1 == card.getRank();
    }

	
	// Kort kan inta plockas bort från högen
    public boolean canDrawCard() {
        return false;
    }


	// Högen är färdig när kungen läggs
	@Override
    public boolean isFinished() {
        return cards.size() == 7 && getCard().getRank() == 13;
    }
	

}