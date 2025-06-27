import javax.swing.*;
import java.awt.*;

import java.util.*;

public class ReverseHangmanGUI extends JFrame {

    // GUI Components
    private JTextArea hangmanDisplay;
    private JLabel computerGuessLabel;
    private JLabel statusLabel;
    private JButton yesButton;
    private JButton noButton;
    private JButton newGameButton;
    private JTextField positionInput;
    private JTextField countInput;
    private JButton submitPositionsButton;
    private JPanel positionPanel;
    private JLabel instructionLabel;
    private JLabel wrongGuessesLabel;
    private JLabel wordDisplayLabel;

    // Game state
    private ArrayList<String> wordList;
    private char[] currentWordState;
    private String[] rightLetters;
    private int checker;
    private int incorrectGuess;
    private boolean gameOver;
    private char mostFrequent;
    private int wordLength;

    // Hangman drawings from your original code
    private String[] Hangmanpics = {
            "|-----|\n      |\n      |\n      |\n      |\n=========",
            "|-----|\nO     |\n      |\n      |\n      |\n=========",
            "|-----|\nO     |\n|     |\n      |\n      |\n=========",
            "|-----|\nO     |\n/|    |\n      |\n      |\n=========",
            "|-----|\n O    |\n/|\\   |\n      |\n      |\n=========",
            "|-----|\n O    |\n/|\\   |\n /    |\n      |\n=========",
            "|-----|\nO     |\n/|\\   |\n/ \\   |\n      |\n========="
    };

