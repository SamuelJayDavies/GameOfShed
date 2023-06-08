import java.util.ArrayList;

/**
 * Models a hand of cards that a particular player would hold. Can add/remove/get cards from their hand.
 */
public class Hand {

    /**
     * An arrayList of cards in the hand.
     */
    private ArrayList<Card> cards;

    /**
     * The hand type of the hand. Either General/Constrained/Hidden.
     */
    private final HandType handType;

    /**
     * Creates a hand of cards initially empty.
     * @param handType The hand type of the hand. Either General/Constrained/Hidden.
     */
    public Hand(HandType handType) {
        this.handType = handType;
        cards = new ArrayList<>();
    }

    /**
     * Returns all the cards in the hand.
     * @return An arrayList of the cards in the hand.
     */
    public ArrayList<Card> getCards() {
        return this.cards;
    }

    /**
     * Returns the card from the hand corresponding to the provided index.
     * @param index The position of the card in the hand. Starts from 0.
     * @return The card found in that position in the hand.
     */
    public Card getCard(int index) {
        try{
            return cards.get(index);
        }catch(ArrayIndexOutOfBoundsException error) {
            return null;
        }
    }

    /**
     * Returns the highest card in the hand.
     * @return The highest card in the hand.
     */
    public Card getHighestCard() {
        Card highestCard = cards.get(0);
        for (Card card: cards) {
            if(card.getValue() > highestCard.getValue()) {
                highestCard = card;
            }
        }
        return highestCard;
    }

    public HandType getHandType() {
        return this.handType;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void addCards(ArrayList<Card> cards) {
        for(Card card: cards) {
            this.cards.add(card);
        }
    }

    public int getNumOfCards() {
        return cards.size();
    }

    public void removeCard(Card card) {
        for(Card currentCard: cards) {
            if(currentCard.equals(card)) {
                cards.remove(currentCard);
                return;
            }
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
