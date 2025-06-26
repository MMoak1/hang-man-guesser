import java.util.*;

public class ReverseHangmanGame {
    // ... all other methods and variables remain the same ...

    private List<String> fullWordList;
    List<String> possibleWords; // Package-private for the tester
    private Set<Character> guessedLetters;
    private int incorrectGuesses;
    private boolean isGameOver;
    private String statusMessage;
    private int wordLength;

    public ReverseHangmanGame(int wordLength) {
        this.wordLength = wordLength;
        this.fullWordList = WordLoader.loadWords(wordLength);
        startNewGame();
    }

    public void startNewGame() {
        this.possibleWords = new ArrayList<>(fullWordList);
        this.guessedLetters = new HashSet<>();
        this.incorrectGuesses = 0;
        this.isGameOver = false;
        this.statusMessage = "Think of a " + wordLength + "-letter word and I will guess it!";
    }

    public char makeGuess() {
        if (isGameOver) {
            return ' ';
        }
        if (possibleWords.isEmpty() || possibleWords.size() == 1) {
            checkWinCondition();
            return ' ';
        }
        return findMostInformativeGuess();
    }

    /**
     * The final, superior guessing algorithm.
     * 1. Uses Minimax to find the guess that minimizes the worst-case outcome.
     * 2. If there's a tie, it chooses the guess that splits the word list into the
     * most partitions.
     */
    private char findMostInformativeGuess() {
        char bestGuess = ' ';
        int minWorstCaseSize = Integer.MAX_VALUE;
        int maxPartitions = 0; // Our new tie-breaker variable

        // Small optimization for the endgame
        if (possibleWords.size() <= 2) {
            for (char c : possibleWords.get(0).toCharArray()) {
                if (!guessedLetters.contains(c))
                    return c;
            }
        }

        for (char guessChar = 'A'; guessChar <= 'Z'; guessChar++) {
            if (guessedLetters.contains(guessChar)) {
                continue;
            }

            Map<String, List<String>> outcomes = new HashMap<>();
            for (String word : possibleWords) {
                StringBuilder patternBuilder = new StringBuilder();
                boolean matchFound = false;
                for (int i = 0; i < word.length(); i++) {
                    if (word.charAt(i) == guessChar) {
                        patternBuilder.append(guessChar);
                        matchFound = true;
                    } else {
                        patternBuilder.append('_');
                    }
                }
                String key = matchFound ? patternBuilder.toString() : "NO_MATCH";
                outcomes.computeIfAbsent(key, k -> new ArrayList<>()).add(word);
            }

            if (outcomes.isEmpty() || (outcomes.size() == 1 && outcomes.containsKey("NO_MATCH"))) {
                continue;
            }

            int worstCaseSizeForThisGuess = 0;
            for (List<String> group : outcomes.values()) {
                if (group.size() > worstCaseSizeForThisGuess) {
                    worstCaseSizeForThisGuess = group.size();
                }
            }

            // --- THIS IS THE NEW, CRITICAL LOGIC ---
            if (worstCaseSizeForThisGuess < minWorstCaseSize) {
                // A new best guess is found.
                minWorstCaseSize = worstCaseSizeForThisGuess;
                maxPartitions = outcomes.size();
                bestGuess = guessChar;
            } else if (worstCaseSizeForThisGuess == minWorstCaseSize) {
                // A tie in the worst-case size! Use the tie-breaker.
                if (outcomes.size() > maxPartitions) {
                    // This guess splits the list into more groups, so it's better.
                    maxPartitions = outcomes.size();
                    bestGuess = guessChar;
                }
            }
        }

        if (bestGuess == ' ') { // Fallback if no guess was chosen
            for (char c = 'A'; c <= 'Z'; c++) {
                if (!guessedLetters.contains(c))
                    return c;
            }
        }
        return bestGuess;
    }

    // --- All other methods (processCorrectGuess, processIncorrectGuess, etc.)
    // remain the same as the previous version ---

    public void processCorrectGuess(char letter, int[] positions) {
        guessedLetters.add(letter);
        possibleWords.removeIf(word -> {
            long actualCount = 0;
            for (char c : word.toCharArray()) {
                if (c == letter)
                    actualCount++;
            }
            if (actualCount != positions.length)
                return true;
            for (int pos : positions) {
                if (word.charAt(pos - 1) != letter)
                    return true;
            }
            return false;
        });
        checkWinCondition();
    }

    public int getWordLength() {
        return wordLength;
    }

    public void processIncorrectGuess(char letter) {
        guessedLetters.add(letter);
        incorrectGuesses++;
        possibleWords.removeIf(word -> word.indexOf(letter) != -1);
        if (incorrectGuesses >= 6) {
            isGameOver = true;
            statusMessage = "Darn! I couldn't guess your word. You win!";
        } else {
            statusMessage = "Darn! I'll try better next time :(";
        }
        checkWinCondition();
    }

    private void checkWinCondition() {
        if (!isGameOver) {
            if (possibleWords.size() == 1) {
                isGameOver = true;
                statusMessage = "Yes! I did it! The word was: " + possibleWords.get(0);
            } else if (possibleWords.isEmpty()) {
                isGameOver = true;
                statusMessage = "I'm stumped! Your word might not be in my list. You win!";
            }
        }
    }

    public int getIncorrectGuesses() {
        return incorrectGuesses;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getFinalWord() {
        if (isGameOver && possibleWords.size() == 1) {
            return possibleWords.get(0);
        }
        return null;
    }
}