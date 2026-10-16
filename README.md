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

Our main design challenge was connecting reusable data structures to game behavior while maintaining a clear separation of responsibilities. `LinkedChain<T>` stores cards and players without knowing blackjack rules, while `Card`, `Player`, and `Game` handle card properties, player state, and gameplay respectively.

Implementation and testing revealed several important edge cases, including preserving insertion order in the linked list, maintaining head and tail references during removal, supporting duplicate cards in a two-deck shoe, handling Aces as either 1 or 11, distinguishing natural blackjack from other 21-point hands, and calculating payouts correctly after doubling down.

Another challenge was integrating the individual components into a working game. Although the individual classes passed their unit tests, running complete rounds exposed issues with input handling, dealer behavior, and terminal output.

We also refined the terminal interface to improve readability by separating player turns, displaying the dealer's visible card, announcing natural blackjacks, and showing net winnings and losses rather than total returned payouts.

Overall, this project reinforced the importance of following method contracts, maintaining data structure invariants, validating input, and testing how different components interact.

## Next Steps

Several features could improve Pure21 beyond the current prototype:

- Add clearer messages when the shoe is rebuilt and shuffled.
- Handle shoe exhaustion during unusually long rounds instead of checking only at the beginning of each round.
- Expand integration tests for multiple players, consecutive rounds, natural blackjack, doubling down, and unexpected end-of-input.
- Implement splitting pairs into separate hands.
- Recycle used cards rather than rebuilding the entire shoe.
- Develop a graphical user interface to replace the terminal-based interface.

Our priority would be improving game robustness and testing before adding more advanced features.

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