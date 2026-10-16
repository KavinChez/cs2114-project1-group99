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
 