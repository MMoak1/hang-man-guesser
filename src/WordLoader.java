import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WordLoader {

    /**
     * Loads a list of words of specified length from corresponding word file.
     *
     * @param wordLength The length of words to load (5, 6, or 7)
     * @return A list of uppercase words of specified length
     */
    public static List<String> loadWords(int wordLength) {
        List<String> wordList = new ArrayList<>();
        String filename = "words/" + wordLength + "letterWords.txt";

        try (InputStream is = WordLoader.class.getClassLoader().getResourceAsStream(filename);
                Scanner scanner = new Scanner(is)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim().toUpperCase();
                if (line.length() == wordLength) {
                    wordList.add(line);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading word file for " + wordLength + " letter words.");
        }

        return wordList;
    }
}