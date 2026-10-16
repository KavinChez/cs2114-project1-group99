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
        try {
            initialTable();
            while (!table.isEmpty()) {
                checkShoe();
                takeBets();
                initialDeal();
                
                for (int i = 0; i < table.size(); i++) {
                    turn(table.get(i));
                }
                
                dealerTurn();
                settleBets();
                endRound();
            }
        }
        catch (IllegalStateException e) {
            // Print final bankrolls upon EOF / exit
            System.out.println("\nGame over. Final bankrolls:");
            for (int i = 0; i < table.size(); i++) {
                Player p = table.get(i);
                System.out.println(p.getName() + ": $" + p.getBankroll());
            }
        }
    }
 
    public void initialTable() {
        while (table.size() < 5) {
            String inputName = readName("Enter Player " + (table.size() + 1) + "'s name (or type END to finish): ");
            if (inputName.equalsIgnoreCase("END")) {
                break;
            }
            table.addToEnd(new Player(inputName, STARTING_BANKROLL));
        }
        if (table.isEmpty()) {
            throw new IllegalStateException("No players entered.");
        }
    }
 
    public void initialDeal() {
        for (int i = 0; i < 2; i++) {
            for (int p = 0; p < table.size(); p++) {
                deal(table.get(p));
            }
            deal(dealer);
        }
    }
 
    public void turn(Player p) {
        if (p.isBlackjack()) {
            return;
        }
        
        boolean active = true;
        while (active && !p.isBust() && p.getHandValue() < 21) {
            System.out.println(p);
            String[] options;
            if (p.canDoubleDown()) {
                options = new String[] { "hit", "stand", "double" };
            } else {
                options = new String[] { "hit", "stand" };
            }
            
            String choice = readChoice("Action for " + p.getName() + " (hit/stand" + (p.canDoubleDown() ? "/double" : "") + "): ", options);
            
            if (choice.equalsIgnoreCase("hit")) {
                deal(p);
            } else if (choice.equalsIgnoreCase("stand")) {
                active = false;
            } else if (choice.equalsIgnoreCase("double")) {
                p.doubleBet();
                deal(p);
                active = false;
            }
        }
        System.out.println(p);
    }
 
    public void dealerTurn() {
        boolean allBust = true;
        for (int i = 0; i < table.size(); i++) {
            if (!table.get(i).isBust()) {
                allBust = false;
                break;
            }
        }
        
        if (!allBust) {
            while (dealer.getHandValue() < 17) {
                deal(dealer);
            }
        }
        System.out.println(dealer);
    }

    public void endRound() {
        dealer.clearHand();
        
        // Remove broke players or players wishing to leave
        int i = 0;
        while (i < table.size()) {
            Player p = table.get(i);
            if (p.isBroke()) {
                System.out.println(p.getName() + " is broke and removed from the table.");
                table.remove(p);
            } else {
                String keepPlaying = readChoice(p.getName() + ", keep playing? (yes/no): ", new String[] { "yes", "no" });
                if (keepPlaying.equalsIgnoreCase("no")) {
                    table.remove(p);
                } else {
                    p.clearHand();
                    i++;
                }
            }
        }
    }

    public boolean removePlayer(Player p) {
        return table.remove(p);
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
 