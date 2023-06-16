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
     *
     * @param players The players that will be participating in the game.
     */
    public Shed(ArrayList<Player> players) {
        this.players = players;
        drawPile = new Deck(DeckType.DRAW);
        discardPile = new Deck(DeckType.DISCARD);
        startGame(getGameType());
    }

    /**
     * Method that returns the game type that the user selects.
     *
     * @return The game type of the game that is going to be played.
     */
    private GameType getGameType() {
        Scanner myReader = new Scanner(System.in);
        System.out.println("Welcome to Shed, which game mode do you want to play\n");
        System.out.println("1: Basic Fast Track \n2: Basic \n3: Regular Fast Track \n4: Regular");


        boolean isFinished = false;
        GameType selectedGameMode = null;
        while (!isFinished) {
            while (!(myReader.hasNextInt())) {
                System.out.println("Please enter a number. " + "\n");
                myReader.nextLine();
            }
            int option = myReader.nextInt();
            switch (option) {
                case 1:
                    selectedGameMode = GameType.BasicFast;
                case 2:
                    selectedGameMode = GameType.Basic;
                case 3:
                    selectedGameMode = GameType.RegularFast;
                case 4:
                    selectedGameMode = GameType.Regular;
                default:
                    System.out.println("Please enter a number that corresponds to one of the game modes");
            }

            isFinished = selectedGameMode != null;
        }
        return selectedGameMode;
    }

    /**
     * Starts a game of Shed. One execution will play a whole game of Shed.
     */
    private void startGame(GameType gameMode) {
        drawPile.shuffle();
        dealCards();
        int n = 1;
        System.out.println("Round " + n);
        System.out.println(this.getCurrentState());
        while (!(roundStart())) {
            System.out.println("Round " + (n + 1));
            System.out.println(this.getCurrentState());
            n++;
        }
        System.out.println("Game Over");
    }

    /**
     * Deals the cards for each player in the players list. Populates their regular, constrained and general hand with
     * 3 cards.
     */
    private void dealCards() {
        for (int i = 0; i < 3; i++) {
            for (Player player : players) {
                receiveCard(player, drawPile.deal(), HandType.Hidden);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (Player player : players) {
                receiveCard(player, drawPile.deal(), HandType.Constrained);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (Player player : players) {
                receiveCard(player, drawPile.deal(), HandType.Regular);
            }
        }
    }

    /**
     * Passes a card into a players correct hand.
     *
     * @param player The Player receiving the card.
     * @param card   The card the player is receiving.
     * @param type   The hand type the card belongs to.
     */
    private void receiveCard(Player player, Card card, HandType type) {
        player.addToHand(card, type);
    }

    /**
     * Adds multiple cards to the general hand of the player.
     *
     * @param player The player receiving the cards.
     * @param cards  The cards being received.
     */
    private void receiveCards(Player player, ArrayList<Card> cards) {
        player.addToGeneral(cards);
    }

    /**
     * Acts as one round of Shed.
     *
     * @return Returns true if a player has won, else returns false to start another round.
     */
    private boolean roundStart() {
        for (Player player : players) {
            Card topCard = discardPile.peekTop();
            System.out.println("Discard Pile's Card : " + ((topCard == null) ? "empty" : topCard) + "\n");
            Hand currentHand = getCurrentHand(player);
            System.out.print(player.getName() + ", ");
            Card cardToPlay = selectCard(currentHand);
            if (cardToPlay != null && ((discardPile.isEmpty() || (discardPile.peekTop().getValue() <= cardToPlay.getValue() && discardPile.peekTop().getValue() != 7)) ||
                    (cardToPlay.getValue() == 2 || cardToPlay.getValue() == 10))) {

                playCard(cardToPlay, currentHand);
                System.out.println(player.getName() + " has played " + cardToPlay);
                if (player.getHiddenHand().getNumOfCards() + player.getConstrainedHand().getNumOfCards() + player.getGeneralHand().getNumOfCards() == 0) {
                    System.out.println(player.getName() + " wins!");
                    return true;
                }
            } else if (cardToPlay == null || (discardPile.peekTop().getValue() > cardToPlay.getValue() || discardPile.peekTop().getValue() == 7)) {
                // Add more sout for when the user plays a lower card
                if (discardPile.isEmpty()) {
                    System.out.println(player.getName() + " picks up nothing.\n");
                } else if (cardToPlay == null) {
                    System.out.println(player.getName() + " picks up the discard pile.\n");
                    player.addToGeneral(discardPile.getCards()); // Look into changing this method to the receiveCards version
                    discardPile.empty();
                } else {
                    System.out.println(player.getName() + "'s card is not suitable.\n");
                    System.out.println(player.getName() + " picks up the discard pile.\n");
                    player.addToGeneral(discardPile.getCards());
                    discardPile.empty();
                }
            }
        }
        return false;
    }

    /**
     * Plays a card by placing it into the discard pile.
     *
     * @param cardToPlay  The card that is being played.
     * @param currentHand The hand the card came from.
     */
    private void playCard(Card cardToPlay, Hand currentHand) {

        discardPile.addCard(cardToPlay);
        currentHand.removeCard(cardToPlay);

        if (cardToPlay.getValue() == 10) {
            discardPile.empty();
            System.out.println("Discard deck has been cleared\n");
            // Add ability to play another card here
        }
    }

    /**
     * Gets the current hand the player can use, based on if the general hand is empty or the constrained hand is empty.
     *
     * @param player The player whose turn it currently is.0
     * @return The hand that they are currently on in the game.
     */
    private Hand getCurrentHand(Player player) {
        if (player.getGeneralHand().getNumOfCards() != 0) {
            return player.getGeneralHand();
        } else if (player.getConstrainedHand().getNumOfCards() != 0) {
            return player.getConstrainedHand();
        } else {
            return player.getHiddenHand();
        }
    }

    /**
     * SelectCard will read out the players available cards and read in which one they want to play. It will only let them
     * specify the index of an available card, and if an invalid card is chosen, they will automatically pick up the pile.
     *
     * @param currentHand The hand the card is being selected from.
     * @return The chosen valid card.
     */
    private Card selectCard(Hand currentHand) {
        Scanner myReader = new Scanner(System.in);
        this.getCurrentState();

        System.out.println(getCardChoices(currentHand));

        boolean isFinished = false;
        int index = -1;
        while (!isFinished) {
            while (!(myReader.hasNextInt())) {
                System.out.println("Please enter a number. " + "\n");
                myReader.nextLine();
            }
            index = myReader.nextInt();
            if (index <= (currentHand.getNumOfCards())) {
                isFinished = true;
            } else {
                System.out.println("Please enter a number between 0 and " + (currentHand.getNumOfCards()) + "\n");
            }
        }
        return index < currentHand.getNumOfCards() ? currentHand.getCard(index) : null;
    }

    private String getCardChoices(Hand currentHand) {
        String stringMsg = "please select which card to play: \n";
        int i;
        for (i = 0; i < currentHand.getNumOfCards(); i++) {
            Card currentCard = currentHand.getCard(i);
            if (currentHand.getHandType().equals(HandType.Hidden)) {
                stringMsg += i + ": Unknown Card";
            } else {
                stringMsg += i + ": " + currentCard + "\n";
            }
        }

        stringMsg += i++ + ": Pick Up Discard Pile";
        return stringMsg;
    }

    /**
     * Returns the count of each player's cards in each of their hands.
     *
     * @return The count of each player's cards in each of their hands.
     */
    private String getCurrentState() {
        Card topCard = discardPile.peekTop();
        String result = "Discard Pile's Card : " + ((topCard == null) ? "empty" : topCard) + "\n";
        for (Player player : players) {
            result += "Player " + player.getName() + " has the following cards:\n";
            result += "Hidden: " + player.getHiddenHand().getNumOfCards() + ", Constrained: " + player.getConstrainedHand().getNumOfCards() +
                    ", General: " + player.getGeneralHand().getNumOfCards() + "\n\n";
        }
        return result;
    }
}
