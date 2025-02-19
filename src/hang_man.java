import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class hang_man {

    public static void main(String[] args) {
        // TODO Auto-generated method stub


        //set up to read data

        Scanner input = new Scanner(System.in);
        int choice;

        System.out.println("lets play Hang man, do you want to guess the word, or for me to guess. enter 1 to guess enter 2 for me to guess");
        choice=input.nextInt();

        File file = new File("src/words/5letterWords.txt");
        ArrayList<String> fiveletterwordslist = new ArrayList<>();

        try {
        Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                fiveletterwordslist.add(line);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }


        String [] Hangmanpics= new String [7];

        Hangmanpics[0]=
                """
                        |-----|
                              |
                              |
                              |
                              |
                        =========""";

        Hangmanpics[1]=
                """
                        |-----|
                        O     |
                              |
                              |
                              |
                        =========""";
        Hangmanpics[2]=
                """
                        |-----|
                        O     |
                        |     |
                              |
                              |
                        =========""";

        Hangmanpics[3]=
                """
                         |-----|
                         O     |
                        /|     |
                               |
                               |
                        =========""";
        Hangmanpics[4]=
                """
                         |-----|
                         O     |
                        /|\\    |
                               |
                               |
                        =========""";
        Hangmanpics[5]=
                """
                         |-----|
                         O     |
                        /|\\    |
                          \\    |
                               |
                        =========""";
        Hangmanpics[6]=
                """
                         |-----|
                         O     |
                        /|\\    |
                        / \\    |
                               |
                        =========""";


        if(choice==2)
        {
            Guesser(fiveletterwordslist, Hangmanpics);
        }

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
        String answer="";
        int placer=0;
        int correct=0;

		/*int length;
		System.out.println("how many letters long is your word.");
		length=input.nextInt();
		*/
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
            for(int i=0; i<Bigstringchars.length ;i++)
            {
                System.out.print(Bigstringchars[i]);
            }
            mostfrequent=mostFrequent(Bigstringchars,BigString.length());

            System.out.println(mostfrequent);

            System.out.println("My guess is "+ mostfrequent + " am I right? enter Y or N");
            correctguess=input.next();

            System.out.println(listofwords.size());

            if(correctguess.equals("Y"))
            {
                System.out.println("Yes!, how many "+ mostfrequent+" are there");
                numcorrect=input.nextInt();

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

                System.out.println(listofwords);

                incorrectguess++;

                System.out.println(pics[incorrectguess]);


            }

            checker++;

        }while(incorrectguess<6);

        if(didwin)
        {
            System.out.println("Yes! I did it the word was "+ answer);
            correct=1;

        }

        return correct;
    }

    static char mostFrequent(char arr[], int n)
    {
        // Sort the array
        Arrays.sort(arr);

        // find the max frequency using linear traversal
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
