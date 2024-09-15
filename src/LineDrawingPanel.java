import javax.swing.*;
import java.awt.*;

public class LineDrawingPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(2));


        int yPosition = 50;
        int lineLength = 40;
        int gapBetweenLines = 50;

        // Drawing the number of lines based on the selected word length
        for (int i = 0; i < display_board.getWordLength(); i++) {
            g2d.drawLine(20 + i * (lineLength + gapBetweenLines), yPosition,
                    50 + i * (lineLength + gapBetweenLines) + lineLength, yPosition);
        }
    }
}