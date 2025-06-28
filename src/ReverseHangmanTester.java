import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A command-line tester for the ReverseHangmanGame logic.
 * It simulates a game for a given secret word and determines if the algorithm
 * can guess it successfully.
 *
 * NOTE: For this tester to provide detailed logs, you may need to make a small
 * change in ReverseHangmanGame.java:
 *
 * Change this line:
 * private List<String> possibleWords;
 *
 * To this (making it package-private):
 * List<String> possibleWords;
 */
public class ReverseHangmanTester {

    private boolean verbose; // Set to true to see turn-by-turn details for each word

    public ReverseHangmanTester(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Tests the game's guessing algorithm against a single secret word.
     *
     * @param secretWord The word the algorithm needs to guess.
     * @return true if the word was guessed correctly, false otherwise.
     */
    public boolean testWord(String secretWord) {
        secretWord = secretWord.toUpperCase();
        int wordLength = secretWord.length();
        ReverseHangmanGame game = new ReverseHangmanGame(wordLength);

        if (verbose) {
            System.out.println("--- Testing word: " + secretWord + " ---");
        }

        while (!game.isGameOver()) {
            char guess = game.makeGuess();

            // This can happen if the algorithm has no more valid letters to guess
            if (guess == ' ') {
                if (verbose) {
                    System.out.println("   Game ended prematurely (no valid guess available).");
                }
                break;
            }

            // Check if the guessed letter is in the secret word
            if (secretWord.indexOf(guess) != -1) {
                // --- Correct Guess ---
                // Find all positions of the letter (1-based index)
                List<Integer> positionsList = new ArrayList<>();
                for (int i = 0; i < secretWord.length(); i++) {
                    if (secretWord.charAt(i) == guess) {
                        positionsList.add(i + 1);
                    }
                }
                int[] positions = positionsList.stream().mapToInt(Integer::intValue).toArray();

                if (verbose) {
                    System.out
                            .println("   Guess: '" + guess + "' -> CORRECT. Positions: " + Arrays.toString(positions));
                }

                // Provide the correct information to the game logic
                game.processCorrectGuess(guess, positions);

            } else {
                // --- Incorrect Guess ---
                if (verbose) {
                    System.out.println("   Guess: '" + guess + "' -> INCORRECT.");
                }
                game.processIncorrectGuess(guess);
            }
        }

        // After the game is over, check if the algorithm succeeded
        String finalGuessedWord = game.getFinalWord();
        boolean success = secretWord.equals(finalGuessedWord);

        if (verbose) {
            System.out.println("   Game Over. Incorrect Guesses: " + game.getIncorrectGuesses());
            // This line requires `possibleWords` to be package-private in
            // ReverseHangmanGame
            // System.out.println(" Remaining possible words: " +
            // game.possibleWords.size());
            System.out.println("   Algorithm's final word: " + finalGuessedWord);
            System.out.println("   Result: " + (success ? "SUCCESS" : "FAILURE"));
            System.out.println("--------------------------\n");
        }

        return success;
    }

    public static void main(String[] args) {
        // Set to 'true' to see the step-by-step process for every single word.
        // Set to 'false' to only see the final summary statistics.
        boolean showVerboseOutput = false;
        ReverseHangmanTester tester = new ReverseHangmanTester(showVerboseOutput);

        int[] lengthsToTest = { 5, 6, 7 };
        int grandTotalSuccesses = 0;
        int grandTotalFailures = 0;

        for (int length : lengthsToTest) {
            System.out.println("=============================================");
            System.out.println("   STARTING TESTS FOR " + length + "-LETTER WORDS   ");
            System.out.println("=============================================");

            List<String> wordsToTest = WordLoader.loadWords(length);
            if (wordsToTest == null || wordsToTest.isEmpty()) {
                System.out.println("Could not load words of length " + length + ". Skipping.");
                continue;
            }

            int successes = 0;
            int failures = 0;

            int totalWords = wordsToTest.size();
            int wordsProcessed = 0;

            System.out.println("\nProgress:");
            System.out.print("[");
            for (int i = 0; i < 50; i++)
                System.out.print(" ");
            System.out.print("] 0%");

            for (String word : wordsToTest) {
                if (tester.testWord(word)) {
                    successes++;
                } else {
                    failures++;
                }
                wordsProcessed++;

                // Update progress every 2% or at the end
                if (wordsProcessed % Math.max(1, totalWords / 50) == 0 || wordsProcessed == totalWords) {
                    int progress = (int) ((double) wordsProcessed / totalWords * 100);
                    int bars = (int) ((double) wordsProcessed / totalWords * 50);

                    // Move cursor back to start of progress line
                    System.out.print("\r[");
                    for (int i = 0; i < 50; i++) {
                        System.out.print(i < bars ? "=" : " ");
                    }
                    System.out.print("] " + progress + "%");
                }
            }
            System.out.println(); // New line after progress completes

            grandTotalSuccesses += successes;
            grandTotalFailures += failures;

            int total = successes + failures;
            double successRate = (total > 0) ? (double) successes / total * 100 : 0;

            System.out.println("\n--- SUMMARY FOR " + length + "-LETTER WORDS ---");
            System.out.println("Total Words Tested: " + total);
            System.out.println("Successes:          " + successes);
            System.out.println("Failures:           " + failures);
            System.out.printf("Success Rate:       %.2f%%\n\n", successRate);
        }

        System.out.println("=============================================");
        System.out.println("           OVERALL TEST SUMMARY              ");
        System.out.println("=============================================");
        int grandTotal = grandTotalSuccesses + grandTotalFailures;
        double overallSuccessRate = (grandTotal > 0) ? (double) grandTotalSuccesses / grandTotal * 100 : 0;
        System.out.println("Total Words Tested: " + grandTotal);
        System.out.println("Total Successes:    " + grandTotalSuccesses);
        System.out.println("Total Failures:     " + grandTotalFailures);
        System.out.printf("Overall Success Rate: %.2f%%\n", overallSuccessRate);
    }
}