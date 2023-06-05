import java.util.ArrayList;
import java.util.Scanner;

public class Shed {

    private Deck drawPile;
    private Deck discardPile;
    private ArrayList<Player> players;

    public Shed(ArrayList<Player> players) {
        this.players = players;
        drawPile = new Deck();
        discardPile = new Deck(-1);
        startGame();
    }

    /**
     * If we wanted a score tracker, have this method return the player that won
     */
    private void startGame() {
        drawPile.shuffle();
        dealCards();
        System.out.println("Player: " + roundStart().getName() + " wins!");
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
    private Player roundStart() {
        for(Player player: players) {
            Hand currentHand = getCurrentHand(player);
            if(discardPile.peekTop().getValue() <= currentHand.getHighestCard().getValue()) {
                // Check for special card here
                discardPile.addCard(selectCard(currentHand));
                if(player.getHiddenHand().getNumOfCards() + player.getConstrainedHand().getNumOfCards() + player.getGeneralHand().getNumOfCards() == 0) {
                    return player;
                }
            } else {
                System.out.println("No available cards to play for " + player.getName() + "\n");
                // Pick up the discard pile
            }
        }
    }

    private Hand getCurrentHand(Player player) {
        if(player.getGeneralHand().getNumOfCards() == 0) {
            return player.getGeneralHand();
        } else if(player.getConstrainedHand().getNumOfCards() == 0) {
            return player.getConstrainedHand();
        } else {
            return player.getHiddenHand();
        }
    }

    private Card selectCard(Hand currentHand) {
        Scanner myReader = new Scanner(System.in);
        System.out.println("Please select which card to play: \n");

        for(int i = 0; i < currentHand.getNumOfCards(); i++) {
            System.out.println(i + ": " + currentHand.getCard(i) + "\n");
        }

        boolean isFinished = false;
        int index = -1;
        while(!isFinished) {
            while(!(myReader.hasNextInt())) {
                System.out.println("Please enter a number. " + "\n");
                myReader.nextLine();
            }
            index = myReader.nextInt();
            if(myReader.nextInt() > (currentHand.getNumOfCards()-1)) {
                isFinished = true;
            } else {
                System.out.println("Please enter a number between 0 and " + (currentHand.getNumOfCards()-1) + "\n");
            }
        }
        return currentHand.getCard(index);
    }
}
