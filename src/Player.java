import java.util.Arrays;
 
public class Player {
    private static final int MAX_HAND_SIZE = 12;
    private static final int BLACKJACK = 21;
 
    private String name;
    private int bankroll;
    private int bet;
    private Card[] hand;
    private int handSize;
    private boolean doubledDown;
    private Player splitHand;
    private boolean hasSplit;
 
    public Player(String name, int bankroll) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("Name cannot be blank.");
        }
        if (bankroll < 0) {
            throw new IllegalArgumentException(
                "Bankroll cannot be negative.");
        }
        this.name = name;
        this.bankroll = bankroll;
        this.bet = 0;
        this.hand = new Card[MAX_HAND_SIZE];
        this.handSize = 0;
        this.doubledDown = false;
        this.splitHand = null;
        this.hasSplit = false;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("Name cannot be blank.");
        }
        this.name = name;
    }
 
    public int getBankroll() {
        return bankroll;
    }
 
    public int getBet() {
        return bet;
    }
 
    public void setBet(int amount) {
        if (bet > 0) {
            throw new IllegalStateException("A bet is already active.");
        }
        if (amount < 1 || amount > bankroll) {
            throw new IllegalArgumentException(
                "Bet must be between 1 and " + bankroll + ".");
        }
        bankroll -= amount;
        bet = amount;
    }
 
    public boolean canDoubleDown() {
        // Needs exactly two cards, an active bet, enough money to match it,
        // no earlier double down this round, and no split this round.
        return handSize == 2 && bet > 0 && bankroll >= bet && !doubledDown
            && !hasSplit;
    }
 
    public void doubleBet() {
        if (!canDoubleDown()) {
            throw new IllegalStateException("Cannot double down now.");
        }
        bankroll -= bet;
        bet *= 2;
        doubledDown = true;
    }

    public boolean canSplit() {
        // Needs a pair (two cards of the same rank), an active bet, enough
        // money to match it, and no earlier split or double down this round.
        return handSize == 2 && hand[0].getRank().equals(hand[1].getRank())
            && bet > 0 && bankroll >= bet && !hasSplit && !doubledDown;
    }

    public void split() {
        if (!canSplit()) {
            throw new IllegalStateException("Cannot split now.");
        }
        // The second hand is its own Player, so it reuses all the hand
        // logic. Its bet comes out of this player's bankroll.
        bankroll -= bet;
        splitHand = new Player(name + " (hand 2)", bet);
        splitHand.setBet(bet);
        splitHand.addToHand(hand[1]);
        splitHand.hasSplit = true;
        hand[1] = null;
        handSize = 1;
        hasSplit = true;
    }

    public Player getSplitHand() {
        return splitHand;
    }

    public void settleSplit(int payout) {
        if (splitHand == null) {
            throw new IllegalStateException("There is no split hand.");
        }
        if (payout < 0) {
            throw new IllegalArgumentException(
                "Payout cannot be negative.");
        }
        bankroll += payout;
        splitHand = null;
    }
 
    public void settle(int payout) {
        if (payout < 0) {
            throw new IllegalArgumentException(
                "Payout cannot be negative.");
        }
        bankroll += payout;
        bet = 0;
    }
 
    public void addToHand(Card c) {
        if (c == null) {
            throw new IllegalArgumentException("Card cannot be null.");
        }
        if (handSize == MAX_HAND_SIZE) {
            throw new IllegalStateException("Hand is full.");
        }
        hand[handSize] = c;
        handSize++;
    }
 
    public Card[] getHand() {
        return Arrays.copyOf(hand, handSize);
    }
 
    public int getHandSize() {
        return handSize;
    }
 
    public int getHandValue() {
        int total = 0;
        int aces = 0;
        for (int i = 0; i < handSize; i++) {
            total += hand[i].getValue();
            if (hand[i].isAce()) {
                aces++;
            }
        }
        // Every Ace starts at 11. While the hand is over 21, turn one Ace
        // into a 1 by subtracting 10.
        while (total > BLACKJACK && aces > 0) {
            total -= 10;
            aces--;
        }
        return total;
    }
 
    public boolean isBust() {
        return getHandValue() > BLACKJACK;
    }
 
    public boolean isBlackjack() {
        // 21 on a split hand is a normal 21, not a natural blackjack.
        return !hasSplit && handSize == 2 && getHandValue() == BLACKJACK;
    }
 
    public void clearHand() {
        for (int i = 0; i < handSize; i++) {
            hand[i] = null;
        }
        handSize = 0;
        doubledDown = false;
        splitHand = null;
        hasSplit = false;
    }
 
    public boolean isBroke() {
        return bankroll < 1 && bet == 0;
    }
 
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name + ":");
        for (int i = 0; i < handSize; i++) {
            sb.append(" ").append(hand[i]);
        }
        sb.append(" (").append(getHandValue()).append(")");
        return sb.toString();
    }
 
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
 