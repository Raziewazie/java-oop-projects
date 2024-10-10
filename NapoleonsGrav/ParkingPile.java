


// Parkering-klassen
// "Kommer en sexa upp, som inte kan placeras i mitten, så får den <<parkeras>> vid sidan om för att användas vid senare tillfälle."
class ParkingPile extends Pile {

	public ParkingPile(int x, int y){
		super(x, y);
	}


	// Tillåter endast ett kort att läggas till Parkering-högen, och det måste vara en 6:a
    public boolean canAddCard(Card card) {
        return cards.isEmpty() && card.getRank() == 6;
    }


    public boolean canDrawCard() {
        return !cards.isEmpty();
    }

 
}