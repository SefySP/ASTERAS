package gr.uop.asteras;

import java.io.*;
public class AddingBib
{

    private final File file;
    private String annotation;
    private String title;
    private String journal;
    private String booktitle;

    public AddingBib(File file) throws IOException
    {
        this.file = file;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));
        int inputCharacter;

        inputCharacter = bufferedReader.read();
        while (inputCharacter != -1)
        {
            if (inputCharacter == (int) '@')
            {
                inputCharacter = bufferedReader.read();
                setAnnotationTypeFromBib(inputCharacter);

                title = findDataFromWord("title", bufferedReader);

                setJournalAndBooktitleFromBib(bufferedReader);
            }
            inputCharacter = bufferedReader.read();
        }
    }

    private void setJournalAndBooktitleFromBib(BufferedReader bufferedReader) throws IOException
    {
        if (annotation.equals("article")) {
            journal   = findDataFromWord("journal", bufferedReader);
            booktitle = "";
        }
        else
        {
            journal   = "";
            booktitle = findDataFromWord("booktitle", bufferedReader);
        }
    }

    private void setAnnotationTypeFromBib(int inputCharacter)
    {
        if (inputCharacter == 'a')
        {
            annotation = "article";
            return;
        }
        annotation = "in proceedings";
    }


    public static String findDataFromWord(String word, BufferedReader bufferedReader) throws IOException
    {
        String data;
        searchFileUntilWord(word, bufferedReader);
        data = getBibFieldData(bufferedReader);
        return data;
    }

    public static void searchFileUntilWord(String word, BufferedReader bufferedReader) throws IOException
    {
        String searchfn = "";
        int fn, wordlength = word.length();
        while (!searchfn.equals(word)) {
            searchfn = "";
            fn = bufferedReader.read();
            while (fn != (int) word.charAt(0)) {
                fn = bufferedReader.read();
                if (fn == -1){
                    throw new IOException("Word not found");
                }
            }
            searchfn += word.charAt(0);
            for (int i = 0; i < wordlength-1 ; i++) {
                searchfn += (char) bufferedReader.read();
            }
        }
    }

    public static String getBibFieldData(BufferedReader bufferedReader) throws IOException
    {
        String data = "";
        int fn,fncount,previousChar;
        fn = bufferedReader.read();
        while (fn != (int) '{'){
            fn = bufferedReader.read();
        }
        fncount = 1;
        previousChar = fn;
        fn = bufferedReader.read();
        while (fncount != 0){
            if (fn == (int) '{'){
                fncount++;
            } else if (fn == (int) '}'){
                fncount--;
            }
            if (fncount != 0){
                if (fn != '\n'){
                    if(!(previousChar == (int)' ' && fn == (int)' ')){
                        data += (char) fn;
                    }
                }
                previousChar = fn;
                fn = bufferedReader.read();
            }
        }
        return data;
    }
}
