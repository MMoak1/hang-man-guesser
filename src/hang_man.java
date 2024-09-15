import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 *
 */

/**
 * @author 23moakm
 *
 */
public class hang_man {

    /**
     * @param args
     */
    public static void main(String[] args) {

        //set up to read data

        Scanner input = new Scanner(System.in);

        ArrayList<String> fiveletterwordslist = new ArrayList<String>();

        // Specify the file path
        String filePath = "src/5letterWords.txt";

        // Read file line by line and store each line in the ArrayList
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                fiveletterwordslist.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        String [] Hangmanpics= new String [7];

        Hangmanpics[0]=
                "|-----|\n" +
                        "      |\n"+
                        "      |\n"+
                        "      |\n"+
                        "      |\n"+
                        "=========";

        Hangmanpics[1]=
                "|-----|\n" +
                        "O     |\n"+
                        "      |\n"+
                        "      |\n"+
                        "      |\n"+
                        "=========";
        Hangmanpics[2]=
                "|-----|\n" +
                        "O     |\n"+
                        "|     |\n"+
                        "      |\n"+
                        "      |\n"+
                        "=========";

        Hangmanpics[3]=
                " |-----|\n" +
                        " O     |\n"+
                        "/|     |\n"+
                        "       |\n"+
                        "       |\n"+
                        "=========";
        Hangmanpics[4]=
                " |-----|\n" +
                        " O     |\n"+
                        "/|\\    |\n"+
                        "       |\n"+
                        "       |\n"+
                        "=========";
        Hangmanpics[5]=
                " |-----|\n" +
                        " O     |\n"+
                        "/|\\    |\n"+
                        "  \\    |\n"+
                        "       |\n"+
                        "=========";
        Hangmanpics[6]=
                " |-----|\n" +
                        " O     |\n"+
                        "/|\\    |\n"+
                        "/ \\    |\n"+
                        "       |\n"+
                        "=========";

            Guesser(fiveletterwordslist, Hangmanpics);

    }

    public static int Guesser (ArrayList<String> listofwords, String [] pics)
    {
        Scanner input = new Scanner(System.in);
        String BigString="";
        char [] Bigstringchars;
        char mostfrequent=0;
        String correctguess;
        int numcorrect;
        int [] correctpostitions= new int [5];
        int count=0;
        int incorrectguess=0;
        String [] rightletters= new String [26];
        int checker=0;
        boolean didwin=false;
        int totalcorrect=0;
        String answer="";
        int placer=0;
        int correct=0;

        do
        {

            String stringmostfrequent= ""+mostfrequent;

            rightletters[checker]=stringmostfrequent;

            BigString="";

            for(int i=0; i<listofwords.size();i++)
            {
                BigString+=listofwords.get(i);
            }

            for(int i=0; i<checker+1 ;i++)
            {
                BigString=BigString.replaceAll(rightletters[i],"");
            }

            Bigstringchars= BigString.toCharArray();

            mostfrequent=mostFrequent(Bigstringchars,BigString.length());


            System.out.println("My guess is "+ mostfrequent + " am I right? enter Y or N");
            correctguess=input.next();


            if(correctguess.equals("Y"))
            {
                System.out.println("Yes!, how many "+ mostfrequent+" are there");
                numcorrect=input.nextInt();
                totalcorrect+=numcorrect;

                for(int i=0; i<numcorrect; i++)
                {
                    System.out.println("what is the postistion of the correct guess "+(i+1));
                    correctpostitions[i]=input.nextInt();
                }

                for(int j=0; j<numcorrect; j++)
                {


                    for (int i=0; i<listofwords.size();i++)
                    {
                        char [] wordchars=(listofwords.get(i)).toCharArray();

                        if(wordchars[correctpostitions[j]-1]!=mostfrequent)
                        {
                            count++;
                        }

                        if(count>0)
                        {
                            listofwords.remove(i);
                            i--;
                        }

                        count=0;
                    }

                }

                for (int i=0; i<listofwords.size();i++)
                {
                    char [] wordchars=(listofwords.get(i)).toCharArray();


                    for(int j=0;j<wordchars.length;j++)
                    {

                        for(int k=0; k<correctpostitions.length;k++)
                        {
                            if(correctpostitions[k]-1==j)
                            {
                                // do nothing
                            }

                            else if(correctpostitions[0]-1!=j)
                            {
                                if(wordchars[j]==mostfrequent)
                                {
                                    placer++;
                                }
                            }
                        }

                    }

                    if(placer>correctpostitions.length-1)
                    {
                        listofwords.remove(i);
                        i--;
                    }
                    placer=0;

                }

                System.out.println(listofwords);
                if(listofwords.size()==1)
                {
                    didwin=true;
                    answer=listofwords.get(0);
                    break;

                }

            }

            else
            {
                System.out.println("darn I'll try better next time :( ");

                for (int i=0; i<listofwords.size();i++)
                {
                    char [] wordchars=(listofwords.get(i)).toCharArray();
                }


                for (int i=0; i<listofwords.size();i++)
                {
                    char [] wordchars=(listofwords.get(i)).toCharArray();

                    for(int j=0;j<wordchars.length;j++)
                    {
                        if(wordchars[j]==mostfrequent)
                        {
                            count++;
                        }

                        if(count>0)
                        {
                            if(i<0)
                            {
                                listofwords.remove(i);
                                i--;
                            }
                        }

                        count=0;

                    }

                }

                incorrectguess++;

                System.out.println(pics[incorrectguess]);


            }
            checker++;

        }while(incorrectguess<6);

        if(didwin==true)
        {
            System.out.println("Yes! I did it the word was "+ answer);
            correct=1;

        }

        return correct;
    }

    static char mostFrequent(char arr[], int n)
    {
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

}