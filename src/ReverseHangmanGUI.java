import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

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

    // Game state - reusing your original logic
    private ArrayList<String> fiveLetterWordsList;
    private String[] rightLetters;
    private int checker;
    private int incorrectGuess;
    private boolean gameOver;
    private char mostFrequent;

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
        loadWordList();
        initializeGUI();
        startNewGame();
    }

    private void loadWordList() {
        fiveLetterWordsList = new ArrayList<>();

        // Try to load from your original file path
        File file = new File("words\\5letterWords.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                fiveLetterWordsList.add(line.toUpperCase());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            // Load default words if file not found
            loadDefaultWords();
        }

        if (fiveLetterWordsList.isEmpty()) {
            loadDefaultWords();
        }
    }

    private void loadDefaultWords() {
        String[] defaultWords = {
                "ABOUT", "ABOVE", "ACTOR", "ADMIT", "ADULT", "AFTER", "AGAIN", "AGENT", "AGREE", "AHEAD",
                "ALARM", "ALBUM", "ALERT", "ALIEN", "ALIKE", "ALIVE", "ALLOW", "ALONE", "ALONG", "AMONG",
                "APPLE", "APPLY", "ARENA", "ARGUE", "ARISE", "ASIDE", "AUDIO", "AVOID", "AWAKE", "AWARD",
                "BASIC", "BEACH", "BEGIN", "BEING", "BELOW", "BLACK", "BLANK", "BLOOD", "BOARD", "BRAIN",
                "BRAND", "BRAVE", "BREAD", "BREAK", "BRING", "BROAD", "BROWN", "BUILD", "BUILT", "CARRY",
                "CATCH", "CAUSE", "CHAIR", "CHART", "CHASE", "CHECK", "CHEST", "CHIEF", "CHILD", "CHINA",
                "CLAIM", "CLASS", "CLEAN", "CLEAR", "CLIMB", "CLOCK", "CLOSE", "CLOUD", "COACH", "COAST",
                "COUNT", "COURT", "COVER", "CRAFT", "CRASH", "CREAM", "CRIME", "CROSS", "CROWD", "DAILY",
                "DANCE", "DEATH", "DELAY", "DEPTH", "DOING", "DOUBT", "DRAFT", "DRAMA", "DREAM", "DRESS",
                "DRINK", "DRIVE", "EARLY", "EARTH", "EIGHT", "EMPTY", "ENEMY", "ENJOY", "ENTER", "EQUAL",
                "ERROR", "EVENT", "EVERY", "EXACT", "EXIST", "EXTRA", "FAITH", "FALSE", "FIELD", "FIGHT",
                "FINAL", "FIRST", "FLASH", "FLOOR", "FOCUS", "FORCE", "FOUND", "FRESH", "FRONT", "FRUIT",
                "FULLY", "GLASS", "GOING", "GRACE", "GRAND", "GRANT", "GRASS", "GREAT", "GREEN", "GROUP",
                "GROWN", "GUARD", "GUESS", "GUEST", "GUIDE", "HAPPY", "HEART", "HEAVY", "HORSE", "HOTEL",
                "HOUSE", "HUMAN", "IMAGE", "INDEX", "INNER", "INPUT", "ISSUE", "JAPAN", "JUDGE", "KNOWN",
                "LABEL", "LARGE", "LATER", "LAUGH", "LAYER", "LEARN", "LEAST", "LEAVE", "LEGAL", "LEVEL",
                "LIGHT", "LIMIT", "LINKS", "LIVES", "LOCAL", "LOGIC", "LOWER", "LUCKY", "LUNCH", "MAGIC",
                "MAJOR", "MARCH", "MATCH", "MAYBE", "MAYOR", "MEANT", "MEDIA", "METAL", "MIGHT", "MINOR",
                "MODEL", "MONEY", "MONTH", "MORAL", "MOTOR", "MOUNT", "MOUSE", "MOUTH", "MOVED", "MOVIE",
                "MUSIC", "NEEDS", "NEVER", "NIGHT", "NOISE", "NORTH", "NOTED", "NOVEL", "NURSE", "OCCUR",
                "OCEAN", "OFFER", "OFTEN", "ORDER", "OTHER", "OUGHT", "PAINT", "PANEL", "PAPER", "PARTY",
                "PEACE", "PHASE", "PHONE", "PHOTO", "PIANO", "PIECE", "PILOT", "PITCH", "PLACE", "PLAIN",
                "PLANE", "PLANT", "PLATE", "POINT", "POUND", "POWER", "PRESS", "PRICE", "PRIDE", "PRIME",
                "PRINT", "PRIOR", "PRIZE", "PROOF", "PROUD", "PROVE", "QUEEN", "QUICK", "QUIET", "QUITE",
                "RADIO", "RAISE", "RANGE", "RAPID", "RATIO", "REACH", "READY", "REFER", "RELAX", "REPLY",
                "RIGHT", "RIVAL", "RIVER", "ROBIN", "ROUGH", "ROUND", "ROUTE", "ROYAL", "RURAL", "SCALE",
                "SCENE", "SCOPE", "SCORE", "SENSE", "SERVE", "SEVEN", "SHALL", "SHAPE", "SHARE", "SHARP",
                "SHEET", "SHELF", "SHELL", "SHIFT", "SHINE", "SHIRT", "SHOCK", "SHOOT", "SHORT", "SHOWN",
                "SIGHT", "SINCE", "SIXTH", "SIXTY", "SKILL", "SLEEP", "SLIDE", "SMALL", "SMART", "SMILE",
                "SMOKE", "SOLID", "SOLVE", "SORRY", "SOUND", "SOUTH", "SPACE", "SPARE", "SPEAK", "SPEED",
                "SPEND", "SPLIT", "SPOKE", "SPORT", "STAFF", "STAGE", "STAKE", "STAND", "START", "STATE",
                "STEAM", "STEEL", "STICK", "STILL", "STOCK", "STONE", "STOOD", "STORE", "STORM", "STORY",
                "STUCK", "STUDY", "STUFF", "STYLE", "SUGAR", "SUPER", "SWEET", "TABLE", "TAKEN", "TASTE",
                "TEACH", "TEETH", "THANK", "THEIR", "THEME", "THERE", "THESE", "THICK", "THING", "THINK",
                "THIRD", "THOSE", "THREE", "THREW", "THROW", "TIGER", "TIGHT", "TIMES", "TIRED", "TITLE",
                "TODAY", "TOPIC", "TOTAL", "TOUCH", "TOUGH", "TOWER", "TRACK", "TRADE", "TRAIN", "TREAT",
                "TRIAL", "TRIBE", "TRUCK", "TRULY", "TRUST", "TRUTH", "TWICE", "UNCLE", "UNDER", "UNION",
                "UNTIL", "UPPER", "UPSET", "URBAN", "USAGE", "USUAL", "VALID", "VALUE", "VIDEO", "VIRUS",
                "VISIT", "VITAL", "VOICE", "WASTE", "WATCH", "WATER", "WHEEL", "WHERE", "WHICH", "WHILE",
                "WHITE", "WHOLE", "WHOSE", "WOMAN", "WOMEN", "WORLD", "WORRY", "WORSE", "WORST", "WORTH",
                "WOULD", "WRITE", "WRONG", "WROTE", "YOUNG", "YOUTH"
        };

        Collections.addAll(fiveLetterWordsList, defaultWords);
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

        statusLabel = new JLabel("Think of a 5-letter word and I will guess it!", JLabel.CENTER);
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
        // Reset game state using your original logic
        fiveLetterWordsList = new ArrayList<>(fiveLetterWordsList); // Reset the word list
        loadWordList(); // Reload the full word list
        rightLetters = new String[26];
        checker = 0;
        incorrectGuess = 0;
        gameOver = false;

        // Update display
        hangmanDisplay.setText(Hangmanpics[0]);
        computerGuessLabel.setText("?");
        statusLabel.setText("Think of a 5-letter word and I will guess it!");
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
        if (gameOver || fiveLetterWordsList.isEmpty()) {
            return;
        }

        // Your original frequency analysis logic
        String BigString = "";
        for (String word : fiveLetterWordsList) {
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
                if (correctPositions[i] < 1 || correctPositions[i] > 5) {
                    JOptionPane.showMessageDialog(this, "Positions must be between 1 and 5!");
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
            if (fiveLetterWordsList.size() == 1) {
                gameOver = true;
                statusLabel.setText("Yes! I did it! The word was: " + fiveLetterWordsList.get(0));
                computerGuessLabel.setText(fiveLetterWordsList.get(0));
                instructionLabel.setText("I guessed your word!");
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
        int count = 0;

        // Remove words that don't have the letter in the correct positions
        for (int j = 0; j < numCorrect; j++) {
            for (int i = 0; i < fiveLetterWordsList.size(); i++) {
                char[] wordChars = fiveLetterWordsList.get(i).toCharArray();

                if (wordChars[correctPositions[j] - 1] != mostFrequent) {
                    fiveLetterWordsList.remove(i);
                    i--;
                }
            }
        }

        // Remove words that have the letter in other positions
        for (int i = 0; i < fiveLetterWordsList.size(); i++) {
            char[] wordChars = fiveLetterWordsList.get(i).toCharArray();
            int letterCount = 0;

            for (int j = 0; j < wordChars.length; j++) {
                if (wordChars[j] == mostFrequent) {
                    letterCount++;
                }
            }

            if (letterCount != numCorrect) {
                fiveLetterWordsList.remove(i);
                i--;
            }
        }
    }

    private void handleIncorrectGuess() {
        yesButton.setEnabled(false);
        noButton.setEnabled(false);

        // Your original logic for incorrect guesses
        for (int i = 0; i < fiveLetterWordsList.size(); i++) {
            char[] wordChars = fiveLetterWordsList.get(i).toCharArray();
            boolean containsLetter = false;

            for (char c : wordChars) {
                if (c == mostFrequent) {
                    containsLetter = true;
                    break;
                }
            }

            if (containsLetter) {
                fiveLetterWordsList.remove(i);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
            } catch (Exception e) {
                // Use default look and feel
            }
            new ReverseHangmanGUI().setVisible(true);
        });
    }
}