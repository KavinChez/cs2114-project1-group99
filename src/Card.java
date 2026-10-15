public class Card {
    public static final String SPADES = "\u2660";
 
    public static final String HEARTS = "\u2665";
 
    public static final String DIAMONDS = "\u2666";
 
    public static final String CLUBS = "\u2663";
 
    private static final String[] SUITS = { SPADES, HEARTS, DIAMONDS, CLUBS };
 
    private static final String[] RANKS = { "A", "2", "3", "4", "5", "6", "7",
        "8", "9", "10", "J", "Q", "K" };
 
    private static final int ACE_VALUE = 11;
 
    private static final int FACE_VALUE = 10;
 
    private final String suit;
 
    private final String rank;
 
    private final int value;
 
    public Card(String suit, String rank) {
        if (suit == null || rank == null) {
            throw new IllegalArgumentException(
                "Suit and rank cannot be null.");
        }
        if (!isValidSuit(suit)) {
            throw new IllegalArgumentException("Invalid suit: " + suit);
        }
        int rankIndex = indexOfRank(rank);
        if (rankIndex == -1) {
            throw new IllegalArgumentException("Invalid rank: " + rank);
        }
        this.suit = suit;
        this.rank = rank;
        if (rankIndex == 0) {
            this.value = ACE_VALUE;
        }
        else {
            // RANKS[1] is "2", so the number cards are worth rankIndex + 1.
            // J, Q, and K would be 11, 12, and 13, so cap them at 10.
            this.value = Math.min(rankIndex + 1, FACE_VALUE);
        }
    }
 
    public String getSuit() {
        return suit;
    }
 
    public String getRank() {
        return rank;
    }
 
    public int getValue() {
        return value;
    }
 
    public boolean isAce() {
        return rank.equals("A");
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Card other = (Card)o;
        return suit.equals(other.suit) && rank.equals(other.rank);
    }
 
    @Override
    public int hashCode() {
        return 31 * suit.hashCode() + rank.hashCode();
    }
 
    @Override
    public String toString() {
        return rank + suit;
    }
 
    private static boolean isValidSuit(String s) {
        for (String validSuit : SUITS) {
            if (validSuit.equals(s)) {
                return true;
            }
        }
        return false;
    }
 
    private static int indexOfRank(String r) {
        for (int i = 0; i < RANKS.length; i++) {
            if (RANKS[i].equals(r)) {
                return i;
            }
        }
        return -1;
    }
}
 