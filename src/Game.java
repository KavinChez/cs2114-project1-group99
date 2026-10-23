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
            System.out.println();

            while (!table.isEmpty()) {
                checkShoe();

                takeBets();
                System.out.println();

                initialDeal();
                System.out.println();

                for (int i = 0; i < table.size(); i++) {
                    turn(table.get(i));
                    System.out.println();
                }

                dealerTurn();
                settleBets();
                System.out.println();

                endRound();
                System.out.println();
            }
        }
        catch (IllegalStateException e) {
            System.out.println("\nInput ended; leaving the game.");
        }

        System.out.println("Game over.");
        for (int i = 0; i < table.size(); i++) {
            Player p = table.get(i);
            System.out.println(p.getName() + " finishes with $" + p
                .getBankroll());
        }
    }


    public void initialTable() {
        while (table.size() < 5) {
            String inputName = readName("Enter Player " + (table.size() + 1)
                + "'s name (or type END to finish): ");
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

        // Display players' hands
        for (int p = 0; p < table.size(); p++) {
            Player player = table.get(p);

            if (player.isBlackjack()) {
                System.out.println(player + " - BLACKJACK!");
            }
            else {
                System.out.println(player);
            }
        }

        // Show dealer's first card, but hide the second
        System.out.println("Dealer: " + dealer.getHand()[0] + " [hidden]");
    }


    public void turn(Player p) {
        if (p.isBlackjack()) {
            return;
        }

        boolean active = true;
        while (active && !p.isBust() && p.getHandValue() < 21) {
            System.out.println(p);
            String[] options = actionsFor(p);

            String choice = readChoice("Action for " + p.getName() + " ("
                + String.join("/", options) + "): ", options);

            if (choice.equalsIgnoreCase("hit")) {
                deal(p);
            }
            else if (choice.equalsIgnoreCase("stand")) {
                active = false;
            }
            else if (choice.equalsIgnoreCase("double")) {
                p.doubleBet();
                deal(p);
                active = false;
            }
            else if (choice.equalsIgnoreCase("split")) {
                p.split();
                deal(p);
                deal(p.getSplitHand());
                System.out.println(p.getName() + " splits into two hands.");
            }
        }
        System.out.println(p);

        // After the first hand is done, play the second hand the same way.
        if (p.getSplitHand() != null) {
            turn(p.getSplitHand());
        }
    }

    private String[] actionsFor(Player p) {
        LinkedChain<String> actions = new LinkedChain<String>();
        actions.add("hit");
        actions.add("stand");
        if (p.canDoubleDown()) {
            actions.add("double");
        }
        if (p.canSplit()) {
            actions.add("split");
        }
        return actions.toArray(new String[actions.size()]);
    }


    public void dealerTurn() {
        boolean allBust = true;
        for (int i = 0; i < table.size(); i++) {
            Player p = table.get(i);
            Player second = p.getSplitHand();
            if (!p.isBust() || (second != null && !second.isBust())) {
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

        int i = 0;
        while (i < table.size()) {
            Player p = table.get(i);
            p.clearHand();

            if (p.isBroke()) {
                System.out.println(p.getName() + " is broke and leaves.");
                table.remove(p);
            }
            else {
                String keepPlaying = readChoice(p.getName()
                    + ", keep playing? (yes/no): ", new String[] { "yes",
                        "no" });

                if (keepPlaying.equalsIgnoreCase("no")) {
                    table.remove(p);
                }
                else {
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
        deck.clear();

        String[] suits = { Card.SPADES, Card.HEARTS, Card.DIAMONDS,
            Card.CLUBS };

        String[] ranks = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "J", "Q", "K" };

        for (int d = 0; d < NUM_DECKS; d++) {
            for (String suit : suits) {
                for (String rank : ranks) {
                    deck.add(new Card(suit, rank));
                }
            }
        }

        shuffle();
    }


    public void shuffle() {
        LinkedChain<Card> shuffled = new LinkedChain<Card>();

        while (!deck.isEmpty()) {
            int index = random.nextInt(deck.size());
            Card card = deck.removeAt(index);
            shuffled.add(card);
        }

        deck = shuffled;
    }


    public void checkShoe() {
        if (deck.size() < 15) {
            System.out.println("\nReshuffling the deck...\n");
            buildDeck();
        }
    }


    public void deal(Player p) {
        if (p == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }

        if (deck.isEmpty()) {
            throw new IllegalStateException("Cannot deal from an empty deck.");
        }

        p.addToHand(deck.removeFirst());
    }

    // ----- Betting and payouts -----


    public void takeBets() {
        for (int i = 0; i < table.size(); i++) {
            Player p = table.get(i);
            if (p.getBankroll() < 1) {
                continue;
            }
            int amount = readInt(p.getName() + ", place your bet ($1-$" + p
                .getBankroll() + "): ", 1, p.getBankroll());
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

            int bet = p.getBet();
            int amount = payout(p);
            int profit = amount - bet;

            p.settle(amount);

            printResult(p.getName(), profit, p.getBankroll());

            Player second = p.getSplitHand();
            if (second != null) {
                int secondBet = second.getBet();
                int secondAmount = payout(second);
                p.settleSplit(secondAmount);
                printResult(second.getName(), secondAmount - secondBet,
                    p.getBankroll());
            }
        }
    }

    private void printResult(String name, int profit, int bankroll) {
        if (profit > 0) {
            System.out.println(name + " wins $" + profit + ". Bankroll: $"
                + bankroll);
        }
        else if (profit < 0) {
            System.out.println(name + " loses $" + (-profit)
                + ". Bankroll: $" + bankroll);
        }
        else {
            System.out.println(name + " pushes. Bankroll: $" + bankroll);
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
        if (options == null || options.length == 0) {
            throw new IllegalArgumentException("Choices cannot be empty.");
        }

        for (String option : options) {
            if (option == null || option.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid choice option.");
            }
        }

        while (true) {
            System.out.print(prompt);

            if (!scanner.hasNextLine()) {
                throw new IllegalStateException("Input ended.");
            }

            String answer = scanner.nextLine().trim();

            for (String option : options) {
                if (answer.equalsIgnoreCase(option.trim())) {
                    return option.trim().toLowerCase(java.util.Locale.ROOT);
                }
            }

            System.out.println("Invalid choice. Try again.");
        }
    }


    public String readName(String prompt) {
        while (true) {
            System.out.print(prompt);

            if (!scanner.hasNextLine()) {
                throw new IllegalStateException("Input ended.");
            }

            String name = scanner.nextLine().trim();

            if (!name.isEmpty()) {
                return name;
            }

            System.out.println("Name cannot be blank.");
        }
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
 