import java.util.Random;
import java.util.Scanner;
 
public class Game {
    private static final int NUM_DECKS = 2;
    private static final int STARTING_BANKROLL = 500;
 
    private LinkedChain<Player> table;
    private LinkedChain<Card> deck;
    private Player dealer;
    private Scanner scanner;
    private Random random;
 
    public static void main(String[] args) {
        new Game(new Scanner(System.in), new Random()).play();
    }
 
    public Game(Scanner scanner, Random random) {
        if (scanner == null || random == null) {
            throw new IllegalArgumentException(
                "Scanner and Random cannot be null.");
        }
        this.scanner = scanner;
        this.random = random;
        this.table = new LinkedChain<Player>();
        this.deck = new LinkedChain<Card>();
        this.dealer = new Player("Dealer", 0);
        buildDeck();
    }
 
    // ----- Round flow -----
 
    public void play() {
        // TODO: initialTable(), then repeat until the table is
        // empty: checkShoe, takeBets, initialDeal, turn for each player,
        // dealerTurn, settleBets, endRound. Catch IllegalStateException
        // from the input methods, print final bankrolls, and return.
    }
 
    public void initialTable() {
        // TODO: readName(), then
        // table.addToEnd(new Player(name, STARTING_BANKROLL)).
    }
 
    public void initialDeal() {
        // TODO: two cards to each player and the dealer.
        // Show the dealer's first card only, e.g. "Dealer: 9? ??".
    }
 
    public void turn(Player p) {
        // TODO: hit / stand / double until stand, bust, or 21.
        // Skip the turn if p.isBlackjack().
    }
 
    public void dealerTurn() {
        // TODO: skip if every player busted, otherwise
        // while (dealer.getHandValue() < 17) deal(dealer);
    }
 
    public void endRound() {
        // TODO: clear hands, remove broke players, ask the rest
        // whether to keep playing.
    }
 
    public boolean removePlayer(Player p) {
        // TODO
        return false;
    }
 
    // ----- Deck -----
 
    public void buildDeck() {
        // TODO: deck.clear(), then NUM_DECKS x 52 cards using
        // Card.SPADES/HEARTS/DIAMONDS/CLUBS and ranks A, 2-10, J, Q, K.
        // Then shuffle().
    }
 
    public void shuffle() {
        // TODO: move cards one at a time from a random index
        // into a new chain. Needs removeAt(int), or get(i) + remove(card).
    }
 
    public void checkShoe() {
        // TODO: if deck.size() < 15, buildDeck().
    }
 
    public void deal(Player p) {
        // TODO: if deck.isEmpty() throw IllegalStateException,
        // otherwise p.addToHand(deck.remove()).
    }
 
    // ----- Betting and payouts -----
 
    public void takeBets() {
        for (int i = 0; i < table.size(); i++) {
            Player p = table.get(i);
            if (p.getBankroll() < 1) {
                continue;
            }
            int amount = readInt(p.getName() + ", place your bet ($1-$"
                + p.getBankroll() + "): ", 1, p.getBankroll());
            p.setBet(amount);
        }
    }
 
    public int payout(Player p) {
        if (p == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        int bet = p.getBet();
        if (p.isBust()) {
            return 0;
        }
        if (p.isBlackjack() && dealer.isBlackjack()) {
            return bet;
        }
        if (p.isBlackjack()) {
            // Blackjack pays 3:2. Integer division rounds the bonus down.
            return bet + (3 * bet) / 2;
        }
        if (dealer.isBlackjack()) {
            return 0;
        }
        if (dealer.isBust() || p.getHandValue() > dealer.getHandValue()) {
            return 2 * bet;
        }
        if (p.getHandValue() == dealer.getHandValue()) {
            return bet;
        }
        return 0;
    }
 
    public void settleBets() {
        for (int i = 0; i < table.size(); i++) {
            Player p = table.get(i);
            int amount = payout(p);
            p.settle(amount);
            System.out.println(p.getName() + " gets $" + amount
                + " back. Bankroll: $" + p.getBankroll());
        }
    }
 
    // ----- Input -----
 
    public int readInt(String prompt, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min cannot be above max.");
        }
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                throw new IllegalStateException("Input ended.");
            }
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
            }
            catch (NumberFormatException e) {
                // Not a whole number, so fall through and ask again.
            }
            System.out.println("Please enter a whole number from " + min
                + " to " + max + ".");
        }
    }
 
    public String readChoice(String prompt, String[] options) {
        // TODO: same pattern as readInt. Use scanner.nextLine(), compare
        // ignoring case and spaces, throw IllegalStateException at EOF.
        return null;
    }
 
    public String readName(String prompt) {
        // TODO: same pattern as readInt. Re-prompt on blank input, return
        // the trimmed name, throw IllegalStateException at EOF.
        return null;
    }
 
    // ----- Test access (package-private, used by GameTest) -----
 
    LinkedChain<Player> getTable() {
        return table;
    }
 
    LinkedChain<Card> getDeck() {
        return deck;
    }
 
    Player getDealer() {
        return dealer;
    }
}
 