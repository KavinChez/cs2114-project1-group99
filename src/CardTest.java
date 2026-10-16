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


    public void testConstructor() {
        assertEquals(Card.SPADES, aceSpades.getSuit());
        assertEquals("A", aceSpades.getRank());
        assertEquals(11, aceSpades.getValue());
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
        assertTrue(thrown instanceof IllegalArgumentException);
    }


    public void testConstructorInvalidRank() {
        Exception thrown = null;
        try {
            new Card(Card.SPADES, "1");
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }


    public void testConstructorNull() {
        Exception thrown = null;
        try {
            new Card(null, "A");
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }


    public void testGetSuit() {
        assertEquals(Card.SPADES, aceSpades.getSuit());
        assertEquals(Card.HEARTS, tenHearts.getSuit());
    }


    public void testGetRank() {
        assertEquals("A", aceSpades.getRank());
        assertEquals("10", tenHearts.getRank());
    }


    public void testGetValue() {
        assertEquals(7, sevenDiamonds.getValue());
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


    public void testIsAce() {
        assertTrue(aceSpades.isAce());
    }


    public void testIsAceFalse() {
        assertFalse(kingSpades.isAce());
    }


    public void testEquals() {
        Card otherAceSpades = new Card(Card.SPADES, "A");
        assertTrue(aceSpades.equals(otherAceSpades));
        assertTrue(aceSpades.equals(aceSpades));
    }


    public void testEqualsFalse() {
        assertFalse(aceSpades.equals(new Card(Card.HEARTS, "A")));
        assertFalse(aceSpades.equals(kingSpades));
        assertFalse(aceSpades.equals(null));
        Object nonCard = "A" + Card.SPADES;
        assertFalse(aceSpades.equals(nonCard));
    }


    public void testHashCode() {
        Card otherAceSpades = new Card(Card.SPADES, "A");
        assertEquals(aceSpades.hashCode(), otherAceSpades.hashCode());
        assertEquals(aceSpades.hashCode(), aceSpades.hashCode());
    }


    public void testToString() {
        assertEquals("A" + Card.SPADES, aceSpades.toString());
    }


    public void testToStringTen() {
        assertEquals("10" + Card.HEARTS, tenHearts.toString());
    }
}
