import java.util.ArrayList;

public class Player {

    private Hand generalHand;
    private Hand constrainedHand;
    private Hand hiddenHand;
    private final String name;
    private final boolean isCpu;

    /**
     * Probably nicer way to do isCpu functionality.
     * @param name
     * @param isCpu
     */
    public Player(String name, boolean isCpu) {
        generalHand = new Hand(HandType.Regular);
        constrainedHand = new Hand(HandType.Constrained);
        hiddenHand = new Hand(HandType.Hidden);
        this.name = name;
        this.isCpu = isCpu;
    }

    public Hand getGeneralHand() {
        return this.generalHand;
    }

    public Hand getConstrainedHand() {
        return this.constrainedHand;
    }

    public Hand getHiddenHand() {
        return this.hiddenHand;
    }

    public String getName() {
        return this.name;
    }

    public void addToHand(Card card, HandType type) {
        switch(type) {
            case Regular -> generalHand.addCard(card);
            case Constrained -> constrainedHand.addCard(card);
            case Hidden -> hiddenHand.addCard(card);
        }
    }

    public void addToGeneral(ArrayList<Card> cards) {
        generalHand.addCards(cards);
    }

}
