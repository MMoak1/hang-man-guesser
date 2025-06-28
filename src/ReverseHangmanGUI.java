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
    private JLabel wordDisplayLabel;

    // Game instance
    private ReverseHangmanGame game;

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

        int wordLength = 5 + selection; // 5, 6, or 7
        game = new ReverseHangmanGame(wordLength);
        initializeGUI();
        startNewGame();

        // Make initial guess and update UI
        makeComputerGuess();
        updateUI();
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

        statusLabel = new JLabel("Think of a " + game.getWordLength() + "-letter word and I will guess it!",
                JLabel.CENTER);
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
        game.startNewGame();
        updateUI();

        // Reset UI elements
        computerGuessLabel.setText("?");
        yesButton.setEnabled(false);
        noButton.setEnabled(false);
        positionPanel.setVisible(false);

        // Make initial guess
        makeComputerGuess();
    }

    private void makeComputerGuess() {
        char guess = game.makeGuess();
        if (guess == ' ') {
            updateUI();
            return;
        }

        computerGuessLabel.setText(String.valueOf(guess));
        instructionLabel.setText("Is the letter '" + guess + "' in your word?");
        yesButton.setEnabled(true);
        noButton.setEnabled(true);
        positionPanel.setVisible(false);

        pack(); // Ensure UI updates properly
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
                if (correctPositions[i] < 1 || correctPositions[i] > game.getWordLength()) {
                    JOptionPane.showMessageDialog(this,
                            "Positions must be between 1 and " + game.getWordLength() + "!");
                    return;
                }
            }

            game.processCorrectGuess(game.getLastGuess(), correctPositions);
            positionPanel.setVisible(false);
            updateUI();

            if (!game.isGameOver()) {
                makeComputerGuess();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!");
        }
    }

    private void handleIncorrectGuess() {
        yesButton.setEnabled(false);
        noButton.setEnabled(false);

        game.processIncorrectGuess(game.getLastGuess());
        updateUI();

        if (!game.isGameOver()) {
            makeComputerGuess();
        }
    }

    private void updateWordDisplay() {
        char[] wordState = game.getCurrentWordState();
        StringBuilder display = new StringBuilder();
        for (char c : wordState) {
            display.append(c).append(' ');
        }
        wordDisplayLabel.setText(display.toString().trim());
    }

    private void updateUI() {
        // Update hangman display
        hangmanDisplay.setText(Hangmanpics[game.getIncorrectGuesses()]);

        // Update status labels
        statusLabel.setText(game.getStatusMessage());
        wrongGuessesLabel.setText(String.valueOf(game.getIncorrectGuesses()));

        // Update word display
        updateWordDisplay();

        // Update computer guess display
        if (game.isGameOver() && game.getFinalWord() != null) {
            computerGuessLabel.setText(game.getFinalWord());
            instructionLabel.setText("I guessed your word!");
        } else if (game.isGameOver()) {
            computerGuessLabel.setText("?");
        }
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