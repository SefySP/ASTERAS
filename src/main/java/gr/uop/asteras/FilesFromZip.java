package gr.uop.asteras;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FilesFromZip
{

    private ZipFile zipFile;
    private Enumeration<? extends ZipEntry> entries;

    public FilesFromZip(String PathToFile) throws IOException
    {
        System.out.println("Hello World here1");
        this.zipFile = new ZipFile(PathToFile);
        this.entries = this.zipFile.entries();
    }

    public File getnextfile() throws IOException, InterruptedException{

        System.out.println(entries);
        if(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            String name = entry.getName();
            name = name.substring(name.lastIndexOf('/') + 1, name.length( ));
            System.out.println(name);
            if (name.toUpperCase().endsWith(".BIB")){
                System.out.println("\nHi there\n");
                File dir = createDirectory("src/main/resources/gr/uop/asteras/temp/");
                File fnfile = new File(dir, name);//Αρχείο
                fnfile.createNewFile();

                OutputStream out = new FileOutputStream(fnfile);
                InputStream in = zipFile.getInputStream(entry); //Ινπουτ
                out.write(in.readAllBytes());
                
                return fnfile;
            }
            else{
                return this.getnextfile();//Επειδή αυτό δεν είναι Bib αρχείο, προχωράμε στο επόμενο
            }
        }
        return null;
    }

    // public void addZip(String PathToFile) throws IOException{
    //     System.out.println("Hello World here1");
    //     ZipFile zipFile = new ZipFile(PathToFile);

    //     Enumeration<? extends ZipEntry> entries = zipFile.entries();

    //     while(entries.hasMoreElements()){
    //         ZipEntry entry = entries.nextElement();
    //         String name = entry.getName();
    //         System.out.println(name);
    //         if (name.toUpperCase().endsWith(".BIB")){
    //             File dir = createDirectory("src/main/resources/gr/uop/asteras/temp/");
    //             File fnfile = new File(dir, "fn.bib");//Αρχείο

    //             OutputStream out = new FileOutputStream(fnfile);
    //             InputStream in = zipFile.getInputStream(entry); //Ινπουτ
    //             out.write(in.readAllBytes());

    //             System.out.println("\n\n");
    //             System.out.println(AddingBib.getBibFieldData("title", fnfile)); // Εδώ Lucene
    //             System.out.println("\n\n");
    //         }
            
    //     }
    // }

    public static File createDirectory(String directoryPath) throws IOException {
        File dir = new File(directoryPath);
        if (dir.exists()) {
            return dir;
        }
        if (dir.mkdirs()) {
            return dir;
        }
        throw new IOException("Failed to create directory '" + dir.getAbsolutePath() + "' for an unknown reason.");
    }
}
