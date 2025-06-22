import javax.swing.*;
import java.awt.*;

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
    private JLabel revealedWordLabel; // <<< NEW: Label for _ _ _ _ _

    // The Game Logic Handler
    private ReverseHangmanGame game;
    private char currentGuess;
    private char[] revealedLetters; // <<< NEW: Array to hold the word being built

    // Hangman drawings
    private final String[] HANGMAN_PICS = {
            "|-----|\n      |\n      |\n      |\n      |\n=========",
            "|-----|\nO     |\n      |\n      |\n      |\n=========",
            "|-----|\nO     |\n|     |\n      |\n      |\n=========",
            "|-----|\nO     |\n/|    |\n      |\n      |\n=========",
            "|-----|\n O    |\n/|\\   |\n      |\n      |\n=========",
            "|-----|\n O    |\n/|\\   |\n /    |\n      |\n=========",
            "|-----|\n O    |\n/|\\   |\n/ \\   |\n      |\n========="
    };

    public ReverseHangmanGUI() {
        this.game = new ReverseHangmanGame();
        this.revealedLetters = new char[5]; // <<< NEW: Initialize the array
        initializeGUI();
        startNewGame();
    }

    private void initializeGUI() {
        setTitle("Reverse Hangman - Computer Guesses Your Word");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void startNewGame() {
        game.startNewGame();
        // <<< MODIFIED: Reset the revealed letters to blanks
        for (int i = 0; i < revealedLetters.length; i++) {
            revealedLetters[i] = '_';
        }
        updateRevealedWordDisplay(); // Update the label to show the blanks
        makeComputerGuess();
    }

    private void makeComputerGuess() {
        currentGuess = game.makeGuess();
        updateUIForNewTurn();
    }

    private void handleCorrectGuess() {
        yesButton.setEnabled(false);
        noButton.setEnabled(false);
        instructionLabel.setText("Great! Please provide details for letter '" + currentGuess + "':");
        positionPanel.setVisible(true);
        countInput.setText("");
        positionInput.setText("");
        countInput.requestFocusInWindow();
        pack();
    }

    private void handleIncorrectGuess() {
        game.processIncorrectGuess(currentGuess);
        makeComputerGuess();
    }

    private void processCorrectGuessSubmission() {
        try {
            int numCorrect = Integer.parseInt(countInput.getText().trim());
            if (numCorrect <= 0 || numCorrect > 5) {
                JOptionPane.showMessageDialog(this, "The count must be between 1 and 5.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] posStrings = positionInput.getText().trim().split("\\s+");
            if (posStrings.length != numCorrect) {
                JOptionPane.showMessageDialog(this, "Number of positions entered does not match the count.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int[] positions = new int[numCorrect];
            for (int i = 0; i < numCorrect; i++) {
                int pos = Integer.parseInt(posStrings[i]);
                if (pos < 1 || pos > 5) {
                    JOptionPane.showMessageDialog(this, "Positions must be between 1 and 5.", "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                positions[i] = pos;
                // <<< NEW: Update our internal array of revealed letters
                revealedLetters[pos - 1] = currentGuess;
            }

            // All input is valid, now we can update the UI and game model
            positionPanel.setVisible(false);
            updateRevealedWordDisplay(); // <<< NEW: Update the display with the new letters
            game.processCorrectGuess(currentGuess, positions);
            makeComputerGuess();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for count and positions.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // <<< NEW: Helper method to format and display the revealed letters
    private void updateRevealedWordDisplay() {
        StringBuilder displayText = new StringBuilder();
        for (char letter : revealedLetters) {
            displayText.append(letter).append(" ");
        }
        revealedWordLabel.setText(displayText.toString().trim());
    }

    private void updateUIForNewTurn() {
        statusLabel.setText(game.getStatusMessage());
        wrongGuessesLabel.setText(String.valueOf(game.getIncorrectGuesses()));

        int hangmanIndex = Math.min(game.getIncorrectGuesses(), HANGMAN_PICS.length - 1);
        hangmanDisplay.setText(HANGMAN_PICS[hangmanIndex]);

        if (game.isGameOver()) {
            yesButton.setEnabled(false);
            noButton.setEnabled(false);
            positionPanel.setVisible(false);

            String finalWord = game.getFinalWord();
            if (finalWord != null) {
                computerGuessLabel.setText(finalWord);
                instructionLabel.setText("I guessed your word!");
                // <<< MODIFIED: Show the final word in the placeholder display
                revealedWordLabel.setText(finalWord.replace("", " ").trim());
            } else {
                computerGuessLabel.setText(":(");
                instructionLabel.setText("I'm all out of guesses!");
            }
        } else {
            computerGuessLabel.setText(String.valueOf(currentGuess));
            instructionLabel.setText("Is the letter '" + currentGuess + "' in your word?");
            yesButton.setEnabled(true);
            noButton.setEnabled(true);
        }
        pack();
    }

    // --- Panel Creation Methods ---

    // <<< MODIFIED: The top panel now includes the revealed word label
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10)); // Add vertical gap
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Sub-panel for title and status
        JPanel infoPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("REVERSE HANGMAN", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLUE);
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        infoPanel.add(titleLabel, BorderLayout.NORTH);
        infoPanel.add(statusLabel, BorderLayout.SOUTH);

        // Revealed word label
        revealedWordLabel = new JLabel();
        revealedWordLabel.setFont(new Font("Courier New", Font.BOLD, 48)); // Monospaced font for alignment
        revealedWordLabel.setHorizontalAlignment(JLabel.CENTER);

        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(revealedWordLabel, BorderLayout.CENTER); // Add new label to the center
        return panel;
    }

    // Unchanged methods below...
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        hangmanDisplay = new JTextArea(8, 15);
        hangmanDisplay.setFont(new Font("Courier New", Font.BOLD, 12));
        hangmanDisplay.setEditable(false);
        hangmanDisplay.setBackground(getBackground());
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.add(hangmanDisplay, BorderLayout.CENTER);
        JPanel statsPanel = new JPanel(new GridLayout(1, 2));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Game Stats"));
        statsPanel.add(new JLabel("Wrong Guesses:"));
        wrongGuessesLabel = new JLabel("0");
        wrongGuessesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        wrongGuessesLabel.setForeground(Color.RED);
        statsPanel.add(wrongGuessesLabel);
        leftPanel.add(statsPanel, BorderLayout.SOUTH);
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(createGuessPanel(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createGuessPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Computer's Guess"));
        computerGuessLabel = new JLabel("", JLabel.CENTER);
        computerGuessLabel.setFont(new Font("Arial", Font.BOLD, 48));
        computerGuessLabel.setForeground(Color.BLUE);
        instructionLabel = new JLabel("", JLabel.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        JPanel responsePanel = new JPanel(new FlowLayout());
        yesButton = new JButton("YES");
        yesButton.setFont(new Font("Arial", Font.BOLD, 16));
        yesButton.setBackground(new Color(34, 139, 34));
        yesButton.setForeground(Color.WHITE);
        yesButton.addActionListener(e -> handleCorrectGuess());
        noButton = new JButton("NO");
        noButton.setFont(new Font("Arial", Font.BOLD, 16));
        noButton.setBackground(new Color(220, 20, 60));
        noButton.setForeground(Color.WHITE);
        noButton.addActionListener(e -> handleIncorrectGuess());
        responsePanel.add(yesButton);
        responsePanel.add(noButton);
        JPanel guessAndInstructionPanel = new JPanel(new BorderLayout());
        guessAndInstructionPanel.add(computerGuessLabel, BorderLayout.CENTER);
        guessAndInstructionPanel.add(instructionLabel, BorderLayout.SOUTH);
        panel.add(guessAndInstructionPanel, BorderLayout.CENTER);
        panel.add(responsePanel, BorderLayout.SOUTH);
        panel.add(createPositionPanel(), BorderLayout.EAST);
        return panel;
    }

    private JPanel createPositionPanel() {
        positionPanel = new JPanel(new GridBagLayout());
        positionPanel.setBorder(BorderFactory.createTitledBorder("Letter Details"));
        positionPanel.setVisible(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        positionPanel.add(new JLabel("How many times?"), gbc);
        gbc.gridx = 1;
        countInput = new JTextField(3);
        positionPanel.add(countInput, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        positionPanel.add(new JLabel("Positions (e.g. 1 3):"), gbc);
        gbc.gridx = 1;
        positionInput = new JTextField(10);
        positionPanel.add(positionInput, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        submitPositionsButton = new JButton("Submit");
        submitPositionsButton.addActionListener(e -> processCorrectGuessSubmission());
        positionPanel.add(submitPositionsButton, gbc);
        return positionPanel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
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
}