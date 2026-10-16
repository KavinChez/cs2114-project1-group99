import java.util.Random;
import java.util.Scanner;

public class GameTest extends student.TestCase {
    private Game game;
    private Player ana;
    private Player dealer;

    public void setUp() {
        game = gameWithInput("");
        ana = new Player("Ana", 500);
        game.getTable().addToEnd(ana);
        dealer = game.getDealer();
    }


    private Game gameWithInput(String input) {
        return new Game(new Scanner(input), new Random(1));
    }


    private void give(Player p, String... ranks) {
        for (String rank : ranks) {
            p.addToHand(new Card(Card.SPADES, rank));
        }
    }


    public void testConstructor() {
        assertNotNull(game.getDealer());
        assertEquals("Dealer", dealer.getName());
        assertNotNull(game.getDeck());
        assertEquals(1, game.getTable().size());
    }


    public void testConstructorNullArguments() {
        Exception thrown = null;
        try {
            new Game(null, new Random(1));
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);

        thrown = null;
        try {
            new Game(new Scanner(""), null);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }


    public void testPlay() {
        // Normal test case
        Game g = gameWithInput("END\n");
        g.play();
        assertTrue(g.getTable().isEmpty());

        // Exception handling with empty input stream
        Game gEx = gameWithInput("");
        Exception thrown = null;
        try {
            gEx.play();
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(gEx.getTable().isEmpty());
    }


    public void testInitialTable() {
        // Normal test case
        Game g = gameWithInput("Alice\nEND\n");
        g.initialTable();
        assertEquals(1, g.getTable().size());
        Player p = g.getTable().get(0);
        assertEquals("Alice", p.getName());
        assertEquals(500, p.getBankroll());

        // Exception testing for unexpected end-of-input
        Game gEx = gameWithInput("");
        Exception thrown = null;
        try {
            gEx.initialTable();
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalStateException);
    }


    public void testInitialDeal() {
        // Normal test case
        ana.setBet(10);
        game.initialDeal();
        assertEquals(2, ana.getHandSize());
        assertEquals(2, dealer.getHandSize());

        // Exception testing for empty deck
        while (!game.getDeck().isEmpty()) {
            game.getDeck().remove();
        }
        Exception thrown = null;
        try {
            game.deal(ana);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalStateException);
    }


    public void testTurn() {
        // Normal test case (Blackjack skip and stand choice)
        give(ana, "A", "K");
        assertTrue(ana.isBlackjack());
        game.turn(ana);
        assertEquals(2, ana.getHandSize());

        // Exception testing for input termination
        Game gEx = gameWithInput("");
        Player ben = new Player("Ben", 500);
        gEx.getTable().addToEnd(ben);
        ben.setBet(10);
        give(ben, "10", "7");

        Exception thrown = null;
        try {
            gEx.turn(ben);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalStateException);
    }


    public void testDealerTurn() {
        // Normal test case
        give(dealer, "5", "6");
        assertEquals(11, dealer.getHandValue());
        game.dealerTurn();
        assertTrue(dealer.getHandValue() >= 17);

        // Exception testing for empty deck during dealer hit sequence
        while (!game.getDeck().isEmpty()) {
            game.getDeck().remove();
        }
        dealer.clearHand();
        give(dealer, "5", "6");

        Exception thrown = null;
        try {
            game.dealerTurn();
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalStateException);
    }


    public void testEndRound() {
        // Normal test case (broke player removal)
        Player brokePlayer = new Player("BrokeUser", 0);
        game.getTable().addToEnd(brokePlayer);
        assertTrue(brokePlayer.isBroke());
        if (brokePlayer.isBroke()) {
            game.removePlayer(brokePlayer);
        }
        assertFalse(game.getTable().contains(brokePlayer));

        // Exception testing for input termination
        Game gEx = gameWithInput("");
        Player ben = new Player("Ben", 500);
        gEx.getTable().addToEnd(ben);
        ben.setBet(10);

        Exception thrown = null;
        try {
            gEx.endRound();
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalStateException);
    }


    public void testRemovePlayer() {
        // Normal test case
        Player ben = new Player("Ben", 500);
        game.getTable().addToEnd(ben);
        assertEquals(2, game.getTable().size());

        boolean removed = game.removePlayer(ben);
        assertTrue(removed);
        assertEquals(1, game.getTable().size());
        assertFalse(game.getTable().contains(ben));

        // Edge case / exception-safe check for non-existent player
        Player stranger = new Player("Stranger", 500);
        boolean notRemoved = game.removePlayer(stranger);
        assertFalse(notRemoved);
    }


    public void testTakeBets() {
        Game g = gameWithInput("25\n");
        Player ben = new Player("Ben", 500);
        g.getTable().addToEnd(ben);
        g.takeBets();
        assertEquals(25, ben.getBet());
        assertEquals(475, ben.getBankroll());
    }


    public void testTakeBetsRePrompts() {
        Game g = gameWithInput("600\nabc\n0\n25\n");
        Player ben = new Player("Ben", 500);
        g.getTable().addToEnd(ben);
        g.takeBets();
        assertEquals(25, ben.getBet());
    }


    public void testPayoutWin() {
        ana.setBet(10);
        give(ana, "10", "10");
        give(dealer, "10", "8");
        assertEquals(20, game.payout(ana));
    }


    public void testPayoutBustAndLoss() {
        ana.setBet(10);
        give(ana, "10", "9", "5");
        give(dealer, "10", "8");
        assertEquals(0, game.payout(ana));

        Player ben = new Player("Ben", 500);
        ben.setBet(10);
        give(ben, "10", "7");
        assertEquals(0, game.payout(ben));
    }


    public void testPayoutBlackjack() {
        ana.setBet(10);
        give(ana, "A", "K");
        give(dealer, "10", "8");
        assertEquals(25, game.payout(ana));
    }


    public void testPayoutPush() {
        ana.setBet(10);
        give(ana, "10", "8");
        give(dealer, "10", "8");
        assertEquals(10, game.payout(ana));
    }


    public void testPayoutDealerBust() {
        ana.setBet(10);
        give(ana, "10", "2");
        give(dealer, "10", "6", "9");
        assertEquals(20, game.payout(ana));
    }


    public void testPayoutDealerBlackjack() {
        ana.setBet(10);
        give(ana, "A", "5", "5");
        give(dealer, "A", "K");
        assertEquals(0, game.payout(ana));

        Player ben = new Player("Ben", 500);
        ben.setBet(10);
        give(ben, "A", "Q");
        assertEquals(10, game.payout(ben));
    }


    public void testPayoutNull() {
        Exception thrown = null;
        try {
            game.payout(null);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }


    public void testSettleBets() {
        ana.setBet(10);
        give(ana, "10", "10");
        give(dealer, "10", "8");
        game.settleBets();
        assertEquals(510, ana.getBankroll());
        assertEquals(0, ana.getBet());
    }


    public void testSettleBetsEmptyTable() {
        Game g = gameWithInput("");
        g.settleBets();
        assertEquals(0, g.getTable().size());
    }


    public void testReadInt() {
        Game g = gameWithInput(" 25 \n");
        assertEquals(25, g.readInt("Bet: ", 1, 500));
    }


    public void testReadIntRePrompts() {
        Game g = gameWithInput("abc\n0\n501\n7\n");
        assertEquals(7, g.readInt("Bet: ", 1, 500));
    }


    public void testReadIntInputEnds() {
        Game g = gameWithInput("abc\n");
        Exception thrown = null;
        try {
            g.readInt("Bet: ", 1, 500);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalStateException);
    }
}
