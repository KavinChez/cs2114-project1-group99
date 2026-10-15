public class PlayerTest extends student.TestCase {
    private Player ana;
    private Card aceSpades;
    private Card aceHearts;
    private Card kingHearts;
    private Card tenSpades;
    private Card nineHearts;
    private Card sevenDiamonds;
    private Card sixClubs;
    private Card fiveHearts;
    private Card fiveDiamonds;
 
    public void setUp() {
        ana = new Player("Ana", 500);
        aceSpades = new Card(Card.SPADES, "A");
        aceHearts = new Card(Card.HEARTS, "A");
        kingHearts = new Card(Card.HEARTS, "K");
        tenSpades = new Card(Card.SPADES, "10");
        nineHearts = new Card(Card.HEARTS, "9");
        sevenDiamonds = new Card(Card.DIAMONDS, "7");
        sixClubs = new Card(Card.CLUBS, "6");
        fiveHearts = new Card(Card.HEARTS, "5");
        fiveDiamonds = new Card(Card.DIAMONDS, "5");
    }
 
    public void testConstructor() {
        assertEquals("Ana", ana.getName());
        assertEquals(500, ana.getBankroll());
        assertEquals(0, ana.getBet());
        assertEquals(0, ana.getHandSize());
    }
 
    public void testConstructorNullName() {
        Exception thrown = null;
        try {
            new Player(null, 500);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }
 
    public void testConstructorNegativeBankroll() {
        Exception thrown = null;
        try {
            new Player("Ana", -1);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }
 
    public void testGetName() {
        assertEquals("Ana", ana.getName());
    }
 
    public void testSetName() {
        ana.setName("Ben");
        assertEquals("Ben", ana.getName());
    }
 
    public void testSetNameBlank() {
        Exception thrown = null;
        try {
            ana.setName("   ");
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals("Ana", ana.getName());
    }
 
    public void testGetBankroll() {
        assertEquals(500, ana.getBankroll());
        ana.setBet(50);
        assertEquals(450, ana.getBankroll());
    }
 
    public void testGetBet() {
        ana.setBet(50);
        assertEquals(50, ana.getBet());
        ana.settle(100);
        assertEquals(0, ana.getBet());
    }
 
    public void testSetBet() {
        ana.setBet(50);
        assertEquals(450, ana.getBankroll());
        assertEquals(50, ana.getBet());
    }
 
    public void testSetBetOutOfRange() {
        Exception thrown = null;
        try {
            ana.setBet(0);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
 
        thrown = null;
        try {
            ana.setBet(600);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals(500, ana.getBankroll());
        assertEquals(0, ana.getBet());
    }
 
    public void testSetBetSecondActiveBet() {
        ana.setBet(50);
        Exception thrown = null;
        try {
            ana.setBet(25);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalStateException);
        assertEquals(450, ana.getBankroll());
        assertEquals(50, ana.getBet());
    }
 
    public void testCanDoubleDown() {
        ana.setBet(50);
        ana.addToHand(fiveHearts);
        ana.addToHand(sixClubs);
        assertTrue(ana.canDoubleDown());
    }
 
    public void testCanDoubleDownFalse() {
        Player noBet = new Player("Ben", 500);
        noBet.addToHand(fiveHearts);
        noBet.addToHand(sixClubs);
        assertFalse(noBet.canDoubleDown());
 
        ana.setBet(300);
        ana.addToHand(fiveHearts);
        ana.addToHand(sixClubs);
        assertFalse(ana.canDoubleDown());
 
        ana.settle(300);
        ana.setBet(50);
        ana.addToHand(fiveDiamonds);
        assertFalse(ana.canDoubleDown());
    }
 
    public void testDoubleBet() {
        ana.setBet(50);
        ana.addToHand(fiveHearts);
        ana.addToHand(sixClubs);
        ana.doubleBet();
        assertEquals(100, ana.getBet());
        assertEquals(400, ana.getBankroll());
        assertFalse(ana.canDoubleDown());
    }
 
    public void testDoubleBetIneligible() {
        ana.setBet(50);
        ana.addToHand(fiveHearts);
        Exception thrown = null;
        try {
            ana.doubleBet();
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalStateException);
        assertEquals(50, ana.getBet());
        assertEquals(450, ana.getBankroll());
    }
 
    public void testSettle() {
        ana.setBet(10);
        ana.settle(20);
        assertEquals(510, ana.getBankroll());
        assertEquals(0, ana.getBet());
    }
 
    public void testSettleNegative() {
        ana.setBet(10);
        Exception thrown = null;
        try {
            ana.settle(-5);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals(490, ana.getBankroll());
        assertEquals(10, ana.getBet());
    }
 
    public void testAddToHand() {
        ana.addToHand(aceSpades);
        assertEquals(1, ana.getHandSize());
        assertEquals(aceSpades, ana.getHand()[0]);
    }
 
    public void testAddToHandNull() {
        Exception thrown = null;
        try {
            ana.addToHand(null);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals(0, ana.getHandSize());
    }
 
    public void testAddToHandFull() {
        Card two = new Card(Card.CLUBS, "2");
        for (int i = 0; i < 12; i++) {
            ana.addToHand(two);
        }
        Exception thrown = null;
        try {
            ana.addToHand(two);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalStateException);
        assertEquals(12, ana.getHandSize());
    }
 
    public void testGetHand() {
        ana.addToHand(aceSpades);
        ana.addToHand(kingHearts);
        Card[] cards = ana.getHand();
        assertEquals(2, cards.length);
        assertEquals(aceSpades, cards[0]);
        assertEquals(kingHearts, cards[1]);
 
        cards[0] = fiveHearts;
        assertEquals(aceSpades, ana.getHand()[0]);
    }
 
    public void testGetHandSize() {
        assertEquals(0, ana.getHandSize());
        ana.addToHand(fiveHearts);
        ana.addToHand(sixClubs);
        ana.addToHand(sevenDiamonds);
        assertEquals(3, ana.getHandSize());
    }
 
    public void testGetHandValue() {
        ana.addToHand(tenSpades);
        ana.addToHand(sevenDiamonds);
        assertEquals(17, ana.getHandValue());
    }
 
    public void testGetHandValueAces() {
        assertEquals(0, ana.getHandValue());
        ana.addToHand(aceSpades);
        ana.addToHand(aceHearts);
        ana.addToHand(nineHearts);
        assertEquals(21, ana.getHandValue());
 
        Player ben = new Player("Ben", 500);
        ben.addToHand(aceSpades);
        ben.addToHand(sixClubs);
        assertEquals(17, ben.getHandValue());
        ben.addToHand(tenSpades);
        assertEquals(17, ben.getHandValue());
    }
 
    public void testIsBust() {
        ana.addToHand(tenSpades);
        ana.addToHand(nineHearts);
        ana.addToHand(fiveDiamonds);
        assertTrue(ana.isBust());
    }
 
    public void testIsBustAceSaves() {
        ana.addToHand(aceSpades);
        ana.addToHand(nineHearts);
        ana.addToHand(fiveDiamonds);
        assertFalse(ana.isBust());
    }
 
    public void testIsBlackjack() {
        ana.addToHand(aceSpades);
        ana.addToHand(kingHearts);
        assertTrue(ana.isBlackjack());
    }
 
    public void testIsBlackjackThreeCards() {
        ana.addToHand(aceSpades);
        ana.addToHand(fiveHearts);
        ana.addToHand(fiveDiamonds);
        assertEquals(21, ana.getHandValue());
        assertFalse(ana.isBlackjack());
    }
 
    public void testClearHand() {
        ana.addToHand(fiveHearts);
        ana.addToHand(sixClubs);
        ana.addToHand(sevenDiamonds);
        ana.clearHand();
        assertEquals(0, ana.getHandSize());
        assertEquals(0, ana.getHandValue());
 
        ana.clearHand();
        assertEquals(0, ana.getHandSize());
    }
 
    public void testClearHandResetsDoubleDown() {
        ana.setBet(50);
        ana.addToHand(fiveHearts);
        ana.addToHand(sixClubs);
        ana.doubleBet();
        ana.settle(0);
        ana.clearHand();
        ana.setBet(50);
        ana.addToHand(fiveDiamonds);
        ana.addToHand(sevenDiamonds);
        assertTrue(ana.canDoubleDown());
    }
 
    public void testIsBroke() {
        assertTrue(new Player("Ben", 0).isBroke());
        assertFalse(ana.isBroke());
    }
 
    public void testIsBrokeWithActiveBet() {
        Player allIn = new Player("Ben", 50);
        allIn.setBet(50);
        assertEquals(0, allIn.getBankroll());
        assertFalse(allIn.isBroke());
    }
 
    public void testToString() {
        ana.addToHand(aceSpades);
        ana.addToHand(sevenDiamonds);
        assertEquals("Ana: A" + Card.SPADES + " 7" + Card.DIAMONDS + " (18)",
            ana.toString());
    }
 
    public void testToStringEmptyHand() {
        assertEquals("Ana: (0)", ana.toString());
    }
}
 
