import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class display_board extends JFrame implements ActionListener {
    private static JFrame game;
    private static JButton letters5;
    private static JButton letters6;
    private static JButton letters7;
    private static JButton letters8;

    private static int wordLength;

    public display_board() {
        game = new JFrame("HangMan");

        game.setSize(1000, 800);
        game.setLayout(null);
        game.setVisible(true);

        game.setDefaultCloseOperation(EXIT_ON_CLOSE);

        letters5 = new JButton("5 letters");
        letters6 = new JButton("6 letters");
        letters7 = new JButton("7 letters");
        letters8 = new JButton("8 letters");

        letters5.setBounds(100, 100, 250, 50);
        letters6.setBounds(400, 100, 250, 50);
        letters7.setBounds(100, 200, 250, 50);
        letters8.setBounds(400, 200, 250, 50);

        game.add(letters5);
        game.add(letters6);
        game.add(letters7);
        game.add(letters8);

        letters5.addActionListener(this);
        letters6.addActionListener(this);
        letters7.addActionListener(this);
        letters8.addActionListener(this);
    }

    public static void main(String[] args) {
        display_board board = new display_board();
    }

    public static int getWordLength() {
        return wordLength;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();

        if (clickedButton == letters5) {
            wordLength = 5;
        } else if (clickedButton == letters6) {
            wordLength = 6;
        } else if (clickedButton == letters7) {
            wordLength = 7;
        } else if (clickedButton == letters8) {
            wordLength = 8;
        }

        // Hide the buttons after selecting a word length
        letters5.setVisible(false);
        letters6.setVisible(false);
        letters7.setVisible(false);
        letters8.setVisible(false);

        LineDrawingPanel lines = new LineDrawingPanel();
        lines.setBounds(50, 100, 900, 200);
        game.add(lines);

        // Refresh the JFrame to reflect the changes
        game.revalidate();
        game.repaint();
    }

    public static JFrame getGame() {
        return game;
    }

    public static void setGame(JFrame game) {
        display_board.game = game;
    }
}

