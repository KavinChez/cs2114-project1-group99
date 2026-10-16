/**
 * Tests for the generic LinkedChain contract in the Project 2 specification.
 */
public class LinkedChainTest extends student.TestCase {
    private LinkedChain<String> chain;

    public void setUp() {
        chain = new LinkedChain<String>();
    }


    private void expectException(
        Class<? extends RuntimeException> kind,
        Runnable operation) {
        try {
            operation.run();
        }
        catch (RuntimeException e) {
            assertTrue(kind.isInstance(e));
            return;
        }
        throw new AssertionError("Expected " + kind.getSimpleName());
    }


    public void testConstructor() {
        assertTrue(chain.isEmpty());
        assertEquals(0, chain.size());
        LinkedChain<String> other = new LinkedChain<String>();
        chain.add("A");
        assertTrue(other.isEmpty());
    }


    public void testAddAndOrder() {
        chain.add("A");
        chain.add("B");
        chain.add("C");
        assertEquals("[A, B, C]", chain.toString());
        assertEquals(3, chain.size());
        assertFalse(chain.isEmpty());
        expectException(IllegalArgumentException.class, () -> chain.add(null));
    }


    public void testLegacyAddToEnd() {
        assertTrue(chain.addToEnd("A"));
        chain.add("B");
        assertTrue(chain.addToEnd("C"));
        assertEquals("[A, B, C]", chain.toString());
    }


    public void testRemoveByValue() {
        chain.add("A");
        chain.add("B");
        chain.add("C");
        assertTrue(chain.remove("B"));
        assertEquals("[A, C]", chain.toString());
        assertFalse(chain.remove("Missing"));
        assertTrue(chain.remove("C"));
        chain.add("D"); // verifies correct tail after last-node removal
        assertEquals("[A, D]", chain.toString());
        assertTrue(chain.remove("A"));
        assertTrue(chain.remove("D"));
        assertTrue(chain.isEmpty());
        chain.add("Again"); // verifies correct tail after last-element removal
        assertEquals("[Again]", chain.toString());
        expectException(IllegalArgumentException.class, () -> chain.remove(
            null));
    }


    public void testRemoveFirst() {
        expectException(IllegalStateException.class, () -> chain.removeFirst());
        chain.add("A");
        chain.add("B");
        assertEquals("A", chain.removeFirst());
        assertEquals("B", chain.removeFirst());
        assertTrue(chain.isEmpty());
        chain.add("C");
        assertEquals("[C]", chain.toString());
    }


    public void testLegacyRemove() {
        assertEquals(null, chain.remove());
        chain.add("A");
        assertEquals("A", chain.remove());
        assertEquals(null, chain.remove());
    }


    public void testRemoveAt() {
        expectException(IndexOutOfBoundsException.class, () -> chain.removeAt(
            0));
        chain.add("A");
        chain.add("B");
        chain.add("C");
        assertEquals("B", chain.removeAt(1));
        assertEquals("[A, C]", chain.toString());
        assertEquals("C", chain.removeAt(1));
        chain.add("D"); // tail update after indexed removal
        assertEquals("[A, D]", chain.toString());
        assertEquals("A", chain.removeAt(0));
        assertEquals("D", chain.removeAt(0));
        expectException(IndexOutOfBoundsException.class, () -> chain.removeAt(
            -1));
        expectException(IndexOutOfBoundsException.class, () -> chain.removeAt(
            0));
    }


    public void testGet() {
        chain.add("A");
        chain.add("B");
        assertEquals("B", chain.get(1));
        assertEquals(2, chain.size());
        expectException(IndexOutOfBoundsException.class, () -> chain.get(-1));
        expectException(IndexOutOfBoundsException.class, () -> chain.get(2));
    }


    public void testContains() {
        chain.add("A");
        assertTrue(chain.contains("A"));
        assertFalse(chain.contains("B"));
        expectException(IllegalArgumentException.class, () -> chain.contains(
            null));
    }


    public void testCountAndDuplicates() {
        chain.add("A");
        chain.add("A");
        chain.add("B");
        chain.add("A");
        assertEquals(3, chain.count("A"));
        assertEquals(0, chain.count("Missing"));
        assertTrue(chain.remove("A"));
        assertEquals("[A, B, A]", chain.toString());
        expectException(IllegalArgumentException.class, () -> chain.count(
            null));
    }


    public void testClear() {
        chain.clear();
        assertEquals("[]", chain.toString());
        chain.add("A");
        chain.add("B");
        chain.clear();
        assertEquals(0, chain.size());
        assertTrue(chain.isEmpty());
        chain.add("C"); // verifies tail was reset
        assertEquals("[C]", chain.toString());
    }


    public void testToArray() {
        chain.add("A");
        chain.add("B");
        String[] values = new String[2];
        assertTrue(chain.toArray(values) == values);
        assertEquals("A", values[0]);
        assertEquals("B", values[1]);
        expectException(IllegalArgumentException.class, () -> chain.toArray(
            null));
        expectException(IllegalArgumentException.class, () -> chain.toArray(
            new String[1]));
    }


    public void testCardsWithEqualValues() {
        LinkedChain<Card> cards = new LinkedChain<Card>();
        Card first = new Card(Card.SPADES, "A");
        Card middle = new Card(Card.HEARTS, "K");
        Card last = new Card(Card.SPADES, "A");
        cards.add(first);
        cards.add(middle);
        cards.add(last);
        assertEquals(2, cards.count(new Card(Card.SPADES, "A")));
        assertTrue(cards.remove(new Card(Card.SPADES, "A")));
        // Indexed removal must return the specific last object, not an equal
        // first.
        assertTrue(cards.removeAt(1) == last);
        assertEquals(middle, cards.removeFirst());
        assertTrue(cards.isEmpty());
    }
}
