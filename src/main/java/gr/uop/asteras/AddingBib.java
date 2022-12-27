package gr.uop.asteras;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
public class AddingBib{
    public AddingBib(File file) {
        System.out.println("AddingBib");
        String filename = file.getName();
        Optional<Object> extension;

        try {
            BufferedReader fget = new BufferedReader(new FileReader(file));
            extension = Optional.ofNullable(filename).filter(f -> f.contains(".")).map(f -> f.substring(filename.lastIndexOf(".") + 1)); // βρίσκει αν είναι .bib
            System.out.println(extension);
            if (extension.toString().equals("Optional[bib]")) {
                String fname,title = "",otherThing,type = "";
                int fn;
                fname = filename.substring(0, filename.lastIndexOf('.'));

                System.out.println(fname);
                System.out.println("bib YES");

                
                fn = fget.read();
                while (fn != -1) { // Δεν ελέγχει το αρχείο για όλων των ειδών τα λάθη
                    
                    title = "";
                    otherThing = "";
                    if (fn == (int) '@'){//Βρέθηκε έγγραφο
                        System.out.println("\n\n\n-----New Document-----");
                        fn = fget.read();
                        if (fn == (int) 'a'){
                            type = "article";
                            System.out.println("------Article---------");
                        }
                        else{
                            type = "inproceedings";
                            System.out.println("------Inproceedings---");
                        }
                            
                        title = findDataFromWord("title", fget);
                        
                        System.out.println("The title is: " + title + "\n");   

                        if (type.equals("article")){
                            otherThing = findDataFromWord("journal", fget);
                            System.out.println("The journal is: " + otherThing + "\n");
                        }
                        else {
                            otherThing = findDataFromWord("booktitle", fget);
                            System.out.println("The booktitle is: " + otherThing + "\n");
                        }


                    }
                    fn = fget.read();
                }//Διάβασμα του αρχείου
                
                
                
            } else {
                System.out.println("not bib");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String findDataFromWord(String word, BufferedReader fget) throws Exception{
        String data ="";
        searchFileUntilWord(word, fget);
        data = getBibFieldData(fget);
        return data;
    }

    static void searchFileUntilWord(String word, BufferedReader fget) throws Exception {
        String searchfn = "";
        int fn, wordlength = word.length();
        while (!searchfn.equals(word)) {
            searchfn = "";
            fn = fget.read();
            while (fn != (int) word.charAt(0)) {
                fn = fget.read();
                if (fn == -1){
                    throw new Exception("Word not found");
                }
            }
            searchfn += word.charAt(0);
            for (int i = 0; i < wordlength-1 ; i++) {
                searchfn += (char) fget.read();
            }
        }
    }

    static String getBibFieldData(BufferedReader fget) throws IOException{
        String data = "";
        int fn,fncount,previousChar;
        fn = fget.read();
        while (fn != (int) '{'){
            fn = fget.read();
        }
        fncount = 1;
        previousChar = fn;
        fn = fget.read();
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
                fn = fget.read();
            }
        }
        return data;
    }
}




// while (!searchfn.equals("title")){
//     while (fget.read() != (int) 't'){
        
//     };
//     searchfn = "t";
//     for (int i = 0; i < 4; i++) {
//         searchfn += (char) fget.read();
//     }
// }




// fn = fget.read();
// while (fn != (int) '{'){
//     fn = fget.read();
// }
// fncount = 1;
// title = "";
// previousChar = fn;
// fn = fget.read();
// while (fncount != 0){
//     if (fn == (int) '{'){
//         fncount++;
//     } else if (fn == (int) '}'){
//         fncount--;
//     }
//     if (fncount != 0){
//         if (fn != '\n'){
//             if(!(previousChar == (int)' ' && fn == (int)' ')){
//                 title += (char) fn;
//             }
//         }
//         previousChar = fn;
//         fn = fget.read();
//     }
// }
