# Pure21 — Blackjack Game

**CS 2114, Group 99**  
**Team:** Nicolas Marchese, Kavin Chezhiyan, Naomika Aithani

Pure21 is a terminal-based blackjack game written in Java. Players place wagers, receive cards, and choose to hit, stand, or double down against a dealer. The program uses a generic, singly linked `LinkedChain<T>` for the card shoe and player table.

## Compile and Run

### Eclipse (Recommended)

1. Import the project using **File → Import → Existing Projects into Workspace**.
2. Make sure the course-provided **CS2-Support** project is imported and available for the `student.TestCase` framework.
3. Build the project using **Project → Build Project**.
4. Open `src/Game.java` and select **Run As → Java Application**.
5. Follow the console prompts to enter player names, place bets, and make decisions. Enter `END` during setup to finish adding players.

### Terminal

From the project root, with a JDK installed:

```bash
mkdir -p bin
javac -encoding UTF-8 -d bin src/Card.java src/Player.java src/LinkedChain.java src/Game.java
java -cp bin Game
```

The commands above compile and run the application. Running tests additionally requires the course's `student.TestCase` dependency.

## Run Tests

In Eclipse, open each test class and select **Run As → JUnit Test**.

- `CardTest.java`
- `PlayerTest.java`
- `LinkedChainTest.java`
- `GameTest.java`
- `GameMethodsTest.java`

The tests cover card properties and equality, player bankroll and hand behavior, linked-list operations, blackjack rules, deck construction and shuffling, input validation, and game integration.


## Reflection

### 1. What changed from the specification, and why?

The Deliverable 2 specification defined a terminal-based, single-player blackjack game as the MVP. We kept the terminal interface to focus on functionality and testing, but expanded the game to support up to five players, completing multiplayer as a stretch goal.

We also changed the starting bankroll from $500 to $100 during play-testing. This made the betting amounts more appropriate for shorter games.

**Origin:** Original specification and gameplay adjustments during development.

### 2. How did LinkedChain change?

We modified the course-provided `LinkedChain<T>` to preserve insertion order. The original `add()` inserted at the front, while our specification required adding to the end in O(1) time.

We added a tail pointer, implemented `removeFirst()` and `removeAt(int)`, and improved null validation.

`removeAt()` was especially important for shuffling because the two-deck shoe contains duplicate cards. Removing by index ensures the correct node is removed.

**Origin:** Specification requirements, code review, and GenAI-assisted implementation.

### 3. What implementation and testing challenges occurred?

During integration, `readName()` returned null, causing a `NullPointerException`. Other unfinished methods prevented cards from being dealt correctly.

We implemented the missing deck and input methods, improved terminal formatting, displayed the dealer's visible card, and added blackjack announcements.

We also changed settlement messages to display net winnings and losses instead of gross payouts.

Testing identified issues with dealer-turn setup and insufficient coverage of round cleanup. We corrected these tests.

**Origin:** Manual testing, observed exceptions, and GenAI-assisted debugging.

### 4. What remains unfinished or uncertain?

The core blackjack game works, including multiplayer, betting, doubling down, and payouts.

However, the shoe is only checked at the beginning of each round. An unusually long multiplayer round could exhaust the remaining cards.

Pair splitting, card recycling, and a graphical interface also remain unimplemented stretch goals.

Further integration testing would improve confidence in consecutive rounds and unusual edge cases.

**Origin:** Review of the completed implementation, test coverage, and original stretch goals.

## Next Steps

The terminal-based MVP is complete. The following tasks are ordered by priority. Responsibilities are proposed and should be confirmed by the group.

1. **Final testing — All members — Committed**
   - Run all five test classes in Eclipse.
   - Verify the $100 starting bankroll.
   - Resolve any remaining failures.

2. **Prevent shoe exhaustion — Nicolas and Naomika — Proposed**
   - Handle an empty shoe during unusually long rounds.
   - Add `testMultiplayerRoundNearEmptyShoe()`.

3. **Expand integration tests — All members — Proposed**
   - Add `testMultiplayerConsecutiveRounds()`.
   - Add `testNaturalBlackjackPayout()`.
   - Add `testDoubleDownEndsTurn()`.

4. **Reshuffling announcement — Nicolas — Deferred**
   - Display a message when the shoe is rebuilt.
   - Add `testReshuffleMessage()`.

### Stretch Goals

- **Multiplayer:** Implemented. Supports up to five players.
- **Pair splitting:** Deferred. Proposed owners: Kavin and Naomika.
- **Card recycling:** Deferred. Proposed owner: Nicolas.
- **Graphical interface:** Deferred. Proposed owners: Nicolas and Naomika.

The deferred features are not required for the current MVP.


## Attribution

- **Nicolas Marchese:** LinkedChain implementation, deck management,
  shuffling, integration, testing, and terminal presentation improvements.

- **Kavin Chezhiyan:** Card and Player classes, betting functionality,
  payout calculations, integration, and testing.

- **Naomika Aithani:** Game flow, player and dealer turns,
  round management, integration, and testing.

## GenAI Note

OpenAI ChatGPT was used during development to assist with reviewing the project specification, modifying the generic linked-list implementation, implementing deck management and input-handling methods, debugging exceptions, improving terminal output, and creating or revising test cases.

AI-generated code and suggestions were incorporated into the project where appropriate. The team is responsible for understanding, reviewing, and testing the final implementation.