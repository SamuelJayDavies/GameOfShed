import java.util.ArrayList;

public class Hand {

    private ArrayList<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public ArrayList<Card> getCards() {
        return this.cards;
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
