import java.util.ArrayList;

/**
 * Models a player of Shed. Each player has three hands, a name and a flag stating if they are a user or a cpu.
 */
public class Player {

    /**
     * The starting hand that needs to be empty before the constrained hand can be used.
     */
    private Hand generalHand;

    /**
     * The hand that sits on top of the hidden hand. ALl the cards need to be removed before hidden cards can be played.
     */
    private Hand constrainedHand;

    /**
     * The last hand of cards. The value of these cards can't be seen until they are played. Once this hand is empty, the
     * player has won the game.
     */
    private Hand hiddenHand;

    /**
     * The name of the player.
     */
    private final String name;

    /**
     * True if the player is the computer, else false for a user.
     */
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

    /**
     *
     * @return
     */
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
