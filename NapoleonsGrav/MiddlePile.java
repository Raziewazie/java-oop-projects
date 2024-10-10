


// Klass för mittersta högen där 6:an läggs först
class MiddlePile extends Pile {


    public MiddlePile(int x, int y) {
        super(x, y);

    }

	// Samma princip som GuardPile, fast i fallande sekvens. Man placerar en 6:a och jobbar ner till esset
    public boolean canAddCard(Card card) {
        if (card == null) {
			return false;
		}
		
        if (card.getRank() == 6) {
            return (cards.isEmpty() || getCard().getRank() == 1 );
        }
        if(!cards.isEmpty()) {
            return getCard().getRank() - 1 == card.getRank();
        }
        return false;
	}
	
	
	// Högen är färdig när alla lämpliga kort har lags till enligt spelets regler
    @Override
    public boolean isFinished() {
        return cards.size() == 24 && getCard().getRank() == 1;
    }

	
	// Kort får inte plockas bort från högen
    public boolean canDrawCard() {
        return false;
    }

	
}