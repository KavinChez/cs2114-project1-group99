public class CardTest extends student.TestCase {
    private Card aceSpades;
    private Card tenHearts;
    private Card sevenDiamonds;
    private Card kingSpades;
 
    public void setUp() {
        aceSpades = new Card(Card.SPADES, "A");
        tenHearts = new Card(Card.HEARTS, "10");
        sevenDiamonds = new Card(Card.DIAMONDS, "7");
        kingSpades = new Card(Card.SPADES, "K");
    }
 
    // ----------------------------------------------------------
    // Card(String suit, String rank)
    // ----------------------------------------------------------
 
    public void testConstructor() {
        assertEquals(Card.SPADES, aceSpades.getSuit());
        assertEquals("A", aceSpades.getRank());
        assertEquals(11, aceSpades.getValue());
    }
 
    public void testConstructorAcceptsEverySuit() {
        assertEquals(Card.SPADES, new Card(Card.SPADES, "5").getSuit());
        assertEquals(Card.HEARTS, new Card(Card.HEARTS, "5").getSuit());
        assertEquals(Card.DIAMONDS, new Card(Card.DIAMONDS, "5").getSuit());
        assertEquals(Card.CLUBS, new Card(Card.CLUBS, "5").getSuit());
    }
 
    public void testConstructorInvalidSuit() {
        Exception thrown = null;
        try {
            new Card("X", "A");
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }
 
    public void testConstructorInvalidRankOne() {
        Exception thrown = null;
        try {
            new Card(Card.SPADES, "1");
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }
 
    public void testConstructorInvalidRankEleven() {
        Exception thrown = null;
        try {
            new Card(Card.SPADES, "11");
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }
 
    public void testConstructorNullSuit() {
        Exception thrown = null;
        try {
            new Card(null, "A");
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }
 
    public void testConstructorNullRank() {
        Exception thrown = null;
        try {
            new Card(Card.SPADES, null);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }
 
    // ----------------------------------------------------------
    // getSuit()
    // ----------------------------------------------------------
 
    public void testGetSuit() {
        assertEquals(Card.SPADES, aceSpades.getSuit());
    }
 
    public void testGetSuitHearts() {
        assertEquals(Card.HEARTS, tenHearts.getSuit());
    }
 
    // ----------------------------------------------------------
    // getRank()
    // ----------------------------------------------------------
 
    public void testGetRank() {
        assertEquals("A", aceSpades.getRank());
    }
 
    public void testGetRankTen() {
        assertEquals("10", tenHearts.getRank());
    }
 
    // ----------------------------------------------------------
    // getValue()
    // ----------------------------------------------------------
 
    public void testGetValue() {
        assertEquals(7, sevenDiamonds.getValue());
    }
 
    public void testGetValueAceAndKing() {
        assertEquals(11, aceSpades.getValue());
        assertEquals(10, kingSpades.getValue());
    }
 
    public void testGetValueEveryRank() {
        String[] ranks = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "J", "Q", "K" };
        int[] values = { 11, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10 };
        for (int i = 0; i < ranks.length; i++) {
            Card card = new Card(Card.CLUBS, ranks[i]);
            assertEquals("Value of " + ranks[i], values[i], card.getValue());
        }
    }
 
    // ----------------------------------------------------------
    // isAce()
    // ----------------------------------------------------------
 
    public void testIsAce() {
        assertTrue(aceSpades.isAce());
    }
 
    public void testIsAceFalse() {
        assertFalse(kingSpades.isAce());
    }
 
    // ----------------------------------------------------------
    // equals(Object o)
    // ----------------------------------------------------------
 
    public void testEquals() {
        Card otherAceSpades = new Card(Card.SPADES, "A");
        assertTrue(aceSpades.equals(otherAceSpades));
        assertTrue(otherAceSpades.equals(aceSpades));
        assertTrue(aceSpades.equals(aceSpades));
    }
 
    public void testEqualsDifferentSuit() {
        assertFalse(aceSpades.equals(new Card(Card.HEARTS, "A")));
    }
 
    public void testEqualsDifferentRank() {
        assertFalse(aceSpades.equals(kingSpades));
    }
 
    public void testEqualsNullAndNonCard() {
        assertFalse(aceSpades.equals(null));
        assertFalse(aceSpades.equals("A" + Card.SPADES));
    }
 
    // ----------------------------------------------------------
    // hashCode()
    // ----------------------------------------------------------
 
    public void testHashCode() {
        Card otherAceSpades = new Card(Card.SPADES, "A");
        assertEquals(aceSpades.hashCode(), otherAceSpades.hashCode());
    }
 
    public void testHashCodeRepeatedCalls() {
        int first = aceSpades.hashCode();
        assertEquals(first, aceSpades.hashCode());
        assertEquals(first, aceSpades.hashCode());
    }
 
    // ----------------------------------------------------------
    // toString()
    // ----------------------------------------------------------
 
    public void testToString() {
        assertEquals("A" + Card.SPADES, aceSpades.toString());
    }
 
    public void testToStringTen() {
        assertEquals("10" + Card.HEARTS, tenHearts.toString());
    }
}
 