    public ReverseHangmanGUI() {
        // Show word length selection dialog
        Object[] options = { "5 Letters", "6 Letters", "7 Letters" };
        int selection = JOptionPane.showOptionDialog(null,
                "Select word length:",
                "Reverse Hangman",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        wordLength = 5 + selection; // 5, 6, or 7
        loadWordList();
        initializeGUI();
        startNewGame();
    }

    private void loadWordList() {
        wordList = new ArrayList<>(WordLoader.loadWords(wordLength));
    }

    private void initializeGUI() {
        setTitle("Reverse Hangman - Computer Guesses Your Word");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create panels
        JPanel topPanel = createTopPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel bottomPanel = createBottomPanel();

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("REVERSE HANGMAN", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLUE);

        statusLabel = new JLabel("Think of a " + wordLength + "-letter word and I will guess it!", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(statusLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left side - Hangman display
        hangmanDisplay = new JTextArea(8, 15);
        hangmanDisplay.setFont(new Font("Courier New", Font.BOLD, 12));
        hangmanDisplay.setEditable(false);
        hangmanDisplay.setBackground(Color.LIGHT_GRAY);
        hangmanDisplay.setBorder(BorderFactory.createLoweredBevelBorder());

        // Right side - Computer's guess and response area
        JPanel rightPanel = createGuessPanel();

        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 2));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Game Stats"));

        statsPanel.add(new JLabel("Wrong Guesses:"));
        wrongGuessesLabel = new JLabel("0");
        wrongGuessesLabel.setForeground(Color.RED);
        statsPanel.add(wrongGuessesLabel);

        wordDisplayLabel = new JLabel("", JLabel.CENTER);
        wordDisplayLabel.setFont(new Font("Courier New", Font.BOLD, 24));
        wordDisplayLabel.setBorder(BorderFactory.createTitledBorder("Current Word"));
        statsPanel.add(wordDisplayLabel);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(hangmanDisplay, BorderLayout.CENTER);
        leftPanel.add(statsPanel, BorderLayout.SOUTH);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createGuessPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Computer's Guess"));

        // Computer's guess display
        computerGuessLabel = new JLabel("", JLabel.CENTER);
        computerGuessLabel.setFont(new Font("Arial", Font.BOLD, 48));
        computerGuessLabel.setForeground(Color.BLUE);
        computerGuessLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Response buttons
        JPanel responsePanel = new JPanel(new FlowLayout());
        yesButton = new JButton("YES - Correct!");
        yesButton.setFont(new Font("Arial", Font.BOLD, 16));
        yesButton.setBackground(Color.GREEN);
        yesButton.setForeground(Color.WHITE);
        yesButton.addActionListener(e -> handleCorrectGuess());

        noButton = new JButton("NO - Wrong");
        noButton.setFont(new Font("Arial", Font.BOLD, 16));
        noButton.setBackground(Color.RED);
        noButton.setForeground(Color.WHITE);
        noButton.addActionListener(e -> handleIncorrectGuess());

        responsePanel.add(yesButton);
        responsePanel.add(noButton);

        // Position input panel (initially hidden)
        positionPanel = createPositionPanel();
        positionPanel.setVisible(false);

        // Instruction label
        instructionLabel = new JLabel("", JLabel.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(computerGuessLabel, BorderLayout.NORTH);
        panel.add(instructionLabel, BorderLayout.CENTER);
        panel.add(responsePanel, BorderLayout.SOUTH);
        panel.add(positionPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createPositionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Letter Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("How many times?"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        countInput = new JTextField(3);
        panel.add(countInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Positions (1-5):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        positionInput = new JTextField(10);
        positionInput.setToolTipText("Enter positions separated by spaces (e.g., '1 3 5')");
        panel.add(positionInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        submitPositionsButton = new JButton("Submit");
        submitPositionsButton.addActionListener(e -> processCorrectGuess());
        panel.add(submitPositionsButton, gbc);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 14));
        newGameButton.addActionListener(e -> startNewGame());

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(newGameButton);
        panel.add(exitButton);

        return panel;
    }

    private void startNewGame() {
        // Reset game state
        wordList = new ArrayList<>(wordList); // Reset the word list
        loadWordList(); // Reload the full word list
        rightLetters = new String[26];
        checker = 0;
        incorrectGuess = 0;
        gameOver = false;
        currentWordState = new char[wordLength];
        Arrays.fill(currentWordState, '_');
        updateWordDisplay();

        // Update display
        hangmanDisplay.setText(Hangmanpics[0]);
        computerGuessLabel.setText("?");
        statusLabel.setText("Think of a " + wordLength + "-letter word and I will guess it!");
        wrongGuessesLabel.setText("0");
        instructionLabel.setText("Ready when you are! Click 'Make Guess' to start.");

        // Reset UI
        yesButton.setEnabled(true);
        noButton.setEnabled(true);
        positionPanel.setVisible(false);

        // Make first guess
        makeComputerGuess();

        pack();
    }

    private void makeComputerGuess() {
        if (gameOver || wordList.isEmpty()) {
            return;
        }

        // Your original frequency analysis logic
        String BigString = "";
        for (String word : wordList) {
            BigString += word;
        }

        // Remove already guessed letters
        for (int i = 0; i < checker; i++) {
            if (rightLetters[i] != null) {
                BigString = BigString.replaceAll(rightLetters[i], "");
            }
        }

        if (BigString.isEmpty()) {
            gameOver = true;
            statusLabel.setText("I'm stumped! You win!");
            return;
        }

        char[] bigStringChars = BigString.toCharArray();
        mostFrequent = mostFrequent(bigStringChars, BigString.length());

        computerGuessLabel.setText(String.valueOf(mostFrequent));
        instructionLabel.setText("Is the letter '" + mostFrequent + "' in your word?");

        // Enable response buttons
        yesButton.setEnabled(true);
        noButton.setEnabled(true);
    }

    private void handleCorrectGuess() {
        yesButton.setEnabled(false);
        noButton.setEnabled(false);

        instructionLabel.setText("Great! Please provide details about this letter:");
        positionPanel.setVisible(true);
        countInput.setText("");
        positionInput.setText("");
        countInput.requestFocus();

        pack();
    }

    private void processCorrectGuess() {
        try {
            int numCorrect = Integer.parseInt(countInput.getText().trim());
            String[] positions = positionInput.getText().trim().split("\\s+");

            if (positions.length != numCorrect) {
                JOptionPane.showMessageDialog(this, "Number of positions doesn't match the count!");
                return;
            }

            int[] correctPositions = new int[numCorrect];
            for (int i = 0; i < numCorrect; i++) {
                correctPositions[i] = Integer.parseInt(positions[i]);
                if (correctPositions[i] < 1 || correctPositions[i] > wordLength) {
                    JOptionPane.showMessageDialog(this, "Positions must be between 1 and " + wordLength + "!");
                    return;
                }
            }

            // Your original logic for processing correct guesses
            processCorrectGuessLogic(numCorrect, correctPositions);

            // Record this letter as guessed
            rightLetters[checker] = String.valueOf(mostFrequent);
            checker++;

            positionPanel.setVisible(false);

            // Check if we've narrowed it down to one word
            if (wordList.size() == 1) {
                gameOver = true;
                String correctWord = wordList.get(0);
                statusLabel.setText("Yes! I did it! The word was: " + correctWord);
                computerGuessLabel.setText(correctWord);
                instructionLabel.setText("I guessed your word!");
                currentWordState = correctWord.toCharArray();
                updateWordDisplay();
            } else {
                makeComputerGuess();
            }

            pack();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!");
        }
    }

    private void processCorrectGuessLogic(int numCorrect, int[] correctPositions) {
        // Your original logic for filtering words when guess is correct

        // Remove words that don't have the letter in the correct positions
        for (int j = 0; j < numCorrect; j++) {
            for (int i = 0; i < wordList.size(); i++) {
                char[] wordChars = wordList.get(i).toCharArray();

                if (wordChars[correctPositions[j] - 1] != mostFrequent) {
                    wordList.remove(i);
                    i--;
                }
            }
        }

        // Remove words that have the letter in other positions
        for (int i = 0; i < wordList.size(); i++) {
            char[] wordChars = wordList.get(i).toCharArray();
            int letterCount = 0;

            for (int j = 0; j < wordChars.length; j++) {
                if (wordChars[j] == mostFrequent) {
                    letterCount++;
                }
            }

            if (letterCount != numCorrect) {
                wordList.remove(i);
                i--;
            }
        }
    }

    private void handleIncorrectGuess() {
        yesButton.setEnabled(false);
        noButton.setEnabled(false);

        // Your original logic for incorrect guesses
        for (int i = 0; i < wordList.size(); i++) {
            char[] wordChars = wordList.get(i).toCharArray();
            boolean containsLetter = false;

            for (char c : wordChars) {
                if (c == mostFrequent) {
                    containsLetter = true;
                    break;
                }
            }

            if (containsLetter) {
                wordList.remove(i);
                i--;
            }
        }

        // Record this letter as guessed
        rightLetters[checker] = String.valueOf(mostFrequent);
        checker++;

        incorrectGuess++;
        wrongGuessesLabel.setText(String.valueOf(incorrectGuess));
        hangmanDisplay.setText(Hangmanpics[incorrectGuess]);

        if (incorrectGuess >= 6) {
            gameOver = true;
            statusLabel.setText("Darn! I couldn't guess your word. You win!");
            instructionLabel.setText("I'm all out of guesses!");
        } else {
            statusLabel.setText("Darn! I'll try better next time :(");
            makeComputerGuess();
        }
    }

    // Your original mostFrequent method
    static char mostFrequent(char arr[], int n) {
        Arrays.sort(arr);

        int max_count = 1;
        char res = arr[0];
        int curr_count = 1;

        for (int i = 1; i < n; i++) {
            if (arr[i] == arr[i - 1])
                curr_count++;
            else
                curr_count = 1;

            if (curr_count > max_count) {
                max_count = curr_count;
                res = arr[i - 1];
            }
        }
        return res;
    }

    private void updateWordDisplay() {
        StringBuilder display = new StringBuilder();
        for (char c : currentWordState) {
            display.append(c).append(' ');
        }
        wordDisplayLabel.setText(display.toString().trim());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Use default look and feel
            }
            new ReverseHangmanGUI().setVisible(true);
        });
    }
}