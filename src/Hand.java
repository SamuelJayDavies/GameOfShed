import java.util.ArrayList;

/**
 * Models a hand of cards that a particular player would hold. Can add/remove/get cards from their hand.
 */
public class Hand {

    private ArrayList<Card> cards;
    private final HandType handType;

    public Hand(HandType handType) {
        this.handType = handType;
        cards = new ArrayList<>();
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public Card getCard(int index) {
        try{
            return cards.get(index);
        }catch(ArrayIndexOutOfBoundsException error) {
            return null;
        }
    }

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
