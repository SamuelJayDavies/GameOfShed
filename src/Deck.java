import java.util.ArrayList;
import java.util.Random;

public class Deck {

    static final int DECK_SIZE = 52;

    private ArrayList<Card> cards;

    /**
     * Generate deck shouldn't be in the constructor, "Discard Pile starts empty"
     */
    public Deck() {
        cards = generateDeck();
    }

    public ArrayList<Card> generateDeck() {
        ArrayList<Card> cards = new ArrayList<>();
        for(Suit suit: Suit.values()) {
            for(int i=1; i<=13; i++) {
                cards.add(new Card(i, suit));
            }
        }
        return cards;
    }

    public void shuffle() {
        Random random = new Random();
        int deckSize = cards.size();
        for(int j=deckSize; j>0; j--) {
            int position = random.nextInt(j);
            cards.add(cards.get(position));
            cards.remove(cards.get(position));
        }
    }

    public Card peekTop() {
        return cards.get(0);
    }

    public Card deal() {
        Card currentCard = cards.get(0);
        cards.remove(currentCard);
        return currentCard;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void addCards(ArrayList<Card> cards) {
        for(Card card: cards) {
            this.cards.add(card);
        }
    }

    @Override
    public String toString() {
        String msg = "";
        for (Card card: cards) {
            msg += card + "\n";
        }
        return msg;
    }

}
