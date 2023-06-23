import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
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
        startGame(setGameType());
    }

    /**
     * Method that returns the game type that the user selects.
     *
     * @return The game type of the game that is going to be played.
     */
    private GameType setGameType() {
        Scanner myReader = new Scanner(System.in);
        System.out.println("Welcome to Shed, which game mode do you want to play\n");
        System.out.println("1: Basic Fast Track \n2: Basic \n3: Regular Fast Track \n4: Regular\n5: Help");


        boolean isFinished = false;
        GameType selectedGameMode = null;
        while (!isFinished) {
            while (!(myReader.hasNextInt())) {
                System.out.println("Please enter a number. " + "\n");
                myReader.nextLine();
            }
            int option = myReader.nextInt();
            switch (option) {
                case 1 -> selectedGameMode = GameType.BasicFast;
                case 2 -> selectedGameMode = GameType.Basic;
                case 3 -> selectedGameMode = GameType.RegularFast;
                case 4 -> selectedGameMode = GameType.Regular;
                case 5 -> System.out.println(getHelpScreen());
                default -> System.out.println("Please enter a number that corresponds to one of the game modes");
            }

            isFinished = selectedGameMode != null;
        }
        return selectedGameMode;
    }

    private String getHelpScreen() {
        File helpTxt = new File("src/helpTxt");
        String result = "";
        try{
            Scanner myReader = new Scanner(helpTxt);
            while(myReader.hasNextLine()) {
                result += myReader.nextLine() + "\n";
            }
        }catch(FileNotFoundException error) {
            System.out.println("File not found, please enter a number");
        }
        return result;
    }

    /**
     * Starts a game of Shed. One execution will play a whole game of Shed.
     */
    private void startGame(GameType gameMode) {
        drawPile.shuffle();
        dealCards();
        int n = 1;
        System.out.println("Round " + n);
        System.out.println(this.getCurrentState(gameMode));
        while (!(roundStart(gameMode))) {
            if ((gameMode.equals(GameType.Basic) || gameMode.equals(GameType.Regular)) && (!(drawPile.isEmpty()))) {
                preGameDraw();
            }
            System.out.println("Round " + (n + 1));
            System.out.println(this.getCurrentState(gameMode));
            n++;
        }
        System.out.println("Game Over");
    }

    private void preGameDraw() {
        for (Player player : players) {
            if (player.getGeneralHand().getNumOfCards() < 3) {
                ArrayList<Card> cards = new ArrayList<>();
                for(int i=3; i>player.getGeneralHand().getNumOfCards(); i--) {
                    if(!(drawPile.isEmpty())) {
                        cards.add(drawPile.deal());
                    }
                }
                receiveCards(player, cards);
            }
        }
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
    private boolean roundStart(GameType gameMode) {
        for (Player player : players) {
            Card topCard = discardPile.peekTop();
            System.out.println("Discard Pile's Card : " + ((topCard == null) ? "empty" : topCard) + "\n");
            Hand currentHand = getCurrentHand(player);
            currentHand.sortHand();

            // For when multiple cards need to be played on the same turn
            boolean isTurnOver;
            do{
                isTurnOver = true;
                // Fix this, messy
                if (!(player.getIsCpu())) {
                    System.out.print(player.getName() + ", ");
                }

                Card cardToPlay = selectCard(currentHand, player.getIsCpu());

                if (isCardPlayable(cardToPlay)) {
                    playCard(cardToPlay, currentHand, player);
                    if (player.getHiddenHand().getNumOfCards() + player.getConstrainedHand().getNumOfCards() + player.getGeneralHand().getNumOfCards() == 0) {
                        System.out.println(player.getName() + " wins!");
                        return true;
                    } else if(gameMode.equals(GameType.Regular) || gameMode.equals(GameType.RegularFast)) {
                        if(cardToPlay.getValue() == 10) {
                            isTurnOver = false;
                            System.out.println("Another card can be played\n");
                            // In case a 10 was played as the last card in the hand
                            currentHand = getCurrentHand(player);
                        } else if(canMultipleBePlayed(currentHand, cardToPlay) && currentHand.getHandType() != HandType.Hidden) {

                            if(!player.getIsCpu()) {
                                if(selectPlayMultiple()) {
                                    for(int i=currentHand.getNumOfCards()-1; i>=0; i--) {
                                        Card currentCard = currentHand.getCard(i);
                                        if(currentCard.getValue() == cardToPlay.getValue()) {
                                            playCard(currentCard, currentHand, player);
                                        }
                                    }
                                }
                            } else {
                                for(int i=currentHand.getNumOfCards()-1; i>=0; i--) {
                                    Card currentCard = currentHand.getCard(i);
                                    if(currentCard.getValue() == cardToPlay.getValue()) {
                                        playCard(currentCard, currentHand, player);
                                    }
                                }
                            }
                        }

                        if(isLastCardsEqual()) {
                            isTurnOver = false;
                            discardPile.empty(); // Don't like this at all
                            System.out.println("Four cards of equal value have been played, the discard pile has been cleared and another card can be played\n");
                        }
                    }
                } else if (cardToPlay == null) {

                    if (discardPile.isEmpty()) {
                        System.out.println(player.getName() + " picks up nothing.\n");
                    } else {
                        System.out.println(player.getName() + " picks up the discard pile.\n");
                        player.addToGeneral(discardPile.getCards()); // Look into changing this method to the receiveCards version
                        discardPile.empty();
                    }
                } else {
                    System.out.println(player.getName() + " tried to play a " + cardToPlay + ". This card is not suitable.\n");
                    System.out.println(player.getName() + " picks up the discard pile.\n");

                    // For when a hidden card is played and fails
                    // Hidden card should return to the general hand, not stay in the hidden hand
                    if(currentHand.getHandType().equals(HandType.Hidden)) {
                        discardPile.addCard(cardToPlay);
                        currentHand.removeCard(cardToPlay);
                    }

                    player.addToGeneral(discardPile.getCards());
                    discardPile.empty();
                }

            }while(!isTurnOver);

        }
        return false;
    }

    private boolean canMultipleBePlayed(Hand currentHand, Card previousCard) {
        boolean multiple = false;
        for(Card card: currentHand.getCards()) {
            if(card.getValue() == previousCard.getValue()) {
                multiple = true;
            }
        }
        return multiple;
    }

    /**
     * Come up with better name and make this method neater
     * @return
     */
    private boolean selectPlayMultiple() {
        Scanner myReader = new Scanner(System.in);
        boolean choice = false;
        boolean validDecision = false;
        while(!validDecision) {
            System.out.println("Do you want to your other cards of the same value? y/n:\n");
            String decision = myReader.nextLine();
            if(decision.equalsIgnoreCase("y")) {
                validDecision = true;
                choice = true;
            } else if(decision.equalsIgnoreCase("n")) {
                validDecision = true;
            } else {
                System.out.println("Please enter either y/n\n");
            }
        }
        return choice;
    }

    private boolean isCardPlayable(Card cardToPlay) {
        if (cardToPlay != null) {
            if (discardPile.isEmpty() || cardToPlay.getValue() == 2 || cardToPlay.getValue() == 10) {
                return true;
            } else {
                if (cardToPlay.getValue() >= discardPile.peekTop().getValue()) {

                    if(discardPile.peekTop().getValue() != 7) {
                        return true;
                    } else {
                        return cardToPlay.getValue() == 7;
                    }

                } else if(discardPile.peekTop().getValue() == 7) {
                    return true;
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
    private void playCard(Card cardToPlay, Hand currentHand, Player player) {

        discardPile.addCard(cardToPlay);
        currentHand.removeCard(cardToPlay);

        System.out.println(player.getName() + " has played " + cardToPlay);

        if (cardToPlay.getValue() == 10) {
            discardPile.empty();
            System.out.println("Discard deck has been cleared\n");
            // Add ability to play another card here
        }


    }

    private boolean isLastCardsEqual() {
        ArrayList<Card> cards = discardPile.getCards();
        if(cards.size() >= 4) {
            if(cards.get(0).getValue() == cards.get(1).getValue()
                    && (cards.get(1).getValue() == cards.get(2).getValue())
                    && (cards.get(2).getValue() == cards.get(3).getValue())) {

                return true;

            }
        }
        return false;
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
    private Card selectCard(Hand currentHand, boolean isCpu) {
        if (isCpu) {
            return cpuCardChoice(currentHand);
        } else {
            Scanner myReader = new Scanner(System.in);
            //this.getCurrentState();

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
    }

    private Card cpuCardChoice(Hand currentHand) {
        if(currentHand.getHandType().equals(HandType.Hidden)) {
            Random random = new Random();
            return currentHand.getCard(random.nextInt(currentHand.getNumOfCards()));
        }

        Card comparisonCard = discardPile.peekTop();

        if (comparisonCard == null) {
            return currentHand.getLowestRegularCard();
        } else {
            Card cardToPlay = null;
            int cardDiff = -1;
            if (comparisonCard.getValue() != 7) {
                for (Card card : currentHand.getCards()) {
                    // Not happy with the way this method is written.
                    // Should have a special card still assigned as a cardToPlay, but just overridden when a better basic card is
                    // found. Currently, it will ignore special cards and just find them at the end.
                    if (card.getValue() >= comparisonCard.getValue() && (card.getValue() != 2 || card.getValue() != 10)) {

                        if (cardToPlay == null) {
                            cardToPlay = card;
                            cardDiff = cardToPlay.getValue() - comparisonCard.getValue();

                        } else if (card.getValue() - comparisonCard.getValue() < cardDiff) {
                            cardToPlay = card;
                            cardDiff = cardToPlay.getValue() - comparisonCard.getValue();

                        }
                    }
                }

            } else {
                cardToPlay = currentHand.getLowestRegularCard();
                if (!(cardToPlay.getValue() <= 7)) {
                    cardToPlay = null;
                }
            }

            return (cardToPlay == null) ? currentHand.getLowestSpecialCard() : cardToPlay;
        }

    }

    private String getCardChoices(Hand currentHand) {
        String stringMsg = "please select which card to play: \n";
        int i;
        for (i = 0; i < currentHand.getNumOfCards(); i++) {
            Card currentCard = currentHand.getCard(i);
            if (currentHand.getHandType().equals(HandType.Hidden)) {
                stringMsg += i + ": Unknown Card" + "\n";
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
    private String getCurrentState(GameType gameType) {
        Card topCard = discardPile.peekTop();
        String result = "";

        if (gameType.equals(GameType.Basic) || gameType.equals(GameType.Regular)) {
            result += "Draw Pile has " + drawPile.getDeckSize() + " cards remaining" + "\n";
        }

        result += "Discard Pile's Card : " + ((topCard == null) ? "empty" : topCard) + "\n";

        for (Player player : players) {
            result += "Player " + player.getName() + " has the following cards:\n";
            result += "Hidden: " + player.getHiddenHand().getNumOfCards() + ", Constrained: " + player.getConstrainedHand().getNumOfCards() +
                    ", General: " + player.getGeneralHand().getNumOfCards() + "\n\n";
        }

        return result;
    }
}
