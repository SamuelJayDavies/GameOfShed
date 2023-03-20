public class Card {

    private int value;
    private String cardName;
    private Suit suit;

    final static String[] cardNames = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven",
                                       "Eight", "Nine", "Ten", "Jack", "Queen", "King"};

    public Card(int value, Suit suit) {
        this.value = value;
        this.cardName = cardNames[value-1];
        this.suit = suit;
    }

    public int getValue() {
        return value;
    }

    public String getCardName() {
        return cardName;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return cardName + " of " + suit;
    }
}
