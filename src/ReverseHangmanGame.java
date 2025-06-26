import java.util.*;

public class ReverseHangmanGame {

    private List<String> fullWordList;
    private List<String> possibleWords;
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

        // Filter out words that don't have the letter in the specified positions
        possibleWords.removeIf(word -> {
            for (int pos : positions) {
                if (pos < 1 || pos > wordLength || word.charAt(pos - 1) != letter) {
                    return true; // Remove if invalid position or letter not in spot
                }
            }
            return false;
        });

        // Filter out words that have a different number of occurrences of the letter
        possibleWords.removeIf(word -> {
            long count = word.chars().filter(ch -> ch == letter).count();
            return count != positions.length;
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