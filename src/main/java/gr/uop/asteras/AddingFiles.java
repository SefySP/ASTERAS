package gr.uop.asteras;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AddingFiles {
    
    public static void AddingFiles() throws IOException {
        System.out.println("Hello World here0");
    }

    public void run(String PathToFile) throws IOException{
        System.out.println("Hello World here1");
        ZipFile zipFile = new ZipFile(PathToFile);

        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            String name = entry.getName();
            System.out.println(name);
            if (name.toUpperCase().endsWith(".BIB")) {
                File dir = createDirectory("temp");
                File fnfile = new File(dir, "fn.bib");//Αρχείο
                // fnfile.getParentFile().mkdirs();
                // fnfile.createNewFile();
                System.out.println("\n\n");
                System.out.println(fnfile.getAbsolutePath());
                System.out.println("\n\n");
                OutputStream out = new FileOutputStream(fnfile);
                InputStream in = zipFile.getInputStream(entry); //Ινπουτ
                out.write(in.readAllBytes());
                System.out.println(AddingBib.getBibFieldData("title", fnfile));
                System.out.println("\n\n");
            }
            
        }
    }

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
