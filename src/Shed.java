import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main class where the game of shed starts. Contains methods for dealing cards, starting a round and getting the
 * correct hand from the player.
 */
public class Shed {

    /**
     * The pile that is drawn from at the beginning of the game.
     */
    private Deck drawPile;

    /**
     * The pile that cards are played onto, for a valid play the card has to be higher value than the last card in this
     * pile.
     */
    private Deck discardPile;

    /**
     * All the players in the game.
     */
    private ArrayList<Player> players;

    /**
     * Constructor for Shed that creates a draw and discard pile while also starting the game.
     * @param players The players that will be participating in the game.
     */
    public Shed(ArrayList<Player> players) {
        this.players = players;
        drawPile = new Deck(DeckType.DRAW);
        discardPile = new Deck(DeckType.DISCARD);
        startGame();
    }

    /**
     * If we wanted a score tracker, have this method return the player that won
     */
    private void startGame() {
        drawPile.shuffle();
        dealCards();
        int n = 1;
        System.out.println("Round " + n);
        System.out.println(this.getCurrentState());
        while(!(roundStart())) {
            System.out.println("Round " + (n+1));
            System.out.println(this.getCurrentState());
            n++;
        }
        System.out.println("Game Over");
    }

    private void dealCards() {
        for(int i = 0; i < 3; i++) {
            for (Player player : players) {
                receiveCard(player, drawPile.deal(), HandType.Hidden);
            }
        }
        for(int i = 0; i < 3; i++) {
            for (Player player : players) {
                receiveCard(player, drawPile.deal(), HandType.Constrained);
            }
        }
        for(int i = 0; i < 3; i++) {
            for(Player player: players) {
                receiveCard(player, drawPile.deal(), HandType.Regular);
            }
        }
    }

    private void receiveCard(Player player, Card card, HandType type) {
        player.addToHand(card, type);
    }

    private void receiveCards(Player player, ArrayList<Card> cards) {
        player.addToGeneral(cards);
    }

    /**
     * Make this method return Player that won?
     */
    private boolean roundStart() {
        for(Player player: players) {
            Card topCard = discardPile.peekTop();
            System.out.println("Discard Pile's Card : " + ((topCard == null) ? "empty" : topCard) + "\n");
            Hand currentHand = getCurrentHand(player);
            System.out.print(player.getName() + ", ");
            Card cardToPlay = selectCard(currentHand);
            if(cardToPlay != null && ((discardPile.isEmpty() || (discardPile.peekTop().getValue() <= cardToPlay.getValue() && discardPile.peekTop().getValue() != 7)) ||
                    (cardToPlay.getValue() == 2 || cardToPlay.getValue() == 10))) {

                playCard(cardToPlay, currentHand);
                System.out.println(player.getName() + " has played " + cardToPlay);
                if(player.getHiddenHand().getNumOfCards() + player.getConstrainedHand().getNumOfCards() + player.getGeneralHand().getNumOfCards() == 0) {
                    System.out.println(player.getName() + " wins!");
                    return true;
                }
            } else if(cardToPlay == null || (discardPile.peekTop().getValue() > cardToPlay.getValue() || discardPile.peekTop().getValue() == 7)) {
                // Add more sout for when the user plays a lower card
                if(discardPile.isEmpty()) {
                    System.out.println(player.getName() + " picks up nothing.\n");
                } else if(cardToPlay == null){
                    System.out.println(player.getName() + " picks up the discard pile.\n" );
                    player.addToGeneral(discardPile.getCards());
                    discardPile.empty();
                } else {
                    System.out.println(player.getName() + "'s card is not suitable.\n");
                    System.out.println(player.getName() + " picks up the discard pile.\n" );
                    player.addToGeneral(discardPile.getCards());
                    discardPile.empty();
                }
            }
        }
        return false;
    }

    private void playCard(Card cardToPlay, Hand currentHand) {

        discardPile.addCard(cardToPlay);
        currentHand.removeCard(cardToPlay);

        if(cardToPlay.getValue() == 10) {
            discardPile.empty();
            System.out.println("Discard deck has been cleared\n");
            // Add ability to play another card here
        }
    }

    private Hand getCurrentHand(Player player) {
        if(player.getGeneralHand().getNumOfCards() != 0) {
            return player.getGeneralHand();
        } else if(player.getConstrainedHand().getNumOfCards() != 0) {
            return player.getConstrainedHand();
        } else {
            return player.getHiddenHand();
        }
    }

    private Card selectCard(Hand currentHand) {
        Scanner myReader = new Scanner(System.in);
        this.getCurrentState();
        System.out.println("please select which card to play: \n");
        int i;
        for(i=0; i < currentHand.getNumOfCards(); i++) {
            Card currentCard = currentHand.getCard(i);
            if(currentHand.getHandType().equals(HandType.Hidden)) {
                System.out.println(i + ": Unknown Card");
            } else {
                System.out.println(i + ": " + currentCard + "\n");
            }
        }

        System.out.println(i++ + ": Pick Up Discard Pile");

        boolean isFinished = false;
        int index = -1;
        while(!isFinished) {
            while(!(myReader.hasNextInt())) {
                System.out.println("Please enter a number. " + "\n");
                myReader.nextLine();
            }
            index = myReader.nextInt();
            if(index <= (currentHand.getNumOfCards())) {
                isFinished = true;
            } else {
                System.out.println("Please enter a number between 0 and " + (currentHand.getNumOfCards()) + "\n");
            }
        }
        return index < currentHand.getNumOfCards() ? currentHand.getCard(index) : null;
    }

    private String getCurrentState() {
        Card topCard = discardPile.peekTop();
        String result = "Discard Pile's Card : " + ((topCard == null) ? "empty" : topCard) + "\n";
        for(Player player : players) {
            result += "Player " + player.getName() + " has the following cards:\n";
            result += "Hidden: " + player.getHiddenHand().getNumOfCards() + ", Constrained: " + player.getConstrainedHand().getNumOfCards() +
                    ", General: " + player.getGeneralHand().getNumOfCards() + "\n\n";
        }
        return result;
    }
}
