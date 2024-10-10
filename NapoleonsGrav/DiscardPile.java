


// Kasthögens klass
	class DiscardPile extends Pile {


    public DiscardPile(int x, int y) {
        super(x, y);

    }


	// Vilket kort som helst kan läggas till
    @Override
    public boolean canAddCard(Card card) {
        return true;
    }


	// Kort kan plockas bort så länge högen inte är tom
    @Override
    public boolean canDrawCard() {
        return !isEmpty() || !cards.isEmpty();
    }

}