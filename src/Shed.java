import java.util.ArrayList;

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

    private void startGame() {
        drawPile.shuffle();
        dealCards();
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
}
