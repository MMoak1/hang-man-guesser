import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class hang_man {

    public static void main(String[] args) {

        ArrayList<String> fiveletterwordslist = new ArrayList<>();

        // Specify the file path
        String filePath = "src/words/5letterWords.txt";

        // Read file line by line and store each line in the ArrayList
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                fiveletterwordslist.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] Hangmanpics = new String[7];

        Hangmanpics[0] =
                "|-----|\n" +
                        "      |\n" +
                        "      |\n" +
                        "      |\n" +
                        "      |\n" +
                        "=========";

        Hangmanpics[1] =
                "|-----|\n" +
                        "O     |\n" +
                        "      |\n" +
                        "      |\n" +
                        "      |\n" +
                        "=========";
        Hangmanpics[2] =
                "|-----|\n" +
                        "O     |\n" +
                        "|     |\n" +
                        "      |\n" +
                        "      |\n" +
                        "=========";

        Hangmanpics[3] =
                " |-----|\n" +
                        " O     |\n" +
                        "/|     |\n" +
                        "       |\n" +
                        "       |\n" +
                        "=========";
        Hangmanpics[4] =
                " |-----|\n" +
                        " O     |\n" +
                        "/|\\    |\n" +
                        "       |\n" +
                        "       |\n" +
                        "=========";
        Hangmanpics[5] =
                " |-----|\n" +
                        " O     |\n" +
                        "/|\\    |\n" +
                        "  \\    |\n" +
                        "       |\n" +
                        "=========";
        Hangmanpics[6] =
                " |-----|\n" +
                        " O     |\n" +
                        "/|\\    |\n" +
                        "/ \\    |\n" +
                        "       |\n" +
                        "=========";

        Guesser(fiveletterwordslist, Hangmanpics);

    }

    public static void Guesser(ArrayList<String> listofwords, String[] pics) {
        Scanner input = new Scanner(System.in);
        char mostfrequent;
        String correctguess;
        int numcorrect;
        int[] correctPostitions = new int[5];
        int count = 0;
        int incorrectguess = 0;
        ArrayList<Character> rightLetters = new ArrayList<>();
        boolean didwin = false;
        int totalcorrect = 0;
        String answer = "";
        int placer = 0;

        while (incorrectguess < 6) {

            mostfrequent = mostFrequent(listofwords, rightLetters);

            System.out.println("My guess is " + mostfrequent + " am I right? enter Y or N");
            correctguess = input.next();


            if (correctguess.equals("Y")) {
                rightLetters.add(mostfrequent);
                System.out.println("Yes!, how many " + mostfrequent + " are there");
                numcorrect = input.nextInt();
                totalcorrect += numcorrect;

                for (int i = 0; i < numcorrect; i++) {
                    System.out.println("what is the position of the correct guess " + (i + 1));
                    correctPostitions[i] = input.nextInt();
                }

                for (int j = 0; j < numcorrect; j++) {

                    for (int i = 0; i < listofwords.size(); i++) {
                        char[] wordchars = (listofwords.get(i)).toCharArray();

                        if (wordchars[correctPostitions[j] - 1] != mostfrequent) {
                            count++;
                        }

                        if (count > 0) {
                            listofwords.remove(i);
                            i--;
                        }

                        count = 0;
                    }

                }

                for (int i = 0; i < listofwords.size(); i++) {
                    char[] wordchars = (listofwords.get(i)).toCharArray();


                    for (int j = 0; j < wordchars.length; j++) {

                        for (int k = 0; k < correctPostitions.length; k++) {
                            if (correctPostitions[k] - 1 == j) {
                                // do nothing

                            } else if (correctPostitions[0] - 1 != j) {
                                if (wordchars[j] == mostfrequent) {
                                    placer++;
                                }
                            }
                        }

                    }

                    if (placer > correctPostitions.length - 1) {

                        listofwords.remove(i);
                        i--;
                    }
                    placer = 0;

                }

                System.out.println(listofwords);
                if (listofwords.size() == 1) {
                    didwin = true;
                    answer = listofwords.get(0);
                    break;

                }

            } else {

                listofwords = wrongGuessRemover(listofwords, mostfrequent);

                incorrectguess++;

                System.out.println(pics[incorrectguess]);


            }

        }

        if (didwin == true) {
            System.out.println("Yes! I did it the word was " + answer);

        }

    }

    static char mostFrequent(ArrayList<String> wordList, ArrayList<Character> rightLetters) {
        int max_count = 0;
        int letterCount = 0;

        char bestLetter = 'a';

        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        // idk why i need to do this
        Character[] alphabetArray = new Character[alphabet.length];
        for (int i = 0; i < alphabet.length; i++) {
            alphabetArray[i] = alphabet[i];
        }

        List<Character> alphabetList = new ArrayList<>(Arrays.asList(alphabetArray));

        for (int i = 0; i < rightLetters.size(); i++) {
            if (alphabetList.contains(rightLetters.get(i))) {
                alphabetList.remove(rightLetters.get(i));
            }

        }

        System.out.println(alphabetList);


        for (int i = 0; i < alphabetList.size(); i++) {
            for (int j = 0; j < wordList.size(); j++) {
                for (int k = 0; k < wordList.get(j).length(); k++) {
                    if (alphabetList.get(i) == wordList.get(j).charAt(k)) {
                        letterCount++;
                        break;
                    }
                }
            }

            if (letterCount > max_count) {
                max_count = letterCount;
                bestLetter = alphabetList.get(i);
            }
            letterCount = 0;
        }


        return bestLetter;

    }

    static ArrayList<String> wrongGuessRemover(ArrayList<String> listofwords, char mostfrequent) {

        System.out.println("darn I'll try better next time :( ");

        //check first
        for (int j = 0; j < listofwords.get(0).length(); j++) {
            if (listofwords.get(0).charAt(j) == mostfrequent) {
                listofwords.remove(0);
            }
        }


        for (int i = 1; i < listofwords.size(); i++) {

            for (int j = 0; j < listofwords.get(i).length(); j++) {
                if (listofwords.get(i).charAt(j) == mostfrequent) {
                    listofwords.remove(i);
                    i--;
                }

            }

        }

        System.out.println(listofwords);

        return listofwords;



    }

}