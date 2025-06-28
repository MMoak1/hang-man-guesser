import java.util.*;

public class ReverseHangmanGame {

    private List<String> fullWordList;
    List<String> possibleWords;
    private Set<Character> guessedLetters;
    private int incorrectGuesses;
    private boolean isGameOver;
    private String statusMessage;
    private int wordLength;
    private Map<String, Map<Character, Integer>> letterPatterns;

    public ReverseHangmanGame(int wordLength) {
        this.wordLength = wordLength;
        this.fullWordList = WordLoader.loadWords(wordLength);
        this.letterPatterns = new HashMap<>();
        precomputeLetterPatterns();
        startNewGame();
    }

    public void startNewGame() {
        this.possibleWords = new ArrayList<>(fullWordList);
        this.guessedLetters = new HashSet<>();
        this.incorrectGuesses = 0;
        this.isGameOver = false;
        this.statusMessage = "Think of a 5-letter word and I will guess it!";
    }

    public char makeGuess() {
        if (isGameOver) {
            return ' '; // Game is over, no more guesses
        }
        if (possibleWords.isEmpty()) {
            isGameOver = true;
            statusMessage = "I'm stumped! You win!";
            return ' ';
        }

        // Frequency analysis on remaining possible words
        StringBuilder bigString = new StringBuilder();
        for (String word : possibleWords) {
            bigString.append(word);
        }

        char[] allChars = bigString.toString().toCharArray();
        return findMostFrequentChar(allChars);
    }

    public void processCorrectGuess(char letter, int[] positions) {
        guessedLetters.add(letter);

        // Filter out words that don't match the letter pattern
        possibleWords.removeIf(word -> {
            Integer pattern = letterPatterns.get(word).get(letter);
            if (pattern == null) {
                return true; // Word doesn't contain this letter
            }

            // Check if positions match
            for (int pos : positions) {
                if ((pattern & (1 << (pos - 1))) == 0) {
                    return true; // Letter not in specified position
                }
            }

            // Check count matches
            return Integer.bitCount(pattern) != positions.length;
        });

        checkWinCondition();
    }

    public int getWordLength() {
        return wordLength;
    }

    public void processIncorrectGuess(char letter) {
        guessedLetters.add(letter);
        incorrectGuesses++;

        // Remove all words containing the incorrect letter
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

    private void precomputeLetterPatterns() {
        for (String word : fullWordList) {
            Map<Character, Integer> patterns = new HashMap<>();
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                patterns.merge(c, 1 << i, (old, newVal) -> old | newVal);
            }
            letterPatterns.put(word, patterns);
        }
    }

    private char findMostFrequentChar(char[] arr) {
        if (arr.length == 0)
            return ' ';

        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : arr) {
            // Only consider letters that have not been guessed yet
            if (!guessedLetters.contains(c)) {
                freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
            }
        }

        if (freqMap.isEmpty()) {
            // This can happen if all remaining letters in possible words have been guessed
            // Fallback to any unguessed letter
            for (char c = 'A'; c <= 'Z'; c++) {
                if (!guessedLetters.contains(c))
                    return c;
            }
        }

        // Find the character with the highest frequency
        return Collections.max(freqMap.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    // --- Getters for the GUI to query state ---

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
        if (possibleWords.size() == 1) {
            return possibleWords.get(0);
        }
        return null;
    }
}